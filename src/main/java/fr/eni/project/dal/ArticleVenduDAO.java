package fr.eni.project.dal;

import java.time.LocalDateTime;
import java.util.List;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Retrait;
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

	List<ArticleVendu> findByDateFinEncheresBefore(LocalDateTime maintenant);

	void deleteArticle(ArticleVendu article);
	
	void updateArticle(ArticleVendu updatedArticle, Retrait updatedRetrait);
	
	List<ArticleVendu> findArticlesEncheresEnCours(Long utilisateurId);

	List<ArticleVendu> findByDateDebutBeforeAndDateFinAfter(LocalDateTime localDateTime);

	List<ArticleVendu> findArticlesRemportes(Long utilisateurId);

	List<ArticleVendu> findByDateFinEncheresAfter(LocalDateTime localDateTime);
	
}
