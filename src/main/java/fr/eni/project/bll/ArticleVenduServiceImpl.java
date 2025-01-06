package fr.eni.project.bll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.ArticleVenduDAO;
import fr.eni.project.dal.EnchereDAO;
import fr.eni.project.dal.RetraitDAO;
import fr.eni.project.dto.FiltreDTO;
import fr.eni.project.exception.BusinessException;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

	@Autowired
	private ArticleVenduDAO articleVenduDAO;
	@Autowired
	private RetraitDAO retraitDAO;
	@Autowired
	private EnchereDAO enchereDAO;

	@Override
	public void addNewArticle(Utilisateur vendeur, ArticleVendu nouvelArticle) {
		articleVenduDAO.createSellArticle(vendeur, nouvelArticle);
		nouvelArticle.setPrixVente(nouvelArticle.getMiseAPrix());
		if (nouvelArticle.getDateDebutEncheres().isBefore(LocalDateTime.now())) {
			nouvelArticle.setEtatVente(-1);
		} else { 
			nouvelArticle.setEtatVente(0);
		}
		retraitDAO.insertLieuRetraitParDefaut(nouvelArticle.getNoArticle(), vendeur);
	}

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
	
	@Override
	public void mettreAJourEtatVentes() {
        List<ArticleVendu> articles = articleVenduDAO.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (ArticleVendu article : articles) {
            if (now.isBefore(article.getDateDebutEncheres())) {
                article.setEtatVente(0);
            } else if (now.isAfter(article.getDateFinEncheres())) {
                article.setEtatVente(2);
            } else {
                article.setEtatVente(1);
            }
            articleVenduDAO.update(article, article.getVendeur()); // Met à jour l'article dans la BDD
        }
    }
	
	@Override
	public void encherir(Long articleId, int montant, Utilisateur encherisseur) throws BusinessException {
	    BusinessException be = new BusinessException();

	    // Règle métier 1 : Vérifier si l'article existe
	    ArticleVendu article = articleVenduDAO.readById(articleId);
	    if (article == null) {
	        be.addMessage("L'article avec l'ID " + articleId + " est introuvable.");
	        throw be; // Lancer directement l'exception si l'article n'existe pas
	    }
	    // Règle métier 2 : Vérifier le montant de l'enchère
	    if (montant <= article.getPrixVente()) {
	        be.addMessage("Le montant doit être supérieur au prix actuel (" + article.getPrixVente() + " €).");
	        throw be; // Lancer l'exception si le montant est invalide
	    }
	    
	    // Règle métier 3 : Vérifier si l'enchérisseur est différent du vendeur
	    if (encherisseur.getNoUtilisateur() == article.getVendeur().getNoUtilisateur()) {
	        be.addMessage("Vous ne pouvez pas enchérir sur un article dont vous êtes le vendeur.");
	        throw be; 
	    }
	    
	    //Règle métier 4 : Vérifier que l'enchérisseur n'ait pas déjà fait la dernière enchère
	    Optional<Enchere> derniereEnchere = enchereDAO.findByArticle(articleId);
	    
	    if (derniereEnchere.isPresent() ) {
	    	// Récupérer l'enchère existante
	        Enchere enchereExistante = derniereEnchere.get();
	        // Vérifier si l'utilisateur actuel est le même que le dernier enchérisseur
	        if (encherisseur.getNoUtilisateur() == enchereExistante.getEncherisseur().getNoUtilisateur()) {
	            be.addMessage("Vous êtes déjà le dernier enchérisseur sur cet article.");
	            throw be;
	        }
		}
	    
	    // Logique d'enchère si tout est valide
	    enchereDAO.deleteEnchere(articleId);
	    Enchere nouvelleEnchere = new Enchere(LocalDateTime.now(), montant, encherisseur, article);
	    enchereDAO.createEnchere(article, nouvelleEnchere, encherisseur);

	    // Mettre à jour le prix de vente
	    article.setPrixVente(montant);
	    
	    articleVenduDAO.update(article, article.getVendeur());
	}



	@Override
    public List<ArticleVendu> filtrerArticles(FiltreDTO filtre) {
        // Récupérer tous les articles
        List<ArticleVendu> articles = articleVenduDAO.findAll();

        // Appliquer les filtres dynamiquement
        return articles.stream()
            .filter(article -> filtre.getIdCat() == null || (article.getCategorie().getNoCategorie()) == (filtre.getIdCat()))
            .filter(article -> filtre.getMotCle() == null || article.getNomArticle().toLowerCase().contains(filtre.getMotCle().toLowerCase()))
            .toList();
    }
	
	@Override
	@Transactional
	public void verifierEtFinaliserVentes() {
	    List<ArticleVendu> articles = articleVenduDAO.findByDateFinEncheresBefore(LocalDateTime.now());

	    for (ArticleVendu article : articles) {
	    	System.out.println(article.getNoArticle()+" est à l'état : "+ article.getEtatVente());
	        Optional<Enchere> meilleureEnchere = enchereDAO.findByArticle(article.getNoArticle());

	        if (meilleureEnchere.isPresent()) {
	            article.setEtatVente(2); // Vente finalisée
	            articleVenduDAO.update(article, article.getVendeur()); // Mettez à jour l'article avec l'état finalisé

	            Utilisateur acheteur = meilleureEnchere.get().getEncherisseur();
	            //System.out.println("Vente finalisée pour l'article : " + article.getNoArticle()+", Acheteur : " + acheteur.getPseudo());
	        } else {
	            article.setEtatVente(1); // Pas d'enchères, état à "invendu"
	            articleVenduDAO.update(article, article.getVendeur()); 
	            //System.out.println("Aucune enchère pour l'article : " + article.getNoArticle());
	        }
	    }
	}
	
	@Override
	public void mettreAJourArticle(ArticleVendu article, Utilisateur vendeur) {
		articleVenduDAO.update(article, vendeur);
	}


}
