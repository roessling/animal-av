package extras.lifecycle.checkpoint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * With this annotation a specific checkpoint event can be defined.
 *
 * @author Mihail Mihaylov
 *
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
public @interface Event {
	String name();
	Property[] properties();
	MultiProperty[] multiProperties() default @MultiProperty(name = "", properties = { @Property(name = "") });
}
