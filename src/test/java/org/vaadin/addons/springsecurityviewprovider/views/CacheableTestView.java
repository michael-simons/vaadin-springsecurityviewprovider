package org.vaadin.addons.springsecurityviewprovider.views;

import org.vaadin.addons.springsecurityviewprovider.ViewDescription;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * @author Michael J. Simons, 2013-04-03
 */
@ViewDescription(
		name=CacheableTestView.VIEW_NAME,		
		cacheable=true
)
public class CacheableTestView implements View {
	private static final long serialVersionUID = 6508531543479423589L;

	public static final String VIEW_NAME = "/test/cacheableView";
	
	@Override
	public void enter(ViewChangeEvent event) {		
	}
}