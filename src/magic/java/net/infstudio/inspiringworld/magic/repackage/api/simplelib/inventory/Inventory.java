package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory;

import javafx.beans.Observable;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.Overview;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author ci010
 */
public interface Inventory extends Observable, Overview<InventoryElement>
{
	@CapabilityInject(Inventory.class)
	Capability<Inventory> CAPABILITY = null;

	IInventory asIInventory();

	ISidedInventory asSideInventory();

	IItemHandlerModifiable asItemHandler();
}
