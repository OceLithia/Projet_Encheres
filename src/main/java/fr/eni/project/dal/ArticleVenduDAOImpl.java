package fr.eni.project.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
			+ "VALUES (:nomArticle, :noCategorie, :description, :dateDebutEncheres, :dateFinEncheres, :prixInitial, :noUtilisateur)";
	private static final String FIND_ALL = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.no_utilisateur, v.nom AS nom, v.prenom AS prenom, a.no_categorie, c.libelle AS libelle_categorie "
			+ "FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie";
	private static final String FIND_BY_ID = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.no_utilisateur, v.nom AS nom, v.prenom AS prenom, a.no_categorie, c.libelle AS libelle_categorie "
			+ "FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie "
			+ "WHERE a.no_article = :no_article";
	private static final String FIND_BY_ID_VENDEUR = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.no_utilisateur, v.nom AS nom, v.prenom AS prenom, a.no_categorie, c.libelle AS libelle_categorie "
			+ "FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie "
			+ "WHERE a.no_utilisateur = :no_vendeur";
	
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
		map.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
		jdbcTemplate.update(INSERT, map, keyHolder);
		//MAJ n°categorie
		if(keyHolder.getKey() != null) {
			newArticle.setNoArticle(keyHolder.getKey().longValue());
		}
	}
	
	@Override 
	public List<ArticleVendu> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(ArticleVendu.class));
	}
	
	
	
	@Override
	public ArticleVendu readById(long id_article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", id_article);
		return jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(ArticleVendu.class));
	}
	
	
	@Override
	public ArticleVendu readByVendeur(long id_vendeur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_vendeur", id_vendeur);
		return jdbcTemplate.queryForObject(FIND_BY_ID_VENDEUR, map, new BeanPropertyRowMapper<>(ArticleVendu.class));
	}

}
