package fr.eni.project.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Retrait;
import fr.eni.project.bo.Utilisateur;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {


	private static final String INSERT = "INSERT INTO ARTICLES_VENDUS (nom_article, no_categorie, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, image_path)"
			+ "VALUES (:nomArticle, :noCategorie, :description, :dateDebutEncheres, :dateFinEncheres, :prixInitial, :prixVente, :noUtilisateur, :imagePath)";
	private static final String FIND_ALL = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.prix_vente, a.image_path, "
			+ "a.no_utilisateur, v.pseudo, v.telephone, a.no_categorie, c.libelle, r.rue, r.code_postal, r.ville "
			+ "FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur "
			+ "INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie "
			+ "LEFT JOIN RETRAITS r ON a.no_article = r.no_article";
	private static final String FIND_BY_ID = FIND_ALL + " WHERE a.no_article = :no_article";
	private static final String FIND_BY_ID_VENDEUR = FIND_ALL + " WHERE a.no_utilisateur = :no_vendeur";
	private static final String FIND_BY_CAT = FIND_ALL + " WHERE a.no_categorie = :no_categorie";
	private static final String FIND_BY_MOTCLE = FIND_ALL + " WHERE a.nom_article LIKE :saisie";
	private static final String UPDATE = "UPDATE ARTICLES_VENDUS SET nom_article = :nomArticle, no_categorie = :noCategorie, description = :description, date_debut_encheres = :dateDebutEncheres, date_fin_encheres = :dateFinEncheres, prix_initial = :prixInitial, prix_vente = :prixVente, no_utilisateur = :noUtilisateur, image_path = :imagePath WHERE no_article = :noArticle";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public boolean checkCategory(long noCategorie) {
		String queryCheck = "SELECT COUNT(*) FROM CATEGORIES WHERE no_categorie = :noCategorie";
		MapSqlParameterSource map = new MapSqlParameterSource("noCategorie", noCategorie);
		Long count = jdbcTemplate.queryForObject(queryCheck, map, Long.class);
		return count != null && count > 0;
	}

	@Override
	public void createSellArticle(Utilisateur vendeur, ArticleVendu newArticle) {
		// vérifie si la catégorie existe
		if (!checkCategory(newArticle.getNoCategorie())) {
			throw new IllegalArgumentException(
					"La catégorie avec no_categorie " + newArticle.getNoCategorie() + " n'existe pas.");
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("nomArticle", newArticle.getNomArticle());
		map.addValue("noCategorie", newArticle.getNoCategorie());
		map.addValue("description", newArticle.getDescription());
		map.addValue("dateDebutEncheres", newArticle.getDateDebutEncheres());
		map.addValue("dateFinEncheres", newArticle.getDateFinEncheres());
		map.addValue("prixInitial", newArticle.getMiseAPrix());
		map.addValue("prixVente", newArticle.getMiseAPrix());
		map.addValue("noUtilisateur", vendeur.getNoUtilisateur());

		jdbcTemplate.update(INSERT, map, keyHolder);
		// MAJ n°categorie
		if (keyHolder.getKey() != null) {
			newArticle.setNoArticle(keyHolder.getKey().longValue());
		}
	}

	@Override
	public List<ArticleVendu> findAll() {
		return jdbcTemplate.query(FIND_ALL, new ArticleRowMapper());
	}

	@Override
	public ArticleVendu readById(long id_article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", id_article);
		return jdbcTemplate.queryForObject(FIND_BY_ID, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> readByVendeur(long id_vendeur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_vendeur", id_vendeur);
		return jdbcTemplate.query(FIND_BY_ID_VENDEUR, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> readByCategorie(long no_categorie) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_categorie", no_categorie);
		return jdbcTemplate.query(FIND_BY_CAT, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> readByKeyword(String motCle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("saisie", "%" + motCle + "%");
		return jdbcTemplate.query(FIND_BY_MOTCLE, map, new ArticleRowMapper());
	}

	@Override
	public void update(ArticleVendu articleVendu, Utilisateur vendeur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", articleVendu.getNoArticle());
		map.addValue("nom_article", articleVendu.getNomArticle());
		map.addValue("noCategorie", articleVendu.getNoCategorie());
		map.addValue("description", articleVendu.getDescription());
		map.addValue("dateDebutEncheres", articleVendu.getDateDebutEncheres());
		map.addValue("dateFinEncheres", articleVendu.getDateFinEncheres());
		map.addValue("prixInitial", articleVendu.getMiseAPrix());
		map.addValue("prixVente", articleVendu.getPrixVente());
		map.addValue("noUtilisateur", vendeur.getNoUtilisateur());
		map.addValue("imagePath", articleVendu.getImagePath());
		jdbcTemplate.update(UPDATE, map);
	}

	class ArticleRowMapper implements RowMapper<ArticleVendu> {

		@Override
		public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
			// Création de l'article
			ArticleVendu articleVendu = new ArticleVendu();
			articleVendu.setNoArticle(rs.getLong("no_article"));
			articleVendu.setNomArticle(rs.getString("nom_article"));
			articleVendu.setDescription(rs.getString("description"));
			// Conversion des dates en LocalDateTime
			Timestamp debutEncheresTimestamp = rs.getTimestamp("date_debut_encheres");
			if (debutEncheresTimestamp != null) {
				articleVendu.setDateDebutEncheres(debutEncheresTimestamp.toLocalDateTime());
			}
			Timestamp finEncheresTimestamp = rs.getTimestamp("date_fin_encheres");
			if (finEncheresTimestamp != null) {
				articleVendu.setDateFinEncheres(finEncheresTimestamp.toLocalDateTime());
			}
			articleVendu.setMiseAPrix(rs.getInt("prix_initial"));
			articleVendu.setPrixVente(rs.getInt("prix_vente"));
			articleVendu.setImagePath(rs.getString("image_path"));


			Categorie categorie = new Categorie();
			categorie.setNoCategorie(rs.getLong("no_categorie"));
			categorie.setLibelle(rs.getString("libelle"));
			articleVendu.setCategorie(categorie);

			Utilisateur vendeur = new Utilisateur();
			vendeur.setNoUtilisateur(rs.getLong("no_utilisateur"));
			vendeur.setPseudo(rs.getString("pseudo"));
			vendeur.setTelephone(rs.getString("telephone"));
			articleVendu.setVendeur(vendeur);

			Retrait lieuRetrait = new Retrait();
			lieuRetrait.setRue(rs.getString("rue"));
			lieuRetrait.setCodePostal(rs.getString("code_postal"));
			lieuRetrait.setVille(rs.getString("ville"));
			articleVendu.setLieuRetrait(lieuRetrait);
			
			// Retourne l'objet ArticleVendu complet
			return articleVendu;
		}

	}

}
