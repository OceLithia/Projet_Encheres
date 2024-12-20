package fr.eni.project.dal;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduDAO {
	
	void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle);
/*	
	List<ArticleVendu> void findAll();
	ArticleVendu read(long noArticle);
	*/

	boolean checkCategory(long noCategorie);

}
