package fr.eni.project.bo;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.Min;

public class Enchere {

	private long noEnchere;
	private LocalDateTime dateEnchere;
	@Min(1)
	private int montantEnchere;

	// relations
	// user qui fait l'enchere
	private Utilisateur encherisseur;
	// article sur lequel l'enchere est faite
	private ArticleVendu articleVendu;

	public Enchere() {
	}

	public Enchere(LocalDateTime dateEnchere, @Min(1) int montantEnchere, Utilisateur encherisseur,
			ArticleVendu articleVendu) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.encherisseur = encherisseur;
		this.articleVendu = articleVendu;
	}

	public long getNoEnchere() {
		return noEnchere;
	}

	public void setNoEnchere(long noEnchere) {
		this.noEnchere = noEnchere;
	}

	public LocalDateTime getDateEnchere() {
		return dateEnchere;
	}

	public void setDateEnchere(LocalDateTime dateEnchere) {
		this.dateEnchere = dateEnchere;
	}

	public int getMontantEnchere() {
		return montantEnchere;
	}

	public void setMontantEnchere(int montantEnchere) {
		this.montantEnchere = montantEnchere;
	}

	public Utilisateur getEncherisseur() {
		return encherisseur;
	}

	public void setEncherisseur(Utilisateur encherisseur) {
		this.encherisseur = encherisseur;
	}

	public ArticleVendu getArticleVendu() {
		return articleVendu;
	}

	public void setArticleVendu(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}

	@Override
	public String toString() {
		return "Enchere [noEnchere=" + noEnchere + ", dateEnchere=" + dateEnchere + ", montantEnchere=" + montantEnchere
				+ ", encherisseur=" + encherisseur + ", articleVendu=" + articleVendu + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(noEnchere);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enchere other = (Enchere) obj;
		return noEnchere == other.noEnchere;
	}

}
