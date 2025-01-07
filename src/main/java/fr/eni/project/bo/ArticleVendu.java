package fr.eni.project.bo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ArticleVendu {

	private long noArticle;
	@NotBlank(message = "Le nom de l''article est obligatoire.")
	private String nomArticle;
	private String oldNomArticle;
	@NotBlank(message = "La description de l''article est obligatoire.")
	private String description;
	private String oldDescription;
	@NotNull(message = "La date et l''heure du début de l''enchère sont obligatoires.")
	private LocalDateTime dateDebutEncheres;
	private LocalDateTime oldDateDebutEncheres;
	@NotNull(message = "La date et l''heure de fin de l''enchère sont obligatoires.")
	private LocalDateTime dateFinEncheres;
	private LocalDateTime oldDateFinEncheres;
	@NotNull(message = "Le prix de départ de l''enchère est obligatoire.")
	@Min(0)
	private Integer miseAPrix;
	private Integer oldMiseAPrix;
	private Integer prixVente;
	private Integer oldPrixVente;
	private Integer etatVente;
	private Integer oldEtatVente;
	private long noUtilisateur;
	private Long noCategorie;
	private Long oldNoCategorie;

	// relations
	private Utilisateur vendeur;
	private List<Enchere> encheres = new ArrayList<Enchere>();
	private Categorie categorie;
	private Retrait lieuRetrait;

	private String imagePath;
	private String oldImagePath;

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
	//Getter Setter
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

	public String getOldNomArticle() {
		return oldNomArticle;
	}

	public void setOldNomArticle(String oldNomArticle) {
		this.oldNomArticle = oldNomArticle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOldDescription() {
		return oldDescription;
	}

	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
	}

	public LocalDateTime getDateDebutEncheres() {
		return dateDebutEncheres;
	}

	public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
		this.dateDebutEncheres = dateDebutEncheres;
	}

	public LocalDateTime getOldDateDebutEncheres() {
		return oldDateDebutEncheres;
	}

	public void setOldDateDebutEncheres(LocalDateTime oldDateDebutEncheres) {
		this.oldDateDebutEncheres = oldDateDebutEncheres;
	}

	public LocalDateTime getDateFinEncheres() {
		return dateFinEncheres;
	}

	public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
		this.dateFinEncheres = dateFinEncheres;
	}

	public LocalDateTime getOldDateFinEncheres() {
		return oldDateFinEncheres;
	}

	public void setOldDateFinEncheres(LocalDateTime oldDateFinEncheres) {
		this.oldDateFinEncheres = oldDateFinEncheres;
	}

	public Integer getMiseAPrix() {
		return miseAPrix;
	}

	public void setMiseAPrix(Integer miseAPrix) {
		this.miseAPrix = miseAPrix;
	}

	public Integer getOldMiseAPrix() {
		return oldMiseAPrix;
	}

	public void setOldMiseAPrix(Integer oldMiseAPrix) {
		this.oldMiseAPrix = oldMiseAPrix;
	}

	public Integer getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Integer prixVente) {
		this.prixVente = prixVente;
	}

	public Integer getOldPrixVente() {
		return oldPrixVente;
	}

	public void setOldPrixVente(Integer oldPrixVente) {
		this.oldPrixVente = oldPrixVente;
	}

	public Integer getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(Integer etatVente) {
		this.etatVente = etatVente;
	}

	public Integer getOldEtatVente() {
		return oldEtatVente;
	}

	public void setOldEtatVente(Integer oldEtatVente) {
		this.oldEtatVente = oldEtatVente;
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

	public Long getOldNoCategorie() {
		return oldNoCategorie;
	}

	public void setOldNoCategorie(Long oldNoCategorie) {
		this.oldNoCategorie = oldNoCategorie;
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

	public String getOldImagePath() {
		return oldImagePath;
	}

	public void setOldImagePath(String oldImagePath) {
		this.oldImagePath = oldImagePath;
	}

	@Override
	public String toString() {
		return "ArticleVendu [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", oldNomArticle="
				+ oldNomArticle + ", description=" + description + ", oldDescription=" + oldDescription
				+ ", dateDebutEncheres=" + dateDebutEncheres + ", oldDateDebutEncheres=" + oldDateDebutEncheres
				+ ", dateFinEncheres=" + dateFinEncheres + ", oldDateFinEncheres=" + oldDateFinEncheres + ", miseAPrix="
				+ miseAPrix + ", oldMiseAPrix=" + oldMiseAPrix + ", prixVente=" + prixVente + ", oldPrixVente="
				+ oldPrixVente + ", etatVente=" + etatVente + ", oldEtatVente=" + oldEtatVente + ", noUtilisateur="
				+ noUtilisateur + ", noCategorie=" + noCategorie + ", oldNoCategorie=" + oldNoCategorie + ", vendeur="
				+ vendeur + ", encheres=" + encheres + ", categorie=" + categorie + ", lieuRetrait=" + lieuRetrait
				+ ", imagePath=" + imagePath + ", oldImagePath=" + oldImagePath + "]";
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
