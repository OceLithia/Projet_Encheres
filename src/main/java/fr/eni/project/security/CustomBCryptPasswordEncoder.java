package fr.eni.project.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomBCryptPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomBCryptPasswordEncoder() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(10); // Le "10" est le facteur de travail
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String encoded = bCryptPasswordEncoder.encode(rawPassword);
        // Remplacer le préfixe par "2y" au lieu de "2a"
        return encoded.replace("$2a$", "$2y$");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // BCrypt vérifie le mot de passe sans tenir compte du préfixe
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
