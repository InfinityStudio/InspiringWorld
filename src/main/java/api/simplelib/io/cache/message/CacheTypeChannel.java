package api.simplelib.io.cache.message;

import api.simplelib.io.cache.CacheInfo;
import api.simplelib.io.cache.CacheManifest;

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
