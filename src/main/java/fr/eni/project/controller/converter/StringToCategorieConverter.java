package fr.eni.project.controller.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import fr.eni.project.bll.CategorieService;
import fr.eni.project.bo.Categorie;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie>{
	
	@Autowired
	private CategorieService categorieService;

	@Override
	public Categorie convert(String idCategorie) {
		Categorie categorie = this.categorieService.consulterCategorieParId(Long.parseLong(idCategorie));
		return categorie;
	}
	
}
