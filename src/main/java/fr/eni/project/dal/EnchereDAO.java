package fr.eni.project.dal;

import java.util.List;
import java.util.Optional;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;

public interface EnchereDAO {
	
	List<Enchere> findAll();

	void createEnchere(ArticleVendu article, Enchere enchere, Utilisateur encherisseur);

	void deleteEnchere(long idArticle);

	Enchere findByArticleAndUtilisateur(long noArticle, long noEncherisseur);

	Optional<Enchere> findByArticle(long idArticle);
	
}
