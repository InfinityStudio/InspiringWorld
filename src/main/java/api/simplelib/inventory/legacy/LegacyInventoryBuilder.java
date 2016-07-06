package api.simplelib.inventory.legacy;

import api.simplelib.capabilities.CapabilityBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

/**
 * @author ci010
 */
@CapabilityBuilder(IItemHandler.class)
public interface LegacyInventoryBuilder
{
	void newSlot(int size, EnumFacing... face);
}
