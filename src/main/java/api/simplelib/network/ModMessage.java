package api.simplelib.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class annotated by this annotation will be registered as a message which can be sent to server/client.
 * <p>About the usage of the message, see
 * {@link ModNetwork#sendTo(IMessage)} and {@link ModNetwork#sendTo(IMessage, EntityPlayerMP)}.</p>
 *
 * @author ci010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
public @interface ModMessage
{
}
