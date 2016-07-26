package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * @author ci010
 */
public interface InventorySpace extends IItemHandler, Iterable<ItemStack>, InventoryElement
{
}
