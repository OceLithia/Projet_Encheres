package fr.eni.project.dal;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduDAO {
	
	void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle);
	
	boolean checkCategory(long noCategorie);

}
