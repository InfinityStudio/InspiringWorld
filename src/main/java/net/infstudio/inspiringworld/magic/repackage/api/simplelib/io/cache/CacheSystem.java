package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache;

import com.google.common.annotations.Beta;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.*;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.Instance;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.IOSystem;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.MD5Test;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.VerifyException;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.FileReference;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading.ExternalResource;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading.PackBase;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author ci010
 */
@LoadingDelegate
@SideOnly(Side.CLIENT)
@Beta
public class CacheSystem
{
	@Instance
	public static final CacheSystem INSTANCE = new CacheSystem();

	private CacheSystem() {}

	private File root = FileReference.getDir(FileReference.mc, "cache"),
			dataRoot = FileReference.getDir(FileReference.mc, "data");

	ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

	private Multimap<String, ResourceLocation> cache = HashMultimap.create();
	private Map<ResourceLocation, ListenableFuture<File>> pending = Maps.newHashMap();
	private Set<ResourceLocation> passed = Sets.newHashSet(), unChecked = Sets.newHashSet();

	private int currentId;

	public Map<ResourceLocation, ListenableFuture<File>> cacheAll(CacheManifest manifest)
	{
		Map<ResourceLocation, ListenableFuture<File>> map = Maps.newHashMap();
		manifest.getLife();
		manifest.getVersion();
		for (CacheInfo cacheInfo : manifest.getInfo())
		{

		}
		return null;
	}

	public ListenableFuture<File> get(CacheInfo cacheInfo) throws IOException
	{
		if (cacheInfo.md5() == null)
			return this.get(cacheInfo.location(), cacheInfo.url());
		else return this.get(cacheInfo.location(), cacheInfo.url(), cacheInfo.md5());
	}

	/**
	 * Cache the file to the resource location.
	 *
	 * @param location The resource location.
	 * @param url      The download URL.
	 * @return The File future status.
	 */
	public ListenableFuture<File> get(final ResourceLocation location, URL url)
	{
		if (pending.containsKey(location))
			return pending.get(location);
		if (!hasCached(location))
		{
			final ListenableFuture<File> future = IOSystem.fetch(getCacheFile(location), url);
			pending.put(location, future);
			future.addListener(new Runnable()
			{
				@Override
				public void run()
				{
					pending.remove(location);
				}
			}, service);
			pending.put(location, future);
			return future;
		}
		ListenableFutureTask<File> task = ListenableFutureTask.create(Runnables.doNothing(), getCacheFile(location));
		task.run();
		return task;
	}

	/**
	 * Cache the file to the resource location.
	 *
	 * @param location The resource location.
	 * @param url      The download URL.
	 * @return The File future status.
	 * @throws IOException
	 */
	public ListenableFuture<File> get(final ResourceLocation location, final URL url, final String md5)
			throws IOException
	{
		if (pending.containsKey(location))
			return pending.get(location);
		final File file = getCacheFile(location);
		if (!hasCached(location))
		{
			final ListenableFuture<File> future = IOSystem.fetch(getCacheFile(location), url, new MD5Test(md5));
			pending.put(location, future);
			future.addListener(new Runnable()
			{
				@Override
				public void run()
				{
					pending.remove(location);
					try
					{
						future.get();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					catch (ExecutionException e)
					{
						if (e.getCause() instanceof VerifyException)
							FileUtils.deleteQuietly(file);
						DebugLogger.fatal("Cannot cache {} with {} due to the illegal md5.", location, url);
					}
				}
			}, service);
			pending.put(location, future);
			return future;
		}
		if (!this.verify(location, md5))
			return Futures.immediateFailedCheckedFuture(new CacheException());
		ListenableFutureTask<File> task = ListenableFutureTask.create(Runnables.doNothing(), file);
		task.run();
		return task;
	}

	public boolean verify(ResourceLocation location, String md5)
	{
		if (!unChecked.contains(location))
		{
			File cacheFile = getCacheFile(location);
			try
			{
				String s = DigestUtils.md5Hex(new FileInputStream(cacheFile));
				if (s.equals(md5))
					passed.add(location);
				else
				{
					FileUtils.deleteQuietly(cacheFile);
					this.cache.remove(location.getResourceDomain(), location);
				}
				unChecked.remove(location);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return passed.contains(location);
	}

	/**
	 * @param location The resource location.
	 * @return Has get this file or not.
	 */
	public boolean hasCached(ResourceLocation location)
	{
		return cache.containsEntry(location.getResourceDomain(), location);
	}

	/**
	 * Determine if the resource is still pending.
	 *
	 * @param location The location
	 * @return Is the get is still been downloading.
	 */
	public boolean isPending(ResourceLocation location)
	{
		return pending.containsKey(location);
	}

	/**
	 * Remove the cached resource.
	 * <p>The file will be deleted.</p>
	 *
	 * @param location The location of the resource.
	 */
	public void remove(final ResourceLocation location)
	{
		if (cache.containsEntry(location.getResourceDomain(), location))
		{
			service.submit(new Runnable()
			{
				@Override
				public void run()
				{
					cache.remove(location.getResourceDomain(), location);
					FileUtils.deleteQuietly(getCacheFile(location));
				}
			});
		}
	}

	/**
	 * Clear all the resources in a domain.
	 *
	 * @param domain The domain name.
	 */
	public void clear(final String domain)
	{
		if (cache.containsKey(domain))
			service.submit(new Runnable()
			{
				@Override
				public void run()
				{
					File dir = new File(root, domain);
					if (dir.exists())
					{
						FileUtils.deleteQuietly(dir);
						cache.removeAll(domain);
					}
				}
			});
	}

	public File getCacheFile(ResourceLocation location)
	{
		return new File(FileReference.getDir(root, location.getResourceDomain()),
				location.getResourcePath());
	}

	public File getDataFile(ResourceLocation location)
	{
		return new File(FileReference.getDir(dataRoot, location.getResourceDomain()), location.getResourcePath());
	}


	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		ExternalResource.register(new PackBase(root)
		{
			@Override
			public Set<String> domain()
			{
				return Sets.newHashSet("cache");
			}
		});
		ExternalResource.register(new PackBase(dataRoot)
		{
			@Override
			public Set<String> domain()
			{
				return Sets.newHashSet("data");
			}
		});
		buildLoc(root);
		buildLoc(dataRoot);
	}

	private void buildLoc(File root)
	{
		for (File domain : root.listFiles(
				new FileFilter()
				{
					@Override
					public boolean accept(File pathname)
					{
						return pathname.isDirectory();
					}
				}))
		{
			File[] files = domain.listFiles();
			if (files != null)
				for (File f : files)
					buildLoc(f, domain.getName(), "");
		}
	}

	private void buildLoc(File file, String domain, String currentPath)
	{
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			if (files == null || file.length() == 0)
			{
				FileUtils.deleteQuietly(file);
				return;
			}
			for (File next : files)
				buildLoc(next, domain, currentPath.concat(file.getName()));
		}
		else
		{
			cache.put(domain, new ResourceLocation(domain, currentPath.concat(file.getName())));
		}
	}

	public ListeningExecutorService getService()
	{
		return this.service;
	}

	public synchronized ResourceLocation generateTempLocation()
	{
		return new ResourceLocation("temp:" + currentId++);
	}

	public synchronized ResourceLocation generateTempLocation(String id)
	{
		return new ResourceLocation("temp:" + id + "." + currentId++);
	}
}
