package fr.eni.project.security;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(SessionTimeoutListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("Session créée : ID = " + se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("Session détruite (inactivité) : ID = " + se.getSession().getId());
	}
}
