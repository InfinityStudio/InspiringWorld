package net.infstudio.inspiringworld.magic.repackage.net.simplelib.network;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.ModNetwork;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ASMRegistryDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.FinalFieldUtils;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author ci010
 */
@LoadingDelegate
public class NetworkDelegate extends ASMRegistryDelegate<ModNetwork>
{
	@SubscribeEvent
	public void constract(FMLConstructionEvent event)
	{
		if (this.getField().isPresent())
		{
			Field field = this.getField().get();
			String modid = this.getAnnotation().modid();
			if (modid.equals(""))
				modid = this.getModid();
			int modifiers = field.getModifiers();
			if (!Modifier.isStatic(modifiers))
			{
				DebugLogger.fatal("Cannot inject network channel a non static filed {} of {}", field, this.getAnnotatedClass());
				return;
			}
			if (Modifier.isFinal(modifiers))
				try
				{
					FinalFieldUtils.INSTANCE.setStatic(field, new ModNetworkImpl(modid));
				}
				catch (Exception e)
				{
					throw new IllegalArgumentException("Unexpected thing happened in network injection.", e);
				}
			else
				ReflectionHelper.setPrivateValue(TypeUtils.cast(this.getAnnotatedClass()), null, field.getName());
		}
		else
			throw new IllegalArgumentException("Unexpected thing happened in network injection.");
	}
}
