package net.simplelib.common.registry;

import api.simplelib.registry.ModProxy;
import api.simplelib.Instance;
import api.simplelib.utils.FinalFieldUtils;
import api.simplelib.utils.TypeUtils;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.*;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.LoadingDelegate;
import api.simplelib.utils.ASMDataUtil;
import api.simplelib.utils.PackageModIdMap;
import net.minecraftforge.fml.relauncher.Side;
import net.simplelib.HelperMod;
import net.simplelib.common.DebugLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author ci010
 */
public final class RegistryBufferManager
{
	private static RegistryBufferManager instance;

	public static RegistryBufferManager instance()
	{
		if (instance == null) instance = new RegistryBufferManager();
		return instance;
	}

	private Multimap<Class<? extends FMLStateEvent>, SubscriberInfo> subscriberInfoMap = HashMultimap.create();

	class SubscriberInfo
	{
		Object obj;
		Method method;

		public SubscriberInfo(Object obj, Method method)
		{
			this.obj = obj;
			this.method = method;
		}

		@Override
		public String toString()
		{
			return obj.getClass().getName().concat(" ").concat(method.getName());
		}
	}

	private PackageModIdMap rootMap = new PackageModIdMap();

	private void loadProxy(ASMDataTable table)
	{
		Map<Class, Object> proxyClasses = Maps.newHashMap();
		Set<ASMDataTable.ASMData> proxyClassData = table.getAll(ModProxy.class.getName());

		for (ASMDataTable.ASMData data : proxyClassData)
		{
			ModProxy modProxy = ASMDataUtil.getAnnotation(data, ModProxy.class);
			if (modProxy.side() == Side.SERVER || modProxy.side() == HelperMod.proxy.getGameSide())
			{
				Class<?> clz = ASMDataUtil.getClass(data);
				Class type = modProxy.genericType();
				if (type == Void.class)
					type = clz.getSuperclass();
				if (type == Object.class)
					type = clz;
				Optional<?> grab = Instance.Utils.grab(clz);
				if (grab.isPresent())
					proxyClasses.put(type, grab.get());
				else
					try
					{
						proxyClasses.put(type, clz.newInstance());
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

		Set<ASMDataTable.ASMData> injectClass = table.getAll(ModProxy.Inject.class.getName());
		for (ASMDataTable.ASMData data : injectClass)
		{
			Field field = ASMDataUtil.getField(data);
			if (field == null)
			{
				DebugLogger.fatal("Cannot inject proxy to class {} as there is no @EnumType field.", data.getClassName());
				continue;
			}
			if (Modifier.isStatic(field.getModifiers()))
			{
				ModProxy.Inject annotation = field.getAnnotation(ModProxy.Inject.class);
				Object o = null;
				if (!annotation.genericType().equals(Void.class))
					o = proxyClasses.get(annotation.genericType());
				if (o == null)
					o = proxyClasses.get(field.getType());
				if (o == null)
				{
					DebugLogger.fatal("Cannot find the proxy with type of {}", field.getType());
					continue;
				}

				try
				{
					if (!field.isAccessible())
						field.setAccessible(true);
					field.set(null, o);
					DebugLogger.info("ForName proxy to field [{}] <- [{}]", field.getName(), o.getClass().toString());

				}
				catch (IllegalAccessException e)
				{
					try
					{
						FinalFieldUtils.INSTANCE.setStatic(field, o);
						DebugLogger.info("ForName proxy to field [{}] <- [{}]", field.getName(), o.getClass().toString());
					}
					catch (Exception e1)
					{
						DebugLogger.fatal("Cannot inject proxy to field [{}] <- [{}]", field.getName(), o.getClass().toString());
					}
				}
			}
			else
			{
				DebugLogger.fatal("Cannot inject proxy to a non-static field {} in class {}", data.getObjectName(),
						data.getClassName());
			}
		}
	}

	private Set<ASMDataTable.ASMData> packageCallback = Sets.newHashSet();

	public final void load(ASMDataTable table)
	{
		DebugLogger.info("========================================================================");
		DebugLogger.info("				Start to init Registry System.");
		DebugLogger.info("========================================================================");
		for (ASMDataTable.ASMData data : table.getAll(Mod.class.getName()))
		{
			try
			{
				Class<?> classSafe = ASMDataUtil.getClassSafe(data);
				rootMap.put(classSafe.getPackage()
						.getName(), data.getAnnotationInfo().get("modid").toString());
			}

			catch (ClassNotFoundException e)
			{
				packageCallback.add(data);
			}
		}

		this.loadProxy(table);

		for (ASMDataTable.ASMData data : table.getAll(LoadingDelegate.class.getName()))
		{
			Class<?> registryDelegateType = ASMDataUtil.getClass(data);
			boolean asmType = ASMRegistryDelegate.class.isAssignableFrom(registryDelegateType);

			Optional<?> optional = Instance.Utils.grab(registryDelegateType);
			Object delegate = null;
			try
			{
				delegate = optional.isPresent() ? optional.get() : registryDelegateType.newInstance();
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

			if (delegate == null)
			{//TODO log
				System.out.println("cannot inject " + registryDelegateType);
				return;
			}
			for (Method method : registryDelegateType.getDeclaredMethods())
			{
				if (!method.isAnnotationPresent(Mod.EventHandler.class))
					continue;

				if (method.getParameterTypes().length != 1)
					throw new UnsupportedOperationException("The method being subscribed need to have ONLY one parameter!");

				Class<?> methodParam = method.getParameterTypes()[0];
				if (!FMLStateEvent.class.isAssignableFrom(methodParam))
					throw new UnsupportedOperationException("The method's parameter must be a FMLStateEvent but not "
							.concat(methodParam.getName()));
				if (methodParam == FMLServerStoppedEvent.class && methodParam == FMLServerStoppingEvent.class)
					throw new UnsupportedOperationException("Not support FMLServerStoppedEvent and FMLServerStoppingEvent");

				Class<? extends FMLStateEvent> state = TypeUtils.cast(methodParam);
				if (asmType)
				{
					ASMRegistryDelegate asmDelegate = (ASMRegistryDelegate) delegate;
					Class<? extends Annotation> target = TypeUtils.getGenericTypeTo(asmDelegate);
					ArrayList<ASMDataTable.ASMData> list = Lists.newArrayList(table.getAll(target.getName()));
					Collections.sort(list, new Comparator<ASMDataTable.ASMData>()
					{
						@Override
						public int compare(ASMDataTable.ASMData o1, ASMDataTable.ASMData o2)
						{
							return o1.getClassName().compareTo(o2.getClassName());
						}
					});
					for (ASMDataTable.ASMData meta : list)
					{
						Package pkg = ASMDataUtil.getClass(meta).getPackage();
						String modid = rootMap.getModid(pkg.getName());
						if (modid == null)
						{
							modid = HelperMod.MODID;//TODO think about anonymous
							System.out.println("Anonymous for " + pkg.getName());
//							HelperMod.metadata.childMods.add();
						}
						asmDelegate.addCache(modid, meta);
					}
				}
				subscriberInfoMap.put(state, new SubscriberInfo(delegate, method));
			}
		}
	}

	public void invoke(FMLStateEvent state)
	{
		DebugLogger.info("========================================================================");
		DebugLogger.info("				Transit to {} state			", state.getModState());
		DebugLogger.info("========================================================================");

		if (state instanceof FMLPreInitializationEvent)
		{

		}

		Class<? extends FMLStateEvent> realType = state.getClass();

		for (SubscriberInfo info : subscriberInfoMap.get(realType))
			try
			{
				if (info.obj instanceof ASMRegistryDelegate)
				{
					ASMRegistryDelegate asmRegistryDelegate = (ASMRegistryDelegate) info.obj;
					while (asmRegistryDelegate.hasNext())
					{
						asmRegistryDelegate.next();
						info.method.invoke(info.obj, state);
					}
				}
				else
					info.method.invoke(info.obj, state);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				System.out.println(info);
				e.printStackTrace();
			}

		if (state instanceof FMLLoadCompleteEvent)
		{
			rootMap = null;
			ASMDataUtil.clear();
			subscriberInfoMap.removeAll(FMLConstructionEvent.class);
			subscriberInfoMap.removeAll(FMLPreInitializationEvent.class);
			subscriberInfoMap.removeAll(FMLInitializationEvent.class);
			subscriberInfoMap.removeAll(FMLPostInitializationEvent.class);
			subscriberInfoMap.removeAll(FMLLoadCompleteEvent.class);
		}
	}
}
