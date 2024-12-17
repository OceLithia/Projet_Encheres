package fr.eni.project.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.UtilisateurDAO;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;

	

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		String motDePasseEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(motDePasseEncode); // Mettre à jour le mot de passe avec la version encodée
		utilisateurDAO.createUser(utilisateur);
	}

	@Override
	public List<Utilisateur> afficherUtilisateurs() {
		return utilisateurDAO.findAll();
	}

	@Override
	public Utilisateur afficherUtilisateurParPseudo(String pseudoUtilisateur) {
		return utilisateurDAO.read(pseudoUtilisateur);
	}

}


