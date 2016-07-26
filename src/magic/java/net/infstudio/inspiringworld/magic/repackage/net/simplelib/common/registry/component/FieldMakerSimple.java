package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.OreDic;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Scan all the field and make them to {@link RegComponentBase}.
 * <p>
 * Simple maker to handle {@link net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ComponentStruct}.
 *
 * @author ci010
 */
public class FieldMakerSimple implements Function<Object, ImmutableSet<RegComponentBase>>
{
	private boolean staticSensitve;

	public FieldMakerSimple staticSensitve(boolean bool)
	{
		this.staticSensitve = bool;
		return this;
	}

	@Override
	public ImmutableSet<RegComponentBase> apply(Object input)
	{
		ImmutableSet.Builder<RegComponentBase> builder = ImmutableSet.builder();
		Class<?> clz;
		Object object;
		RegComponentBase space;
		boolean handled = false;
		if (input instanceof Class)
		{
			clz = (Class<?>) input;
			object = TypeUtils.instantiateQuite(clz);
		}
		else
		{
			object = input;
			clz = input.getClass();
		}

		for (Field f : clz.getDeclaredFields())
		{
			if (Modifier.isStatic(f.getModifiers()))
				if (!staticSensitve)
					continue;

			Class<?> type = f.getType();
			if (Block.class.isAssignableFrom(type) || Item.class.isAssignableFrom(type))
			{
				handled = true;
				if (!f.isAccessible())
					f.setAccessible(true);
				Object obj = null;
				try
				{
					obj = f.get(object);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				if (obj == null)
					obj = TypeUtils.instantiateQuite(type);
				if (obj != null)
				{
					builder.add(space = RegComponentBase.of(f.getName(), obj));
					String ore = null;
					OreDic anno = f.getAnnotation(OreDic.class);
					if (anno != null)
						ore = anno.value();
					space.setOreName(ore);
				}
				else
				{
					//TODO inject this.
					DebugLogger.fatal("Cannot include the {}.");
				}

			}
		}
		if (!handled)
		{
			DebugLogger.fatal("The class {} is neither a block nor an item. Moreover, it doesn't contain any " +
					"block or item");
		}
		return builder.build();
	}
}
