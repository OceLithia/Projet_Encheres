package fr.eni.project.dal;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduDAO {
	
	void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle);
	
	boolean checkCategory(long noCategorie);
	
	List<ArticleVendu> findAll();

	ArticleVendu readById(long id_article);

	ArticleVendu readByVendeur(long id_vendeur);



}
