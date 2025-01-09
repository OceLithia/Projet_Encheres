package fr.eni.project.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

	@GetMapping("/sell-article")
	public String afficherVendreArticle(Authentication authentication, Model model) {
		// Création d'un nouvel article à vendre
		ArticleVendu articleVendu = new ArticleVendu();
		// récupérer date et heure actuelle
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
		/*
		 * // ajouter la date l'heure model.addAttribute("currentDateTime",
		 * currentDateTime);
		 */

		return "sell-article";
	}

	@PostMapping("/sell-article")
	public String createSellArticle(@Valid @ModelAttribute ArticleVendu articleVendu, 
            BindingResult bindingResult,
            @RequestParam("image") MultipartFile image,
            @RequestParam("rueRetrait") String rueRetrait,
            @RequestParam("codePostalRetrait") String codePostalRetrait,
            @RequestParam("villeRetrait") String villeRetrait,
            Model model, 
            Authentication authentication) {
		if (bindingResult.hasErrors()) {
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
				articleVenduService.addNewArticle(vendeur, articleVendu, rueRetrait, codePostalRetrait, villeRetrait);
				return "redirect:/article-detail?noArticle=" + articleVendu.getNoArticle();

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
	 @GetMapping("/encheres")
	    public String showEncherePage(Model model) {
	        // Exemple : une date de fin d'enchère
	        LocalDateTime dateFinEncheres = LocalDateTime.now();
	        
	        // Formater la date en ISO 8601
	        String dateFinEncheresFormatee = dateFinEncheres.format(DateTimeFormatter.ISO_DATE_TIME);

	        // Ajouter la variable au modèle
	        model.addAttribute("dateFinEncheresFormatee", dateFinEncheresFormatee);

	        return "encheres";  // Nom du template Thymeleaf
	    }
	 
	@GetMapping({ "/article-detail", "/encherir" })
	public String afficherDetailsArticle(@RequestParam("noArticle") long id, Model model,
			Authentication authentication) {
		ArticleVendu article = this.articleVenduService.afficherArticleParNoArticle(id);
		System.out.println(article.getEtatVente());
		Utilisateur utilisateur = utilisateurService.afficherUtilisateurParPseudo(authentication.getName());
		if (article.getDateFinEncheres() != null) {
            model.addAttribute("dateFinEncheresFormatee", article.getDateFinEncheres().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
		} else {
            model.addAttribute("dateFinEncheresFormatee", "Date non disponible");
        }
		if (article.getDateDebutEncheres() != null) {
            model.addAttribute("dateDebutEncheresFormatee", article.getDateDebutEncheres().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")));
        } else {
            model.addAttribute("dateDebutEncheresFormatee", "Date non disponible");
        }
		model.addAttribute("articleVendu", article);
		model.addAttribute("utilisateur", utilisateur);

		// Afficher le chemin de l'image de l'article
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
		model.addAttribute("articleVendu", article); 
		model.addAttribute("categories", categorieService.getAllCategories()); 
		return "article-update";
	}

	@PostMapping("/article/update")
	public String updateArticle(@ModelAttribute("articleVendu") ArticleVendu articleVendu, @ModelAttribute Retrait adresseRetrait, @RequestParam(value = "image", required = false) MultipartFile image,
			RedirectAttributes redirectAttributes) {
	
		try {
			// Gérer le téléchargement de l'image seulement si une nouvelle image est fournie
			if (image != null && !image.isEmpty()) {
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
			if (articleVendu.getCategorie().getNoCategorie() == 0) {
			    throw new IllegalArgumentException("La catégorie de l'article ne peut pas être nulle");
			}
			articleVenduService.saveUpdate(articleVendu, adresseRetrait);
			redirectAttributes.addFlashAttribute("successMessage", "L'article a été mis à jour avec succès.");
	    } catch (IllegalArgumentException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
	    } catch (IOException e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la sauvegarde de l'image : " + e.getMessage());
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage",
	                "Une erreur est survenue lors de la mise à jour de l'article : " + e.getMessage());
	    }
		return "redirect:/article-detail?noArticle=" + articleVendu.getNoArticle();
	}
}