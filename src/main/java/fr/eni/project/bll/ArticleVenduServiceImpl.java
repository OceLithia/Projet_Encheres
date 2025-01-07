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

		// Règle métier 4 : Vérifier que l'enchérisseur n'ait pas déjà fait la dernière
		// enchère
		Optional<Enchere> derniereEnchere = enchereDAO.findLastEnchereByArticle(articleId);

		if (derniereEnchere.isPresent()) {
			// Récupérer la dernière enchère
			Enchere derniereEnchereExistante = derniereEnchere.get();

			// Vérifier si l'utilisateur actuel est le même que le dernier enchérisseur
			if (encherisseur.getNoUtilisateur() == derniereEnchereExistante.getEncherisseur().getNoUtilisateur()) {
				be.addMessage("Vous êtes déjà le dernier enchérisseur sur cet article.");
				throw be;
			}
		}

		// Logique d'enchère si tout est valide
		Enchere nouvelleEnchere = new Enchere(LocalDateTime.now(), montant, encherisseur, article);
		enchereDAO.createEnchere(article, nouvelleEnchere, encherisseur);

		// Mettre à jour le prix de vente
		article.setPrixVente(montant);

		articleVenduDAO.update(article, article.getVendeur());
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

	@Override
	@Transactional
	public void verifierEtFinaliserVentes() {
		List<ArticleVendu> articles = articleVenduDAO.findByDateFinEncheresBefore(LocalDateTime.now());

		for (ArticleVendu article : articles) {
			System.out.println(article.getNoArticle() + " est à l'état : " + article.getEtatVente());
			Optional<Enchere> meilleureEnchere = enchereDAO.findLastEnchereByArticle(article.getNoArticle());

			if (meilleureEnchere.isPresent()) {
				article.setEtatVente(2); // Vente finalisée
				articleVenduDAO.update(article, article.getVendeur()); // Mettez à jour l'article avec l'état finalisé

				Utilisateur acheteur = meilleureEnchere.get().getEncherisseur();
				// System.out.println("Vente finalisée pour l'article : " +
				// article.getNoArticle()+", Acheteur : " + acheteur.getPseudo());
			} else {
				article.setEtatVente(1); // Pas d'enchères, état à "invendu"
				articleVenduDAO.update(article, article.getVendeur());
			}
		}
	}

	@Override
	public void mettreAJourArticle(ArticleVendu article, Utilisateur vendeur) {
		articleVenduDAO.update(article, vendeur);
	}

	@Override
	public void supprimerArticle(ArticleVendu article) {
		articleVenduDAO.deleteArticle(article);
	}

	@Override
	public void savedUpdate(ArticleVendu updatedArticle, Retrait updatedRetrait) {
		ArticleVendu articleAvantMaj = articleVenduDAO.readById(updatedArticle.getNoArticle());
		System.out.println(articleAvantMaj);
		updatedArticle.setPrixVente(updatedArticle.getMiseAPrix());
		updatedArticle.setEtatVente(articleAvantMaj.getEtatVente());
		Retrait retraitAvantMaj = articleAvantMaj.getLieuRetrait();
		if (updatedRetrait.getRue() == null) {
			updatedRetrait.setRue(retraitAvantMaj.getRue());
		}
		if (updatedRetrait.getCodePostal() == null) {
			updatedRetrait.setCodePostal(retraitAvantMaj.getCodePostal());
		}
		if (updatedRetrait.getVille() == null) {
			updatedRetrait.setVille(retraitAvantMaj.getVille());
		}
		System.out.println(updatedArticle);
		articleVenduDAO.updateArticle(updatedArticle, updatedRetrait);
	}

}
