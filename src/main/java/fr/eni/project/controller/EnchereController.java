package fr.eni.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;

@Controller
@RequestMapping
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

    @Autowired
    private CategorieService categorieService;
	private EnchereService enchereService;
	@Autowired
	private UtilisateurService addressUser;
	

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}
/*
	@GetMapping
	public String afficherEncheres(Model model) {
		List<Enchere> encheres = this.enchereService.afficherEncheres();
		model.addAttribute("encheres", encheres);
		return "index";
		}
*/
	@GetMapping("/sell-article")
	public String afficherVendreArticle(Model model) {
		ArticleVendu articleVendu = new ArticleVendu();
		articleVendu.setVendeur(new Utilisateur());
		// Récupérer la liste des catégories depuis le service
		List<Categorie> categories = this.categorieService.readCategory();
		// Ajouter la liste des catégories au modèle
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
	
	/**
	 * 
	 */
}

