package fr.eni.project.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.Enchere;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	private static final String FIND_ALL = "SELECT * FROM ENCHERES";
	private static final String INSERT = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (:idUtilisateur, :idArticle, :dateDebutEnchere, :miseAPrix)";
	
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public void createEnchere(Enchere newEnchere) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", newEnchere.getNoUtilisateur());
		map.addValue("idArticle", newEnchere.getNoArticle());
		map.addValue("dateDebutEnchere", newEnchere.getDateEncheres());
		map.addValue("miseAPrix", newEnchere.getMontant_enchere());
		jdbcTemplate.update(INSERT, map);
	}

	@Override
	public List<Enchere> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}

	@Override
	public void updateEnchere(Enchere enchere) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		
	}

	
}
