package net.infstudio.inspiringworld.magic.repackage.api.simplelib.container;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * A wrapper interface for {@link net.minecraftforge.fml.common.network.IGuiHandler}.
 * <p>Use {@link GuiHandler#addContainerProvider(IContainerProvider)} to register this.</p>
 *
 * @author ci010
 */
public interface IContainerProvider
{
	/**
	 * Returns a Server side Container to be displayed to the user.
	 *
	 * @param player The player viewing the Gui
	 * @param world  The current world
	 * @param x      X Position
	 * @param y      Y Position
	 * @param z      Z Position
	 * @return A Container to be displayed to the user, null if none.
	 */
	Container getContainer(EntityPlayer player, World world, int x, int y, int z);

	/**
	 * Returns a Container to be displayed to the user. On the client side, this
	 * needs to return a instance of GuiScreen On the server side, this needs to
	 * return a instance of Container
	 *
	 * @param player The player viewing the Gui
	 * @param world  The current world
	 * @param x      X Position
	 * @param y      Y Position
	 * @param z      Z Position
	 * @return A GuiScreen to be displayed to the user, null if none.
	 */
	GuiScreen getGuiContainer(EntityPlayer player, World world, int x, int y, int z);
}
