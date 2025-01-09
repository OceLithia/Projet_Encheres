package fr.eni.project.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ArticleVendu {

	private long noArticle;

	@NotBlank(message = "Le nom de l'article est obligatoire")
	@Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 150 caractères")
	private String nomArticle;

	@NotBlank(message = "La description est obligatoire")
	@Size(min = 10, max = 1000, message = "La description doit contenir entre 10 et 1000 caractères")
	private String description;

	@NotNull(message = "La date de début des enchères est obligatoire")
	@Future(message = "La date de début doit être future")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime dateDebutEncheres;

	@NotNull(message = "La date de fin des enchères est obligatoire")
	@Future(message = "La date de fin doit être future")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime dateFinEncheres;

	@NotNull(message = "Le prix initial est obligatoire")
	@Min(value = 1, message = "Le prix initial doit être supérieur à 0")
	@Max(value = 100000, message = "Le prix initial ne peut pas dépasser 100 000")
	private Integer miseAPrix;

	private Integer prixVente;
	private Integer etatVente;
	private long noUtilisateur;
	private Long noCategorie;

	// relations
	private Utilisateur vendeur;
	private List<Enchere> encheres = new ArrayList<Enchere>();
	private Categorie categorie;
	private Retrait lieuRetrait;

	private String imagePath;

	public ArticleVendu() {
	}

	public ArticleVendu(long noArticle,
			@NotBlank(message = "Le nom de l'article est obligatoire") @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 150 caractères") String nomArticle,
			@NotBlank(message = "La description est obligatoire") @Size(min = 10, max = 1000, message = "La description doit contenir entre 10 et 1000 caractères") String description,
			@NotNull(message = "La date de début des enchères est obligatoire") @Future(message = "La date de début doit être future") LocalDateTime dateDebutEncheres,
			@NotNull(message = "La date de fin des enchères est obligatoire") @Future(message = "La date de fin doit être future") LocalDateTime dateFinEncheres,
			@NotNull(message = "Le prix initial est obligatoire") @Min(value = 1, message = "Le prix initial doit être supérieur à 0") @Max(value = 100000, message = "Le prix initial ne peut pas dépasser 100 000") Integer miseAPrix,
			Integer prixVente, Integer etatVente, long noUtilisateur, Long noCategorie, Utilisateur vendeur,
			List<Enchere> encheres, Categorie categorie, Retrait lieuRetrait, String imagePath) {
		super();
		this.noArticle = noArticle;
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEncheres = dateDebutEncheres;
		this.dateFinEncheres = dateFinEncheres;
		this.miseAPrix = miseAPrix;
		this.prixVente = prixVente;
		this.etatVente = etatVente;
		this.noUtilisateur = noUtilisateur;
		this.noCategorie = noCategorie;
		this.vendeur = vendeur;
		this.encheres = encheres;
		this.categorie = categorie;
		this.lieuRetrait = lieuRetrait;
		this.imagePath = imagePath;
	}

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

	public Integer getMiseAPrix() {
		return miseAPrix;
	}

	public void setMiseAPrix(Integer miseAPrix) {
		this.miseAPrix = miseAPrix;
	}

	public Integer getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Integer prixVente) {
		this.prixVente = prixVente;
	}

	public Integer getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(Integer etatVente) {
		this.etatVente = etatVente;
	}

	public long getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public Long getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(Long noCategorie) {
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
		return "ArticleVendu [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description
				+ ", dateDebutEncheres=" + dateDebutEncheres + ", dateFinEncheres=" + dateFinEncheres + ", miseAPrix="
				+ miseAPrix + ", prixVente=" + prixVente + ", etatVente=" + etatVente + ", noUtilisateur="
				+ noUtilisateur + ", noCategorie=" + noCategorie + ", vendeur=" + vendeur + ", encheres=" + encheres
				+ ", categorie=" + categorie + ", lieuRetrait=" + lieuRetrait + ", imagePath=" + imagePath + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(noArticle);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleVendu other = (ArticleVendu) obj;
		return noArticle == other.noArticle;
	}

}
