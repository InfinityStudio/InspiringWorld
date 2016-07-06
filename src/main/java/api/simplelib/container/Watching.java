package api.simplelib.container;

import api.simplelib.utils.ArrayUtils;
import api.simplelib.vars.Var;
import com.google.common.collect.Sets;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Inherited
public @interface Watching
{
	class Utils
	{
		public static Field[] scan(Object entity)
		{
			Set<Field> watchings = Sets.newHashSet();
			for (Field field : entity.getClass().getDeclaredFields())
				if (!Modifier.isStatic(field.getModifiers()) && field.getType() == Integer.class &&
						field.isAnnotationPresent(Watching.class))
					watchings.add(field);
			for (Field field : entity.getClass().getFields())
				if (!Modifier.isStatic(field.getModifiers()) && field.getType() == Integer.class &&
						field.isAnnotationPresent(Watching.class))
					watchings.add(field);
			return watchings.toArray(new Field[watchings.size()]);
		}

		public static Var<Integer>[] wrap(Object o, Field[] field)
		{
			Var<Integer>[] vars = ArrayUtils.newArrayWithCapacity(field.length);
			for (int i = 0; i < field.length; i++)
			{
				Field f = field[i];
				if (!f.isAccessible())
					f.setAccessible(true);
				vars[i] = new VarBean(o, f);
			}
			return vars;
		}

		private static class VarBean implements Var<Integer>
		{
			Object object;
			Field field;

			VarBean(Object object, Field field)
			{
				this.object = object;
				this.field = field;
			}

			@Override
			public void set(Integer value)
			{
				try
				{
					field.set(object, value);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public Integer get()
			{
				try
				{
					return (Integer) field.get(object);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				return -1;
			}
		}
	}
}
