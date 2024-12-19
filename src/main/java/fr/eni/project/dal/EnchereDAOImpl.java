package fr.eni.project.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.Enchere;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	private static final String FIND_ALL = "SELECT * FROM ENCHERES";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public void createEnchere(Enchere newEnchere) {
		
	}

	@Override
	public List<Enchere> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Enchere.class));
	}

	
}
