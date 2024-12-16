package fr.eni.project.bo;

import java.time.LocalDate;

public class Enchere {

	private LocalDate dateEncheres;
	private int montant_enchere;
	
	public Enchere(LocalDate dateEncheres, int montant_enchere) {
		super();
		this.dateEncheres = dateEncheres;
		this.montant_enchere = montant_enchere;
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

	@Override
	public String toString() {
		return "enchere [dateEncheres=" + dateEncheres + ", montant_enchere=" + montant_enchere + "]";
	}
	
	
	
}
