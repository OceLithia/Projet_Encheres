package fr.eni.project.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.project.bll.ArticleVenduService;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Retrait;
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
					String fileName = System.currentTimeMillis() + "_"
							+ URLEncoder.encode(image.getOriginalFilename(), "UTF-8");
					String uploadDir = "src/main/resources/static/uploads/"; // Répertoire pour les images d'articles
					Path filePath = Path.of(uploadDir, fileName);
					// Créer les répertoires nécessaires s'ils n'existent pas
					Files.createDirectories(filePath.getParent());
					// Sauvegarder l'image sur le disque
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
				// Gestion des erreurs lors du téléchargement de l'image
				e.printStackTrace();
				model.addAttribute("erreur", "Erreur lors du téléchargement de l'image.");
				return "sell-article";
			}
		}
	}

	private String preparerVueDetailArticle(long articleId, Model model, Authentication authentication) {
		ArticleVendu article = articleVenduService.afficherArticleParNoArticle(articleId);
		Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		model.addAttribute("articleVendu", article);
		model.addAttribute("utilisateur", utilisateur);

	    Enchere enchere = null;
	    try {
	        enchere = enchereService.consulterDerniereEnchereParArticle(articleId);
	    } catch (EnchereNotFoundException e) {
	        // Pas d'enchère, rien à ajouter
	    }
	    
	    if (article.getEtatVente() == 2) {
	        // Finaliser la vente si nécessaire
	        Enchere meilleureEnchere = enchereService.consulterDerniereEnchereParArticle(articleId);
	        if (meilleureEnchere != null) {
	        	model.addAttribute("enchere", meilleureEnchere);
		        model.addAttribute("acheteur", meilleureEnchere.getEncherisseur());
			}
	    } else {
	        model.addAttribute("enchere", enchere);
	    }

		return "article-detail";
	}

	@GetMapping({ "/article-detail", "/encherir" })
	public String afficherDetailsArticle(@RequestParam("noArticle") long id, Model model,
			Authentication authentication) {
		ArticleVendu article = this.articleVenduService.afficherArticleParNoArticle(id);
		System.out.println(article.getEtatVente());
		Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (article.getDateFinEncheres() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dateFormatee = article.getDateFinEncheres().format(formatter);
            model.addAttribute("dateFinEncheresFormatee", dateFormatee);
        } else {
            model.addAttribute("dateFinEncheresFormatee", "Date non disponible");
        }
		model.addAttribute("articleVendu", article);
		model.addAttribute("utilisateur", utilisateur);

		// Afficher le chemin de l'image de l'article
		System.out.println("Chemin sauvegardé : " + article.getImagePath());
		model.addAttribute("imagePath", article.getImagePath());

		Enchere enchere;
		try {
		    enchere = enchereService.consulterDerniereEnchereParArticle(id);
		    model.addAttribute("enchere", enchere);
		} catch (EnchereNotFoundException e) {
		    System.out.println("Aucune enchère trouvée pour l'article ID : " + id);
		    model.addAttribute("enchere", null); // Aucun enchère à afficher
		}


		if (article.getEtatVente() == 2) {
			System.out.println("etat de la vente si 2 : "+article.getEtatVente());
	        // Finaliser les ventes pour obtenir les informations de l'acheteur
	        Enchere meilleureEnchere = enchereService.consulterDerniereEnchereParArticle(article.getNoArticle());
	        model.addAttribute("enchere", meilleureEnchere);
	        model.addAttribute("acheteur", meilleureEnchere.getEncherisseur());
	    }

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
			ArticleVendu article = articleVenduService.afficherArticleParNoArticle(enchereDTO.getArticleId());
			articleVenduService.mettreAJourArticle(article, article.getVendeur());
		} catch (BusinessException e) {
			// Gestion des exceptions métiers
			model.addAttribute("erreur", e.getMessage());
			return preparerVueDetailArticle(enchereDTO.getArticleId(), model, authentication);
		}

		// Redirection après une enchère réussie
		return "redirect:/article-detail?noArticle=" + enchereDTO.getArticleId();
	}

	@GetMapping("/delete-article-detail")
	public String supprimerArticle(@RequestParam("noArticle") Long idArticle, RedirectAttributes redirectAttributes) {
		// Vérifie si l'article existe
		ArticleVendu article = this.articleVenduService.afficherArticleParNoArticle(idArticle);
		if (article == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "L'article avec l'ID " + idArticle + " n'existe pas.");
			return "redirect:/";
		}
		// Supprime l'article
		this.articleVenduService.supprimerArticle(article);
		redirectAttributes.addFlashAttribute("successMessage", "Votre article a été supprimé avec succès.");
		return "redirect:/";
	}

	@GetMapping("/article/update")
	public String viewArticleUpdate(@RequestParam("noArticle") Long noArticle, RedirectAttributes redirectAttributes,
			Model model) {
		// Récupérer l'article depuis le service
		ArticleVendu article = this.articleVenduService.afficherArticleParNoArticle(noArticle);

		// Vérifier si l'article existe
		if (article == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "L'article avec l'ID " + noArticle + " n'existe pas.");
			return "redirect:/article-detail?noArticle=" + noArticle;
		}
		// Ajouter l'article et les catégories au modèle
		model.addAttribute("articleVendu", article); // Changé de "article" à "articleVendu" pour correspondre au
														// th:object
		model.addAttribute("categories", categorieService.getAllCategories()); // Ajout des catégories nécessaires pour le select
		return "article-update";
	}

	@PostMapping("/article/update")
	public String updateArticle(@ModelAttribute("articleVendu") ArticleVendu articleVendu, @ModelAttribute Retrait adresseRetrait, @RequestParam(value = "image", required = false) MultipartFile image,
			RedirectAttributes redirectAttributes) {
		System.out.println("postemapp");
		
		try {
			System.out.println("try");
			// Gérer le téléchargement de l'image seulement si une nouvelle image est fournie
			if (image != null && !image.isEmpty()) {
				/*
				 * // Validation du type de fichier String contentType = image.getContentType();
				 * System.out.println("if image"); if (!contentType.startsWith("image/")) {
				 * throw new
				 * IllegalArgumentException("Le fichier téléchargé doit être une image."); } //
				 * Générer un nom unique pour l'image String fileName =
				 * System.currentTimeMillis() + "_" +
				 * URLEncoder.encode(image.getOriginalFilename(), StandardCharsets.UTF_8); //
				 * Définir le chemin de sauvegarde String uploadDir =
				 * "src/main/resources/static/uploads/"; Path uploadPath = Paths.get(uploadDir);
				 * //chemin complet Path filePath = uploadPath.resolve(fileName); // Créer le
				 * répertoire s'il n'existe pas if (!Files.exists(uploadPath)) {
				 * Files.createDirectories(uploadPath); // Donner les permissions de
				 * lecture/écriture uploadPath.toFile().setWritable(true, false);
				 * uploadPath.toFile().setReadable(true, false); } // Sauvegarder l'image
				 * Files.copy(image.getInputStream(), filePath,
				 * StandardCopyOption.REPLACE_EXISTING); // Mettre à jour le chemin de l'image
				 * dans l'entité articleVendu.setImagePath(fileName); // Assurer que le chemin
				 * relatif est bien enregistré System.out.println(fileName);
				 */
				System.out.println("if");
				// Nom unique pour l'image
				String fileName = System.currentTimeMillis() + "_"
						+ URLEncoder.encode(image.getOriginalFilename(), "UTF-8");
				String uploadDir = "src/main/resources/static/uploads/"; // Répertoire pour les images d'articles
				Path filePath = Path.of(uploadDir, fileName);
				// Créer les répertoires nécessaires s'ils n'existent pas
				Files.createDirectories(filePath.getParent());
				// Sauvegarder l'image sur le disque
				Files.write(filePath, image.getBytes());
				// Stocker le chemin dans l'entité
				articleVendu.setImagePath(fileName);
			}
			System.out.println("categorie : "+articleVendu.getCategorie());
			if (articleVendu.getCategorie().getNoCategorie() == 0) {
				System.out.println("if cat");
			    // Traitez cette situation, soit en affectant une valeur par défaut, soit en levant une exception
			    throw new IllegalArgumentException("La catégorie de l'article ne peut pas être nulle");
			}
			System.out.println("avant d'update");
			// Sauvegarder l'article et son adresse de retrait
			articleVenduService.savedUpdate(articleVendu, adresseRetrait);
			System.out.println("après update");
			redirectAttributes.addFlashAttribute("successMessage", "L'article a été mis à jour avec succès.");
	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
	    } catch (IOException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la sauvegarde de l'image : " + e.getMessage());
	    } catch (Exception e) {
	    	System.out.println(e);
	        redirectAttributes.addFlashAttribute("errorMessage",
	                "Une erreur est survenue lors de la mise à jour de l'article : " + e.getMessage());
	    }
		return "redirect:/article-detail?noArticle=" + articleVendu.getNoArticle();
	}
}