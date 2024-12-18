package fr.eni.project.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import fr.eni.project.bo.Categorie;
import fr.eni.project.dal.CategorieDAO;

@Service
public class CategorieServiceImpl implements CategorieService {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
	private CategorieDAO categorieDAO;

	public CategorieServiceImpl(CategorieDAO categorieDAO) {
		this.categorieDAO = categorieDAO;
	}

	@Override
	public List<Categorie> readCategory() {
		return this.categorieDAO.findAll();
	}

    public List<Categorie> getAllCategories() {
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Categorie categorie = new Categorie();
            categorie.setNoCategorie(rs.getInt("no_categorie"));
            categorie.setLibelle(rs.getString("libelle"));
            return categorie;
        });
    }
    
	@Override
	public void addCategory(Categorie category) {
		categorieDAO.createCategory(category);
	}
}