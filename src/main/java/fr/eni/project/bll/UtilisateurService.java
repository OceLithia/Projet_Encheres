package fr.eni.project.bll;


import java.util.List;

import fr.eni.project.bo.Utilisateur;

public interface UtilisateurService {

	void creerUtilisateur(Utilisateur utilisateur);

	List<Utilisateur> afficherUtilisateurs();

	Utilisateur afficherUtilisateurParPseudo(String pseudo);

	
}
