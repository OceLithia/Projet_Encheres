package fr.eni.project.bo;

public class Filtre {

	private Categorie categorie;
	private String motCle;
	
	public Filtre(Categorie categorie, String motCle) {
		super();
		this.categorie = categorie;
		this.motCle = motCle;
	}
	

	public Filtre() {
		super();
	}


	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public String getMotCle() {
		return motCle;
	}

	public void setMotCle(String motCle) {
		this.motCle = motCle;
	}

	@Override
	public String toString() {
		return "Filtre [categorie=" + categorie + ", motCle=" + motCle + "]";
	}
	
	
	
}
