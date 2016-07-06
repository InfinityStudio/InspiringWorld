package net.simplelib.world;

import api.simplelib.sync.VarSync;
import api.simplelib.utils.RangedHelper;
import api.simplelib.vars.Var;
import io.netty.buffer.Unpooled;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import java.io.IOException;

/**
 * @author ci010
 */
public class VarWatching<T> implements VarSync<T>
{
	private DataParameter<T> id;
	private String name;
	private EntityDataManager delegate;

	protected VarWatching(DataParameter<T> id, EntityDataManager watcher, String name, T data)
	{
		if (data == null)
			throw new NullPointerException("The initial data cannot be null!");
		if (name == null)
			throw new NullPointerException("The name cannot be null!");
		this.name = name;
		this.delegate = watcher;
		this.id = id;
	}

	public DataParameter<T> getId()
	{
		return this.id;
	}

	protected EntityDataManager getDelegate()
	{
		return this.delegate;
	}

	public T get()
	{
		return delegate.get(id);
	}

	public void set(T data)
	{
		this.delegate.set(id, data);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		packetBuffer.writeNBTTagCompoundToBuffer(tag.getCompoundTag(this.name()));
		try
		{
			this.set(this.id.getSerializer().read(packetBuffer));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		this.id.getSerializer().write(packetBuffer, this.get());
		try
		{
			NBTTagCompound newTag = packetBuffer.readNBTTagCompoundFromBuffer();
			tag.setTag(this.name(), newTag);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return this.get().toString();
	}

	@Override
	public boolean isPresent()
	{
		return true;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public void addListener(ChangeListener<? super T> listener)
	{}

	@Override
	public void removeListener(ChangeListener<? super T> listener)
	{}

	@Override
	public T getValue()
	{
		return get();
	}

	@Override
	public void addListener(InvalidationListener listener)
	{}

	@Override
	public void removeListener(InvalidationListener listener)
	{}

	public static class VarWatchingEnum<T extends Enum<T>> implements VarSync<T>
	{
		private DataParameter<String> id;
		private String name;
		private EntityDataManager delegate;
		private Class<T> tClass;

		public VarWatchingEnum(Entity entity, Class<T> tClass, String name)
		{
			this.name = name;
			this.tClass = tClass;
			this.delegate = entity.getDataManager();
			id = EntityDataManager.createKey(entity.getClass(), DataSerializers.STRING);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			delegate.set(id, tag.getString(name()));
		}

		@Override
		public void writeToNBT(NBTTagCompound tag)
		{
			tag.setString(name(), delegate.get(id));
		}

		@Override
		public boolean isPresent() {return true;}

		@Override
		public String name()
		{
			return name;
		}

		@Override
		public void set(T value)
		{
			delegate.set(id, value.name());
		}

		@Override
		public T get()
		{
			return Enum.valueOf(tClass, delegate.get(id));
		}

		@Override
		public void addListener(ChangeListener<? super T> listener) {}

		@Override
		public void removeListener(ChangeListener<? super T> listener) {}

		@Override
		public T getValue() {return get();}

		@Override
		public void addListener(InvalidationListener listener) {}

		@Override
		public void removeListener(InvalidationListener listener) {}
	}

	public static abstract class Ranged<T extends Number> extends VarWatching<T>
	{
		private Var<T> helper;

		public Ranged(DataParameter<T> id, EntityDataManager watcher, String name, T data, T max, T min)
		{
			super(id, watcher, name, data);
			helper = new RangedHelper.Primitive<T>(this, min, max);
		}

		@Override
		public void set(T data)
		{
			helper.set(data);
		}
	}
}
