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
	
	//relations 
	//vendeur de l'article
	private Utilisateur vendeur;
	//encheres sur l'article
	private List<Enchere> encheres = new ArrayList<Enchere>();
	//categorie article
	private Categorie categorie;
	//vers retrait (1 lieu max)
	private Retrait lieuRetrait;
	
	public ArticleVendu() {
	}

	public ArticleVendu(long noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres,
			LocalDateTime dateFinEncheres, int miseAPrix, int prixVente, int etatVente, long noUtilisateur,
			long noCategorie, Utilisateur vendeur, List<Enchere> encheres, Categorie categorie,
			Retrait lieuRetrait) {
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

	public int getEtatVente() {
		return etatVente;
	}

	public void setEtatVente(int etatVente) {
		this.etatVente = etatVente;
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

	@Override
	public int hashCode() {
		return Objects.hash(lieuRetrait, noArticle, vendeur);
	}

	@Override
	public String toString() {
		return "ArticleVendu [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description
				+ ", dateDebutEncheres=" + dateDebutEncheres + ", dateFinEncheres=" + dateFinEncheres + ", miseAPrix="
				+ miseAPrix + ", prixVente=" + prixVente + ", etatVente=" + etatVente + ", noUtilisateur="
				+ noUtilisateur + ", noCategorie=" + noCategorie + ", vendeur=" + vendeur + ", encheres=" + encheres
				+ ", categoryArticle=" + categorie + ", lieuRetrait=" + lieuRetrait + "]";
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
		return Objects.equals(lieuRetrait, other.lieuRetrait) && noArticle == other.noArticle
				&& Objects.equals(vendeur, other.vendeur);
	}

	
}
