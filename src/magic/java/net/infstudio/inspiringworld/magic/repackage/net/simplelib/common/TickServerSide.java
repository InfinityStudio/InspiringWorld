package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.UpdateSafe;
import net.minecraft.util.ITickable;

import java.util.WeakHashMap;

/**
 * @author ci010
 */
public enum TickServerSide implements ITickable
{
	INSTANCE;

	private WeakHashMap<Object, ITickable> updateWeakHashMap = new WeakHashMap<Object, ITickable>();
	private WeakHashMap<Object, UpdateSafe> safeWeakHashMap = new WeakHashMap<Object, UpdateSafe>();

	public void put(Object key, ITickable tickable)
	{
		if (tickable instanceof UpdateSafe)
			safeWeakHashMap.put(key, (UpdateSafe) tickable);
		updateWeakHashMap.put(key, tickable);
	}

	public ITickable remove(Object key)
	{
		if (updateWeakHashMap.containsKey(key))
			return updateWeakHashMap.remove(key);
		if (safeWeakHashMap.containsKey(key))
			return safeWeakHashMap.remove(key);
		return null;
	}

	@Override
	public void update()
	{
		for (ITickable tickable : updateWeakHashMap.values())
			tickable.update();
		for (UpdateSafe updateSafe : safeWeakHashMap.values())
			if (updateSafe.shouldUpdate())
				updateSafe.update();
	}
}
