package net.infstudio.inspiringworld.magic.repackage.net.simplelib.world;

import com.google.common.collect.Maps;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.Instance;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.Capabilities;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModHandler;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModProxy;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril.NBTTagBuilder;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.AttachWorldCapEvent;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.WorldPropertiesManager;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.chunk.ChunkData;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.entity.EntityState;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.entity.LivingAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

/**
 * @author ci010
 */
@ModHandler
@ModProxy(side = Side.SERVER, genericType = WorldPropertiesManager.class)
public class WorldPropertiesManagers implements WorldPropertiesManager
{
	private Map<Integer, CapabilityDispatcher> dimensions;

	@Instance
	private static WorldPropertiesManagers instance = new WorldPropertiesManagers();

	public static WorldPropertiesManagers instance()
	{
		return instance;
	}

	private WorldPropertiesManagers()
	{
		dimensions = Maps.newHashMapWithExpectedSize(DimensionManager.getIDs().length);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		CapabilityDispatcher dispatcher = Capabilities.gatherCapabilities(new AttachWorldCapEvent(event.getWorld()));
		if (dispatcher != null)
		{
			int dimension = event.getWorld().provider.getDimension();
			String name = "world_cap_" + dimension;
			CapLoader loader = (CapLoader) event.getWorld().loadItemData(CapLoader.class, name);
			loader.load(dispatcher);
			dimensions.put(dimension, dispatcher);
		}
	}

	@Override
	public ICapabilityProvider getCapabilityProvider(int dimension)
	{
		return this.dimensions.get(dimension);
	}

	@Override
	public ICapabilityProvider getCapabilityProvider(World world)
	{
		return getCapabilityProvider(world.provider.getDimension());
	}

	@Override
	public ChunkData getChunkData(World world, BlockPos pos)
	{
		return ChunkDataImpl.getChunkData(world, pos);
	}

	@Override
	public ChunkData getChunkData(World world, ChunkPos pos)
	{
		return ChunkDataImpl.getChunkData(world, pos);
	}

	@Override
	public ChunkData getChunkData(Chunk chunk)
	{
		return ChunkDataImpl.getChunkData(chunk);
	}

	@Override
	public EntityState getEntityState(Entity entity)
	{
		return null;
	}

	@Override
	public LivingAbility getAi(EntityLiving living)
	{
		return new LivingAIImpl(living);
	}

	private class CapLoader extends WorldSavedData
	{
		CapabilityDispatcher dispatcher;
		NBTTagCompound cache;

		public CapLoader(String name)
		{
			super(name);
		}

		void load(CapabilityDispatcher dispatcher)
		{
			this.dispatcher = dispatcher;
			dispatcher.deserializeNBT(cache);
			cache = null;
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt)
		{
			cache = NBTTagBuilder.create().copyFrom(nbt).build();
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
		{
			if (dispatcher != null)
				NBTTagBuilder.create(dispatcher.serializeNBT()).copyTo(nbt);
			return nbt;
		}
	}
}
