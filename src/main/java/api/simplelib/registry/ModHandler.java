package api.simplelib.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class annotated by this will be registered as a handler to event buses.
 *
 * @author CI010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
public @interface ModHandler
{
	/**
	 * @return The type of event bus you want to register to. Default is
	 * {@link net.minecraftforge.fml.common.FMLCommonHandler#eventBus} and {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
	 */
	Type[] value() default {Type.FML, Type.Forge};

	enum Type
	{
		/**
		 * {@link net.minecraftforge.fml.common.FMLCommonHandler#eventBus}
		 */
		FML
		/**
		 * {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
		 */
		, Forge
		/**
		 * {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS}
		 */
		, Terrain
		/**
		 * {@link net.minecraftforge.common.MinecraftForge#ORE_GEN_BUS}
		 */
		, OreGen
	}
}
