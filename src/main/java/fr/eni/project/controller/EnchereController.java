package fr.eni.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;

@Controller
@RequestMapping
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

    @Autowired
    private CategorieService categorieService;
	private EnchereService enchereService;

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
		model.addAttribute("enchere", new Enchere());
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
}

