package fr.eni.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Utilisateur;

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
	public String inscrireUtilisateur(@Validated @ModelAttribute Utilisateur utilisateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}
		this.utilisateurService.creerUtilisateur(utilisateur);
		return "redirect:/user-profile";
	}
	
	
	@GetMapping("/user-profile")
	public String afficherProfilUtilisateur(/*Authentication authentication, */Model model) {
		System.out.println("afficherProfilUtilisateur ");
		//String pseudoUtilisateur = authentication.getName();
		String pseudoUtilisateur = "pseudo6";
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
	    if (utilisateur == null) {
	        model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + pseudoUtilisateur);
	        return "error-page"; // Une page d'erreur Thymeleaf personnalisée
	    }
		model.addAttribute("utilisateur", utilisateur);
		return "user-profile";
	}

}
