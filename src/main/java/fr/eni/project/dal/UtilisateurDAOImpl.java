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
		map.addValue("credit", utilisateur.getCredit());
		map.addValue("administrateur", utilisateur.isAdministrateur());
		
		// Exécution de l'insertion
		jdbcTemplate.update(INSERT, map, keyHolder);

		// Mise à jour de l'ID de l'utilisateur avec la clé générée
		if (keyHolder.getKey() != null) {
			utilisateur.setNoUtilisateur(keyHolder.getKey().longValue());
		}
		
	}
	
	@Override
	public Utilisateur read(String pseudo) {
	    MapSqlParameterSource map = new MapSqlParameterSource();
	    map.addValue("pseudo", pseudo);
	    return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, map, new BeanPropertyRowMapper<>(Utilisateur.class)); 
	}

	
	@Override
	public List<Utilisateur> findAll() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	

}
