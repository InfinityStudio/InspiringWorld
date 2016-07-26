package net.infstudio.inspiringworld.magic.repackage.api.simplelib.world;

import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * @author ci010
 */
public class AttachWorldCapEvent extends AttachCapabilitiesEvent
{
	private World world;

	public AttachWorldCapEvent(World world)
	{
		super(world);
		this.world = world;
	}

	public World getWorld()
	{
		return world;
	}
}
