package net.simplelib.common.registry.delegate;

import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.registry.ModProxy;
import api.simplelib.registry.components.ComponentStruct;
import api.simplelib.registry.components.ComponentsReference;
import api.simplelib.registry.components.ModComponent;
import api.simplelib.utils.TypeUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.simplelib.HelperMod;
import net.simplelib.common.registry.RegContainer;
import net.simplelib.common.registry.RegistryHelper;
import net.simplelib.common.registry.annotation.field.OreDic;
import net.simplelib.common.registry.component.ComponentMaker;
import net.simplelib.common.registry.component.RegComponentBase;

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
					.getAnnotatedClass()) || this.getAnnotatedClass().isAnnotationPresent(ComponentStruct.class))
			{
				RegComponentBase<?> base = RegComponentBase.of(name, TypeUtils.instantiateQuite(this.getAnnotatedClass()));
				RegistryHelper.INSTANCE.include(this.getModid(), base);
				if (this.getAnnotatedClass().isAnnotationPresent(OreDic.class))
					base.setOreName(this.getAnnotatedClass().getAnnotation(OreDic.class).value());
			}
			else
				HelperMod.LOG.warn("The class {} is neither a block nor an item! Moreover, it not a ComponentStruct. " +
						"It will not be registered!", this
						.getAnnotatedClass().getName());
		}

	}

	@SubscribeEvent
	public void preInit(FMLPreInitializationEvent event)
	{
		ComponentMaker maker = new ComponentMaker(RegistryHelper.INSTANCE.getAnnotationMap());
//		ModRestriction.preInit(event);
		for (RegContainer meta : RegistryHelper.INSTANCE)
		{
			RegistryHelper.INSTANCE.start(meta);
			HelperMod.LOG.info("Start to handle [".concat(meta.modid).concat("] mod."));
			ImmutableSet<RegComponentBase> cache;
			for (Class c : meta.getRawContainer())
				if ((cache = maker.apply(c)) != null)
					meta.addAll(cache);
			RegistryHelper.INSTANCE.end();
		}
	}

	@SubscribeEvent
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
			namespace.register();
	}

	@ModProxy(side = Side.CLIENT)
	public static class RegDelegateClient extends RegDelegate
	{
		@Override
		protected void register(RegContainer meta)
		{
			super.register(meta);
			for (RegComponentBase regComponentBase : meta)
				regComponentBase.getRegClient().run();
//			if (Environment.debug())
//			{
//				FileReference.registerFile("all");
//				FileReference.registerFile(meta.modid);
//				LanguageReporter.instance().start(meta.modid, meta.langType());
//					Local.trans(namespace.getComponent().getRegisterName());
//				LanguageReporter.instance().end();
//			}
		}
	}


}
