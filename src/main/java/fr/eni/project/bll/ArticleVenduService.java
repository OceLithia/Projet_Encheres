package fr.eni.project.bll;

import java.util.List;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dto.FiltreDTO;
import fr.eni.project.exception.BusinessException;

public interface ArticleVenduService {

	void addNewArticle(Utilisateur utilisateur, ArticleVendu articleVendu);

	List<ArticleVendu> afficherArticles();

	ArticleVendu afficherArticleParNoArticle(long noArticle);

	List<ArticleVendu> afficherArticleParNoVendeur(long noVendeur);

	List<ArticleVendu> afficherArticlesParNoCategorie(long noCategorie);

	List<ArticleVendu> afficherArticlesParMotCle(String motCle);

	List<ArticleVendu> filtrerArticles(FiltreDTO filtres);

	void encherir(Long articleId, int montant, Utilisateur encherisseur) throws BusinessException;

	void verifierEtFinaliserVentes();

	void mettreAJourEtatVentes();
	
}
