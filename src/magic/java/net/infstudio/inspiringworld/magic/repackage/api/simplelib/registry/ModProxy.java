package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry;


import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModProxy
{
	/**
	 * The side of the proxy.
	 * <p>To apply it as common injection, fill this with {@link Side#SERVER}</p>
	 *
	 * @return side
	 */
	Side side();

	Class genericType() default Void.class;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Inject
	{
		Class genericType() default Void.class;
	}
}
