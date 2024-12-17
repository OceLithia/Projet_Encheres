package fr.eni.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Utilisateur;

@Controller
public class LoginController {

	@Autowired
	private UtilisateurService utilisateurService;

	public LoginController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
	
	@GetMapping("/login")
	public String afficherSeConnecter() {
		return "login";
	}

	@GetMapping("/signup")
	public String afficherSinscrire() {
		return "signup";
	}

	@PostMapping("/signup")
	public String ajouterUtilisateur(@ModelAttribute Utilisateur utilisateur) {
		return "redirect:/user-profil";
	}

}
