package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.lang.Local;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;

/**
 * @author ci010
 */
public class StringLocal extends StringSource
{
	private String localized;

	public StringLocal(String id)
	{
		super(id);
		localized = Local.trans(id);
	}

	public StringLocal(String id, Handler handler)
	{
		super(id, handler);
		localized = Local.trans(id);
	}

	@Override
	public StringLocal setSource(Source source)
	{
		this.source = source;
		return this;
	}

	@Override
	public String toString()
	{
		if (source != null)
			return String.format(localized, this.getSource());
		else
			return getRawContent();
	}
}
