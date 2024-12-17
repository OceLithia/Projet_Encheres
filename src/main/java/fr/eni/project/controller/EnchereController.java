package fr.eni.project.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fr.eni.project.bll.EnchereService;
import fr.eni.project.bo.Enchere;

@Controller
@RequestMapping
//@SessionAttributes({"utilisateur-profile"})
public class EnchereController {


	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@GetMapping
	public String afficherEncheres(Model model) {
		List<Enchere> encheres = this.enchereService.afficherEncheres();
		model.addAttribute("encheres", encheres);
		return "index";
		}
	
	
	
	
	
}
