package fr.eni.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.multipart.MultipartFile;

import fr.eni.project.bll.ArticleVenduService;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dto.EnchereDTO;
import fr.eni.project.exception.BusinessException;
import fr.eni.project.exception.EnchereNotFoundException;
import jakarta.validation.Valid;

@Controller
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

	@Autowired
	private CategorieService categorieService;
	@Autowired
	private EnchereService enchereService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private ArticleVenduService articleVenduService;

	/*
	 * @GetMapping public String afficherEncheres(Model model) { List<Enchere>
	 * encheres = this.enchereService.afficherEncheres();
	 * model.addAttribute("encheres", encheres); return "index"; }
	 */

	@GetMapping("/sell-article")
	public String afficherVendreArticle(Authentication authentication, Model model) {
		// Création d'un nouvel article à vendre
		ArticleVendu articleVendu = new ArticleVendu();
		// récupérer date et heure actuelle
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		String currentDateTime = dateFormat.format(now);
		// Récupération de l'utilisateur authentifié via Spring Security
		Utilisateur vendeur = this.utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		// Vérification que l'utilisateur existe et est co
		if (vendeur == null) {
			model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " + authentication.getName());
			return "error-page"; // Une page d'erreur Thymeleaf personnalisée
		}
		articleVendu.setVendeur(vendeur);
		// Récupérer la liste des catégories depuis le service
		List<Categorie> categories = categorieService.afficherCategories();
		// Ajouter les données au modèle pour le formulaire
		model.addAttribute("categories", categories);
		model.addAttribute("articleVendu", articleVendu);
		model.addAttribute("utilisateur", vendeur);
		// ajouter la date l'heure
		model.addAttribute("currentDateTime", currentDateTime);
		return "sell-article";
	}

	/*
	 * @PostMapping("/sell-article") public String
	 * createSellArticle(@Valid @ModelAttribute ArticleVendu articleVendu,
	 * BindingResult bindingResult, Model model, Authentication authentication) { if
	 * (bindingResult.hasErrors()) { // Si des erreurs existent, retourner à la page
	 * du formulaire avec les erreurs model.addAttribute("currentDateTime", new
	 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date())); return
	 * "sell-article"; } else { // Récupérer l'utilisateur authentifié String
	 * pseudoUtilisateur = authentication.getName(); Utilisateur vendeur =
	 * utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur); //
	 * Associer l'utilisateur au nouvel article articleVendu.setVendeur(vendeur);
	 * this.articleVenduService.addNewArticle(vendeur, articleVendu);
	 * System.out.println("controller : "+articleVendu.getPrixVente()); return
	 * "redirect:/encheres"; } }
	 */
	@PostMapping("/sell-article")
	public String createSellArticle(@Valid @ModelAttribute ArticleVendu articleVendu, BindingResult bindingResult,
			@RequestParam("image") MultipartFile image, Model model, Authentication authentication) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("currentDateTime", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date()));
			return "sell-article";
		} else {
			try {
				// Gérer le fichier image
				if (!image.isEmpty()) {
					// Nom unique pour l'image
					String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
					String uploadDir = "src/main/resources/static/uploads/"; // Dossier où les images seront stockées
					Path filePath = Path.of(uploadDir + fileName);

					// Sauvegarder l'image sur le disque
					Files.createDirectories(filePath.getParent());
					Files.write(filePath, image.getBytes());

					// Stocker le chemin dans l'entité
					articleVendu.setImagePath(fileName);
				}

				// Associer le vendeur
				String pseudoUtilisateur = authentication.getName();
				Utilisateur vendeur = utilisateurService.afficherUtilisateurParPseudo(pseudoUtilisateur);
				articleVendu.setVendeur(vendeur);

				// Sauvegarder l'article
				articleVenduService.addNewArticle(vendeur, articleVendu);
				return "redirect:/encheres";
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("erreur", "Erreur lors du téléchargement de l'image.");
				return "sell-article";
			}
		}
	}

	private String preparerVueDetailArticle(Long articleId, Model model, Authentication authentication) {
	    // Récupérer l'article et l'utilisateur connecté
	    ArticleVendu article = articleVenduService.afficherArticleParNoArticle(articleId);
	    Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());

	    model.addAttribute("articleVendu", article);
	    model.addAttribute("utilisateur", utilisateur);

	    try {
	        // Vérifier si une enchère existe pour cet article
	        Enchere enchereExistante = enchereService.consulterEnchereParArticle(articleId);
	        model.addAttribute("enchere", enchereExistante);
	    } catch (EnchereNotFoundException ex) {
	        model.addAttribute("enchere", null);
	    }

	    if (article.getEtatVente() == 2) {
	        // Finaliser les ventes pour obtenir les informations de l'acheteur
	        Enchere meilleureEnchere = enchereService.consulterEnchereParArticle(article.getNoArticle());
	        model.addAttribute("enchere", meilleureEnchere);
	        model.addAttribute("acheteur", meilleureEnchere.getEncherisseur());
	    }

	    return "article-detail";
	}


	@GetMapping({ "/article-detail", "/encherir" })
	public String afficherDetailsArticle(@RequestParam("noArticle") long id, Model model,
			Authentication authentication) {
		ArticleVendu article = this.articleVenduService.afficherArticleParNoArticle(id);
		Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		
		model.addAttribute("articleVendu", article);
		model.addAttribute("utilisateur", utilisateur);

		Enchere enchere;
		try {
			enchere = this.enchereService.consulterEnchereParArticle(id);
		} catch (EnchereNotFoundException e) {
			enchere = null; // Si aucune enchère, on passe un objet null (ou un objet vide)
		}
		
		if (article.getEtatVente() == 2) {
	        // Finaliser les ventes pour obtenir les informations de l'acheteur
	        Enchere meilleureEnchere = enchereService.consulterEnchereParArticle(article.getNoArticle());
	        model.addAttribute("enchere", meilleureEnchere);
	        model.addAttribute("acheteur", meilleureEnchere.getEncherisseur());
	    }

		
		model.addAttribute("enchere", enchere);
		model.addAttribute("enchereDTO", new EnchereDTO()); // Ajouter un DTO vide pour le formulaire
		return "article-detail";
	}

	@PostMapping("/encherir")
	public String creerEnchereSurArticle(@Valid @ModelAttribute("enchereDTO") EnchereDTO enchereDTO, BindingResult br,
			Authentication authentication, Model model) {

		// Vérification des erreurs de validation
		if (br.hasErrors()) {
			model.addAttribute("erreur", "Le formulaire contient des erreurs.");
			return preparerVueDetailArticle(enchereDTO.getArticleId(), model, authentication);
		}

		// Récupérer l'utilisateur authentifié
		Utilisateur encherisseur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (encherisseur == null) {
			model.addAttribute("erreur", "Utilisateur introuvable.");
			return "error-page";
		}

		try {
			// Appeler la logique métier pour créer une enchère
			articleVenduService.encherir(enchereDTO.getArticleId(), enchereDTO.getMontant(), encherisseur);
		} catch (BusinessException e) {
			// Gestion des exceptions métiers
			model.addAttribute("erreur", e.getMessage());
			return preparerVueDetailArticle(enchereDTO.getArticleId(), model, authentication);
		}

		// Redirection après une enchère réussie
		return "redirect:/article-detail?noArticle=" + enchereDTO.getArticleId();
	}

	/*
	 * @GetMapping({"/", "/index"}) public String showForm(Model model,
	 * Authentication authentication) { Enchere enchere = new Enchere(); // L'objet
	 * qui contient le champ categorie model.addAttribute("enchere", enchere); //
	 * L'ajouter au modèle List<Categorie> categories =
	 * categorieService.getAllCategories(); model.addAttribute("categories",
	 * categories); Utilisateur vendeur =
	 * this.addressUser.afficherUtilisateurParPseudo(authentication.getName()); //
	 * Vérification que l'utilisateur existe et est co if (vendeur == null) {
	 * model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " +
	 * authentication.getName()); return "error-page"; // Une page d'erreur
	 * Thymeleaf personnalisée } model.addAttribute("utilisateur", vendeur); return
	 * "index"; }
	 */
	/*
	 * @GetMapping("/sell-article") public String afficherVendreArticle(Model model)
	 * { String pseudoUser = addressUser.afficherUtilisateurParPseudo(); Utilisateur
	 * user = addressUser.afficherUtilisateurParPseudo(noUtilisateur);
	 * user.getRue(); user.getCodePostal(); user.getVille();
	 * model.addAttribute("utilisateur", user); return "sell-article"; }
	 */
	/*
	 * @GetMapping("/encheres") public String showCategories(@RequestParam(name =
	 * "categorie", required = false) Integer categorieId, Model model,
	 * Authentication authentication) { if (categorieId != null) { // Récupérer les
	 * articles pour cette catégorie model.addAttribute("articles",
	 * CategorieService.getArticlesByCategorie(categorieId)); }
	 * model.addAttribute("categories", categorieService.getAllCategories());
	 * Utilisateur vendeur =
	 * this.addressUser.afficherUtilisateurParPseudo(authentication.getName()); //
	 * Vérification que l'utilisateur existe et est co if (vendeur == null) {
	 * model.addAttribute("erreur", "Aucun utilisateur trouvé avec le pseudo : " +
	 * authentication.getName()); return "error-page"; // Une page d'erreur
	 * Thymeleaf personnalisée } model.addAttribute("utilisateur", vendeur);
	 * List<Enchere> encheres = this.enchereService.afficherEncheres();
	 * System.out.println("recupere liste encheres" +encheres); return "encheres"; }
	 */

}