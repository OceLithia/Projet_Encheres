package fr.eni.project.bll;

import java.util.List;

import fr.eni.project.bo.Categorie;

public interface CategorieService {

	void addCategory(Categorie category);
	
	List<Categorie> readCategory();
	
}
