package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.base.Optional;
import com.google.common.primitives.Primitives;

/**
 * @author ci010
 */
public enum PrimitiveType
{
	BOOL(Boolean.class)
			{
				@Override
				public Object parse(String s)
				{
					return Boolean.parseBoolean(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Boolean.parseBoolean(s);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Boolean) a).compareTo((Boolean) b);
				}
			},
	BYTE(Byte.class)
			{
				@Override
				public Object parse(String s)
				{
					return Byte.parseByte(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Byte.parseByte(s, radix);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Byte) a).compareTo((Byte) b);
				}
			},
	SHORT(Short.class)
			{
				@Override
				public Object parse(String s)
				{
					return Short.parseShort(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Short.parseShort(s, radix);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Short) a).compareTo((Short) b);
				}
			},
	INT(Integer.class)
			{
				@Override
				public Object parse(String s)
				{
					return Integer.parseInt(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Integer.parseInt(s, radix);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Integer) a).compareTo((Integer) b);
				}
			},
	LONG(Long.class)
			{
				@Override
				public Object parse(String s)
				{
					return Long.parseLong(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Long.parseLong(s, radix);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Long) a).compareTo((Long) b);
				}
			},
	FLOAT(Float.class)
			{
				@Override
				public Object parse(String s)
				{
					return Float.parseFloat(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Float.parseFloat(s);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Float) a).compareTo((Float) b);
				}
			},
	DOUBLE(Double.class)
			{
				@Override
				public Object parse(String s)
				{
					return Double.parseDouble(s);
				}

				@Override
				public Object parse(String s, int radix)
				{
					return Double.parseDouble(s);
				}

				@Override
				protected int compare(Object a, Object b)
				{
					return ((Double) a).compareTo((Double) b);
				}
			};

	public static Optional<PrimitiveType> of(Class<?> type)
	{
		return Optional.fromNullable(ofUnsafe(type));
	}

	public static PrimitiveType ofUnsafe(Class<?> type)
	{
		Class<?> unwrap = Primitives.unwrap(type), wrap = type;
		if (unwrap == type)
		{
			wrap = Primitives.wrap(unwrap);
			if (wrap == type)
				return null;
		}
		for (PrimitiveType t : values())
			if (t.type == wrap)
				return t;
		return null;
	}

	private Class<?> type, unwrap;

	PrimitiveType(Class<?> type)
	{
		this.type = type;
		this.unwrap = Primitives.unwrap(type);
	}

	public abstract Object parse(String s);

	public abstract Object parse(String s, int radix);

	public boolean greater(Object a, Object b)
	{
		return this.compareBetween(a, b) > 0;
	}

	public boolean less(Object a, Object b)
	{
		int i = compareBetween(a, b);
		if (i == Integer.MIN_VALUE)
			return false;
		return i < 0;
	}

	public boolean equal(Object a, Object b)
	{
		return compareBetween(a, b) == 0;
	}

	public int compareBetween(Object a, Object b)
	{
		if (a.getClass() == type && type == b.getClass())
			return compare(a, b);
		return Integer.MIN_VALUE;
	}

	protected abstract int compare(Object a, Object b);

	public Class<?> getType()
	{
		return type;
	}

	public Class<?> getPrimitiveType()
	{
		return unwrap;
	}
}
