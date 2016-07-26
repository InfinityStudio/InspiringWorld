package net.infstudio.inspiringworld.magic.repackage.api.simplelib;

import java.util.Collection;

/**
 * @author ci010
 */
public interface Overview<T>
{
	T getById(int id);

	T getByName(String name);

	Collection<T> getAll();
}
