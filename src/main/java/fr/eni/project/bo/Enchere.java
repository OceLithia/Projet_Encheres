package fr.eni.project.bo;

import java.time.LocalDate;


public class Enchere {

	private LocalDate dateEncheres;
	private int montant_enchere;
	private long noUtilisateur;
	private long noArticle;
	
	//relations
	//user qui fait l'enchere
	private Utilisateur acheteur;
	//article sur lequel l'enchere est faite
	private ArticleVendu articleVendu;
	

	
	private Categorie categorie;
	
	public Enchere(Categorie categorie) {
		super();
		this.categorie = categorie;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}



	public Enchere() {
	}

	
	public Enchere(LocalDate dateEncheres, int montant_enchere, Utilisateur acheteur, ArticleVendu articleVendu, long noUtilisateur, long noArticle) {
		this.dateEncheres = dateEncheres;
		this.montant_enchere = montant_enchere;
		this.acheteur = acheteur;
		this.articleVendu = articleVendu;
		this.noUtilisateur = noUtilisateur;
		this.noArticle = noArticle;

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

	public long getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public long getNoArticle() {
		return noArticle;
	}

	public void setNoArticle(long noArticle) {
		this.noArticle = noArticle;
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
		return "Enchere [dateEncheres=" + dateEncheres + ", montant_enchere=" + montant_enchere + ", noUtilisateur="
				+ noUtilisateur + ", noArticle=" + noArticle + ", acheteur=" + acheteur + ", articleVendu="
				+ articleVendu + "]";
	}

}
	
