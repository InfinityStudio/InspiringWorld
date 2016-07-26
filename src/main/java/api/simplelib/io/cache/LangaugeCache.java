package api.simplelib.io.cache;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.LanguageMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * @author ci010
 */
public class LangaugeCache
{
	public void cacheLanguage(URL url)
	{
		ResourceLocation location = CacheSystem.INSTANCE.generateTempLocation();
		final ListenableFuture<File> cache = CacheSystem.INSTANCE.get(location, url);
		cache.addListener(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					File file = cache.get();
					LanguageMap.inject(new FileInputStream(file));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}, CacheSystem.INSTANCE.getService());
	}

	public void updateLanguage(String lang, URL url)
	{

//		final ListenableFuture<File> cache = CacheSystem.INSTANCE.get(location, url, false);

	}
}
