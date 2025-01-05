package fr.eni.project.bll;

import java.util.List;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.exception.BusinessException;

public interface EnchereService {

	List<Enchere> afficherEncheres();

	void creerEnchere(Enchere nouvelleEnchere, Utilisateur encherisseur, ArticleVendu article) throws BusinessException;

	void supprimerEnchere(long idArticle);

	Enchere consulterEnchereParArticleEtUtilisateur(long idArticle, long idUtilisateur);

	Enchere consulterEnchereParArticle(long id);
	
	
}
