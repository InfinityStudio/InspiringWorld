package api.simplelib.registry.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CI010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.FIELD, ElementType.TYPE})
public @interface OreDic
{
	String value();
}
