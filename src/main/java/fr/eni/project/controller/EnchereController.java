package fr.eni.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import fr.eni.project.bo.Enchere;

@Controller
@RequestMapping
@SessionAttributes({"utilisateur-profile"})
public class EnchereController {

	private Enchere enchere;

	public EnchereController(Enchere enchere) {
		this.enchere = enchere;
	}
	
}
