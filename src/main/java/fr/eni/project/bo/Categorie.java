package fr.eni.project.bo;

public class Categorie {

	private long noCategorie;
	private String libelle;
	
	//relation
	private ArticleVendu article;

	public Categorie(long noCategorie, String libelle, ArticleVendu article) {
		this.noCategorie = noCategorie;
		this.libelle = libelle;
		this.article = article;
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

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Categorie [noCategorie=" + noCategorie + ", libelle=" + libelle + ", article=" + article + "]";
	}
	
}
