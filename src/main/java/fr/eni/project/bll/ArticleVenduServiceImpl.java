package fr.eni.project.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.ArticleVenduDAO;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

	@Autowired
	private ArticleVenduDAO enchereDao;

	@Override
	public void addNewArticle(ArticleVendu articleVendu, Utilisateur utilisateur) {
		enchereDao.createSellArticle(utilisateur, articleVendu);		
	}

}
