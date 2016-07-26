package net.infstudio.inspiringworld.magic.repackage.net.simplelib.world;

import com.google.common.collect.ImmutableList;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSync;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSyncBase;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSyncFactory;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.VarSyncPrimitive;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public class CommonVarFactory implements VarSyncFactory
{
	private ImmutableList.Builder<VarSync> varCache = ImmutableList.builder();

	@Override
	public Var<Integer> newInteger(String name, int i)
	{
		VarSyncPrimitive<Integer> var = new VarSyncPrimitive<Integer>(name, i);
		varCache.add(var);
		return var;
	}

	@Override
	public Var<Integer> newInteger(String name, int i, int min, int max)
	{
		VarSyncPrimitive.Ranged<Integer> ranged = new VarSyncPrimitive.Ranged<Integer>(name, i, min, max);
		varCache.add(ranged);
		return ranged;
	}

	@Override
	public VarSync<Float> newFloat(String name, float f)
	{
		VarSyncPrimitive<Float> var = new VarSyncPrimitive<Float>(name, f);
		varCache.add(var);
		return var;
	}

	@Override
	public Var<Float> newFloat(String name, float f, float min, float max)
	{
		VarSyncPrimitive.Ranged<Float> ranged = new VarSyncPrimitive.Ranged<Float>(name, f, min, max);
		varCache.add(ranged);
		return ranged;
	}

	@Override
	public VarSync<Short> newShort(String name, short l)
	{
		VarSyncPrimitive<Short> var = new VarSyncPrimitive<Short>(name, l);
		varCache.add(var);
		return var;
	}

	@Override
	public Var<Short> newShort(String name, short s, short min, short max)
	{
		VarSyncPrimitive.Ranged<Short> ranged = new VarSyncPrimitive.Ranged<Short>(name, s, min, max);
		varCache.add(ranged);
		return ranged;
	}

	@Override
	public VarSync<Byte> newByte(String name, byte b)
	{
		VarSyncPrimitive<Byte> var = new VarSyncPrimitive<Byte>(name, b);
		varCache.add(var);
		return var;
	}

	@Override
	public Var<Byte> newByte(String name, byte b, byte min, byte max)
	{
		VarSyncPrimitive.Ranged<Byte> ranged = new VarSyncPrimitive.Ranged<Byte>(name, b, min, max);
		varCache.add(ranged);
		return ranged;
	}

	@Override
	public Var<Double> newDouble(String name, double d, double min, double max)
	{
		VarSyncPrimitive.Ranged<Double> ranged = new VarSyncPrimitive.Ranged<Double>(name, d, min, max);
		varCache.add(ranged);
		return ranged;
	}

	@Override
	public VarSync<String> newString(String name, String s)
	{
		VarSyncPrimitive<String> var = new VarSyncPrimitive<String>(name, s);
		varCache.add(var);
		return var;
	}

	@Override
	public Var<Double> newDouble(String name, double d)
	{
		VarSyncPrimitive<Double> var = new VarSyncPrimitive<Double>(name, d);
		varCache.add(var);
		return var;
	}

	@Override
	public <T extends Enum<T>> VarSync<T> newEnum(String name, T e, Class<T> enumClass)
	{
		VarSyncBase<T> var = new VarSyncBase<T>(name)
		{
			@Override
			public void readFromNBT(NBTTagCompound tag)
			{
				this.load(Enum.valueOf(this.get().getDeclaringClass(), tag.getString("enum-name")));
			}

			@Override
			public void writeToNBT(NBTTagCompound tag)
			{
				tag.setString("enum-name", this.get().name());
			}
		};
		varCache.add(var);
		return var;
	}

	ImmutableList<VarSync> getAllTracking()
	{
		return this.varCache.build();
	}
}
