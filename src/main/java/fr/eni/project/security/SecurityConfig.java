package fr.eni.project.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import fr.eni.project.bo.Utilisateur;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
				new ClearSiteDataHeaderWriter(Directive.ALL));

		http.authorizeHttpRequests(
				(authorize) -> authorize.requestMatchers("/", "/signup", "/img/**", "/css/**").permitAll()
						// .requestMatchers("/user-profile").permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				// personnalise la connexion
				.formLogin(form -> form.loginPage("/login")
						.defaultSuccessUrl("/user-profile") // Redirection après une connexion réussie															
						.failureUrl("/login?error=true") // URL en cas d'erreur
						.permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
						.addLogoutHandler(clearSiteData) // vide les données de l'utilisateur
				);

		return http.build();
	}

	@Bean
	public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		// Requête pour récupérer l'utilisateur (username = pseudo)
		jdbcUserDetailsManager.setUsersByUsernameQuery(
				"SELECT pseudo AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE pseudo = ?");

		// Requête pour mapper les rôles en fonction du booléen administrateur
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
				"SELECT pseudo AS username, COALESCE(CASE WHEN administrateur = 1 THEN 'ROLE_ADMIN' ELSE 'ROLE_USER' END, 'ROLE_USER') AS authority FROM UTILISATEURS WHERE pseudo = ?");
		return jdbcUserDetailsManager;
	}

}