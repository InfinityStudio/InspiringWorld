package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.delegate;

import com.google.common.collect.ImmutableSet;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.Instance;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ASMRegistryDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModProxy;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.*;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.RegContainer;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.RegistryHelper;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component.ComponentMaker;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component.RegComponentBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ci010
 */
@LoadingDelegate
@ModProxy(side = Side.SERVER)
public class RegDelegate
{
	@Instance
	@ModProxy.Inject
	public static RegDelegate instance;

	@LoadingDelegate
	public static class ReferenceDelegate extends ASMRegistryDelegate<ComponentsReference>
	{
		@Mod.EventHandler
		public void construct(FMLConstructionEvent event)
		{
			RegistryHelper.INSTANCE.includeMod(this.getModid(), this.getAnnotatedClass());
		}
	}

	@LoadingDelegate
	public static class ModComponentDelegate extends ASMRegistryDelegate<ModComponent>
	{
		@Mod.EventHandler
		public void construct(FMLConstructionEvent event)
		{
			String name = this.getAnnotation().name().equals("") ?
					this.getAnnotatedClass().getSimpleName() :
					this.getAnnotation().name();
			if (Item.class.isAssignableFrom(this.getAnnotatedClass()) || Block.class.isAssignableFrom(this
					.getAnnotatedClass()))
			{
				RegComponentBase<?> base = RegComponentBase.of(name, TypeUtils.instantiateQuite(this.getAnnotatedClass()));
				RegistryHelper.INSTANCE.include(this.getModid(), base);
				if (this.getAnnotatedClass().isAnnotationPresent(OreDic.class))
					base.setOreName(this.getAnnotatedClass().getAnnotation(OreDic.class).value());
				if (this.getAnnotatedClass().isAnnotationPresent(ModCreativeTab.class))
					base.setCreativeTabId(this.getAnnotatedClass().getAnnotation(ModCreativeTab.class).value());
			}
			else if (this.getAnnotatedClass().isAnnotationPresent(ComponentStruct.class))
			{
				RegComponentBase<?> base = RegComponentBase.of(name, TypeUtils.instantiateQuite(this.getAnnotatedClass()));
				RegistryHelper.INSTANCE.include(this.getModid(), base);
			}
			else
				DebugLogger.warn("The class {} is neither a block nor an item! Moreover, it not a ComponentStruct. " +
						"It will not be registered!", this
						.getAnnotatedClass().getName());
		}

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ComponentMaker maker = new ComponentMaker(RegistryHelper.INSTANCE.getAnnotationMap());
		for (RegContainer meta : RegistryHelper.INSTANCE)
		{
			RegistryHelper.INSTANCE.start(meta);
			DebugLogger.info("Start to handle [".concat(meta.modid).concat("] mod."));
			ImmutableSet<RegComponentBase> cache;
			for (Class c : meta.getRawContainer())
				if ((cache = maker.apply(c)) != null)
					meta.addAll(cache);
			RegistryHelper.INSTANCE.end();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		for (RegContainer meta : RegistryHelper.INSTANCE)
		{
			RegistryHelper.INSTANCE.start(meta);
			this.register(meta);
			RegistryHelper.INSTANCE.end();
		}
	}

	protected void register(RegContainer meta)
	{
		for (RegComponentBase namespace : meta)
		{
			DebugLogger.info("Register Component: [{}] <- [{}]", namespace.getComponent().getClass().getSimpleName(), namespace
					.getRegisterName());
			namespace.register();
		}
	}

	@ModProxy(side = Side.CLIENT)
	public static class RegDelegateClient extends RegDelegate
	{
		@Override
		protected void register(RegContainer container)
		{
			super.register(container);
			for (RegComponentBase base : container)
			{
				Runnable regClient = base.getRegClient();
				if (regClient != null) regClient.run();
			}
		}
	}
}
