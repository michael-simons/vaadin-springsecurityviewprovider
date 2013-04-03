package org.vaadin.addons.springsecurityviewprovider;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;

/**
 * An annotation that describes a Vaadin {@link View}<br>
 * A view is a managed Component whose scope is prototype, meaning
 * you'll always get a fresh instance for every application instance.
 * @author Michael J. Simons, 2013-03-04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope(SCOPE_PROTOTYPE)
public @interface ViewDescription {
	/** The name of the view, also used by the navigator, can be a complete path like /foo/bar/baz */
	String name() default "";

	/**
	 * @return the Spring-EL expression to be evaluated before the view described here is finally added to the navigator.
	 */
	String requiredPermissions() default "";

	/**
	 * @return Can this view be cached if caching is available?
	 */
	boolean cacheable() default false;
}