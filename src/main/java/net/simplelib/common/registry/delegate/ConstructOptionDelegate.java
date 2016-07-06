package net.simplelib.common.registry.delegate;

import api.simplelib.registry.components.Construct;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.simplelib.common.registry.RegistryHelper;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.LoadingDelegate;
import api.simplelib.utils.TypeUtils;

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
