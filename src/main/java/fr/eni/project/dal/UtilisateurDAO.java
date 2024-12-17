package fr.eni.project.dal;

import java.util.List;

import fr.eni.project.bo.Utilisateur;

public interface UtilisateurDAO {

	void createUser(Utilisateur utilisateur);

	Utilisateur read(String pseudo);

	List<Utilisateur> findAll();
	
}
