package net.simplelib.common;

import api.simplelib.utils.RangedHelper;
import api.simplelib.utils.TypeUtils;
import api.simplelib.sync.VarSyncBase;
import net.minecraft.nbt.NBTTagCompound;
import api.simplelib.seril.NBTBases;

/**
 * @author ci010
 */
public class VarSyncPrimitive<T> extends VarSyncBase<T>
{
	public VarSyncPrimitive(String id, T data)
	{
		super(id);
		this.set(data);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.getTag(name()) == null)
			return;
		this.data = (TypeUtils.cast(NBTBases.instance().deserialize(tag.getTag(name()))));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setTag(name(), NBTBases.instance().serialize(this.get()));
	}

	@Override
	public boolean isPresent()
	{
		return true;
	}

	public static class Ranged<T extends Number> extends VarSyncPrimitive<T>
	{
		private RangedHelper.Primitive<T> helper;

		public Ranged(String id, T data, T max, T min)
		{
			super(id, data);
			helper = new RangedHelper.Primitive<T>(this, min, max);
		}

		@Override
		public void set(T data)
		{
			helper.set(data);
		}
	}
}
