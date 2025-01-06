package fr.eni.project.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ArticleVendu {

	@Id
	private long noArticle;
	@NotBlank(message = "Le nom de l''article est obligatoire.")
	private String nomArticle;
	@NotBlank(message = "La description de l''article est obligatoire.")
	private String description;
	@NotNull(message = "La date et l''heure du début de l''enchère sont obligatoires.")
	private LocalDateTime dateDebutEncheres;
	@NotNull(message = "La date et l''heure de fin de l''enchère sont obligatoires.")
	private LocalDateTime dateFinEncheres;
	@NotNull(message = "Le prix de départ de l''enchère est obligatoire.")
	@Min(0)
	private int miseAPrix;
	private int prixVente;
	private int etatVente;
	private long noUtilisateur;
	private long noCategorie;

	// relations
	private Utilisateur vendeur;
	private List<Enchere> encheres = new ArrayList<Enchere>();
	private Categorie categorie;
	private Retrait lieuRetrait;

	private String imagePath;

	public ArticleVendu() {
	}

	// Constructeur avec paramètres
	public ArticleVendu(long noArticle, @NotBlank(message = "Le nom de l''article est obligatoire.") String nomArticle,
			@NotBlank(message = "La description de l''article est obligatoire.") String description,
			@NotNull(message = "La date et l''heure du début de l''enchère sont obligatoires.") LocalDateTime dateDebutEncheres,
			@NotNull(message = "La date et l''heure de fin de l''enchère sont obligatoires.") LocalDateTime dateFinEncheres,
			@NotNull(message = "Le prix de départ de l''enchère est obligatoire.") @Min(0) int miseAPrix, int prixVente,
			int etatVente, long noUtilisateur, long noCategorie, Utilisateur vendeur, List<Enchere> encheres,
			Categorie categorie, Retrait lieuRetrait, String imagePath) {
		this.noArticle = noArticle;
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.miseAPrix = miseAPrix;
		this.prixVente = prixVente;
		this.setEtatVente(etatVente);
		this.noUtilisateur = noUtilisateur;
		this.noCategorie = noCategorie;
		this.vendeur = vendeur;
		this.encheres = encheres;
		this.categorie = categorie;
		this.lieuRetrait = lieuRetrait;
		this.imagePath = imagePath;
	}

	// Getters et setters
	public long getNoArticle() {
		return noArticle;
	}

	public void setNoArticle(long noArticle) {
		this.noArticle = noArticle;
	}

	public String getNomArticle() {
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateDebutEncheres() {
		return dateDebutEncheres;
	}

	public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
		this.dateDebutEncheres = dateDebutEncheres;
	}

	public LocalDateTime getDateFinEncheres() {
		return dateFinEncheres;
	}

	public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
		this.dateFinEncheres = dateFinEncheres;
	}

	public int getMiseAPrix() {
		return miseAPrix;
	}

	public void setMiseAPrix(int miseAPrix) {
		this.miseAPrix = miseAPrix;
	}

	public int getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(int prixVente) {
		this.prixVente = prixVente;
	}

	public long getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public long getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(long noCategorie) {
		this.noCategorie = noCategorie;
	}

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public Retrait getLieuRetrait() {
		return lieuRetrait;
	}

	public void setLieuRetrait(Retrait lieuRetrait) {
		this.lieuRetrait = lieuRetrait;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return String.format(
				"ArticleVendu [noArticle=%s, nomArticle=%s, description=%s, dateDebutEncheres=%s, dateFinEncheres=%s, miseAPrix=%s, prixVente=%s, noUtilisateur=%s, noCategorie=%s, vendeur=%s, encheres=%s, categorie=%s, lieuRetrait=%s, imagePath=%s]",
				noArticle, nomArticle, description, dateDebutEncheres, dateFinEncheres, miseAPrix, prixVente,
				noUtilisateur, noCategorie, vendeur, encheres, categorie, lieuRetrait, imagePath);
	}

	public int getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(int etatVente) {
		this.etatVente = etatVente;
	}
}
