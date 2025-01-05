package fr.eni.project.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

	private static final String FIND_ALL = "SELECT e.no_utilisateur, e.no_article, e.date_enchere, e.montant_enchere, u.pseudo FROM ENCHERES e INNER JOIN UTILISATEURS u ON e.no_utilisateur = u.no_utilisateur";
	private static final String FIND_BY_ARTICLE = FIND_ALL + " WHERE no_article = :idArticle";
	private static final String INSERT = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (:idUtilisateur, :idArticle, :dateEnchere, :montant)";
	private static final String DELETE = "DELETE FROM ENCHERES WHERE no_article = :idArticle";
	private static final String FIND_BY_ARTICLE_AND_UTILISATEUR = FIND_ALL + " WHERE no_article = :idArticle AND no_utilisateur = :idUtilisateur";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public Optional<Enchere> findByArticle(long noArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
	    map.addValue("idArticle", noArticle);

	    try {
	        Enchere enchere = jdbcTemplate.queryForObject(FIND_BY_ARTICLE, map, new EnchereRowMapper());
	        return Optional.of(enchere);
	    } catch (EmptyResultDataAccessException e) {
	        return Optional.empty(); // Aucun résultat trouvé
	    }
	}
	
	@Override
	public Enchere findByArticleAndUtilisateur(long noArticle, long noEncherisseur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", noArticle);
		map.addValue("idUtilisateur", noEncherisseur);
		return jdbcTemplate.queryForObject(FIND_BY_ARTICLE_AND_UTILISATEUR, map, new EnchereRowMapper());
	}
	

	@Override
	public List<Enchere> findAll() {
		return jdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
	}

	@Override
	public void deleteEnchere(long idArticle) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idArticle", idArticle);
		jdbcTemplate.update(DELETE, map);
	}

	@Override
	public void createEnchere(ArticleVendu article, Enchere enchere, Utilisateur encherisseur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("idUtilisateur", encherisseur.getNoUtilisateur());
		map.addValue("idArticle", article.getNoArticle());
		map.addValue("dateEnchere", enchere.getDateEnchere());
		map.addValue("montant", enchere.getMontantEnchere());
		jdbcTemplate.update(INSERT, map);
	}

	class EnchereRowMapper implements RowMapper<Enchere> {

		@Override
		public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
			// Création de l'article
			Enchere enchere = new Enchere();
			enchere.setMontantEnchere(rs.getInt("montant_enchere"));
			Timestamp dateEnchereTimestamp = rs.getTimestamp("date_enchere");
			if (dateEnchereTimestamp != null) {
				enchere.setDateEnchere(dateEnchereTimestamp.toLocalDateTime());
			}

			Utilisateur encherisseur = new Utilisateur();
			encherisseur.setNoUtilisateur(rs.getLong("no_utilisateur"));
			encherisseur.setPseudo(rs.getString("pseudo"));
			enchere.setEncherisseur(encherisseur);
			
			ArticleVendu articleVendu = new ArticleVendu();
			articleVendu.setNoArticle(rs.getLong("no_article"));
			
			return enchere;

		}

	}

}
