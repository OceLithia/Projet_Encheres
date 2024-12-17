package fr.eni.project.bo;

import java.time.LocalDate;


public class Enchere {

	private LocalDate dateEncheres;
	private int montant_enchere;
	
	//relations
	//user qui fait l'enchere
	private Utilisateur acheteur;
	//article sur lequel l'enchere est faite
	private ArticleVendu articleVendu;
	
	public Enchere() {
	}

	public Enchere(LocalDate dateEncheres, int montant_enchere, Utilisateur acheteur, ArticleVendu articleVendu) {
		this.dateEncheres = dateEncheres;
		this.montant_enchere = montant_enchere;
		this.acheteur = acheteur;
		this.articleVendu = articleVendu;
	}

	public LocalDate getDateEncheres() {
		return dateEncheres;
	}

	public void setDateEncheres(LocalDate dateEncheres) {
		this.dateEncheres = dateEncheres;
	}

	public int getMontant_enchere() {
		return montant_enchere;
	}

	public void setMontant_enchere(int montant_enchere) {
		this.montant_enchere = montant_enchere;
	}

	public Utilisateur getUtilisateur() {
		return acheteur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.acheteur = utilisateur;
	}

	public ArticleVendu getArticleVendu() {
		return articleVendu;
	}

	public void setArticleVendu(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}

	@Override
	public String toString() {
		return "Enchere [dateEncheres=" + dateEncheres + ", montant_enchere=" + montant_enchere + ", utilisateur="
				+ acheteur + ", articleVendu=" + articleVendu + "]";
	}
	
}
