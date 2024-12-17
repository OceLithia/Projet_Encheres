package fr.eni.project.bll;

import java.util.List;
import org.springframework.stereotype.Service;
import fr.eni.project.bo.Categorie;
import fr.eni.project.bo.Enchere;
import fr.eni.project.dal.EnchereDAO;
import fr.eni.project.dal.UtilisateurDAO;

@Service
public class EnchereServiceImpl implements EnchereService{

	private EnchereDAO enchereDAO;
	private UtilisateurDAO utilisateurDAO;

	
		public EnchereServiceImpl(EnchereDAO enchereDAO, UtilisateurDAO utilisateurDAO) {
		super();
		this.enchereDAO = enchereDAO;
		this.utilisateurDAO = utilisateurDAO;
	}


		@Override
		public List<Enchere> afficherEncheres(){
			return this.enchereDAO.findAll();
		}


		@Override
		public Enchere consulterEnchereParId(long id) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<Categorie> afficherCategories() {
			// TODO Auto-generated method stub
			return null;
		}
		

	
	
}
