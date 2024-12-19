package fr.eni.project.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.UtilisateurDAO;
import fr.eni.project.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;

	@Override
	public void creerUtilisateur(Utilisateur utilisateur) {
		String motDePasseEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder()
				.encode(utilisateur.getMotDePasse());

		System.out.println(motDePasseEncode);
		utilisateur.setMotDePasse(motDePasseEncode); // Mettre à jour le mot de passe avec la version encodée
		utilisateurDAO.createUser(utilisateur);
	}

	@Override
	public List<Utilisateur> afficherUtilisateurs() {
		return utilisateurDAO.findAll();
	}

	@Override
	public Utilisateur afficherUtilisateurParPseudo(String pseudoUtilisateur) {
		return utilisateurDAO.readByPseudo(pseudoUtilisateur);
	}

	@Override
	public Utilisateur afficherUtilisateurParId(long idUtilisateur) {
		return utilisateurDAO.readById(idUtilisateur);
	}

	@Override
	public void mettreAJourUtilisateur(Utilisateur utilisateur) {
		String motDePasseEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder()
				.encode(utilisateur.getMotDePasse());
		if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {

			utilisateur.setMotDePasse(motDePasseEncode);
		}
		utilisateurDAO.update(utilisateur);
	}

	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		utilisateurDAO.delete(utilisateur);
	}
	
	private boolean validerPseudoUnique(String pseudo, BusinessException be) {
		boolean pseudoExiste = this.utilisateurDAO.existePseudo(pseudo);

		if (pseudoExiste) {
			be.addMessage("Le pseudo existe déjà");
		}
		
		return !pseudoExiste;
	}
	
	private boolean validerEmailUnique(String email, BusinessException be) {
		boolean emailExiste = this.utilisateurDAO.existeEmail(email);

		if (emailExiste) {
			be.addMessage("L'email existe déjà");
		}
		
		return !emailExiste;
	}

}
