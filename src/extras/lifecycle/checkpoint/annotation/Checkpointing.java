/**
 * 
 */
package extras.lifecycle.checkpoint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This marks the field, that it should be included in the checkpoint state.
 *
 * @author Mihail Mihaylov
 *
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.TYPE })
public @interface Checkpointing {
	
}
