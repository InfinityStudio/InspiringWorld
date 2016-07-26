package net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars;

/**
 * @author ci010
 */
public class VarBase<T> implements Var<T>
{
	protected T data;

	public VarBase(T data)
	{
		this.data = data;
	}

	public VarBase()
	{}

	public T get()
	{
		return data;
	}

	public void set(T data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return data.toString();
	}
}
