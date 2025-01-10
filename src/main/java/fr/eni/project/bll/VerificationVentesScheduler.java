package fr.eni.project.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VerificationVentesScheduler {

    @Autowired
    private ArticleVenduService articleVenduService;

    @Scheduled(fixedRate = 60000)
    public void verifierVentes() {
        articleVenduService.verifierEtFinaliserVentes();
    }
	
}
