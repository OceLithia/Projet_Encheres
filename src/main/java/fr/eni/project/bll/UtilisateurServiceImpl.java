package fr.eni.project.bll;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.UtilisateurDAO;
import fr.eni.project.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;

	@Override
	@Transactional
	public void creerUtilisateur(Utilisateur utilisateur) throws BusinessException {

		BusinessException be = new BusinessException();
		boolean valide = validerPseudoUnique(utilisateur.getPseudo(), be);
		valide &= validerEmailUnique(utilisateur.getEmail(), be);
		valide &= validerCorrespondanceMotDePasse(utilisateur, be);

		if (valide) {
			utilisateur.setMotDePasse(encodeMotDePasse(utilisateur.getMotDePasse()));
			utilisateurDAO.createUser(utilisateur);
		} else {
			throw be;
		}
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
	public void mettreAJourUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException be = new BusinessException();

		// Récupérer l'utilisateur en session
		Utilisateur utilisateurEnSession = utilisateurDAO.readById(utilisateur.getNoUtilisateur());
		// Vérifier si le mot de passe est fourni
		if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {
			// Uniquement valider la correspondance si un nouveau mot de passe est fourni
			if (!validerCorrespondanceMotDePassePourModification(utilisateur, be)) {
				throw be;
			}
			utilisateur.setMotDePasse(encodeMotDePasse(utilisateur.getMotDePasse()));
		} else {
			// Conserver l'ancien mot de passe
			utilisateur.setMotDePasse(utilisateurEnSession.getMotDePasse());
		}
		// Vérifier si le pseudo a changé et s'il est unique
		if (!utilisateur.getPseudo().equals(utilisateurEnSession.getPseudo())) {
			if (!validerPseudoUnique(utilisateur.getPseudo(), be)) {
				throw be;
			}
		}

		// Vérifier si l'email a changé et s'il est unique
		if (!utilisateur.getEmail().equals(utilisateurEnSession.getEmail())) {
			if (!validerEmailUnique(utilisateur.getEmail(), be)) {
				throw be;
			}
		}

		// Mise à jour de l'utilisateur
		utilisateurDAO.update(utilisateur);
	}

	// Nouvelle méthode spécifique pour la validation du mot de passe lors de la
	// modification
	private boolean validerCorrespondanceMotDePassePourModification(Utilisateur utilisateur, BusinessException be) {
		if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {
			// Ne vérifier la correspondance que si un nouveau mot de passe est fourni
			if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmPassword())) {
				return false;
			}
		}
		return true;
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

	private boolean validerCorrespondanceMotDePasse(Utilisateur utilisateur, BusinessException be) {
		if (utilisateur.getMotDePasse() == null || utilisateur.getConfirmPassword() == null
				|| !utilisateur.getMotDePasse().equals(utilisateur.getConfirmPassword())) {
			be.addMessage("Les mots de passe ne correspondent pas.");
			return false;
		}
		return true;
	}

}
