package fr.eni.project.dal;

import java.util.List;
import fr.eni.project.bo.Enchere;

public interface EnchereDAO {

	void createEnchere (Enchere newEnchere);
	List<Enchere> findAll();
}
