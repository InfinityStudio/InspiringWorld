package api.simplelib.utils;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author ci010
 */
public class ASMDataUtil
{
	private static Map<String, Class<?>> cache;

	/**
	 * @param data The ASMData harvested by FML.
	 * @return The mod's id of the ASMData referring to.
	 */
	public static String getModId(ASMDataTable.ASMData data)
	{
		return data.getCandidate().getContainedMods().get(0).getModId();
	}

	/**
	 * @param data   The ASMData harvested by FML.
	 * @param clz    The annotation class you want to harvest from ASMData.
	 * @param <Anno> The type of that annotation.
	 * @return The specific type of Annotation in the ASMData.
	 */
	public static <Anno extends Annotation> Anno getAnnotation(ASMDataTable.ASMData data, Class<Anno> clz)
	{
		return getClass(data).getAnnotation(clz);
	}

	public static Field getField(ASMDataTable.ASMData data)
	{
		try
		{
			return getClass(data).getField(data.getObjectName());
		}
		catch (NoSuchFieldException e)
		{
			return null;
		}
	}

	public static Optional<Object> getObject(ASMDataTable.ASMData data)
	{
		try
		{
			return Optional.fromNullable(getClass(data).getField(data.getObjectName()).get(null));
		}
		catch (NoSuchFieldException e)
		{
			return Optional.absent();
		}
		catch (IllegalAccessException e)
		{
			return Optional.absent();
		}
	}

	/**
	 * @param data The ASMData harvested by FML.
	 * @return The class referred by this ASMData
	 */
	public static Class<?> getClass(ASMDataTable.ASMData data)
	{
		if (cache == null)
			cache = Maps.newHashMap();
		Class<?> c = null;
		try
		{
			String name = data.getClassName();
			if (cache.containsKey(name))
				c = cache.get(name);
			else
			{
				c = Class.forName(name);
				cache.put(name, c);
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * @param data The ASMData harvested by FML.
	 * @return The class referred by this ASMData
	 */
	public static Class<?> getClassSafe(ASMDataTable.ASMData data) throws ClassNotFoundException
	{
		if (cache == null)
			cache = Maps.newHashMap();
		Class<?> c = null;
		String name = data.getClassName();
		if (cache.containsKey(name))
			c = cache.get(name);
		else
		{
			c = Class.forName(name);
			cache.put(name, c);
		}
		return c;
	}

	public static void clear()
	{
		if (Loader.instance().getLoaderState() == LoaderState.AVAILABLE)
			cache = null;
	}
}
