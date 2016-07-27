package net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.component;

/**
 * @author ci010
 */
public class RegComponentComplete extends RegComponentBase<Object>
{
	public RegComponentComplete(String name, Object wrap)
	{
		super(name, wrap);
	}

	@Override
	public RegComponentBase<Object> register()
	{
		return this;
	}

	@Override
	public Runnable getRegClient()
	{
		return null;
	}
}
