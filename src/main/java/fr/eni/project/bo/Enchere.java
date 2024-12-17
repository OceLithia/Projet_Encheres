package fr.eni.project.bo;

import java.time.LocalDate;

public class Enchere {

	private LocalDate dateEncheres;
	private int montant_enchere;
	private long noUtilisateur;
	private long noArticle;
	
	public Enchere(LocalDate dateEncheres, int montant_enchere, long noUtilisateur, long noArticle) {
		super();
		this.dateEncheres = dateEncheres;
		this.montant_enchere = montant_enchere;
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

	@Override
	public String toString() {
		return "Enchere [dateEncheres=" + dateEncheres + ", montant_enchere=" + montant_enchere + ", noUtilisateur="
				+ noUtilisateur + ", noArticle=" + noArticle + "]";
	}
	
	

	
	
	
}
