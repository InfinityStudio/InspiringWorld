package api.simplelib.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * Use {@link ModNetwork} to inject
 *
 * @author ci010
 * @see ModNetwork
 */
public interface Network
{
	/**
	 * Send this message to the server.
	 *
	 * @param message The message will be sent.
	 */
	void send(IMessage message);

	/**
	 * Send this message to the specified client of the player. See
	 * {@link SimpleNetworkWrapper#sendTo(IMessage, EntityPlayerMP)}
	 */
	void send(IMessage message, EntityPlayerMP player);

	/**
	 * Send this message to everyone within a certain range of a point. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, int)}
	 */
	void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point);

	/**
	 * Sends a message to everyone within a certain range of the coordinates in
	 * the same dimension.
	 */
	void sendToAllAround(IMessage message, int dimension, double x, double y, double z,
						 double range);

	/**
	 * Sends a message to everyone within a certain range of the player
	 * provided.
	 */
	void sendToAllAround(IMessage message, EntityPlayer player, double range);

	/**
	 * Send this message to everyone within the supplied dimension. See
	 * {@link SimpleNetworkWrapper#sendToDimension(IMessage, int)}
	 */
	void sendToDimension(IMessage message, int dimensionId);
}
