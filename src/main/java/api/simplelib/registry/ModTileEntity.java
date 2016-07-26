package api.simplelib.registry;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class annotated by this will be registered as a {@link TileEntity}.
 *
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
public @interface ModTileEntity
{
	/**
	 * @return the id of the tileEntity
	 */
	String value() default "";

	/**
	 * Register the special renderer for this {@link TileEntity}.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.TYPE})
	@interface Render
	{
		Class<? extends TileEntitySpecialRenderer> value();
	}
}
