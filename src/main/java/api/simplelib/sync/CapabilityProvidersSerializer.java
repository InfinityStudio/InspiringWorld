package api.simplelib.sync;

import api.simplelib.seril.ITagSerializer;
import api.simplelib.seril.NBTTagBuilder;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

/**
 * @author ci010
 */
public class CapabilityProvidersSerializer implements ITagSerializer<ICapabilityProvider>
{
	private static List<ITagSerializer<ICapabilityProvider>> list = Lists.newArrayList();

	private CapabilityProvidersSerializer() {}

	static
	{
		list.add(new CapabilityProvidersSerializer());
	}

	public static void register(ITagSerializer<ICapabilityProvider> prov)
	{
		list.add(prov);
	}

	public static NBTTagCompound of(ICapabilityProvider provider)
	{
		for (ITagSerializer<ICapabilityProvider> serializer : list)
		{
			NBTTagCompound tag = serializer.serialize(provider);
			if (tag != null)
				return tag;
		}
		return null;
	}

	public static ICapabilityProvider of(NBTTagCompound tagCompound)
	{
		for (ITagSerializer<ICapabilityProvider> serializer : list)
		{
			ICapabilityProvider provider = serializer.deserialize(tagCompound);
			if (provider != null)
				return provider;
		}
		return null;
	}

	@Override
	public ICapabilityProvider deserialize(NBTTagCompound tag)
	{
		String type = tag.getString("type");
		if (type.equals("tileentity"))
		{
			int world = tag.getInteger("world"), x = tag.getInteger("x"), y = tag.getInteger("y"), z = tag.getInteger
					("z");
			return DimensionManager.getWorld(world).getTileEntity(new BlockPos(x, y, z));
		}
		if (type.equals("entity"))
		{
			int world = tag.getInteger("world"), id = tag.getInteger("entity");
			return DimensionManager.getWorld(world).getEntityByID(id);
		}
		return null;
	}

	@Override
	public NBTTagCompound serialize(ICapabilityProvider data)
	{
		if (data instanceof Entity)
			return NBTTagBuilder.create().addInt("world", ((Entity) data).worldObj.provider.getDimension()).addInt("entity", (
					(Entity) data)
					.getEntityId()).build();
		if (data instanceof TileEntity)
			return NBTTagBuilder.create().addInt("world", ((TileEntity) data).getWorld().provider.getDimension())
					.addInt("x", ((TileEntity) data).getPos().getX())
					.addInt("y", ((TileEntity) data).getPos().getY())
					.addInt("z", ((TileEntity) data).getPos().getZ()).build();
		return null;
	}

}
