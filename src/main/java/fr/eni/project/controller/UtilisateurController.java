package fr.eni.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.project.bll.ArticleVenduService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dto.ValidationGroups;
import fr.eni.project.exception.BusinessException;
import jakarta.servlet.http.HttpSession;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private ArticleVenduService articleVenduService;

	@GetMapping("/login")
	public String afficherSeConnecter(HttpSession session, Model model) {
		// Récupère le message de succès dans la session
		String message = (String) session.getAttribute("message");
		if (message != null) {
			model.addAttribute("successMessage", message);
			session.removeAttribute("message"); // Supprime le message après affichage
		}
		return "login";
	}

	@PostMapping("/login")
	public String seConnecter() {
		return "redirect:/encheres";
	}

	@GetMapping("/signup")
	public String afficherSinscrire(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "signup";
	}

	@PostMapping("/signup")
	public String inscrireUtilisateur(
			@Validated(ValidationGroups.Creation.class) @ModelAttribute Utilisateur utilisateur,
			BindingResult bindingResult, HttpSession session, Model model) {

		// Vérification de la correspondance des mots de passe
		if (!utilisateur.getMotDePasse().equals(utilisateur.getConfirmPassword())) {
			bindingResult.reject("passwordMismatch", "Les mots de passe ne correspondent pas.");
			return "signup";
		}

		if (bindingResult.hasErrors()) {
			return "signup";
		}

		try {
			this.utilisateurService.creerUtilisateur(utilisateur);

			// Ajout du message de succès dans la session
			session.setAttribute("message", "Inscription réussie !");
			return "redirect:/login";
		} catch (BusinessException e) {
			e.getListeMessages().forEach(m -> {
				ObjectError error = new ObjectError("globalError", m);
				bindingResult.addError(error);
			});
		}
		return "signup";
	}

	@GetMapping("/user-profile")
	public String afficherProfilUtilisateur(Authentication authentication, Model model) {
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (utilisateur == null) {
			model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + authentication.getName());
			return "error-page"; // Une page d'erreur Thymeleaf personnalisée
		}
		List<ArticleVendu> articlesEnVente = this.articleVenduService
				.afficherArticleParNoVendeur(utilisateur.getNoUtilisateur());
		// List<ArticleVendu> articlesAvecEnchereEnCours = this.articleVenduService.a
		model.addAttribute("articles", articlesEnVente);
		model.addAttribute("utilisateur", utilisateur);
		return "user-profile";
	}

	@GetMapping("/details")
	public String afficherModifierProfil(Authentication authentication, Model model) {
		Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (utilisateur == null) {
			throw new RuntimeException("Utilisateur non trouvé ou non authentifié.");
		}

		model.addAttribute("utilisateur", utilisateur);
		return "user-profile-details";
	}

	@PostMapping("/update-user")
	public String modifierProfil(@Validated(ValidationGroups.Update.class) @ModelAttribute Utilisateur utilisateur,
	        BindingResult bindingResult, Model model, Authentication authentication) {

	    if (bindingResult.hasErrors()) {
			model.addAttribute("utilisateur", utilisateur);
	        return "user-profile-details";
	    }

	    utilisateur.setNoUtilisateur(utilisateurService.afficherUtilisateurParPseudo(authentication.getName()).getNoUtilisateur());
	    try {
	        utilisateurService.mettreAJourUtilisateur(utilisateur);
	        System.out.println("Profil mis à jour avec succès");
	    } catch (BusinessException e) {
	        e.getListeMessages().forEach(m -> {
	            ObjectError error = new ObjectError("globalError", m);
	            bindingResult.addError(error);
	        });
	        model.addAttribute("utilisateur", utilisateur);
	        return "user-profile-details";  // Retourner à la vue en cas d'erreur
	    }

	    // Update authentication
	    Authentication newAuth = new UsernamePasswordAuthenticationToken(utilisateur, utilisateur.getMotDePasse(),
	            authentication.getAuthorities());
	    SecurityContextHolder.getContext().setAuthentication(newAuth);

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
		model.addAttribute("message", "Votre profil a été supprimé avec succès.");
		authentication.setAuthenticated(false);
		return "redirect:/";
	}

	@GetMapping("/view-seller")
	public String afficherProfilVendeur(@RequestParam("noVendeur") long noVendeur, Model model) {
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParId(noVendeur);
		if (utilisateur == null) {
			model.addAttribute("erreur", "Aucun utilisateur trouvé avec l'ID : " + noVendeur);
			return "error-page"; // Une page d'erreur Thymeleaf personnalisée
		}
		model.addAttribute("vendeur", utilisateur);
		return "view-seller";
	}

}
