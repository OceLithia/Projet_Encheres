package fr.eni.project.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.project.bo.Enchere;
import fr.eni.project.dal.EnchereDAO;
import fr.eni.project.dal.UtilisateurDAO;

@Service
public class EnchereServiceImpl implements EnchereService{

	private EnchereDAO enchereDAO;
	private UtilisateurDAO utilisateurDAO;
	
	@Override
	public List<Enchere> listeEncheres() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
