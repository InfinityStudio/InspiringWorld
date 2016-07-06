package api.simplelib;

import net.minecraft.util.ITickable;

/**
 * @author ci010
 */
public interface UpdateSafe extends ITickable
{
	boolean shouldUpdate();
}
