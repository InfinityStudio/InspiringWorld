package api.simplelib;

import net.minecraft.client.renderer.texture.ITickable;

/**
 * @author ci010
 */
public interface ITickableTask extends ITickable
{
	boolean isDone();
}
