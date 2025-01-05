package fr.eni.project.dal;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.project.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :code_postal, :ville, :mot_de_passe, :credit, :administrateur)";
	private static final String FIND_ALL = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM UTILISATEURS";
	private static final String FIND_BY_PSEUDO = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM UTILISATEURS WHERE pseudo = :pseudo";
	private static final String FIND_BY_ID = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur FROM UTILISATEURS WHERE no_utilisateur = :id";
	//private static final String FIND_BY_PSEUDO_OR_EMAIL = "SELECT u FROM UTILISATEURS u WHERE u.pseudo = :identifier OR u.email = :identifier";
	private static final String DELETE_BY_PSEUDO = "DELETE FROM UTILISATEURS WHERE no_utilisateur = :id";
	private static final String UPDATE = "UPDATE UTILISATEURS SET pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, code_postal = :code_postal, ville = :ville, mot_de_passe = :mot_de_passe WHERE no_utilisateur = :id";
	private static final String COUNT_PSEUDO = "SELECT COUNT(*) FROM UTILISATEURS WHERE pseudo = :pseudo";
	private static final String COUNT_EMAIL = "SELECT COUNT(*) FROM UTILISATEURS WHERE email = :email";
	private static final String SELECT_PASSWORD = "SELECT mot_de_passe FROM UTILISATEURS WHERE no_utilisateurs = :id";

	private NamedParameterJdbcTemplate jdbcTemplate;

	public UtilisateurDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void createUser(Utilisateur utilisateur) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource map = new MapSqlParameterSource();
		// Préparation des paramètres pour l'insertion
		map.addValue("pseudo", utilisateur.getPseudo());
		map.addValue("nom", utilisateur.getNom());
		map.addValue("prenom", utilisateur.getPrenom());
		map.addValue("email", utilisateur.getEmail());
		map.addValue("telephone", utilisateur.getTelephone());
		map.addValue("rue", utilisateur.getRue());
		map.addValue("code_postal", utilisateur.getCodePostal());
		map.addValue("ville", utilisateur.getVille());
		map.addValue("mot_de_passe", utilisateur.getMotDePasse());
		map.addValue("credit", 0);
		map.addValue("administrateur", utilisateur.isAdministrateur());

		// Exécution de l'insertion
		jdbcTemplate.update(INSERT, map, keyHolder);

		// Mise à jour de l'ID de l'utilisateur avec la clé générée
		if (keyHolder.getKey() != null) {
			utilisateur.setNoUtilisateur(keyHolder.getKey().longValue());
		}

	}

	@Override
	public Utilisateur readByPseudo(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public Utilisateur readById(long id) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id", id);
		return jdbcTemplate.queryForObject(FIND_BY_ID, map, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	@Override
	public List<Utilisateur> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public void delete(Utilisateur utilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", utilisateur.getPseudo());
		jdbcTemplate.update(DELETE_BY_PSEUDO, map);
	}

	@Override
	public void update(Utilisateur utilisateur) {
	    MapSqlParameterSource map = new MapSqlParameterSource();
	    map.addValue("id", utilisateur.getNoUtilisateur());
	    map.addValue("pseudo", utilisateur.getPseudo());
	    map.addValue("nom", utilisateur.getNom());
	    map.addValue("prenom", utilisateur.getPrenom());
	    map.addValue("email", utilisateur.getEmail());
	    map.addValue("telephone", utilisateur.getTelephone());
	    map.addValue("rue", utilisateur.getRue());
	    map.addValue("code_postal", utilisateur.getCodePostal());
	    map.addValue("ville", utilisateur.getVille());
	    map.addValue("mot_de_passe", utilisateur.getMotDePasse());
	    this.jdbcTemplate.update(UPDATE, map);
	}


	@Override
	public String getMotDePasseEncode(Utilisateur utilisateur) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("id", utilisateur.getNoUtilisateur());
		return jdbcTemplate.queryForObject(SELECT_PASSWORD, map, String.class);
	}

	@Override
	public boolean existePseudo(String pseudo) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("pseudo", pseudo);
		int nbPseudo = jdbcTemplate.queryForObject(COUNT_PSEUDO, map, Integer.class);
		return nbPseudo > 0 ? true : false;
	}

	@Override
	public boolean existeEmail(String email) {
		MapSqlParameterSource map = new MapSqlParameterSource();
		map.addValue("email", email);
		int nbEmail = jdbcTemplate.queryForObject(COUNT_EMAIL, map, Integer.class);
		return nbEmail > 0 ? true : false;
	}

}
