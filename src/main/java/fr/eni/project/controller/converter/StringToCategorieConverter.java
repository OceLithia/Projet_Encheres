package fr.eni.project.controller.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import fr.eni.project.bll.CategorieService;
import fr.eni.project.bo.Categorie;

@Component
public class StringToCategorieConverter implements Converter<String, Categorie>{
	
	private CategorieService categorieService;

	public StringToCategorieConverter(CategorieService categorieService) {
		this.categorieService = categorieService;
	}

	@Override
	public Categorie convert(String idCategorie) {
		Categorie categorie = this.categorieService.consulterCategorieParId(Long.parseLong(idCategorie));
		return categorie;
	}

	@Override
	public JavaType getInputType(TypeFactory typeFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JavaType getOutputType(TypeFactory typeFactory) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
