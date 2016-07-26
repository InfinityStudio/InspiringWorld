package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModConfig
{
	String categoryId() default "default";

	String id();

	String comment() default "";

	boolean showInGui() default false;

	boolean requireMCRestart() default false;

	boolean requireWorldRestart() default false;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface ValidRange
	{
		/**
		 * @return The valid range of the config. If the config is number, it will find the max/min value in this
		 * range to set as maximum value and minimum value.
		 */
		String[] range();
	}

	/**
	 * An OPTIONAL @interface for array. If you didn't initialized array, you have to have this annotation so that we
	 * will know the length of the array!
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Length
	{
		int maxLength();
	}
}
