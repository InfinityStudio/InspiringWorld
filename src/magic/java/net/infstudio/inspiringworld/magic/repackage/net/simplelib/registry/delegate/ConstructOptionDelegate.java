package net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.delegate;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ASMRegistryDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.Construct;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.RegistryHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;

import java.lang.annotation.Annotation;

/**
 * @author ci010
 */
@LoadingDelegate
public class ConstructOptionDelegate extends ASMRegistryDelegate<Construct.Option>
{
	@Mod.EventHandler
	public void construct(FMLConstructionEvent event)
	{
		try
		{
			RegistryHelper.INSTANCE.registerAnnotation(TypeUtils.<Annotation>cast(this.getAnnotatedClass()),
					this.getAnnotation().value().newInstance());
		}
		catch (InstantiationException e)
		{
			//TODO log that it need no parameter constructor.
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			if (this.getAnnotatedClass() == null)
				System.out.println("class null");
			if (this.getAnnotation() == null)
				System.out.println("annotaton null");
		}
	}
}
