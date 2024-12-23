package fr.eni.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;
	
    @GetMapping({"/", "/index"})
    public String index() {
        return "index"; 
    }

	@GetMapping("/login")
	public String afficherSeConnecter() {
		return "login";
	}

	@GetMapping("/signup")
	public String afficherSinscrire(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "signup";
	}

	@PostMapping("/signup")
	public String inscrireUtilisateur(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult,
			Authentication authentication, Model model) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		// Vérification si le pseudo ou l'email existe déjà
		/*
		 * if (utilisateurService.ex(utilisateur.getPseudo())) {
		 * model.addAttribute("pseudoErreur", "Ce pseudo est déjà utilisé."); return
		 * "signup"; } if
		 * (utilisateurService.validerEmailUnique(utilisateur.getEmail())) {
		 * model.addAttribute("emailErreur", "Cette adresse email est déjà utilisée.");
		 * return "signup"; }
		 */
		try {
			this.utilisateurService.creerUtilisateur(utilisateur);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		authentication.setAuthenticated(true);
		return "redirect:/";
	}

	@GetMapping("/user-profile")
	public String afficherProfilUtilisateur(Authentication authentication, Model model) {
		System.out.println("afficherProfilUtilisateur ");
		
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (utilisateur == null) {
			model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + authentication.getName());
			return "error-page"; // Une page d'erreur Thymeleaf personnalisée
		}
		System.out.println(utilisateur.getNoUtilisateur());
		
		model.addAttribute("utilisateur", utilisateur);
		return "user-profile";
	}

	@GetMapping("/details")
	public String afficherModifierProfil(Authentication authentication, Model model) {
		System.out.println("afficherModifierProfil");
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		// Si le champ motDePasse est vide, conservez l'ancien mot de passe
		if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isBlank()) {
			utilisateur.setMotDePasse(utilisateur.getMotDePasse());
		}

		model.addAttribute("utilisateur", utilisateur);
		return "user-profile-details";
	}

	@PostMapping("/update-user")
	public String modifierProfil(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult,
			Model model) {
		// Validation des erreurs (si des annotations @Valid sont présentes dans
		// l'entité)
		if (bindingResult.hasErrors()) {
			model.addAttribute("utilisateur", utilisateur);
			return "user-profile-details"; // Retourne la page avec les erreurs
		}

		// Met à jour l'utilisateur dans la base de données
		try {
			utilisateurService.mettreAJourUtilisateur(utilisateur);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ajoute un message de confirmation pour la vue
		model.addAttribute("message", "Profil mis à jour avec succès.");
		System.out.println(utilisateur);

		return "redirect:/user-profile";
	}

	@GetMapping("/delete-profile")
	public String supprimerProfilUtilisateur(Authentication authentication, Model model) {
		String pseudoUtilisateur = authentication.getName();
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
		// Vérifier si l'utilisateur existe
		if (utilisateur == null) {
			model.addAttribute("erreur", "Utilisateur non trouvé.");
			return "error-page"; // page d'erreur personnalisée avec Thymeleaf
		}
		utilisateurService.supprimerUtilisateur(utilisateur);
		System.out.println("utilisateur " + utilisateur.getPseudo() + " supprimé");
		model.addAttribute("message", "Votre profil a été supprimé avec succès.");
		authentication.setAuthenticated(false);
		return "redirect:/";
	}

}
