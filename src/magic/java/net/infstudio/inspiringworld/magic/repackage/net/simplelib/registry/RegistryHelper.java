package net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ArgumentHelper;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.FMLLoadingUtil;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.component.RegComponentBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;

/**
 * This class handles most block/item create/include things.
 */
public enum RegistryHelper implements Iterable<RegContainer>
{
	INSTANCE;

	private Map<String, RegContainer> containerIdx = Maps.newHashMap();

	private Map<Class<? extends Annotation>, ArgumentHelper> annoMap = Maps.newHashMap();

	void track(RegContainer meta)
	{
		this.containerIdx.put(meta.modid, meta);
	}

	private String currentModid;
	private ModContainer container;

	public void start(RegContainer meta)
	{
		if (container == null)
			container = Loader.instance().activeModContainer();
		FMLLoadingUtil.setActiveContainer(FMLLoadingUtil.getModContainer(currentModid = meta.modid));
	}

	public void end()
	{
		FMLLoadingUtil.setActiveContainer(container);
		currentModid = container.getModId();
	}

	public String currentMod()
	{
		return this.currentModid;
	}

	public void setLang(String modid, String... lang)
	{
		if (lang == null || lang.length == 0 || lang[0].equals(""))
			lang = new String[]
					{"zh_CN", "en_US"};
		if (!this.containerIdx.containsKey(modid))
			this.track(new RegContainer(modid).lang(true).langType(lang));
		else
			this.containerIdx.get(modid).lang(true).langType(lang);
	}

	public void setModel(String modid)
	{
		if (!this.containerIdx.containsKey(modid))
			this.track(new RegContainer(modid).model(true));
		else
			this.containerIdx.get(modid).model(true);
	}

	public void includeMod(String modid, Class<?> container)
	{
		if (!this.containerIdx.containsKey(modid))
			this.track(new RegContainer(modid).addRawContainer(container));
		else
			this.containerIdx.get(modid).addRawContainer(container);
	}


	public void include(String modid, RegComponentBase componentBase)
	{
		if (!this.containerIdx.containsKey(modid))
			this.track(new RegContainer(modid).add(componentBase));
		else this.containerIdx.get(modid).add(componentBase);
	}

	public Map<Class<? extends Annotation>, ArgumentHelper> getAnnotationMap()
	{
		return ImmutableMap.copyOf(this.annoMap);
	}

	@Override
	public Iterator<RegContainer> iterator()
	{
		return this.containerIdx.values().iterator();
	}

	public Object find(ResourceLocation location)
	{
		Object obj = GameRegistry.findBlock(location.getResourceDomain(), location.getResourcePath());
		if (obj == null)
			GameRegistry.findItem(location.getResourceDomain(), location.getResourcePath());
		if (obj == null)
		{}
		return obj;
	}


	/**
	 * Register custom annotation for constructing object.
	 *
	 * @param annotation The annotation used to catch constructing arguments.
	 * @param helper     The helper will inject the annotation above.
	 */
	public void registerAnnotation(Class<? extends Annotation> annotation, ArgumentHelper helper)
	{
		if (!annoMap.containsKey(annotation))
			annoMap.put(annotation, helper);
		else
			throw new IllegalArgumentException("The annotation has already been registerd!");
	}

	public void close()
	{
		if (Loader.instance().getLoaderState() == LoaderState.AVAILABLE)
		{
			this.containerIdx = null;
			this.annoMap = null;
		}
	}
}
