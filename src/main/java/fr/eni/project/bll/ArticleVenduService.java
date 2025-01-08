package fr.eni.project.bll;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Retrait;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dto.FiltreDTO;
import fr.eni.project.exception.BusinessException;

public interface ArticleVenduService {

	void addNewArticle(Utilisateur vendeur, ArticleVendu nouvelArticle, String rue, String codePostal, String ville);

	void verifierEtFinaliserVentes();

	void encherir(Long articleId, int montant, Utilisateur encherisseur) throws BusinessException;

	void savedUpdate(ArticleVendu updatedArticle, Retrait updatedRetrait);

	List<ArticleVendu> afficherArticles();

	ArticleVendu afficherArticleParNoArticle(long noArticle);

	List<ArticleVendu> afficherArticleParNoVendeur(long noVendeur);

	List<ArticleVendu> afficherArticlesParNoCategorie(long noCategorie);

	List<ArticleVendu> afficherArticlesParMotCle(String motCle);

	void mettreAJourArticle(ArticleVendu article, Utilisateur vendeur);

	void supprimerArticle(ArticleVendu article);

	List<ArticleVendu> filtrerArticles(FiltreDTO filtre);


	
}
