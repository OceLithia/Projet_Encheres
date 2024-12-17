package fr.eni.project.bll;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduService {

	void addNewArticle(ArticleVendu articleVendu, Utilisateur utilisateur);
	
}
