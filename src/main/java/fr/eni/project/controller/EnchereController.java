package fr.eni.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.project.bll.EnchereService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;

@Controller
@RequestMapping
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

	private EnchereService enchereService;
	@Autowired
	private UtilisateurService addressUser;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@GetMapping
	public String afficherEncheres(Model model) {
		List<Enchere> encheres = this.enchereService.afficherEncheres();
		model.addAttribute("encheres", encheres);
		return "index";
	}

	/*
	 * @GetMapping("/sell-article") public String afficherVendreArticle(Model model)
	 * { String pseudoUser = addressUser.afficherUtilisateurParPseudo(); Utilisateur
	 * user = addressUser.afficherUtilisateurParPseudo(noUtilisateur);
	 * user.getRue(); user.getCodePostal(); user.getVille();
	 * model.addAttribute("utilisateur", user); return "sell-article"; }
	 */

}
