package br.com.don.util;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;

public class FacesMessages implements Serializable {

	private static final long serialVersionUID = 1L;


	private void add(String message, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(message);
		msg.setSeverity(severity);

		context.addMessage(null, msg);
	}


	public void info(String message) {
		add(message, FacesMessage.SEVERITY_INFO);
	}


	public void error(String message) {
		add(message, FacesMessage.SEVERITY_ERROR);
	}
}
