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
	public Enchere convert(String id) {
		Enchere enchere = this.enchereService.consulterEnchereParNoEnchere(Long.parseLong(id));
		return enchere;
	}

}
