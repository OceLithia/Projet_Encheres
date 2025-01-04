package fr.eni.project.dal;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;

public interface ArticleVenduDAO {

	void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle);

	boolean checkCategory(long noCategorie);

	List<ArticleVendu> findAll();

	ArticleVendu readById(long id_article);

	List<ArticleVendu> readByVendeur(long id_vendeur);

	List<ArticleVendu> readByCategorie(long no_categorie);
	
	List<ArticleVendu> readByKeyword(String motCle);
	
	void update(ArticleVendu articleVendu, Utilisateur vendeur);

}
