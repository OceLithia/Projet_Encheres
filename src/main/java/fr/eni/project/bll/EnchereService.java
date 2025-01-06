package fr.eni.project.bll;

import java.util.List;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.exception.BusinessException;

public interface EnchereService {

	List<Enchere> afficherEncheres();

	void creerEnchere(Enchere nouvelleEnchere, Utilisateur encherisseur, ArticleVendu article) throws BusinessException;

	List<Enchere> consulterEncheresParArticleEtUtilisateur(long idArticle, long idUtilisateur);

	List<Enchere> consulterEncheresParArticle(long idArticle);

	Enchere consulterEnchereParNoEnchere(long noEnchere);

	Enchere consulterDerniereEnchereParArticle(Long noArticle);
	
	
}
