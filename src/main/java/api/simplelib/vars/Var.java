package api.simplelib.vars;

import com.google.common.base.Supplier;

/**
 * @author ci010
 */
public interface Var<T> extends Supplier<T>
{
	void set(T value);

	/**
	 * All the toString method of a Var should be delegated to the actual data toString method.
	 *
	 * @return The data string.
	 */
	String toString();
}
