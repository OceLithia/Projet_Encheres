package fr.eni.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import fr.eni.project.bll.ArticleVenduService;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.dto.FiltreDTO;

@Controller
public class IndexController {

	@Autowired
	private ArticleVenduService articleVenduService;
	@Autowired
	private CategorieService categorieService;
	@Autowired
	private UtilisateurService utilisateurService;

	@GetMapping({ "/", "/index" })
	public String index(Model model) {
		System.out.println("get index");
		List<ArticleVendu> articles = this.articleVenduService.afficherArticles();
		List<Categorie> categories = this.categorieService.afficherCategories();
		model.addAttribute("categories", categories);
		model.addAttribute("articles", articles);
		return "index";
	}
	
	@GetMapping("/encheres")
	public String afficherAccueilConnecte(Model model, Authentication authentication) {
		List<ArticleVendu> articles = this.articleVenduService.afficherArticles();
		List<Categorie> categories = this.categorieService.afficherCategories();
		model.addAttribute("categories", categories);
		model.addAttribute("articles", articles);
		model.addAttribute("utilisateur", utilisateurService.afficherUtilisateurParPseudo(authentication.getName()));
		return "encheres";
	}


	@GetMapping("/filtrer")
	public String rechercheParFiltre(@ModelAttribute FiltreDTO filtres, Model model, Authentication authentication) {
	    System.out.println("Filtres reçus : " + filtres);

	    List<ArticleVendu> articlesFiltres = this.articleVenduService.filtrerArticles(filtres);
	    List<Categorie> categories = this.categorieService.afficherCategories();

	    model.addAttribute("articles", articlesFiltres);
	    model.addAttribute("categories", categories);
	    model.addAttribute("utilisateur", utilisateurService.afficherUtilisateurParPseudo(authentication.getName()));
	    if (authentication.isAuthenticated()) {
			return "encheres";
		} else {
	    return "index";
		}
	}

	/*
	 * @PostMapping("/filtrer") public String
	 * rechercheParFiltre(@Valid @ModelAttribute Filtre filtre, BindingResult
	 * bindingResult) { return "encheres"; }
	 */
	/*
	 * @GetMapping("/") public String index(@RequestParam(value = "categorie",
	 * required = false) String category, Model model) { // Récupérer les catégories
	 * depuis la base de données List<Categorie> categories =
	 * categorieDAO.findAll();
	 * 
	 * // Liste simulée des objets List<String> objets =
	 * List.of("Objet 1 - Catégorie 1", "Objet 2 - Catégorie 2",
	 * "Objet 3 - Catégorie 1", "Objet 4 - Catégorie 3");
	 * 
	 * // Si une catégorie est sélectionnée, filtrer les objets if (categories !=
	 * null) { objets = objets.stream().filter(obj -> obj.contains(obj)).toList(); }
	 * 
	 * // Ajouter les données au modèle model.addAttribute("categories",
	 * categories); model.addAttribute("objets", objets);
	 * model.addAttribute("selectedCategory", category); // Conserve la catégorie
	 * sélectionnée return "index"; }
	 */



}
