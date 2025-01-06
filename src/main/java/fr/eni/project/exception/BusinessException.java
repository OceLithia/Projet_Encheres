package fr.eni.project.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Liste des messages d'erreur
    private final List<String> listeMessages = new ArrayList<>();

    // Constructeur avec un seul message
    public BusinessException(String message) {
        super(message); // Appel au constructeur de la classe parent
        this.listeMessages.add(message); // Ajout du message à la liste
    }

    // Constructeur vide pour ajouter des messages ultérieurement
    public BusinessException() {
        super("Une ou plusieurs erreurs sont survenues.");
    }

	// Ajouter un message d'erreur à la liste
    public void addMessage(String message) {
        this.listeMessages.add(message);
    }

    // Récupérer tous les messages d'erreur
    public List<String> getListeMessages() {
        return new ArrayList<>(this.listeMessages); // Retourner une copie pour éviter les modifications
    }

    // Vérifier si des messages sont présents
    public boolean hasMessages() {
        return !this.listeMessages.isEmpty();
    }

    // Récupérer un message unique (le premier, par exemple)
    public String getFirstMessage() {
        return this.listeMessages.isEmpty() ? null : this.listeMessages.get(0);
    }
    
    @Override
    public String getMessage() {
        return String.join(", ", listeMessages); // Concatène les messages pour un usage simple
    }
    
}

