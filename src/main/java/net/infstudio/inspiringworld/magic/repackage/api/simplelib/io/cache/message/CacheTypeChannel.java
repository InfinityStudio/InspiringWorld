package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache.message;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache.CacheInfo;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache.CacheManifest;

/**
 * @author ci010
 */
public class CacheTypeChannel
{
	private static CacheTypeChannel ourInstance = new CacheTypeChannel();

	public static CacheTypeChannel instance()
	{
		return ourInstance;
	}

	private CacheTypeChannel()
	{
	}

	public void pipe(CacheInfo info)
	{
		String type = info.type();
	}

	public void pipe(CacheManifest manifest)
	{

	}
}
