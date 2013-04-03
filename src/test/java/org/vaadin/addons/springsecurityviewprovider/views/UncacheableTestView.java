package org.vaadin.addons.springsecurityviewprovider.views;

import org.vaadin.addons.springsecurityviewprovider.ViewDescription;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author Michael J. Simons, 2013-04-03
 */
@ViewDescription(
		name=UncacheableTestView.VIEW_NAME
)
public class UncacheableTestView implements View {
	private static final long serialVersionUID = -46560630345070079L;

	public static final String VIEW_NAME = "/test/uncacheableView";
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
}