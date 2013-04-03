package org.vaadin.addons.springsecurityviewprovider.views;

import org.vaadin.addons.springsecurityviewprovider.ViewDescription;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author Michael J. Simons, 2013-04-03
 */
@ViewDescription(
		name=RestrictedView1.VIEW_NAME,		
		requiredPermissions="isAuthenticated() and hasRole('superUser')"
)
public class RestrictedView1 implements View {
	private static final long serialVersionUID = 4600098199813036228L;
	
	public static final String VIEW_NAME = "/test/restrictedView";
	
	public RestrictedView1() {
		throw new RuntimeException("I don't want to be instantiated.");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
}