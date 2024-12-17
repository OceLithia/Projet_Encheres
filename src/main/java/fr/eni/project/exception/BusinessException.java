package fr.eni.project.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;
	private List<String> listeMessage;

	public BusinessException() {
		this.listeMessage = new ArrayList<String>();
	}

	public void addMessage(String message) {
		this.listeMessage.add(message);
	}

	public List<String> getListeMessage() {
		return listeMessage;
	}

}
