package api.simplelib.capabilities;

import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

/**
 * A class define style capability.
 * <p>Use {@link ModCapability} to register this.</p>
 *
 * @author ci010
 */
public interface ICapability<T>
{
	Capability.IStorage<T> storage();

	Callable<T> factory();

	Class<T> type();
}
