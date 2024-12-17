package fr.eni.project.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.eni.project.dal.UtilisateurDAO;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;
	
}


