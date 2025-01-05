package fr.eni.project.bll;

import java.util.List;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.exception.BusinessException;

public interface UtilisateurService {

	void creerUtilisateur(Utilisateur utilisateur) throws BusinessException;

	List<Utilisateur> afficherUtilisateurs();

	Utilisateur afficherUtilisateurParPseudo(String pseudo);

	Utilisateur afficherUtilisateurParId(long idUtilisateur);

	void supprimerUtilisateur(Utilisateur utilisateur);

	void mettreAJourUtilisateur(Utilisateur utilisateur) throws BusinessException;

	String getMotDePasseEncode(Utilisateur utilisateur);

}
