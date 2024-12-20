package fr.eni.project.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.tree.RowMapper;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {

	private static final String INSERT = "INSERT INTO ARTICLES_VENDUS (nom_article, no_categorie, description, date_debut_encheres, date_fin_encheres, prix_initial, no_utilisateur)"
			+ "VALUES (:nom_article, :no_categorie, :description, :date_debut_encheres, :date_fin_encheres, :prix_initial, :no_utilisateur)";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public ArticleVenduDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("nom_article", newArticle.getNomArticle());
		map.addValue("no_categorie", newArticle.getNoArticle());
		map.addValue("description", newArticle.getDescription());
		map.addValue("date_debut_encheres", newArticle.getDateDebutEncheres());
		map.addValue("date_fin_encheres", newArticle.getDateFinEncheres());
		map.addValue("prix_initial", newArticle.getMiseAPrix());
		map.addValue("no_utilisateur", utilisateur.getNoUtilisateur());
		
		jdbcTemplate.update(INSERT, map, keyHolder);
		//MAJ n°categorie
		if(keyHolder.getKey() != null) {
			newArticle.setNoArticle(keyHolder.getKey().longValue());
		}
	}

	@Override
	public List<ArticleVendu> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
/*
	@Override
	public ArticleVendu read(long noArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noArticle", noArticle);		
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new FilmRowMapper());
	}
*/

}

