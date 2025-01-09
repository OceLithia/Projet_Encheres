package fr.eni.project.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Retrait {

    @NotBlank(message = "L'adresse de retrait est obligatoire")
    private String rue;
    
    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "\\d{5}", message = "Le code postal doit contenir 5 chiffres")
    private String codePostal;
    
    @NotBlank(message = "La ville est obligatoire")
    private String ville;
	
	//relation 
	private ArticleVendu article;
	
	public Retrait() {
	}

	public Retrait(@NotBlank(message = "L'adresse de retrait est obligatoire") String rue,
			@NotBlank(message = "Le code postal est obligatoire") @Pattern(regexp = "\\d{5}", message = "Le code postal doit contenir 5 chiffres") String codePostal,
			@NotBlank(message = "La ville est obligatoire") String ville, ArticleVendu article) {
		super();
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.article = article;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Retrait [rue=" + rue + ", codePostal=" + codePostal + ", ville=" + ville + ", article=" + article + "]";
	}

}
