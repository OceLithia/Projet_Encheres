package fr.eni.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Filtre;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.CategorieDAO;
import jakarta.validation.Valid;

@Controller
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

	@Autowired
	private CategorieService categorieService;
	@Autowired
	private EnchereService enchereService;
	@Autowired
	private UtilisateurService addressUser;
	@Autowired
	private CategorieDAO categorieDAO;

	/*
	 * @GetMapping public String afficherEncheres(Model model) { List<Enchere>
	 * encheres = this.enchereService.afficherEncheres();
	 * model.addAttribute("encheres", encheres); return "index"; }
	 */

	@GetMapping("/sell-article")
	public String afficherVendreArticle(Authentication authentication, Model model) {
		// Création d'un nouvel article à vendre
		ArticleVendu articleVendu = new ArticleVendu();
		// Récupération de l'utilisateur authentifié via Spring Security
		String pseudoUtilisateur = authentication.getName(); // Récupère le pseudo ou nom d'utilisateur
		Utilisateur vendeur = addressUser.afficherUtilisateurParPseudo(pseudoUtilisateur);

		// Vérification que l'utilisateur existe et initialisation des valeurs par
		// défaut
		if (vendeur != null) {
			articleVendu.setVendeur(vendeur);
		} else {
			// Gestion du cas où l'utilisateur n'est pas trouvé
			vendeur = new Utilisateur();
			vendeur.setRue("Adresse par défaut");
			vendeur.setCodePostal("00000");
			vendeur.setVille("Ville par défaut");
			articleVendu.setVendeur(vendeur);
		}

		// Récupérer la liste des catégories depuis le service
		List<Categorie> categories = categorieService.readCategory();

		// Ajouter les données au modèle pour le formulaire
		model.addAttribute("categories", categories);
		model.addAttribute("articleVendu", articleVendu);

		return "sell-article";
	}

	@GetMapping("/index")
	public String showForm(Model model) {
		Enchere enchere = new Enchere(); // L'objet qui contient le champ categorie
		model.addAttribute("enchere", enchere); // L'ajouter au modèle
		List<Categorie> categories = categorieService.getAllCategories();
		model.addAttribute("categories", categories);
		return "index";
	}

	/*
	 * @GetMapping("/sell-article") public String afficherVendreArticle(Model model)
	 * { String pseudoUser = addressUser.afficherUtilisateurParPseudo(); Utilisateur
	 * user = addressUser.afficherUtilisateurParPseudo(noUtilisateur);
	 * user.getRue(); user.getCodePostal(); user.getVille();
	 * model.addAttribute("utilisateur", user); return "sell-article"; }
	 */

	@GetMapping("/encheres")
	public String showCategories(@RequestParam(name = "categorie", required = false) Integer categorieId, Model model) {
		if (categorieId != null) {
			// Récupérer les articles pour cette catégorie
			model.addAttribute("articles", CategorieService.getArticlesByCategorie(categorieId));
		}
		model.addAttribute("categories", categorieService.getAllCategories());
		return "encheres";
	}

	@GetMapping("/article-details")
	public String afficherDetailArticle(@RequestParam("id") long id, Model model) {
		Enchere e = this.enchereService.consulterEnchereParId(id);
		System.out.println(e);
		model.addAttribute("encheres", e);
		return "article-details";
	}

	@PostMapping("/filtrer")
	public String rechercheParFiltre(@Valid @ModelAttribute Filtre filtre, BindingResult bindingResult) {
		return "encheres";
	}

	@GetMapping("/")
	public String index(@RequestParam(value = "categorie", required = false) String category, Model model) {
		// Récupérer les catégories depuis la base de données
		List<Categorie> categories = categorieDAO.findAll();

		// Liste simulée des objets
		List<String> objets = List.of("Objet 1 - Catégorie 1", "Objet 2 - Catégorie 2", "Objet 3 - Catégorie 1",
				"Objet 4 - Catégorie 3");

		// Si une catégorie est sélectionnée, filtrer les objets
		if (categories != null) {
			objets = objets.stream().filter(obj -> obj.contains(obj)).toList();
		}

		// Ajouter les données au modèle
		model.addAttribute("categories", categories);
		model.addAttribute("objets", objets);
		model.addAttribute("selectedCategory", category); // Conserve la catégorie sélectionnée
		return "index";
	}

}
