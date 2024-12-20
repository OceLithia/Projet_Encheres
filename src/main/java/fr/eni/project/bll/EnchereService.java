package fr.eni.project.bll;

import java.util.List;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;

public interface EnchereService {

	List<Enchere> afficherEncheres();
	
	Enchere consulterEnchereParId(long id);
	
	void addEnchere(Enchere	enchere);
	
	
}
