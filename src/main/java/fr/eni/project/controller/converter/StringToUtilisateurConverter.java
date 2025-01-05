package fr.eni.project.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.project.bll.UtilisateurService;
import fr.eni.project.bo.Utilisateur;

@Component
public class StringToUtilisateurConverter implements Converter<String, Utilisateur>{
	
	@Autowired
	private UtilisateurService utilisateurService;

	@Override
	public Utilisateur convert(String id) {
		Utilisateur utilisateur = this.utilisateurService.afficherUtilisateurParId(Long.parseLong(id));
		return utilisateur;
	}

}
