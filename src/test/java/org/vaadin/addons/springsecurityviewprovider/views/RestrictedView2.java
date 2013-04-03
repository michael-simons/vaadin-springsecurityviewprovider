package org.vaadin.addons.springsecurityviewprovider.views;

import org.vaadin.addons.springsecurityviewprovider.ViewDescription;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author Michael J. Simons, 2013-04-03
 */
@ViewDescription(
		name=RestrictedView2.VIEW_NAME,		
		requiredPermissions="isAuthenticated() and hasRole('superUser')"
)
public class RestrictedView2 implements View {
	private static final long serialVersionUID = 4600098199813036228L;
	
	public static final String VIEW_NAME = "/test/restrictedView2";
	
	public RestrictedView2() {		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
}