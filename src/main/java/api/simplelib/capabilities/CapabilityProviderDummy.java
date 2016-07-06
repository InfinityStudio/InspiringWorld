package api.simplelib.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public class CapabilityProviderDummy implements ICapabilityProvider
{
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return null;
	}
}
