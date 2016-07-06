package net.simplelib.inventory;

import api.simplelib.capabilities.Capabilities;
import api.simplelib.capabilities.CapabilityBuilderHandler;
import api.simplelib.inventory.*;
import api.simplelib.utils.ArrayUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * @author ci010
 */
public class InventoryBuilderHandler implements CapabilityBuilderHandler<InventoryBuilder>
{
	@Override
	public InventoryBuilder createBuilder(Object contextSrc)
	{
		return new Impl();
	}

	@Override
	public void build(ImmutableMap.Builder<ResourceLocation, ICapabilityProvider> storage, InventoryBuilder inventoryBuilder, Object context)
	{
		Impl impl = (Impl) inventoryBuilder;
		InvImpl inv = impl.buildInventory();

		storage.put(new ResourceLocation(context + "inventory"), Capabilities.newBuilder(Inventory.CAPABILITY)
				.append(inv).build());

		Capabilities.Builder<IItemHandler> builder = Capabilities.newBuilder(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		if (!impl.sideMap.isEmpty())
			for (EnumFacing facing : impl.sideMap.keySet())
				builder.append(new SidedInvWrapper(inv, facing), facing).build();
		else builder.append(new InvWrapper(inv));
		storage.put(new ResourceLocation(context + "invbase"), builder.build());
	}

	private class Impl implements InventoryBuilder
	{
		private int currentIdx = 0;
		private InvImpl inv = new InvImpl();
		private EnumMap<EnumFacing, int[]> sideMap = new EnumMap<EnumFacing, int[]>(EnumFacing.class);
		private ArrayList<InventoryElement> elements = new ArrayList<InventoryElement>();

		public InvImpl buildInventory()
		{
			if (currentIdx == 0)
				return null;
			inv.build(sideMap, currentIdx, elements);
			return inv;
		}

		public InventorySpace newSpace(String name, int size, EnumFacing facing, InventoryRule rule)
		{
			if (facing != null)
			{
				int[] newArr = new int[size];
				for (int i = 0; i < size; i++)
					newArr[i] = currentIdx + i;
				if (sideMap.containsKey(facing))
					newArr = ArrayUtils.concat(sideMap.get(facing), newArr);
				sideMap.put(facing, newArr);
			}
			InvSpaceImpl space = new InvSpaceImpl(name, inv, currentIdx, size, rule);
			currentIdx += size;
			elements.add(space);
			return space;
		}

		public InventorySlot newSlot(String name, EnumFacing facing, InventoryRule rule)
		{
			if (facing != null)
			{
				int[] newArr = new int[]{currentIdx};
				if (sideMap.containsKey(facing))
					newArr = ArrayUtils.concat(sideMap.get(facing), newArr);
				sideMap.put(facing, newArr);
			}
			SlotSpaceImpl slotSpace = new SlotSpaceImpl(name, inv, currentIdx, rule);
			++currentIdx;
			elements.add(slotSpace);
			return slotSpace;
		}

		@Override
		public InventorySlot newSlot(String name)
		{
			return newSlot(name, null, null);
		}

		@Override
		public InventorySpace newSpace(String name, int size)
		{
			return newSpace(name, size, null, null);
		}

		public InventorySpace newSpace(String name, int size, EnumFacing facing)
		{
			return this.newSpace(name, size, facing, null);
		}

		@Override
		public InventorySpace newSpace(String name, int size, @Nullable InventoryRule rule)
		{
			return newSpace(name, size, null, rule);
		}

		public InventorySlot newSlot(String name, EnumFacing facing)
		{
			return this.newSlot(name, facing, InventoryRule.COMMON);
		}

		@Override
		public InventorySlot newSlot(String name, @Nullable InventoryRule rule)
		{
			return newSlot(name, null, rule);
		}

		@Override
		public int currentSize()
		{
			return elements.size();
		}

		@Override
		public InventoryElement getElement(int i)
		{
			return elements.get(i);
		}

		@Override
		public Allocator getAllocator()
		{
			return null;
		}
	}
}
