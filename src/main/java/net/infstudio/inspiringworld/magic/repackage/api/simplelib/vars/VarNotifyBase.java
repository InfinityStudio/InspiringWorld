package net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

/**
 * @author ci010
 */
public abstract class VarNotifyBase<T> extends VarBase<T> implements VarNotify<T>
{
	private ExpressionHelper<T> helper = null;

	protected void load(T data)
	{
		super.set(data);
	}

	public void set(T data)
	{
		super.set(data);
		if (!data.equals(this.get()))
			if (helper != null)
				ExpressionHelper.fireValueChangedEvent(helper);
	}

	@Override
	public void addListener(ChangeListener<? super T> listener)
	{
		helper = ExpressionHelper.addListener(helper, this, listener);
	}

	@Override
	public void removeListener(ChangeListener<? super T> listener)
	{
		helper = ExpressionHelper.removeListener(helper, listener);
	}

	@Override
	public T getValue()
	{
		return this.get();
	}

	@Override
	public void addListener(InvalidationListener listener)
	{
		helper = ExpressionHelper.addListener(helper, this, listener);
	}

	@Override
	public void removeListener(InvalidationListener listener)
	{
		helper = ExpressionHelper.removeListener(helper, listener);
	}
}
