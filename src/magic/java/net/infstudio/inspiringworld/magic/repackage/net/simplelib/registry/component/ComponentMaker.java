package net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.component;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.Instance;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ArgumentHelper;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ComponentStruct;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.Construct;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.OreDic;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.DebugLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Used to handle the {@link net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ComponentsReference}.
 *
 * @author ci010
 */
public class ComponentMaker implements Function<Class<?>, ImmutableSet<RegComponentBase>>
{
	private ReflectionAnnotatedMaker<Object, ImmutableSet<RegComponentBase>> maker;

	public ComponentMaker(Map<Class<? extends Annotation>, ArgumentHelper> map)
	{
		maker = new ReflectionAnnotatedMaker<Object, ImmutableSet<RegComponentBase>>(map)
		{
			@Override
			protected ImmutableSet<RegComponentBase> warp(Field f, Object target)
			{
				RegComponentBase space = RegComponentBase.of(f.getName(), target);
				String ore = null;
				OreDic anno = f.getAnnotation(OreDic.class);
				if (anno != null)
					ore = anno.value();
				space.setOreName(ore);
				return ImmutableSet.of(space);
			}
		};
	}

	@Override
	public ImmutableSet<RegComponentBase> apply(Class<?> container)
	{
		ImmutableSet.Builder<RegComponentBase> builder = ImmutableSet.builder();
		Optional<?> grab = Instance.Utils.grabAll(container);
		if (grab.isPresent())
		{
			maker.setObject(grab.get());
			for (Field f : container.getFields())
				if (f.isAnnotationPresent(Construct.class))
				{
					Class c = f.getType();
					if (Item.class.isAssignableFrom(c) || Block.class.isAssignableFrom(c) || f.isAnnotationPresent(ComponentStruct.class))
						builder.addAll(maker.apply(f));
				}
		}
		else
			for (Field f : container.getFields())
				if (Modifier.isStatic(f.getModifiers()))
					if (f.isAnnotationPresent(Construct.class))
					{
						Class c = f.getType();
						if (Item.class.isAssignableFrom(c) || Block.class.isAssignableFrom(c) || f.isAnnotationPresent(ComponentStruct.class))
							builder.addAll(maker.apply(f));
					}
					else
						DebugLogger.info("The field {} in container {} is not static so that it won'registerInit be constructed and registered",
								f.getName(),
								container.getName());
		return builder.build();
	}
}
