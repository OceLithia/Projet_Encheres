package fr.eni.project.dto;

public class FiltreDTO {

	private Long idCat; // Filtre par catégorie
	private String motCle; // Filtre par mot-clé

	public Long getIdCat() {
		return idCat;
	}

	public void setIdCat(Long idCat) {
		this.idCat = idCat;
	}

	public String getMotCle() {
		return motCle;
	}

	public void setMotCle(String motCle) {
		this.motCle = motCle;
	}
}
