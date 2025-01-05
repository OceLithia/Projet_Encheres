package fr.eni.project.bll;

import java.util.List;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Filtre;
import fr.eni.project.exception.BusinessException;

public interface CategorieService {

	void addCategory(Categorie category);
	
	List<Categorie> getAllCategories();

	void rechercherParFiltre(Filtre filtre) throws BusinessException;

	static Object getArticlesByCategorie(Integer categorieId) {
		// TODO Auto-generated method stub
		return categorieId;
	}
	
	Categorie consulterCategorieParId(long id);

	List<Categorie> afficherCategories();
}
