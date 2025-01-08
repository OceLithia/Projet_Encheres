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
	        .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
	            .addLogoutHandler(clearSiteData));
	    return http.build();
	}
	
}