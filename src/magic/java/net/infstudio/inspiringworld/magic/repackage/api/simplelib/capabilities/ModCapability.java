package net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotations is used to register {@link ICapability}.
 *
 * @author ci010
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModCapability
{

}
