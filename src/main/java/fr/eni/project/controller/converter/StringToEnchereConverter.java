package fr.eni.project.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.project.bll.EnchereService;
import fr.eni.project.bo.Enchere;

@Component
public class StringToEnchereConverter implements Converter<String, Enchere>{
	
	@Autowired
	private EnchereService enchereService;

	@Override
    public Enchere convert(String source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("La chaîne source est vide ou nulle");
        }

        try {
            // Diviser la chaîne source en noArticle et noUtilisateur
            String[] parts = source.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Le format attendu est 'noArticle-noUtilisateur'");
            }

            long noArticle = Long.parseLong(parts[0]);
            long noUtilisateur = Long.parseLong(parts[1]);

            // Récupérer l'enchère via le service
            return enchereService.consulterEnchereParArticleEtUtilisateur(noArticle, noUtilisateur);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Les identifiants doivent être des entiers : " + source, e);
        }
    }

}
