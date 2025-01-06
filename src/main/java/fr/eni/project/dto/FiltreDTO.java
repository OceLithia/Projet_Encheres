package fr.eni.project.dto;

public class FiltreDTO {

	private Long idCat; // Filtre par catégorie
	private String motCle; // Filtre par mot-clé
	private String typeFiltre; //Filtre par achat ou vente. 0 = achat, 1 = vente
	private Long utilisateurId;
	
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

	public String getTypeFiltre() {
		return typeFiltre;
	}

	public void setTypeFiltre(String typeFiltre) {
		this.typeFiltre = typeFiltre;
	}

	public Long getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(Long utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	
}
