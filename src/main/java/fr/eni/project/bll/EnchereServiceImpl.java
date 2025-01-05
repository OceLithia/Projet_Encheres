package fr.eni.project.bll;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.eni.project.bo.ArticleVendu;
import fr.eni.project.bo.Enchere;
import fr.eni.project.bo.Utilisateur;
import fr.eni.project.dal.ArticleVenduDAO;
import fr.eni.project.dal.EnchereDAO;
import fr.eni.project.dal.UtilisateurDAO;
import fr.eni.project.exception.BusinessException;
import fr.eni.project.exception.EnchereNotFoundException;

@Service
public class EnchereServiceImpl implements EnchereService {

	@Autowired
	private EnchereDAO enchereDAO;
	@Autowired
	private UtilisateurDAO utilisateurDAO;
	@Autowired
	private ArticleVenduDAO articleVenduDAO;

	@Override
	public List<Enchere> afficherEncheres() {
		return this.enchereDAO.findAll();
	}

	@Override

	public void creerEnchere(Enchere nouvelleEnchere, Utilisateur encherisseur, ArticleVendu article)
			throws BusinessException {

		BusinessException be = new BusinessException();
		if (nouvelleEnchere.getMontantEnchere() > article.getPrixVente()) {
			enchereDAO.createEnchere(article, nouvelleEnchere, encherisseur);
		} else {
			throw be;
		}
	}

	@Override
	public void supprimerEnchere(long idArticle) {
		enchereDAO.deleteEnchere(idArticle);
	}

	@Override
	public Enchere consulterEnchereParArticleEtUtilisateur(long idArticle, long idUtilisateur) {
		return enchereDAO.findByArticleAndUtilisateur(idArticle, idUtilisateur);
	}

	@Override
	public Enchere consulterEnchereParArticle(long idArticle) {
		Optional<Enchere> optionalEnchere = enchereDAO.findByArticle(idArticle);
	    // Si aucune enchère n'existe pour cet article, gérer comme suit :
		return optionalEnchere.orElseThrow(() -> new EnchereNotFoundException("Aucune enchère trouvée pour l'article ID : " + idArticle));
	}

}
