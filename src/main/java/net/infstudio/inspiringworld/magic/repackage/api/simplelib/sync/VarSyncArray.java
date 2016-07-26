package net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync;

/**
 * @author ci010
 */
public abstract class VarSyncArray<T> extends VarSyncBase<T[]> implements VarSync<T[]>
{
	public VarSyncArray(String name)
	{
		super(name);
	}

	public void update(int i, T data)
	{
		this.data[i] = data;
	}

	public T get(int i)
	{
		return this.data[i];
	}
}
