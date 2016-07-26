package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.base.Predicate;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;

/**
 * @author ci010
 */
public class RangedHelper<T> implements Var<T>
{
	private Predicate<T> predicate;
	private Var<T> var;

	public RangedHelper(Predicate<T> predicate, Var<T> var)
	{
		this.predicate = predicate;
		this.var = var;
	}

	@Override
	public void set(T value)
	{
		if (predicate.apply(value))
			var.set(value);
	}

	@Override
	public T get()
	{
		return var.get();
	}

	public static class Primitive<T extends Number> implements Var<T>
	{
		private Var<T> var;
		private T min, max;
		private PrimitiveType type;

		public Primitive(Var<T> var, T min, T max)
		{
			this.var = var;
			this.min = min;
			this.max = max;
			this.type = PrimitiveType.ofUnsafe(max.getClass());
		}

		@Override
		public void set(T value)
		{
			if (type.greater(max, value) && type.less(min, value))
				var.set(value);
		}

		@Override
		public T get()
		{
			return var.get();
		}
	}
}
