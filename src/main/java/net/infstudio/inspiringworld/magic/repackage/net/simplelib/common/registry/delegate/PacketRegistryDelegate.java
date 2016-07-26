package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.delegate;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.ModMessage;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.ModNetwork;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ASMRegistryDelegate;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

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
