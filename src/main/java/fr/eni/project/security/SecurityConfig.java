package fr.eni.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final String REMEMBER_ME_KEY = "uniqueAndSecretKey123";
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
	            new ClearSiteDataHeaderWriter(Directive.ALL));
	    http.userDetailsService(customUserDetailsService)  // Use custom UserDetailsService
	        .authorizeHttpRequests((authorize) -> authorize
	            .requestMatchers("/","/encheres/**","/filtrer/**").permitAll()
	            .requestMatchers("/uploads/**").permitAll()
	            .requestMatchers("/signup/**", "/img/**", "/css/**").permitAll()
	            .requestMatchers("/article-update/**").hasAnyRole("USER", "ADMIN") // Autoriser USER ou ADMIN
	            .anyRequest().authenticated())
	        .httpBasic(Customizer.withDefaults())
	        .formLogin(form -> form.loginPage("/login")
	            .defaultSuccessUrl("/encheres", true)
	            .failureUrl("/login?error=true")
	            .permitAll())
	        .sessionManagement(session -> session
	                .maximumSessions(1) // Limite à une session active par utilisateur
	                .and()
	                .invalidSessionUrl("/login?expired") // Redirige en cas de session invalide/expirée
	            )
	        .rememberMe(remember -> remember
	                .key(REMEMBER_ME_KEY)
	                .rememberMeParameter("remember-me") // nom du paramètre dans le formulaire
	                .rememberMeCookieName("remember-me-cookie") // nom du cookie
	                .useSecureCookie(true) // force HTTPS
	                .alwaysRemember(false) // désactive le remember-me automatique
	                .tokenValiditySeconds(7 * 24 * 60 * 60) // durée de validité : 7 jours
	                .userDetailsService(customUserDetailsService))
	        .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
	            .addLogoutHandler(clearSiteData)
	    		.deleteCookies("remember-me-cookie")); // supprime le cookie lors de la déconnexion
	    return http.build();
	}
	
}