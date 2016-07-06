package net.simplelib.common.registry.delegate;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import api.simplelib.registry.ASMRegistryDelegate;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import api.simplelib.LoadingDelegate;
import api.simplelib.network.ModMessage;
import api.simplelib.network.ModNetwork;

/**
 * @author ci010
 */
@LoadingDelegate
public class PacketRegistryDelegate extends ASMRegistryDelegate<ModMessage>
{
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (IMessageHandler.class.isAssignableFrom(this.getAnnotatedClass()))
			try
			{
				IMessageHandler msg = (IMessageHandler) this.getAnnotatedClass().newInstance();
//				ModNetwork.instance().registerMessage(msg); TODO fix
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
	}
}
