package fr.eni.project.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.Utilisateur;

@Repository
public class RetraitDAOImpl implements RetraitDAO {

	private static final String INSERT = "INSERT INTO RETRAITS (no_article,rue,code_postal,ville) VALUES (:no_article, :rue, :code_postal, :ville)";
	
	@Autowired
	public NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public void insertLieuRetraitParDefaut(long noArticle, Utilisateur vendeur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", noArticle);
		map.addValue("rue", vendeur.getRue());
		map.addValue("code_postal", vendeur.getCodePostal());
		map.addValue("ville", vendeur.getVille());
		jdbcTemplate.update(INSERT, map);
	}

	@Override
	public void insertLieuRetrait(long noArticle, String rue, String codePostal, String ville) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", noArticle);
		map.addValue("rue", rue);
		map.addValue("code_postal", codePostal);
		map.addValue("ville", ville);
		jdbcTemplate.update(INSERT, map);
	}
	
}
