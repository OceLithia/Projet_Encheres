package fr.eni.project.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import fr.eni.project.dto.ValidationGroups;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Utilisateur implements UserDetails {

	private static final long serialVersionUID = 1L;

	private long noUtilisateur;

	@NotBlank(message = "Le pseudo est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Le pseudo ne doit contenir que des caractères alphanumériques (lettres et chiffres).", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String pseudo;

	@NotBlank(message = "Le nom de famille est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-zÀ-ÿ -]+$", message = "Format incorrect.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String nom;

	@NotBlank(message = "Le prénom est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-zÀ-ÿ -]+$", message = "Format incorrect.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String prenom;

	@NotBlank(message = "L''adresse e-mail est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "L''adresse e-mail n''est pas valide.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String email;

	@NotBlank(message = "Le numéro de téléphone est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^0\\d{9}$", message = "Le numéro de téléphone doit contenir exactement 10 chiffres et commencer par 0.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String telephone;

	@NotBlank(message = "L''adresse postale est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-zÀ-ÿ0-9 ,.'-]+$", message = "L''adresse postale n''est pas valide.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String rue;

	@NotBlank(message = "Le code postal est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^0[1-9]\\d{3}|[1-9]\\d{4}$", message = "Le code postal doit contenir exactement 5 chiffres et être compris entre 01000 et 99999.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String codePostal;

	@NotBlank(message = "La ville est obligatoire.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	@Pattern(regexp = "^[A-Za-zÀ-ÿ -]+$", message = "Format incorrect.", groups = { ValidationGroups.Creation.class,
			ValidationGroups.Update.class })
	private String ville;

	private int credit;
	private boolean administrateur;

	private List<Enchere> encheres = new ArrayList<>();
	private List<ArticleVendu> articlesVendus = new ArrayList<>();

	
	private String motDePasse;
	private String confirmPassword;

	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
	         message = "Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial.", 
	         groups = ValidationGroups.Creation.class)
	@NotBlank(message = "Le mot de passe est obligatoire.", 
	          groups = ValidationGroups.Creation.class)
	public String getMotDePasseCreation() {
	    return motDePasse;
	}

	@AssertTrue(message = "Les mots de passe ne correspondent pas.", 
	            groups = ValidationGroups.Creation.class)
	public boolean isPasswordMatchingCreate() {
	    return motDePasse != null && motDePasse.equals(confirmPassword);
	}
	
	public Utilisateur() {
	}

	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue,
			String codePostal, String ville, String motDePasse, int credit, boolean administrateur) {
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.administrateur = administrateur;
	}

	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue,
			String codePostal, String ville, String motDePasse, int credit, boolean administrateur,
			List<Enchere> encheres, List<ArticleVendu> articlesVendus) {
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
		this.credit = credit;
		this.administrateur = administrateur;
		this.encheres = encheres;
		this.articlesVendus = articlesVendus;
	}

	public long getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public boolean isAdministrateur() {
		return administrateur;
	}

	public void setAdministrateur(boolean administrateur) {
		this.administrateur = administrateur;
	}

	public List<Enchere> getEncheres() {
		return encheres;
	}

	public void setEncheres(List<Enchere> encheres) {
		this.encheres = encheres;
	}

	public List<ArticleVendu> getArticlesVendus() {
		return articlesVendus;
	}

	public void setArticlesVendus(List<ArticleVendu> articlesVendus) {
		this.articlesVendus = articlesVendus;
	}

	public final String getConfirmPassword() {
		return confirmPassword;
	}

	public final void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public int hashCode() {
		return Objects.hash(noUtilisateur);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilisateur other = (Utilisateur) obj;
		return noUtilisateur == other.noUtilisateur;
	}

	@Override
	public String toString() {
		return "Utilisateur [noUtilisateur=" + noUtilisateur + ", pseudo=" + pseudo + ", nom=" + nom + ", prenom="
				+ prenom + ", email=" + email + ", telephone=" + telephone + ", rue=" + rue + ", codePostal="
				+ codePostal + ", ville=" + ville + ", credit=" + credit + ", administrateur=" + administrateur + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Si l'utilisateur est administrateur, on lui attribue le rôle ROLE_ADMIN
		// Sinon, on lui attribue le rôle ROLE_USER
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (this.administrateur) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.motDePasse;
	}

	@Override
	public String getUsername() {
		return this.pseudo;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Par défaut, le compte n'est pas expiré
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // Par défaut, le compte n'est pas verrouillé
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // Par défaut, les identifiants ne sont pas expirés
	}

	@Override
	public boolean isEnabled() {
		return true; // Par défaut, l'utilisateur est activé
	}

}
