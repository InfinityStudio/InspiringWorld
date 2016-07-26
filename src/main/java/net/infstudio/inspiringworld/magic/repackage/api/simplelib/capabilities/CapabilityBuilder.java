package net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is just a indicator for the class which could be a CapabilityBuilder.
 * <p>The context will be create by {@link CapabilityBuilderHandler#createBuilder(Object)}.</p>
 *
 * @author ci010
 * @see CapabilityBuilderHandler
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CapabilityBuilder
{
	/**
	 * @return The type of this context will bring.
	 */
	Class<?>[] value();
}
