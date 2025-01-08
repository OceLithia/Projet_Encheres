package fr.eni.project.bll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Retrait;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.ArticleVenduDAO;
import fr.eni.project.dal.EnchereDAO;
import fr.eni.project.dal.RetraitDAO;
import fr.eni.project.dto.FiltreDTO;
import fr.eni.project.exception.BusinessException;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

    private static final int ETAT_NON_DEBUTE = -1;
    private static final int ETAT_EN_COURS = 0;
    private static final int ETAT_NON_VENDU = 1;
    private static final int ETAT_VENDU = 2;

    @Autowired
    private ArticleVenduDAO articleVenduDAO;
    @Autowired
    private RetraitDAO retraitDAO;
    @Autowired
    private EnchereDAO enchereDAO;

    @Override
    public void addNewArticle(Utilisateur vendeur, ArticleVendu nouvelArticle, String rue, String codePostal, String ville) {
        articleVenduDAO.createSellArticle(vendeur, nouvelArticle);
        nouvelArticle.setPrixVente(nouvelArticle.getMiseAPrix());
        nouvelArticle.setEtatVente(determinerEtatInitial(nouvelArticle));
        creerLieuRetrait(vendeur, nouvelArticle, rue, codePostal, ville);
    }

    private int determinerEtatInitial(ArticleVendu article) {
        return article.getDateDebutEncheres().isBefore(LocalDateTime.now()) ? ETAT_EN_COURS : ETAT_NON_DEBUTE;
        
    }

    private void creerLieuRetrait(Utilisateur vendeur, ArticleVendu article, String rue, String codePostal, String ville) {
        boolean adresseDifferente = !rue.equals(vendeur.getRue()) 
            || !codePostal.equals(vendeur.getCodePostal()) 
            || !ville.equals(vendeur.getVille());

        if (adresseDifferente) {
            retraitDAO.insertLieuRetrait(article.getNoArticle(), rue, codePostal, ville);
        } else {
            retraitDAO.insertLieuRetraitParDefaut(article.getNoArticle(), vendeur);
        }
    }

    @Override
    @Transactional
    public void verifierEtFinaliserVentes() {
    	List<ArticleVendu> articlesEnVente = articleVenduDAO.findByDateDebutBeforeAndDateFinAfter(LocalDateTime.now());
    	for (ArticleVendu article : articlesEnVente) {
    		article.setEtatVente(determinerEtatInitial(article));
    		articleVenduDAO.update(article, article.getVendeur());
		}
        List<ArticleVendu> articlesAFinaliser = articleVenduDAO.findByDateFinEncheresBefore(LocalDateTime.now());
        for (ArticleVendu article : articlesAFinaliser) {
            mettreAJourEtatArticle(article);
        }
    }

    private void mettreAJourEtatArticle(ArticleVendu article) {
        Optional<Enchere> derniereEnchere = enchereDAO.findLastEnchereByArticle(article.getNoArticle());
        int nouvelEtat = derniereEnchere.isPresent() ? ETAT_VENDU : ETAT_NON_VENDU;
        article.setEtatVente(nouvelEtat);
        articleVenduDAO.update(article, article.getVendeur());
    }

    @Override
    public void encherir(Long articleId, int montant, Utilisateur encherisseur) throws BusinessException {
        ArticleVendu article = validerEnchere(articleId, montant, encherisseur);
        
        Enchere nouvelleEnchere = new Enchere(LocalDateTime.now(), montant, encherisseur, article);
        enchereDAO.createEnchere(article, nouvelleEnchere, encherisseur);
        
        article.setPrixVente(montant);
        articleVenduDAO.update(article, article.getVendeur());
    }

    private ArticleVendu validerEnchere(Long articleId, int montant, Utilisateur encherisseur) throws BusinessException {
        BusinessException be = new BusinessException();
        ArticleVendu article = articleVenduDAO.readById(articleId);

        if (article == null) {
            be.addMessage("L'article avec l'ID " + articleId + " est introuvable.");
            throw be;
        }

        if (montant <= article.getPrixVente()) {
            be.addMessage("Le montant doit être supérieur au prix actuel (" + article.getPrixVente() + " €).");
            throw be;
        }

        if (encherisseur.getNoUtilisateur() == article.getVendeur().getNoUtilisateur()) {
            be.addMessage("Vous ne pouvez pas enchérir sur un article dont vous êtes le vendeur.");
            throw be;
        }

        Optional<Enchere> derniereEnchere = enchereDAO.findLastEnchereByArticle(articleId);
        if (derniereEnchere.isPresent() && 
            encherisseur.getNoUtilisateur() == derniereEnchere.get().getEncherisseur().getNoUtilisateur()) {
            be.addMessage("Vous êtes déjà le dernier enchérisseur sur cet article.");
            throw be;
        }

        return article;
    }

    @Override
    public void savedUpdate(ArticleVendu updatedArticle, Retrait updatedRetrait) {
        ArticleVendu articleAvantMaj = articleVenduDAO.readById(updatedArticle.getNoArticle());
        mergeArticleUpdate(updatedArticle, articleAvantMaj);
        mergeRetraitUpdate(updatedRetrait, articleAvantMaj.getLieuRetrait());
        articleVenduDAO.updateArticle(updatedArticle, updatedRetrait);
    }

    private void mergeArticleUpdate(ArticleVendu updatedArticle, ArticleVendu articleAvantMaj) {
        updatedArticle.setPrixVente(updatedArticle.getMiseAPrix());
        updatedArticle.setEtatVente(articleAvantMaj.getEtatVente());
    }

    private void mergeRetraitUpdate(Retrait updatedRetrait, Retrait retraitAvantMaj) {
        if (updatedRetrait.getRue() == null) {
            updatedRetrait.setRue(retraitAvantMaj.getRue());
        }
        if (updatedRetrait.getCodePostal() == null) {
            updatedRetrait.setCodePostal(retraitAvantMaj.getCodePostal());
        }
        if (updatedRetrait.getVille() == null) {
            updatedRetrait.setVille(retraitAvantMaj.getVille());
        }
    }

    // Getters simples
    @Override
    public List<ArticleVendu> afficherArticles() {
        return articleVenduDAO.findAll();
    }

    @Override
    public ArticleVendu afficherArticleParNoArticle(long noArticle) {
        return articleVenduDAO.readById(noArticle);
    }

    @Override
    public List<ArticleVendu> afficherArticleParNoVendeur(long noVendeur) {
        return articleVenduDAO.readByVendeur(noVendeur);
    }

    @Override
    public List<ArticleVendu> afficherArticlesParNoCategorie(long noCategorie) {
        return articleVenduDAO.readByCategorie(noCategorie);
    }

    @Override
    public List<ArticleVendu> afficherArticlesParMotCle(String motCle) {
        return articleVenduDAO.readByKeyword(motCle);
    }

    // Délégations simples
    @Override
    public void mettreAJourArticle(ArticleVendu article, Utilisateur vendeur) {
        articleVenduDAO.update(article, vendeur);
    }

    @Override
    public void supprimerArticle(ArticleVendu article) {
        articleVenduDAO.deleteArticle(article);
    }
	
	@Override
	public List<ArticleVendu> filtrerArticles(FiltreDTO filtre) {
	    List<ArticleVendu> articles = articleVenduDAO.findAll();

	    return articles.stream()
	            // Filtrer par catégorie
	            .filter(article -> {
	                boolean match = filtre.getIdCat() == null || article.getCategorie().getNoCategorie() == filtre.getIdCat();
	                return match;
	            })
	            // Filtrer par mot-clé
	            .filter(article -> {
	                boolean match = filtre.getMotCle() == null || article.getNomArticle().toLowerCase().contains(filtre.getMotCle().toLowerCase());
	                return match;
	            })
	            // Filtrer par type d'article (achats ou ventes)
	            .filter(article -> {
	                boolean match = true;
	                if ("achats".equals(filtre.getTypeFiltre())) {
	                    match = article.getVendeur().getNoUtilisateur() != filtre.getUtilisateurId();
	                } else if ("ventes".equals(filtre.getTypeFiltre())) {
	                    match = article.getVendeur().getNoUtilisateur() == filtre.getUtilisateurId();
	                }
	                return match;
	            })
	            // Filtrer par état des ventes
	            .filter(article -> {
	                boolean ventesEnCours = filtre.getVentesEnCours() != null && filtre.getVentesEnCours() && article.getEtatVente() == 0;
	                boolean ventesTerminees = filtre.getVentesTerminees() != null && filtre.getVentesTerminees() && (article.getEtatVente() == 1 || article.getEtatVente() == 2);
	                boolean ventesNonDebutees = filtre.getVentesNonDebutees() != null && filtre.getVentesNonDebutees() && article.getEtatVente() == -1;

	                boolean match = ventesEnCours || ventesTerminees || ventesNonDebutees || (filtre.getVentesEnCours() == null && filtre.getVentesTerminees() == null && filtre.getVentesNonDebutees() == null);
	                return match;
	            })
	            // Filtrer par état des enchères
	            .filter(article -> {
	                boolean encheresOuvertes = filtre.getEncheresOuvertes() != null && filtre.getEncheresOuvertes() && article.getEtatVente() == 0;
	                boolean encheresEnCours = false;
	                boolean encheresRemportees = filtre.getEncheresRemportees() != null && filtre.getEncheresRemportees() && article.getEtatVente() == 2;

	                if (filtre.getEncheresEnCours() != null && filtre.getEncheresEnCours()) {
	                    List<ArticleVendu> articlesEncheris = articleVenduDAO.findArticlesEncheresEnCours(filtre.getUtilisateurId());
	                    encheresEnCours = articlesEncheris.contains(article);
	                }

	                boolean match = encheresOuvertes || encheresEnCours || encheresRemportees || (filtre.getEncheresOuvertes() == null && filtre.getEncheresEnCours() == null && filtre.getEncheresRemportees() == null);
	                return match;
	            })
	            .toList();
	}

}
