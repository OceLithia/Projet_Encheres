package fr.eni.project.bll;

import java.util.List;

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
	public void addNewArticle(Utilisateur utilisateur, ArticleVendu articleVendu) {
		enchereDao.createSellArticle(utilisateur, articleVendu);		
	}
	
	@Override
	public List<ArticleVendu> afficherArticles() {
		return enchereDao.findAll();
	}
	
	@Override
	public ArticleVendu afficherArticleParNoArticle(long noArticle) {
		return enchereDao.readById(noArticle);
	}
	
	@Override
	public ArticleVendu afficherArticleParNoVendeur(long noVendeur) {
		return enchereDao.readById(noVendeur);
	}
	
	
	

}
