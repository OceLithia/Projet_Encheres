package fr.eni.project.dto;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

public class ArticleDTO {
    private String nomArticle;
    private String dateFinFormattee;
    private Utilisateur vendeur;
    private String imagePath;
    private Integer prixVente;
    private Long noArticle;

    // Constructeur à partir de ArticleVendu
    public ArticleDTO(ArticleVendu article, String dateFinFormattee) {
        this.nomArticle = article.getNomArticle();
        this.dateFinFormattee = dateFinFormattee;
        this.vendeur = article.getVendeur();
        this.imagePath = article.getImagePath();
        this.prixVente = article.getPrixVente();
        this.noArticle = article.getNoArticle();
    }

	public String getNomArticle() {
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}

	public String getDateFinFormattee() {
		return dateFinFormattee;
	}

	public void setDateFinFormattee(String dateFinFormattee) {
		this.dateFinFormattee = dateFinFormattee;
	}

	public Utilisateur getVendeur() {
		return vendeur;
	}

	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Integer prixVente) {
		this.prixVente = prixVente;
	}

	public Long getNoArticle() {
		return noArticle;
	}

	public void setNoArticle(Long noArticle) {
		this.noArticle = noArticle;
	}

}

