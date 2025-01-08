package fr.eni.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class EnchereDTO {

    @NotNull(message = "L'identifiant de l'article est requis.")
    private Long articleId;

    @NotNull(message = "Le montant de l'enchère est requis.")
    @Min(value = 1, message = "Le montant doit être supérieur à zéro.")
    private Integer montant;

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }
}
