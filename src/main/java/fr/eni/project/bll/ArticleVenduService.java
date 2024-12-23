package fr.eni.project.bll;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduService {

	void addNewArticle(Utilisateur utilisateur, ArticleVendu articleVendu);

	List<ArticleVendu> afficherArticles();

	ArticleVendu afficherArticleParNoArticle(long noArticle);

	ArticleVendu afficherArticleParNoVendeur(long noVendeur);
	
}
