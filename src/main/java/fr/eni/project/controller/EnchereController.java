package fr.eni.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Filtre;
import fr.eni.project.dal.CategorieDAO;
import jakarta.validation.Valid;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;


@Controller
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

    @Autowired
    private CategorieService categorieService;
    
	private EnchereService enchereService;

	
	private final CategorieDAO categorieDAO = null;

	@Autowired
	private UtilisateurService addressUser;
	

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
	  Filtre filtre = new Filtre();
	    model.addAttribute("filtre", filtre); // L'ajouter au modèle
	    return "index";
	}
	
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
    List<String> objets = List.of(
            "Objet 1 - Catégorie 1",
            "Objet 2 - Catégorie 2",
            "Objet 3 - Catégorie 1",
            "Objet 4 - Catégorie 3"
    );

    // Si une catégorie est sélectionnée, filtrer les objets
    if (categories != null) {
        objets = objets.stream()
                       .filter(obj -> obj.contains(obj))
                       .toList();
    }

    // Ajouter les données au modèle
    model.addAttribute("categories", categories);
    model.addAttribute("objets", objets);
    model.addAttribute("selectedCategory", category); // Conserve la catégorie sélectionnée
    return "index";
}
}
