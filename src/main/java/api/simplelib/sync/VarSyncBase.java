package api.simplelib.sync;

import api.simplelib.vars.VarNotifyBase;

/**
 * @author ci010
 */
public abstract class VarSyncBase<T> extends VarNotifyBase<T> implements VarSync<T>
{
	protected String name;

	public VarSyncBase(String name)
	{
		this.name = name;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return this.data.toString();
	}

	@Override
	public boolean isPresent()
	{
		return true;
	}
}
