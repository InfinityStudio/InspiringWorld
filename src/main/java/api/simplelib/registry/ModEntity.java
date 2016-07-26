package api.simplelib.registry;

import net.minecraftforge.fml.client.registry.IRenderFactory;

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
public @interface ModEntity
{
	String name() default "";

	int id() default -1;

	int trackingRange() default 32;

	int updateFrequency() default 3;

	boolean sendsVelocityUpdates() default true;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.TYPE})
	@Deprecated
	@interface Render
	{
		Class<? extends net.minecraft.client.renderer.entity.Render> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.TYPE})
	@interface RenderFactory
	{
		Class<? extends IRenderFactory<?>> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.TYPE})
	@interface Spawner
	{
		int primaryColor();

		int secondaryColor();
	}
}
