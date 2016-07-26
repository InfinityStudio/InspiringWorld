package api.simplelib.sync;


import api.simplelib.Overview;
import api.simplelib.network.simple.NBTClientMessage;
import api.simplelib.seril.NBTBases;
import api.simplelib.seril.NBTListBuilder;
import api.simplelib.seril.NBTTagBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Collection;
import java.util.List;

/**
 * @author ci010
 */
public class Attributes implements Overview<VarSync>, ChangeListener
{
	@CapabilityInject(Attributes.class)
	public static Capability<Attributes> CAPABILITY = null;

	private ImmutableList<VarSync> list;
	private ImmutableMap<String, VarSync> map;
	private NBTTagCompound location;

	public Attributes(ICapabilityProvider src, List<VarSync> vars)
	{
		ImmutableMap.Builder<String, VarSync> builder = ImmutableMap.builder();
		this.location = CapabilityProvidersSerializer.of(src);
		for (VarSync var : vars)
		{
			var.addListener(this);
			builder.put(var.name(), var);
		}
		this.list = ImmutableList.copyOf(vars);
		this.map = builder.build();
	}

	@Override
	public VarSync getById(int id)
	{
		return list.get(id);
	}

	@Override
	public VarSync getByName(String name)
	{
		return map.get(name);
	}

	@Override
	public Collection<VarSync> getAll()
	{
		return list;
	}

	@Override
	public void changed(ObservableValue observable, Object oldValue, Object newValue)
	{
		VarSync var = (VarSync) observable;
		NBTTagCompound tag = new NBTTagCompound();
		var.writeToNBT(tag);
		new SyncMessage(location, list.indexOf(var), tag);
	}

	public static class SyncMessage extends NBTClientMessage
	{
		public SyncMessage()
		{
			super();
		}

		public SyncMessage(NBTTagCompound location, ImmutableList<VarSync> data)
		{
			this();
			this.delegate.set(NBTTagBuilder.create().addTag("loc", location).addTag("data",
					NBTListBuilder.create(VarSync.class).appendAll(data).build()).build());
		}

		public SyncMessage(NBTTagCompound location, int id, NBTTagCompound data)
		{
			this();
			this.delegate.set(NBTTagBuilder.create(location).addTag("loc", location)
					.addTag("data", data).addInt("id", id).build());
		}

		@Override
		public IMessage handleClientMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
		{
			if (data.hasKey("id"))
			{
				int id = data.getInteger("id");
				NBTTagCompound loc = data.getCompoundTag("loc");
				ICapabilityProvider of = CapabilityProvidersSerializer.of(loc);
				if (of == null)
					throw new IllegalArgumentException();
				of.getCapability(CAPABILITY, null).list.get(id).readFromNBT(data.getCompoundTag("data"));
			}
			else
			{
				NBTTagList lst = data.getTagList("data", NBTBases.COMPOUND);
				NBTTagCompound loc = data.getCompoundTag("loc");
				ICapabilityProvider of = CapabilityProvidersSerializer.of(loc);
				if(of==null)
					throw new IllegalArgumentException();
				ImmutableList<VarSync> list = of.getCapability(CAPABILITY, null).list;
				for (int i = 0; i < lst.tagCount(); i++)
					list.get(i).readFromNBT(lst.getCompoundTagAt(i));
			}
			return null;
		}
	}
}
