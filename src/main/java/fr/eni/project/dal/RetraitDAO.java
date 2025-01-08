package fr.eni.project.dal;

import fr.eni.project.bo.Utilisateur;

public interface RetraitDAO {

	void insertLieuRetraitParDefaut(long noArticle, Utilisateur vendeur);

	void insertLieuRetrait(long noArticle, String rue, String codePostal, String ville);

}
