package net.simplelib.common.registry.delegate;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import api.simplelib.registry.ASMRegistryDelegate;
import net.simplelib.HelperMod;
import api.simplelib.LoadingDelegate;
import api.simplelib.registry.ModGuiHandler;

/**
 * @author ci010
 */
@LoadingDelegate
public class IGuiHandlerDelegate extends ASMRegistryDelegate<ModGuiHandler>
{
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		try
		{
			ModContainer modContainer = Loader.instance().getIndexedModList().get(this.getModid());
			Object mod;
			if (modContainer == null)
				mod = HelperMod.instance;
			else mod = modContainer.getMod();
			NetworkRegistry.INSTANCE.registerGuiHandler(mod, (IGuiHandler) this.getAnnotatedClass().newInstance());
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
