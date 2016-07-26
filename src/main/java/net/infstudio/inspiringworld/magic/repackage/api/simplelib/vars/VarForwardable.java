package net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars;

/**
 * @author ci010
 */
public class VarForwardable<T> implements VarForward<T>
{
	private Var<T> delegate;

	@Override
	public boolean isPresent()
	{
		return delegate == null;
	}

	@Override
	public String name()
	{
		return null;
	}

	@Override
	public void delegate(Var<T> var)
	{
		if (delegate != null)
			this.delegate = var;
	}

	@Override
	public Var<T> delegate()
	{
		return delegate;
	}

	@Override
	public void set(T value)
	{
		if (this.delegate != null)
			delegate.set(value);
		else
			delegate = new VarBase<T>(value);
	}

	@Override
	public T get()
	{
		return delegate == null ? null : delegate.get();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof Var)
			return delegate.equals(((Var<?>) o).get());
		else return super.equals(o);
	}

	@Override
	public int hashCode()
	{
		return delegate != null ? delegate.hashCode() : 0;
	}
}
