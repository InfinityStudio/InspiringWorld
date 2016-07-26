package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry;

import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * The class annotated by this will be registered as a {@link net.minecraftforge.fml.common.network.IGuiHandler}
 * <p>This is equivalent with {@link NetworkRegistry#registerGuiHandler(Object, IGuiHandler)} <p/>
 *
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
public @interface ModGuiHandler
{
}
