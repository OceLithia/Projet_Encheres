package fr.eni.project.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Retrait;
import fr.eni.project.bo.Utilisateur;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {

	private static final String INSERT = "INSERT INTO ARTICLES_VENDUS (nom_article, no_categorie, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, image_path, etat_vente) "
			+ "VALUES (:nomArticle, :noCategorie, :description, :dateDebutEncheres, :dateFinEncheres, :prixInitial, :prixVente, :noUtilisateur, :imagePath, :etatVente)";
	private static final String FIND_ALL = "SELECT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.prix_vente, a.image_path, a.no_utilisateur, v.pseudo, v.telephone, a.no_categorie, c.libelle, r.rue, r.code_postal, r.ville, a.etat_vente "
			+ "FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur "
			+ "INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie "
			+ "LEFT JOIN RETRAITS r ON a.no_article = r.no_article";
	private static final String FIND_BY_ID = FIND_ALL + " WHERE a.no_article = :no_article";
	private static final String FIND_BY_ID_VENDEUR = FIND_ALL + " WHERE a.no_utilisateur = :no_vendeur";
	private static final String FIND_BY_CAT = FIND_ALL + " WHERE a.no_categorie = :no_categorie";
	private static final String FIND_BY_MOTCLE = FIND_ALL + " WHERE a.nom_article LIKE :saisie";
	private static final String UPDATE = "UPDATE ARTICLES_VENDUS SET nom_article = :nomArticle, description = :description, date_debut_encheres = :dateDebutEncheres, date_fin_encheres = :dateFinEncheres, prix_initial = :prixInitial, prix_vente = :prixVente, no_utilisateur = :noUtilisateur, etat_vente = :etatVente, image_path = :imagePath WHERE no_article = :noArticle";
	private static final String FIND_BY_DATE_FIN_BEFORE = FIND_ALL + " WHERE a.date_fin_encheres < :maintenant";
	private static final String FIND_BY_DATE_FIN_AFTER = FIND_ALL + " WHERE a.date_fin_encheres > :maintenant";
	private static final String DELETE_ARTICLE_BY_ID = "delete from [PROJECT_ENCHERES].[dbo].[ARTICLES_VENDUS] where no_article = :no_article";
	private static final String DELETE_RETRAIT_BY_ARTICLE = "DELETE FROM [PROJECT_ENCHERES].[dbo].[RETRAITS] WHERE [no_article] = :no_article";
	private static final String UPDATE_ARTICLE = "UPDATE ARTICLES_VENDUS SET nom_article = :nomArticle, description = :description, no_categorie = :noCategorie, date_debut_encheres = :dateDebutEncheres, date_fin_encheres = :dateFinEncheres, prix_initial = :prixInitial, prix_vente = :prixVente, etat_vente = :etatVente, image_path = :imagePath WHERE no_article = :noArticle";
	private static final String UPDATE_RETRAIT = "UPDATE RETRAITS SET rue = :rue, code_postal = :codePostal, ville = :ville WHERE no_article = :noArticle";
	private static final String FIND_ARTICLES_ENCHERES_EN_COURS = "SELECT top 1 a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.prix_vente, a.image_path, a.no_utilisateur, v.pseudo, v.telephone, a.no_categorie, c.libelle, r.rue, r.code_postal, r.ville, a.etat_vente FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie LEFT JOIN RETRAITS r ON a.no_article = r.no_article inner join ENCHERES e on a.no_article = e.no_article where e.no_utilisateur = :noUtilisateur and a.etat_vente = 0 ORDER BY e.date_enchere DESC";
	private static final String FIND_ARTICLES_EN_VENTE = FIND_ALL
			+ " WHERE a.date_debut_encheres < :maintenant AND a.date_fin_encheres > :maintenant ";
	private static final String FIND_ARTICLES_REMPORTES = "SELECT DISTINCT a.no_article, a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, a.prix_initial, a.prix_vente, a.image_path, a.no_utilisateur, v.pseudo, v.telephone, a.no_categorie, c.libelle, r.rue, r.code_postal, r.ville, a.etat_vente FROM ARTICLES_VENDUS a INNER JOIN UTILISATEURS v ON a.no_utilisateur = v.no_utilisateur  INNER JOIN CATEGORIES c ON a.no_categorie = c.no_categorie  LEFT JOIN RETRAITS r ON a.no_article = r.no_article INNER JOIN ENCHERES e ON a.no_article = e.no_article WHERE a.etat_vente = 2 AND e.montant_enchere = (SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article = a.no_article) AND e.no_utilisateur = :noUtilisateur";

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
		map.addValue("imagePath", newArticle.getImagePath());
		if (newArticle.getDateDebutEncheres().isBefore(LocalDateTime.now())) {
			map.addValue("etatVente", -1);
		} else {
			map.addValue("etatVente", 0);
		}
		jdbcTemplate.update(INSERT, map, keyHolder);
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
	public List<ArticleVendu> findByDateFinEncheresBefore(LocalDateTime localDateTime) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("maintenant", localDateTime);
		return jdbcTemplate.query(FIND_BY_DATE_FIN_BEFORE, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> findByDateFinEncheresAfter(LocalDateTime localDateTime) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("maintenant", localDateTime);
		return jdbcTemplate.query(FIND_BY_DATE_FIN_AFTER, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> findByDateDebutBeforeAndDateFinAfter(LocalDateTime localDateTime) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("maintenant", localDateTime);
		return jdbcTemplate.query(FIND_ARTICLES_EN_VENTE, map, new ArticleRowMapper());
	}

	@Override
	public List<ArticleVendu> findArticlesEncheresEnCours(Long noUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noUtilisateur", noUtilisateur);
		return jdbcTemplate.query(FIND_ARTICLES_ENCHERES_EN_COURS, map, new ArticleRowMapper());
	};

	@Override
	public List<ArticleVendu> findArticlesRemportes(Long noUtilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noUtilisateur", noUtilisateur);
		return jdbcTemplate.query(FIND_ARTICLES_REMPORTES, map, new ArticleRowMapper());
	}

	@Override
	public void update(ArticleVendu articleVendu, Utilisateur vendeur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("noArticle", articleVendu.getNoArticle());
		map.addValue("nomArticle", articleVendu.getNomArticle());
		map.addValue("description", articleVendu.getDescription());
		map.addValue("dateDebutEncheres", articleVendu.getDateDebutEncheres());
		map.addValue("dateFinEncheres", articleVendu.getDateFinEncheres());
		map.addValue("prixInitial", articleVendu.getMiseAPrix());
		map.addValue("prixVente", articleVendu.getPrixVente());
		map.addValue("noUtilisateur", vendeur.getNoUtilisateur());
		map.addValue("etatVente", articleVendu.getEtatVente());
		if (articleVendu.getImagePath() != null) {
			map.addValue("imagePath", articleVendu.getImagePath());
		} else {
			map.addValue("imagePath", null);
		}
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
			articleVendu.setEtatVente(rs.getInt("etat_vente"));

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

	@Override
	public void deleteArticle(ArticleVendu article) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("no_article", article.getNoArticle());
		jdbcTemplate.update(DELETE_RETRAIT_BY_ARTICLE, map);
		jdbcTemplate.update(DELETE_ARTICLE_BY_ID, map);
	}

	/*
	 * @Override
	 * 
	 * @Transactional public void updateArticle(ArticleVendu updatedArticle, Retrait
	 * updateRetrait) { MapSqlParameterSource map = new MapSqlParameterSource();
	 * map.addValue("noArticle", updatedArticle.getNoArticle());
	 * map.addValue("nomArticle", updatedArticle.getNomArticle());
	 * map.addValue("description", updatedArticle.getDescription());
	 * map.addValue("noCategorie", updatedArticle.getCategorie().getNoCategorie());
	 * if (updatedArticle.getCategorie() == null) { throw new
	 * IllegalArgumentException("La catégorie ne peut pas être nulle."); }
	 * map.addValue("dateDebutEncheres", updatedArticle.getDateDebutEncheres());
	 * map.addValue("dateFinEncheres", updatedArticle.getDateFinEncheres());
	 * map.addValue("prixInitial", updatedArticle.getMiseAPrix()); Integer prixVente
	 * = updatedArticle.getPrixVente(); if (prixVente != null) {
	 * map.addValue("prixVente", prixVente); } else { map.addValue("prixVente",
	 * Types.INTEGER); // ou ne pas ajouter cette valeur si le champ doit rester
	 * inchangé }
	 * 
	 * map.addValue("prixVente", updatedArticle.getMiseAPrix());
	 * map.addValue("etatVente", updatedArticle.getEtatVente());
	 * 
	 * Integer etatVente = updatedArticle.getEtatVente(); if (etatVente != null) {
	 * map.addValue("etatVente", etatVente); } else { map.addValue("etatVente",
	 * Types.INTEGER); } System.out.println("etatVente : "+ etatVente);
	 * 
	 * // Gestion propre de l'image String imagePath =
	 * updatedArticle.getImagePath(); if (imagePath == null ||
	 * imagePath.trim().isEmpty()) { System.out.println("if DAO vide");
	 * map.addValue("imagePath", null); // Laisser la valeur NULL si l'image est
	 * vide } else { System.out.println("if DAO non vide");
	 * System.out.println(imagePath); map.addValue("imagePath", imagePath);
	 * System.out.println(imagePath); }
	 * System.out.println(updatedArticle.getImagePath()+ " dans la fonction DAO");
	 * 
	 * map.addValue("rue", updateRetrait.getRue()); map.addValue("codePostal",
	 * updateRetrait.getCodePostal()); map.addValue("ville",
	 * updateRetrait.getVille()); this.jdbcTemplate.update(UPDATE_RETRAIT, map);
	 * this.jdbcTemplate.update(UPDATE_ARTICLE, map);
	 * 
	 * System.out.println("Article et retrait mis à jour avec succès.");
	 * 
	 * try {
	 * 
	 * 
	 * 
	 * } catch (DataAccessException e) {
	 * System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
	 * throw new
	 * RuntimeException("Erreur lors de la mise à jour de l'article en base : " +
	 * e.getMessage(), e); }
	 * 
	 * }
	 */
	
	@Override
	@Transactional
	public void updateArticle(ArticleVendu updatedArticle, Retrait updateRetrait) {
	    MapSqlParameterSource map = new MapSqlParameterSource();
	    
	    // Validate inputs
	    if (updatedArticle.getNoArticle() <= 0) {
	        throw new IllegalArgumentException("Article ID must be positive");
	    }
	    if (updatedArticle.getCategorie() == null) {
	        throw new IllegalArgumentException("La catégorie ne peut pas être nulle.");
	    }
	    
	    if (updatedArticle.getDateDebutEncheres() == null || updatedArticle.getDateFinEncheres() == null) {
	        throw new IllegalArgumentException("Les dates de début et de fin d'enchères ne peuvent pas être nulles");
	    }

	    // Prepare article parameters
	    map.addValue("noArticle", updatedArticle.getNoArticle());
	    map.addValue("nomArticle", updatedArticle.getNomArticle());
	    map.addValue("description", updatedArticle.getDescription());
	    map.addValue("noCategorie", updatedArticle.getCategorie().getNoCategorie());
	    map.addValue("dateDebutEncheres", updatedArticle.getDateDebutEncheres());
	    map.addValue("dateFinEncheres", updatedArticle.getDateFinEncheres());
	    map.addValue("prixInitial", updatedArticle.getMiseAPrix());
	    map.addValue("prixVente", updatedArticle.getMiseAPrix());
	    map.addValue("etatVente", updatedArticle.getEtatVente());
	    map.addValue("imagePath", updatedArticle.getImagePath() != null && !updatedArticle.getImagePath().trim().isEmpty() 
	        ? updatedArticle.getImagePath().trim() 
	        : null);

	    // First check if article exists
	    String checkArticle = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_article = :noArticle";
	    int articleExists = jdbcTemplate.queryForObject(checkArticle, map, Integer.class);
	    if (articleExists == 0) {
	        throw new RuntimeException("Article with ID " + updatedArticle.getNoArticle() + " not found");
	    }

	    // Update article
	    int updatedRows = jdbcTemplate.update(UPDATE_ARTICLE, map);
	    if (updatedRows != 1) {
	        throw new RuntimeException("Failed to update article, rows affected: " + updatedRows);
	    }

	    // Handle retrait update/insert
	    if (updateRetrait != null) {
	        map.addValue("rue", updateRetrait.getRue());
	        map.addValue("codePostal", updateRetrait.getCodePostal());
	        map.addValue("ville", updateRetrait.getVille());

	        // Check if retrait exists
	        String checkRetrait = "SELECT COUNT(*) FROM RETRAITS WHERE no_article = :noArticle";
	        int retraitExists = jdbcTemplate.queryForObject(checkRetrait, map, Integer.class);

	        if (retraitExists > 0) {
	            // Update existing retrait
	            int retraitUpdated = jdbcTemplate.update(UPDATE_RETRAIT, map);
	            if (retraitUpdated != 1) {
	                throw new RuntimeException("Failed to update retrait, rows affected: " + retraitUpdated);
	            }
	        } else {
	            // Insert new retrait
	            String insertRetrait = "INSERT INTO RETRAITS (no_article, rue, code_postal, ville) VALUES (:noArticle, :rue, :codePostal, :ville)";
	            int retraitInserted = jdbcTemplate.update(insertRetrait, map);
	            if (retraitInserted != 1) {
	                throw new RuntimeException("Failed to insert retrait, rows affected: " + retraitInserted);
	            }
	        }
	    }
	}

}
