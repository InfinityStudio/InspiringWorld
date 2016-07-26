package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.CapabilityBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @author ci010
 */
@CapabilityBuilder({Inventory.class, IItemHandler.class})
public interface InventoryBuilder
{
	InventorySpace newSpace(String name, int size);

	InventorySpace newSpace(String name, int size, @Nullable EnumFacing facing);

	InventorySpace newSpace(String name, int size, @Nullable InventoryRule rule);

	InventorySpace newSpace(String name, int size, @Nullable EnumFacing facing, @Nullable InventoryRule rule);

	InventorySlot newSlot(String name);

	InventorySlot newSlot(String name, @Nullable EnumFacing facing);

	InventorySlot newSlot(String name, @Nullable InventoryRule rule);

	InventorySlot newSlot(String name, @Nullable EnumFacing facing, @Nullable InventoryRule rule);

	int currentSize();

	InventoryElement getElement(int i);

	Allocator getAllocator();

	interface Allocator
	{
		void allocPos(InventoryElement element, int x, int y);

		void acllocSize(InventorySpace space, int xSize);
	}
}
