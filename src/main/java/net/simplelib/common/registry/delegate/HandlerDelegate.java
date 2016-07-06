package net.simplelib.common.registry.delegate;

import com.google.common.collect.ImmutableSet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.simplelib.HelperMod;
import net.simplelib.common.DebugLogger;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import api.simplelib.registry.ModHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * @author ci010
 */
@LoadingDelegate
public class HandlerDelegate extends ASMRegistryDelegate<ModHandler>
{
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Object obj = null;
		Field field = null;
		boolean setField = false, weak = false;
		for (Field f : this.getAnnotatedClass().getDeclaredFields())
			if (f.isAnnotationPresent(Instance.class))
			{
				Instance anno = f.getAnnotation(Instance.class);
				weak = anno.weak();
				if (Modifier.isStatic(f.getModifiers()))
					field = f;
				else
				{}
			}
		if (field != null)
		{
			if (!field.isAccessible())
				field.setAccessible(true);
			try
			{
				obj = field.get(null);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		if (obj == null && !weak)
			try
			{
				Constructor<?> constructor = this.getAnnotatedClass().getDeclaredConstructor();
				if (!constructor.isAccessible())
					constructor.setAccessible(true);
				obj = constructor.newInstance();
				setField = true;
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		if (field != null && setField)
			try
			{
				if (!field.isAccessible())
					field.setAccessible(true);
				field.set(null, obj);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		if (obj == null)
		{
			HelperMod.LOG.fatal("Cannot create an instance of {}. It will not be registered as a handler.", this.getAnnotatedClass());
			return;
		}

//		if (IFuelHandler.class.isAssignableFrom(this.getAnnotatedClass()))
//			GameRegistry.registerFuelHandler((IFuelHandler) obj);
		ImmutableSet<ModHandler.Type> set = ImmutableSet.copyOf(this.getAnnotation().value());
		String info = "Register EventHandler: [";
		for (ModHandler.Type type : set)
			info = info.concat(type.toString()).concat("|");
		info = info.substring(0, info.length() - 1).concat("] <- [{}:{}]");

		DebugLogger.info(info, this.getModid(), this.getAnnotatedClass().getName());
		if (set.isEmpty())
		{
			DebugLogger.warn("The handler class [{}] doesn't contain any method needed to be registered!",
					this.getAnnotatedClass().getName());
			return;
		}
		if (set.contains(ModHandler.Type.Terrain))
			MinecraftForge.TERRAIN_GEN_BUS.register(obj);
		if (set.contains(ModHandler.Type.Forge) || set.contains(ModHandler.Type.FML))
			MinecraftForge.EVENT_BUS.register(obj);
		if (set.contains(ModHandler.Type.OreGen))
			MinecraftForge.ORE_GEN_BUS.register(obj);
	}
}
