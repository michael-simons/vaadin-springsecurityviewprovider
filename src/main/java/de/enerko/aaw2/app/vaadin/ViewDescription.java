package de.enerko.aaw2.app.vaadin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @author Michael J. Simons, 2013-03-04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ViewDescription {
	/** The name of the view, also used by the navigator */
	String name() default "";

    /**
     * @return the Spring-EL expression to be evaluated before invoking the protected method
     */
	String requiredPermissions() default "";
}