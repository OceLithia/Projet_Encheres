package fr.eni.project.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
	public void creerUtilisateur(Utilisateur utilisateur) throws BusinessException {
		
	    BusinessException be = new BusinessException();
	    validerPseudoUnique(utilisateur.getPseudo(), be);
	    validerEmailUnique(utilisateur.getEmail(), be);

	   

	    utilisateur.setMotDePasse(encodeMotDePasse(utilisateur.getMotDePasse()));
	    utilisateurDAO.createUser(utilisateur);
	}

	@Override
	public List<Utilisateur> afficherUtilisateurs() {
		return utilisateurDAO.findAll();
	}

	@Override
	public Utilisateur afficherUtilisateurParPseudo(String pseudoUtilisateur) {
		System.out.println(pseudoUtilisateur);
		System.out.println(utilisateurDAO.readByPseudo(pseudoUtilisateur).getMotDePasse());
		return utilisateurDAO.readByPseudo(pseudoUtilisateur);
	}

	@Override
	public Utilisateur afficherUtilisateurParId(long idUtilisateur) {
		return utilisateurDAO.readById(idUtilisateur);
	}

	@Override
	public void mettreAJourUtilisateur(Utilisateur utilisateur) {
	    // Récupérer l'utilisateur en session pour conserver l'ancien mot de passe si nécessaire
	    Utilisateur utilisateurEnSession = utilisateurDAO.readById(utilisateur.getNoUtilisateur());

	    
	    // Si le mot de passe est vide, conserver l'ancien mot de passe
	    if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()) {
	        utilisateur.setMotDePasse(utilisateurEnSession.getMotDePasse()); // Conserver le mot de passe actuel
	    } else {
	        // Si le mot de passe est non vide, l'encoder et mettre à jour
	    	utilisateur.setMotDePasse(encodeMotDePasse(utilisateur.getMotDePasse()));
	    }

	    // Mise à jour de l'utilisateur dans la base de données
	    utilisateurDAO.update(utilisateur);
	}

	
	@Override
	public String getMotDePasseEncode(Utilisateur utilisateur) {
		return utilisateurDAO.getMotDePasseEncode(utilisateur);
	}


	@Override
	public void supprimerUtilisateur(Utilisateur utilisateur) {
		utilisateurDAO.delete(utilisateur);
	}
	
	private String encodeMotDePasse(String motDePasse) {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(motDePasse);
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
