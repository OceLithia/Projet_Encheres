package fr.eni.project.dal;

import java.util.List;

import fr.eni.project.bo.Utilisateur;

public interface UtilisateurDAO {

	void createUser(Utilisateur utilisateur);

	Utilisateur readByPseudo(String pseudo);
	
	Utilisateur readById(long id);

	List<Utilisateur> findAll();

	void delete(Utilisateur utilisateur);

	void update(Utilisateur utilisateur);

	String getMotDePasseEncode(Utilisateur utilisateur);

	boolean existeEmail(String email);

	boolean existePseudo(String pseudo);
	
}
