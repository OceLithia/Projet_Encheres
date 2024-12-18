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
import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;

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
	public String inscrireUtilisateur(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		this.utilisateurService.creerUtilisateur(utilisateur);
		return "redirect:/user-profile";
	}
	
	@GetMapping("/user-profile")
	public String afficherProfilUtilisateur(Authentication authentication, Model model) {
		System.out.println("afficherProfilUtilisateur ");
		String pseudoUtilisateur = authentication.getName();
		System.out.println(authentication);
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
	    if (utilisateur == null) {
	        model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + pseudoUtilisateur);
	        return "error-page"; // Une page d'erreur Thymeleaf personnalisée
	    }
		model.addAttribute("utilisateur", utilisateur);
		return "user-profile";
	}
	
	@GetMapping("/details")
	public String afficherModifierProfil(Authentication authentication, Model model) {
		System.out.println("afficherModifierProfil");
		String pseudoUtilisateur = authentication.getName();
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
	    if (utilisateur == null) {
	        model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + pseudoUtilisateur);
	        return "error-page"; // Une page d'erreur Thymeleaf personnalisée
	    }
		model.addAttribute("utilisateur", utilisateur);
		return "update-user";
	}
	
	/*
	@PostMapping("/update-user")
	public String modifierProfil(Authentication authentication, Model model) {
		System.out.println("modifierProfil");
		String pseudoUtilisateur = authentication.getName();
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
	    if (utilisateur == null) {
	        model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + pseudoUtilisateur);
	        return "error-page"; // Une page d'erreur Thymeleaf personnalisée
	    }
		model.addAttribute("utilisateur", utilisateur);
		return "update-user";
	}
	*/
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
		
		System.out.println("utilisateur "+utilisateur.getPseudo()+" supprimé");
		model.addAttribute("message", "Votre profil a été supprimé avec succès.");
		authentication.setAuthenticated(false);
		return "redirect:/";
	}
	
	
	

}
