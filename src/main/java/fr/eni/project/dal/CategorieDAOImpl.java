package fr.eni.project.dal;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
	
	private static final String INSERT = "INSERT INTO CATEGORIES (libelle) VALUES (:libelle)";
	private static final String FIND_ALL = "SELECT no_categorie, libelle FROM CATEGORIES";
	
	private NamedParameterJdbcTemplate jdbcTemplate;

	public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void createCategory(Categorie newCategory) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("libelle", newCategory.getLibelle());
		jdbcTemplate.update(INSERT, map, keyHolder);
		if (keyHolder.getKey() != null) {
			newCategory.setNoCategorie(keyHolder.getKey().longValue());
		}
	}

	@Override
	public List<Categorie> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
	}
/*
	@Override
	public Categorie afficherCategories(long noCategorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noCategorie", noCategorie);
		
		return this.jdbcTemplate.queryForObject(noCategorie, map, new BeanPropertyRowMapper<>(Categorie.class));
	}
*/

	@Override
	public Categorie afficherCategories(long noCategorie) {
		// TODO Auto-generated method stub
		return null;
	}
}
