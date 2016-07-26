package net.infstudio.inspiringworld.magic.repackage.net.simplelib.world;

import com.google.common.collect.ImmutableList;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril.DataSerializerView;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSync;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSyncBase;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.VarSyncFactory;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * @author ci010
 */
public class EntityVarFactory implements VarSyncFactory
{
	private ImmutableList.Builder<VarSync> builder = ImmutableList.builder();
	private Entity entity;
	private int currentId;

	public EntityVarFactory(Entity entity)
	{
		this.entity = entity;
		currentId = 10;
	}

	protected <T> DataParameter<T> registerDataWatcher(T o)
	{
		DataSerializer<T> byClass = TypeUtils.cast(DataSerializerView.getByClass(o.getClass()));
		DataParameter<T> key = EntityDataManager.createKey(entity.getClass(), byClass);
		entity.getDataManager().register(key, o);
		return key;
	}

	@Override
	public VarWatching<Integer> newInteger(String name, int i)
	{
		VarWatching<Integer> var = new VarWatching<Integer>(registerDataWatcher(i), entity.getDataManager(),
				name, i);
		builder.add(var);
		return var;
	}

	@Override
	public Var<Integer> newInteger(String name, int i, int min, int max)
	{
		return null;
	}

	@Override
	public VarWatching<Float> newFloat(String name, float f)
	{
		VarWatching<Float> var = new VarWatching<Float>(registerDataWatcher(f), entity.getDataManager(), name, f);
		builder.add(var);
		return var;
	}

	@Override
	public Var<Float> newFloat(String name, float f, float min, float max)
	{
		return null;
	}

	@Override
	public VarWatching<Short> newShort(String name, short s)
	{
		VarWatching<Short> var = new VarWatching<Short>(registerDataWatcher(s), entity.getDataManager(), name, s);
		builder.add(var);
		return var;
	}

	@Override
	public Var<Short> newShort(String name, short s, short min, short max)
	{
		return null;
	}

	@Override
	public VarWatching<Byte> newByte(String name, byte b)
	{
		VarWatching<Byte> var = new VarWatching<Byte>(registerDataWatcher(b), entity.getDataManager(), name, b);
		builder.add(var);
		return var;
	}

	@Override
	public Var<Byte> newByte(String name, byte b, byte min, byte max)
	{
		return null;
	}

	@Override
	public VarWatching<String> newString(String name, String s)
	{
		VarWatching<String> var = new VarWatching<String>(registerDataWatcher(s), entity.getDataManager(), name, s);
		builder.add(var);
		return var;
	}

	@Override
	public VarSync<Double> newDouble(final String name, double d)
	{
		VarWatching<Double> var = new VarWatching<Double>(registerDataWatcher(d), entity.getDataManager(),
				name, d);
		builder.add(var);
		return var;
	}

	@Override
	public Var<Double> newDouble(String name, double d, double min, double max)
	{
		return null;
	}

	@Override
	public <T extends Enum<T>> VarSync<T> newEnum(String name, T e, final Class<T> enumClass)
	{
		VarWatching.VarWatchingEnum<T> var = new VarWatching.VarWatchingEnum<T>(entity, enumClass, name);
		this.builder.add(var);
		return var;
	}

	ImmutableList<VarSync> getAllTracking()
	{
		return this.builder.build();
	}

}
