package api.simplelib;

import api.simplelib.utils.TypeUtils;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBufInputStream;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.simplelib.HelperMod;
import net.simplelib.common.DebugLogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

/**
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.FIELD, ElementType.METHOD})
public @interface Instance
{
	boolean weak() default false;

	class Utils
	{
		static Cache<Class, Object> cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();

		public static <T> Optional<T> grabFast(Class<T> clz)
		{
			Field f = null;
			Object ifPresent = cache.getIfPresent(clz);
			if (ifPresent != null) return Optional.of(TypeUtils.<T>cast(ifPresent));
			T instance = null;
			try {f = clz.getDeclaredField("instance");}
			catch (NoSuchFieldException ignored) {}
			if (f == null)
				try {f = clz.getField("INSTANCE");}
				catch (NoSuchFieldException ignored) {}
			if (f != null)
				if (Modifier.isStatic(f.getModifiers()))
					try
					{
						if (!f.isAccessible())
							f.setAccessible(true);
						instance = TypeUtils.cast(f.get(null));
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
			if (instance == null)
			{
				Method method = null;
				try {method = clz.getMethod("instance");}
				catch (NoSuchMethodException ignored) {}
				if (method == null)
					try {method = clz.getMethod("getInstance");}
					catch (NoSuchMethodException e) {e.printStackTrace();}
				if (method != null)
					if (Modifier.isStatic(method.getModifiers()) && method.getReturnType().isAssignableFrom(clz))
					{
						if (!method.isAnnotationPresent(Instance.class))
							DebugLogger.warn("Grab the instance from the method {} which wasn't annotated by @Instance!", method);
						try
						{
							instance = TypeUtils.cast(method.invoke(null));
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
					}
			}
			if (instance != null) cache.put(clz, instance);
			return Optional.fromNullable(instance);
		}

		public static <T> Optional<T> grab(Class<T> clz)
		{
			T o = null;
			for (Method m : clz.getMethods())
				if (m.isAnnotationPresent(Instance.class))
				{
					if (Modifier.isStatic(m.getModifiers()) && m.getReturnType().isAssignableFrom(clz))
						try
						{
							o = TypeUtils.cast(m.invoke(null));
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
				}
			if (o == null)
			{
				for (Field field : clz.getDeclaredFields())
				{
					Instance annotation = field.getAnnotation(Instance.class);
					if (annotation != null)
					{
						int modifiers = field.getModifiers();
						if (!Modifier.isStatic(modifiers))
						{
							HelperMod.LOG.fatal("The field annotated by EnumType should be static! cannot grab the instance.");
							return Optional.absent();
						}
						if (!field.getType().isAssignableFrom(clz))
						{
							HelperMod.LOG.fatal("Illegal field type! The type {} cannot cast to type {}", clz, field.getType());
							return Optional.absent();
						}
						if (Modifier.isPrivate(modifiers))
							o = ReflectionHelper.getPrivateValue(clz, null, field.getName());
						else
							try
							{
								o = TypeUtils.cast(field.get(null));
							}
							catch (IllegalAccessException e)
							{
								e.printStackTrace();
							}
						if (o == null)
							if (!annotation.weak())
								try
								{
									o = clz.newInstance();
								}
								catch (InstantiationException e)
								{
									e.printStackTrace();
								}
								catch (IllegalAccessException e)
								{
									e.printStackTrace();
								}
					}
				}
			}
			if (o != null) cache.put(clz, o);
			return Optional.fromNullable(o);
		}

	}
}
