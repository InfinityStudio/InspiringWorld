package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component.RegComponentBase;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ci010
 */
public class RegContainer implements Iterable<RegComponentBase>
{
	public final String modid;
	private boolean ifGenerateLang = true, ifGenerateModel = true;
	private String[] langType = new String[]{"zh_CN", "en_US"};
	private Set<Class> rawContainer;
	private Set<RegComponentBase> all;

	public RegContainer(String modid)
	{
		this.modid = modid;
		this.rawContainer = Sets.newHashSet();
		this.all = Sets.newHashSet();
	}

	public RegContainer lang(boolean b)
	{
		this.ifGenerateLang = b;
		return this;
	}

	public RegContainer langType(String[] t)
	{
		this.langType = t;
		return this;
	}

	public String[] langType()
	{
		return this.langType;
	}

	public boolean needLang()
	{
		return this.ifGenerateLang;
	}

	public RegContainer model(boolean b)
	{
		this.ifGenerateModel = b;
		return this;
	}

	public Set<Class> getRawContainer()
	{
		return this.rawContainer;
	}

	public RegContainer addRawContainer(Class<?> containers)
	{
		this.rawContainer.add(containers);
		return this;
	}

	public boolean needModel()
	{
		return this.ifGenerateModel;
	}

	public void addAll(Collection<RegComponentBase> componentBases)
	{
		this.all.addAll(componentBases);
	}

	public RegContainer add(RegComponentBase base)
	{
		this.all.add(base);
		return this;
	}


	@Override
	public Iterator<RegComponentBase> iterator()
	{
		return this.all.iterator();
	}
}
