package net.infstudio.inspiringworld.magic.repackage.api.simplelib;

import net.minecraft.util.ITickable;

/**
 * @author ci010
 */
public interface UpdateSafe extends ITickable
{
	boolean shouldUpdate();
}
