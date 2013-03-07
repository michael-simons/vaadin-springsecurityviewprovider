package de.enerko.aaw2.app.vaadin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.UI;

/**
 * Dieser spezielle Navigator wertet die Annotation
 * {@link ViewDescription} aus.
 * 
 * Falls die Beschreibung {@link ViewDescription#requiredPermissions()}
 * anfordert, werden diese Permissions genauso wie {@link PreAuthorize
 * Ausdrücke im aktuellen Spring Security Context ausgewertet. Fals die
 * Auswertung false ergibt, wird diese View erst gar nicht geladen.
 * 
 * Ansonsten wird ein eigener View Provider genutzt, der die Beans bei
 * Bedarf aus dem Application Context bezieht.
 * 
 * Von diesem Navigator werden keine <code>add*</code> oder <code>remove*</code>
 * Methoden unterstützt.
 * 
 * @author Michael J. Simons, 2013-03-04
 */
public class SpringSecurityNavigator extends Navigator {
	private static final long serialVersionUID = -7896790310309542217L;

	/**
	 * View Provider, der Views aus dem Spring Application Context bezieht
	 */
	@Configurable
	private class SpringViewProvider implements ViewProvider {		
		private static final long serialVersionUID = -8555986824827085073L;
		@Autowired
		transient ApplicationContext applicationContext;
		final Map<String, Class<? extends View>> views = new HashMap<>();		

		@Override
		public String getViewName(String viewAndParameters) {
			String rv = null;
			if(viewAndParameters != null) {
				if(views.containsKey(viewAndParameters))
					rv = viewAndParameters;
				else {
					for(String viewName : views.keySet()) {
						if(viewAndParameters.startsWith(viewName + "/")) {
							rv = viewName;
							break;
						}
					}
				}
			}
			return rv;
		}

		@Override
		public View getView(String viewName) {
			return applicationContext.getBean(this.views.get(viewName));
		}		
	}

	@SuppressWarnings("unchecked")
	public SpringSecurityNavigator(
			final ApplicationContext applicationContext,
			final Authentication authentication,
			final UI ui, 
			final ViewDisplay display
	) {
		super(ui, display);

		try {
			final SpringViewProvider springViewProvider = new SpringViewProvider();		
	
			final MethodSecurityExpressionHandler securityExpressionHandler = applicationContext.getBean(DefaultMethodSecurityExpressionHandler.class);
			final Method getViewMethod = SpringViewProvider.class.getMethod("getView", String.class);
			final SpelExpressionParser parser = new SpelExpressionParser();
	
			for(String beanName : applicationContext.getBeanDefinitionNames()) {
				final Class<?> beanClass = applicationContext.getType(beanName);
				if (beanClass.isAnnotationPresent(ViewDescription.class) && View.class.isAssignableFrom(beanClass)) {
					ViewDescription viewDescription = beanClass.getAnnotation(ViewDescription.class);
					if(StringUtils.isBlank(viewDescription.requiredPermissions())) {
						springViewProvider.views.put(viewDescription.name(), (Class<? extends View>) beanClass);
					} else {
						final EvaluationContext evaluationContext = securityExpressionHandler.createEvaluationContext(authentication, new SimpleMethodInvocation(springViewProvider, getViewMethod, viewDescription.name()));
						if(ExpressionUtils.evaluateAsBoolean(parser.parseExpression(viewDescription.requiredPermissions()), evaluationContext))
							springViewProvider.views.put(viewDescription.name(), (Class<? extends View>) beanClass);							
					}
				}        
	        }			
			super.addProvider(springViewProvider);
		} catch (NoSuchMethodException | SecurityException e) {
			// Won't happen
		} 
	}

	@Override
	public void addView(String viewName, View view) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addView(String viewName, Class<? extends View> viewClass) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addProvider(ViewProvider provider) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeView(String viewName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeProvider(ViewProvider provider) {
		throw new UnsupportedOperationException();
	}
}