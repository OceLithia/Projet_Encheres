package fr.eni.project.dal;

import java.util.List;
import fr.eni.project.bo.Categorie;

public interface CategorieDAO {

	void createCategory(Categorie newCategory);
	
	List<Categorie> findAll();

	Categorie readById(long noCategorie);

}
