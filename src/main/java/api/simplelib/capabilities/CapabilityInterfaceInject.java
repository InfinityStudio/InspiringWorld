package api.simplelib.capabilities;

import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.registry.ModHandler;
import api.simplelib.utils.TypeUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.simplelib.common.DebugLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * @author ci010
 */
@ModHandler
public class CapabilityInterfaceInject
{
	@Instance
	private static CapabilityInterfaceInject instance = new CapabilityInterfaceInject();

	private Map<Class, CapabilityBuilderHandler<?>> paramToBuilder = Maps.newHashMap();
	private Map<Class, SubscribeInfo> watchedToMethods = Maps.newHashMap();

	public static CapabilityInterfaceInject instance()
	{
		return instance;
	}

	@LoadingDelegate
	public static class PreReg extends ASMRegistryDelegate<ModCapabilityBuilderHandler>
	{
		@Mod.EventHandler
		public void pre(FMLPreInitializationEvent event)
		{
			Map<Class, CapabilityBuilderHandler<?>> paramToBuilder
					= CapabilityInterfaceInject.instance.paramToBuilder;
			Class<?> providerClass = this.getAnnotatedClass();
			if (CapabilityBuilderHandler.class.isAssignableFrom(providerClass))
			{
				Class paramType = (Class) TypeUtils.getInterfaceGenericType(providerClass, CapabilityBuilderHandler.class, 0);
				try
				{
					if (paramToBuilder.containsKey(paramType))
					{
						DebugLogger.fatal("Duplicated CapabilityBuild class has been register! The handler {} won't " +
								"be registered with {}!", providerClass, paramType);
						return;
					}
					paramToBuilder.put(paramType, TypeUtils.<CapabilityBuilderHandler<?>>cast(providerClass
							.newInstance()));
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
	}

	@LoadingDelegate
	public static class PostReg extends ASMRegistryDelegate<CapabilityBuild>
	{
		private List<Method> cacheMethod = Lists.newArrayList();
		private List<CapabilityBuilderHandler<?>> cacheCap = Lists.newArrayList();

		@Mod.EventHandler
		public void post(FMLPostInitializationEvent event)
		{
			cacheCap.clear();
			cacheMethod.clear();
			if (!CapabilityInterfaceInject.instance.watchedToMethods.containsKey(this.getAnnotatedClass()))
				CapabilityInterfaceInject.instance.revolveImplicit(this.getAnnotatedClass(), cacheMethod, cacheCap);
		}
	}

	private static class SubscribeInfo
	{
		Method m[];
		CapabilityBuilderHandler<Object>[] arg;
		int size;

		public SubscribeInfo(Method[] m, CapabilityBuilderHandler[] arg)
		{
			if (m.length != arg.length)
				throw new IllegalArgumentException();
			this.m = m;
			this.arg = arg;
			this.size = m.length;
		}
	}

	@SubscribeEvent
	public void onAttach(AttachCapabilitiesEvent event)
	{
		Map<ResourceLocation, ICapabilityProvider> map = Maps.newHashMap();
		if (event instanceof AttachCapabilitiesEvent.Item)
			handle(map, event.getObject(), ((AttachCapabilitiesEvent.Item) event).getItemStack());
		else if (event instanceof AttachCapabilitiesEvent.Entity)
			handle(map, event.getObject(), ((AttachCapabilitiesEvent.Entity) event).getEntity());
		else if (event instanceof AttachCapabilitiesEvent.TileEntity)
			handle(map, event.getObject(), ((AttachCapabilitiesEvent.TileEntity) event).getTileEntity());
		else
			handle(map, event.getObject(), event.getObject());
		for (ICapabilityProvider iCapabilityProvider : event.getCapabilities().values())
			handle(map, iCapabilityProvider, event.getObject());
		for (Map.Entry<ResourceLocation, ICapabilityProvider> entry : map.entrySet())
			event.addCapability(entry.getKey(), entry.getValue());
	}

	public void revolveImplicit(Class clz)
	{
		this.revolveImplicit(clz, Lists.<Method>newArrayList(), Lists.<CapabilityBuilderHandler<?>>newArrayList());
	}

	public Map<ResourceLocation, ICapabilityProvider> handle(Object owner, Object obj)
	{
		Map<ResourceLocation, ICapabilityProvider> map = Maps.newHashMap();
		handle(map, owner, obj);
		return map;
	}

	private void revolveImplicit(Class clz, List<Method> cacheMethod, List<CapabilityBuilderHandler<?>> cacheCap)
	{
		Map<Class, SubscribeInfo> watchedToMethods = CapabilityInterfaceInject.instance.watchedToMethods;
		Map<Class, CapabilityBuilderHandler<?>> paramToBuilder = CapabilityInterfaceInject.instance.paramToBuilder;
		for (Method method : clz.getMethods())
			if (method.isAnnotationPresent(CapabilityBuild.class))
				if (!Modifier.isStatic(method.getModifiers()))
				{
					Class<?>[] types = method.getParameterTypes();
					if (types.length == 1)
					{
						cacheMethod.add(method);
						cacheCap.add(paramToBuilder.get(types[0]));
					}
					else
					{
						DebugLogger.fatal("");
						return;
					}
				}
				else
				{
					DebugLogger.fatal("Capability buildUnsaved method should be public & non-static.");
					return;
				}
		watchedToMethods.put(clz,
				new SubscribeInfo(cacheMethod.toArray(new Method[cacheMethod.size()]),
						cacheCap.toArray(new CapabilityBuilderHandler[cacheCap.size()])));
	}

	private void handle(Map<ResourceLocation, ICapabilityProvider> map, Object owner, Object obj)
	{
		Class clz = obj.getClass();
		SubscribeInfo info = watchedToMethods.get(clz);
		if (info == null)
			revolveImplicit(clz);
		info = watchedToMethods.get(clz);
		if (info == null)
			return;
		for (int i = 0; i < info.size; i++)
		{
			Method method = info.m[i];
			Object build = info.arg[i].createBuilder(owner);
			if (build == null)
			{
				DebugLogger.fatal("The handler {} does not handle {} as context! It will not provider capability!",
						info.arg[i], owner);
				continue;
			}
			try
			{
				method.invoke(obj, build);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			ImmutableMap.Builder<ResourceLocation, ICapabilityProvider> storage = ImmutableMap.builder();
			info.arg[i].build(storage, build, owner);
			map.putAll(storage.build());
		}
	}

	private CapabilityInterfaceInject() {}
}
