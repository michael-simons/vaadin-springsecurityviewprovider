package org.vaadin.addons.springsecurityviewprovider;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.atmosphere.cpr.Action;
import org.atmosphere.cpr.AtmosphereConfig;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * Interceptor Atmosphere permettant de restaurer le SecurityContext dans le SecurityContextHolder
 * @see <a href="https://groups.google.com/forum/#!msg/atmosphere-framework/8yyOQALZEP8/ZCf4BHRgh_EJ">https://groups.google.com/forum/#!msg/atmosphere-framework/8yyOQALZEP8/ZCf4BHRgh_EJ</a>
 * and
 * @see <a href="https://vaadin.com/old-forum/-/message_boards/view_message/3383121">https://vaadin.com/old-forum/-/message_boards/view_message/3383121</a>
 * 
 * @author Adrien Colson, Michael J. Simons
 */
public class RecoverSecurityContextAtmosphereInterceptor implements AtmosphereInterceptor {
	private static final Logger logger = Logger.getLogger(RecoverSecurityContextAtmosphereInterceptor.class.getName());

	@Override
	public void configure(AtmosphereConfig atmosphereConfig) {
	}

	@Override
	public Action inspect(AtmosphereResource atmosphereResource) {
		logger.log(Level.FINE, "Recover SecurityContext in SecurityContextHolder");
		SecurityContext context = (SecurityContext) atmosphereResource.getRequest().getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		SecurityContextHolder.setContext(context);
		return Action.CONTINUE;
	}

	@Override
	public void postInspect(AtmosphereResource atmosphereResource) {
	}
}