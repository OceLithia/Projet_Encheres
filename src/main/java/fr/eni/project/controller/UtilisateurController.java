package fr.eni.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Utilisateur;

@Controller
public class UtilisateurController {

	private UtilisateurService utilisateurService;

	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/login")
	public String afficherSeConnecter() {
		return "login";
	}
	
	@PostMapping("/login")
	public String seConnecter() {
		System.out.println("user connecté");
		return "redirect:/user-profile";
	}

	@GetMapping("/signup")
	public String afficherSinscrire(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "signup";
	}

	@PostMapping("/signup")
	public String inscrireUtilisateur(@Validated @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		this.utilisateurService.creerUtilisateur(utilisateur);
		return "redirect:/user-profile";
	}
	
	
	@GetMapping("/user-profile")
	public String afficherProfilUtilisateur(Authentication authentication, Model model) {
		String pseudoUtilisateur = authentication.getName();
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
	    if (utilisateur == null) {
	        model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + pseudoUtilisateur);
	        return "error-page"; // Une page d'erreur Thymeleaf personnalisée
	    }
		model.addAttribute("utilisateur", utilisateur);
		return "user-profile";
	}

}
