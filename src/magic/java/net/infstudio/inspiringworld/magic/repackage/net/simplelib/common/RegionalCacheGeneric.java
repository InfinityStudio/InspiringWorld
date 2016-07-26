package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;

/**
 * @author ci010
 */
public class RegionalCacheGeneric<T>
{
	private T[][] cache;
	private int radius, diameter;

	private OffsetMapping mapping;
	private int centerX, centerZ;
	private ValueReplacement<T> delegate;
	private Logic logic;

	public RegionalCacheGeneric(int radius, OffsetMapping mapping, ValueReplacement<T> delegate)
	{
		this.diameter = radius * 2 + 1;
		this.cache = TypeUtils.cast(new Object[diameter][diameter]);
		this.mapping = mapping;
		this.radius = radius;
		this.centerX = Integer.MIN_VALUE;
		this.centerZ = Integer.MIN_VALUE;
		this.delegate = delegate;
		this.logic = initLogic();

	}


	public T getCenter()
	{
		return this.cache[radius][radius];//TODO check this
	}

	public T[][] getCache()
	{
		T[][] temp = TypeUtils.cast(new Object[this.diameter][this.diameter]);
		System.arraycopy(cache, 0, temp, 0, this.diameter);
		return temp;
	}

	public void moveTo(int x, int z)
	{
		int oldX = this.centerX, oldZ = this.centerZ;
		this.centerX = x;
		this.centerZ = z;
		int xOffset = mapping.getOffset(this.centerX - x);
		int zOffset = mapping.getOffset(this.centerZ - z);

		if (oldX == Integer.MIN_VALUE || oldZ == Integer.MIN_VALUE || xOffset > diameter || zOffset > diameter)
			this.logic.refresh();
		else
			this.logic.move(xOffset, zOffset);
	}

	public T get(int x, int z)
	{
		return cache[x][z];
	}

	public void set(T data, int x, int z)
	{
		this.cache[x][z] = data;
	}

	private interface Logic
	{
		void refresh();

		void move(int xOffset, int zOffset);
	}

	private Logic initLogic()
	{
		return this.delegate instanceof ValueModify ?
				new Logic()
				{
					private ValueModify<T> delegate = (ValueModify<T>) RegionalCacheGeneric.this.delegate;
					private boolean init = false;

					@Override
					public void refresh()
					{
						if (!init)
						{
							for (int x = 0; x < diameter; x++)
								for (int z = 0; z < diameter; z++)
									cache[x][z] = this.delegate.create(
											centerX + mapping.getRealOffset(x - radius),
											centerZ + mapping.getRealOffset(z - radius));
							init = true;
							return;
						}
						for (int x = 0; x < diameter; x++)
							for (int z = 0; z < diameter; z++)
								this.delegate.modify(cache[x][z],
										centerX + mapping.getRealOffset(x - radius),
										centerZ + mapping.getRealOffset(z - radius));
					}

					@Override
					public void move(int xOffset, int zOffset)
					{
						if (xOffset < 0)
						{
							for (int x = 0; x < diameter + xOffset; x++)
							{
								if (zOffset >= 0)
									for (int z = 0; z < diameter - zOffset; z++)
										this.delegate.switchValue(cache[x][z], cache[x - xOffset][z + zOffset]);
							}
						}
					}
				} :
				new Logic()
				{
					@Override
					public void refresh()
					{
						for (int x = 0; x < diameter; x++)
							for (int z = 0; z < diameter; z++)
								cache[x][z] = delegate.create(
										centerX + mapping.getRealOffset(x - radius),
										centerZ + mapping.getRealOffset(z - radius));
					}

					@Override
					public void move(int xOffset, int zOffset)
					{
						T[][] temp = TypeUtils.cast(new Object[diameter][diameter]);
						int range = diameter - xOffset;
						if (xOffset < 0)
							for (int x = 0; x < range; x++)
								if (zOffset >= 0)
									System.arraycopy(cache[x], 0, temp[x - xOffset], zOffset, diameter - zOffset);
								else
									System.arraycopy(cache[x], -zOffset, temp[x - xOffset], 0, diameter + zOffset);
						else if (xOffset >= 0)
							for (int x = xOffset; x < diameter; x++)
								if (zOffset >= 0)
									System.arraycopy(cache[x], 0, temp[x - xOffset], zOffset, diameter - zOffset);
								else
									System.arraycopy(cache[x], -zOffset, temp[x - xOffset], 0, diameter + zOffset);
						for (int x = 0; x < diameter; x++)
							for (int z = 0; z < diameter; z++)
							{
								T v = temp[x][z];
								if (v == null)
									temp[x][z] = delegate.create(
											centerX + mapping.getRealOffset(x - radius),
											centerZ + mapping.getRealOffset(z - radius));
							}
						cache = temp;
					}
				};
	}

	public interface ValueReplacement<T>
	{
		T create(int x, int z);
	}

	public interface ValueModify<T> extends ValueReplacement<T>
	{
		void modify(T container, int x, int z);

		void switchValue(T container, T target);
	}

	public interface OffsetMapping
	{
		int getOffset(int realOffset);

		int getRealOffset(int offset);
	}
}
