package fr.eni.project.bll;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduService {

	void addNewArticle(ArticleVendu articleVendu, Utilisateur utilisateur);
	
	List<ArticleVendu> consulterArticleVendu();
	
}
