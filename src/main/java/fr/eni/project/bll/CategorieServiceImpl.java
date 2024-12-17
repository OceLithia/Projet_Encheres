package fr.eni.project.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.project.bo.Categorie;
import fr.eni.project.dal.CategorieDAO;

@Service
public class CategorieServiceImpl implements CategorieService {
	
	private CategorieDAO categorieDAO;

	public CategorieServiceImpl(CategorieDAO categorieDAO) {
		this.categorieDAO = categorieDAO;
	}

	@Override
	public List<Categorie> readCategory() {
		return this.categorieDAO.findAll();
	}

	@Override
	public void addCategory(Categorie category) {
		categorieDAO.createCategory(category);
	}

}
