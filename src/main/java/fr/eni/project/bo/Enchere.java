package fr.eni.project.bo;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Min;

public class Enchere {

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

	public Enchere(LocalDateTime dateEnchere, int montantEnchere, Utilisateur encherisseur,
			ArticleVendu articleVendu) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.encherisseur = encherisseur;
		this.articleVendu = articleVendu;

	}

	public LocalDateTime getDateEnchere() {
		return dateEnchere;
	}

	public void setDateEnchere(LocalDateTime dateEnchere) {
		this.dateEnchere = dateEnchere;
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

	public int getMontantEnchere() {
		return montantEnchere;
	}

	public void setMontantEnchere(int montantEnchere) {
		this.montantEnchere = montantEnchere;
	}

	@Override
	public String toString() {
		return String.format("Enchere [dateEncheres=%s, montantEnchere=%s, encherisseur=%s, articleVendu=%s]",
				dateEnchere, montantEnchere, encherisseur, articleVendu);
	}
	
	

}
