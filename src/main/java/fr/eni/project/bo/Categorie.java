package fr.eni.project.bo;

public class Categorie {

	private long noCategorie;
	private String libelle;
	
	public Categorie(long noCategorie, String libelle) {
		super();
		this.noCategorie = noCategorie;
		this.libelle = libelle;
	}

	public long getNoCategorie() {
		return noCategorie;
	}

	public void setNoCategorie(long noCategorie) {
		this.noCategorie = noCategorie;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return "categorie [noCategorie=" + noCategorie + ", libelle=" + libelle + "]";
	}
	
	
	
}
