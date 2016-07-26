package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory.legacy;

import com.google.common.collect.ImmutableMap;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.Capabilities;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.CapabilityBuilderHandler;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.ModCapabilityBuilderHandler;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.ArrayUtils;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.EnumMap;

/**
 * @author ci010
 */
@ModCapabilityBuilderHandler
public class InvBaseProvider implements CapabilityBuilderHandler<LegacyInventoryBuilder>
{
	private class Impl implements LegacyInventoryBuilder
	{
		EnumMap<EnumFacing, int[]> map = new EnumMap<EnumFacing, int[]>(EnumFacing.class);
		int currentId = 0;
		boolean ignoreSide = false;

		@Override
		public void newSlot(int size, EnumFacing... faces)
		{
			if (faces == null)
			{
				ignoreSide = true;
				currentId = size;
				return;
			}
			if (ignoreSide)
				throw new IllegalArgumentException("All the side are occupied! Cannot have extra space!");
			int[] ints = ArrayUtils.iterate(currentId, currentId += size);
			for (EnumFacing face : faces)
				if (map.containsKey(face))
					throw new IllegalArgumentException("The side " + face + " is already occupied! Cannot allocate with " +
							"space!");
			for (EnumFacing value : faces)
				map.put(value, ints);
		}
	}

	@Override
	public LegacyInventoryBuilder createBuilder(Object contextSrc)
	{
		return new Impl();
	}

	@Override
	public void build(ImmutableMap.Builder<ResourceLocation, ICapabilityProvider> storage, LegacyInventoryBuilder o, Object context)
	{
		ResourceLocation location = new ResourceLocation(context + "invbase");
		Impl impl = (Impl) o;
		if (!impl.ignoreSide)
		{
			Capabilities.Builder<IItemHandler> builder = Capabilities.newBuilder(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			ISidedInventory inventory = InventoryBase.newInventoryWithSides(impl.map, impl.currentId);
			for (EnumFacing facing : impl.map.keySet())
				builder.append(new SidedInvWrapper(inventory, facing), facing);
			storage.put(location, builder.build());
		}
		else
			storage.put(location,
					Capabilities.newBuilder(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
							.append(new InvWrapper(InventoryBase.newInventory(impl.currentId))).build());
	}
}
