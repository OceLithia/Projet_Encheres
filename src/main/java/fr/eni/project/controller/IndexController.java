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
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dto.ArticleDTO;
import fr.eni.project.dto.FiltreDTO;
import fr.eni.project.dto.FormatDTO;

@Controller
public class IndexController {

	@Autowired
	private ArticleVenduService articleVenduService;
	@Autowired
	private CategorieService categorieService;
	@Autowired
	private UtilisateurService utilisateurService;

	@GetMapping({ "/", "/encheres" })
	public String afficherAccueil(Model model, Authentication authentication) {

		List<ArticleVendu> articles = this.articleVenduService.afficherArticles();
		List<Categorie> categories = this.categorieService.afficherCategories();

		List<ArticleDTO> articlesDTO = articles.stream()
				.map(article -> new ArticleDTO(article, FormatDTO.formatDate(article.getDateFinEncheres()))).toList();

		model.addAttribute("categories", categories);
		model.addAttribute("articles", articlesDTO);
		if (authentication != null) {
			model.addAttribute("utilisateur",
					utilisateurService.afficherUtilisateurParPseudo(authentication.getName()));
		}

		return "encheres";
	}

	@GetMapping("/filtrer")
	public String rechercheParFiltre(@ModelAttribute FiltreDTO filtres, Model model, Authentication authentication) {

		if (authentication != null) {
			Utilisateur utilisateurConnecte = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
			filtres.setUtilisateurId(utilisateurConnecte.getNoUtilisateur());
			model.addAttribute("utilisateur", utilisateurConnecte);
		}

		// Appliquer les filtres
		List<ArticleVendu> articlesFiltres = this.articleVenduService.filtrerArticles(filtres);
		if (articlesFiltres.isEmpty()) {
			model.addAttribute("message", "Oups ! Aucun article ne correspond à votre recherche. Ajustez vos filtres ou explorez d'autres catégories pour trouver votre bonheur !");
		}
		List<Categorie> categories = this.categorieService.afficherCategories();
		List<ArticleDTO> articlesDTO = articlesFiltres.stream()
				.map(article -> new ArticleDTO(article, FormatDTO.formatDate(article.getDateFinEncheres()))).toList();
		// Ajouter les données au modèle
		model.addAttribute("articles", articlesDTO);
		model.addAttribute("categories", categories);

		return "encheres";
	}
	
}
