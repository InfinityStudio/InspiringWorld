package api.simplelib.world.region;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author ci010
 */
public class RegionEvent extends Event
{
	public final Region region;
	public final EntityPlayer player;

	public RegionEvent(Region region, EntityPlayer player)
	{
		this.region = region;
		this.player = player;
	}

	/**
	 * @author ci010
	 */
	public static class Enter extends RegionEvent
	{
		public Enter(Region region, EntityPlayer player)
		{
			super(region, player);
		}
	}

	/**
	 * @author ci010
	 */
	public static class Leave extends RegionEvent
	{
		public Leave(Region region, EntityPlayer player)
		{
			super(region, player);
		}
	}

	/**
	 * @author ci010
	 */
	public static class AttachCapability extends AttachCapabilitiesEvent
	{
		private Region region;

		public AttachCapability(Region obj)
		{
			super(obj);
			this.region = obj;
		}

		public Region getRegion()
		{
			return region;
		}
	}
}
