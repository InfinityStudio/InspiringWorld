package net.infstudio.inspiringworld.magic.repackage.api.simplelib;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
public @interface LoadingDelegate
{
	Side value() default Side.SERVER;

	String session() default "";
}
