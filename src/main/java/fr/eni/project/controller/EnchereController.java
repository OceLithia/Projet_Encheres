package fr.eni.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.project.bll.EnchereService;

@Controller
@RequestMapping
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}
	
}
