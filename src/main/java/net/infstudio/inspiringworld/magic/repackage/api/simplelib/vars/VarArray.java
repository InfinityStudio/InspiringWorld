package net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars;

/**
 * @author ci010
 */
public interface VarArray<T>
{
	void update(int i, T value);

	T get(int i);

	int size();

	T[] toArray();
}
