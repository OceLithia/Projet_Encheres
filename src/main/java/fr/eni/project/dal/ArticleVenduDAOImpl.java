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
			+ "VALUES (:nomArticle, :noCategorie, :description, :dateDebutEncheres, :dateFinEncheres, :prixInitial, 38)";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public ArticleVenduDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean checkCategory(long noCategorie) {
		String queryCheck = "SELECT COUNT(*) FROM CATEGORIES WHERE no_categorie = :noCategorie";
	    MapSqlParameterSource map = new MapSqlParameterSource("noCategorie", noCategorie);
	    Long count = jdbcTemplate.queryForObject(queryCheck, map, Long.class);
	    return count != null && count > 0;
	}

	@Override
	public void createSellArticle(Utilisateur utilisateur, ArticleVendu newArticle) {
		//vérifie si la catégorie existe
		if (!checkCategory(newArticle.getNoCategorie())) {
		    throw new IllegalArgumentException("La catégorie avec no_categorie " + newArticle.getNoCategorie() + " n'existe pas.");
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("nomArticle", newArticle.getNomArticle());
		map.addValue("noCategorie", newArticle.getNoCategorie());
		map.addValue("description", newArticle.getDescription());
		map.addValue("dateDebutEncheres", newArticle.getDateDebutEncheres());
		map.addValue("dateFinEncheres", newArticle.getDateFinEncheres());
		map.addValue("prixInitial", newArticle.getMiseAPrix());
		//map.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
		jdbcTemplate.update(INSERT, map, keyHolder);
		//MAJ n°categorie
		if(keyHolder.getKey() != null) {
			newArticle.setNoArticle(keyHolder.getKey().longValue());
		}
	}
/*
	@Override
	public void findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArticleVendu read(long noArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noArticle", noArticle);		
		return this.jdbcTemplate.queryForObject(FIND_BY_ID, map, new FilmRowMapper());
	}
*/

}

