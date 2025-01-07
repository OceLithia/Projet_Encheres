package fr.eni.project.dto;

public class FiltreDTO {

	private Long idCat;
	private String motCle;
	private String typeFiltre;
	private Long utilisateurId;

	private Boolean ventesEnCours; 
	private Boolean ventesTerminees; 
	private Boolean ventesNonDebutees; 
	private Boolean encheresOuvertes;
	private Boolean encheresEnCours;
	private Boolean encheresRemportees;

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

	public Boolean getVentesEnCours() {
		return ventesEnCours;
	}

	public void setVentesEnCours(Boolean ventesEnCours) {
		this.ventesEnCours = ventesEnCours;
	}

	public Boolean getVentesTerminees() {
		return ventesTerminees;
	}

	public void setVentesTerminees(Boolean ventesTerminees) {
		this.ventesTerminees = ventesTerminees;
	}

	public Boolean getVentesNonDebutees() {
		return ventesNonDebutees;
	}

	public void setVentesNonDebutees(Boolean ventesNonDebutees) {
		this.ventesNonDebutees = ventesNonDebutees;
	}

	public Boolean getEncheresOuvertes() {
		return encheresOuvertes;
	}

	public void setEncheresOuvertes(Boolean encheresOuvertes) {
		this.encheresOuvertes = encheresOuvertes;
	}

	public Boolean getEncheresEnCours() {
		return encheresEnCours;
	}

	public void setEncheresEnCours(Boolean encheresEnCours) {
		this.encheresEnCours = encheresEnCours;
	}

	public Boolean getEncheresRemportees() {
		return encheresRemportees;
	}

	public void setEncheresRemportees(Boolean encheresRemportees) {
		this.encheresRemportees = encheresRemportees;
	}
}
