package net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.component;

import com.google.common.base.Function;
import com.google.common.primitives.Primitives;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ArgumentHelper;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.Construct;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ComponentsReference.Ref;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.DebugLogger;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.RegistryHelper;
import net.minecraft.util.ResourceLocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author ci010
 */
public abstract class ReflectionAnnotatedMaker<Input, Output> implements Function<Field, Output>
{
	private Map<Class<? extends Annotation>, ArgumentHelper> map;
	private Object obj;

	public ReflectionAnnotatedMaker(Map<Class<? extends Annotation>, ArgumentHelper> map)
	{
		this.map = map;
	}

	public void setObject(Object o)
	{
		this.obj = o;
	}

	/**
	 * Construct the field if it's null.
	 * Extract the value and wrap it into the output type.
	 *
	 * @param f The field will be extracted.
	 * @return The output
	 */
	public Output apply(Field f)
	{
		Input item = null;
		Construct ctr = f.getAnnotation(Construct.class);
		Ref ref;
		if (ctr != null)
		{
			Object[] args = new Object[]{};

			for (Annotation a : f.getAnnotations())
				if (map.containsKey(a.annotationType()))
				{
					args = map.get(a.annotationType()).getArguments(a);
					break;
				}
			int length = args.length;
			Class<?>[] argType = new Class<?>[length];

			for (int i = 0; i < length; i++)
				argType[i] = Primitives.unwrap(args[i].getClass());
			Class<? extends Input> temp = TypeUtils.cast(ctr.value());
			try
			{
				Constructor<? extends Input> constructor = temp.getConstructor(argType);
				item = TypeUtils.instantiateQuite(constructor, args);
			}
			catch (NoSuchMethodException e)
			{
				DebugLogger.fatal("Cannot find a constructor in {} with type {}. Therefore, it cannot be " +
						"instantiated", ctr.value().getName(), argType);
			}
		}
		else if ((ref = f.getAnnotation(Ref.class)) != null)
			item = TypeUtils.cast(RegistryHelper.INSTANCE.find(new ResourceLocation(ref.value())));

		if (item == null)
		{
			DebugLogger.fatal("Item Field {}'s value is null. It will not be registered!", f.getName());
			return null;
		}

		try
		{
			if (obj == null)
				f.set(null, item);
			else
				f.set(obj, item);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return this.warp(f, item);
	}

	/**
	 * Wrap the value of the field into certain type.
	 *
	 * @param target The value of the field.
	 * @return The standard output for this value.
	 */
	protected abstract Output warp(Field f, Input target);
}
