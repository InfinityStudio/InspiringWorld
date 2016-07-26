package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io;

import com.google.common.base.Predicate;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.FileReference;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * @author ci010
 */
public class IOSystem
{
	/**
	 * Fetch
	 *
	 * @param file
	 * @param url
	 * @return
	 */
	public static ListenableFuture<File> fetch(File file, URL url)
	{
		DownloadTask task = new DownloadTask(file, url);
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(task);
		return task.future;
	}

	public static ListenableFuture<File> fetch(File file, URL url, VerifyTest verify)
	{
		DownloadTask task = new DownLoadVerified(file, url, verify);
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(task);
		return task.future;
	}

	public static void save(ResourceLocation location, NBTTagCompound data)
	{
		save(location, data, false);
	}

	public static void save(ResourceLocation location, NBTTagCompound data, boolean compressed)
	{
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(new SaveTask(data, getPath(location), compressed));
	}

	public static ListenableFuture<NBTTagCompound> load(ResourceLocation location, boolean compressed)
	{
		final File path = getPath(location);
		final ReadTask task = new ReadTask(path, compressed);
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(task);
		return task.getFuture();
	}

	public static ListenableFuture<NBTTagCompound> load(ResourceLocation location)
	{
		return load(location, false);
	}

	private static File getPath(ResourceLocation location)
	{
		File domain = FileReference.getDir(FileReference.getSave(), location.getResourceDomain());
		return new File(domain, location.getResourcePath().concat(".dat"));
	}

	private static class ReadTask implements IThreadedFileIO
	{
		NBTTagCompound data;
		File path;
		boolean compressed;
		ListenableFutureTask<NBTTagCompound> task;

		public ReadTask(File path, boolean compressed)
		{
			this.path = path;
			this.compressed = compressed;
			task = ListenableFutureTask.create(new Callable<NBTTagCompound>()
			{
				@Override
				public NBTTagCompound call() throws Exception
				{
					return data;
				}
			});
		}

		ListenableFuture<NBTTagCompound> getFuture()
		{
			return task;
		}

		@Override
		public boolean writeNextIO()
		{
			try
			{
				if (!compressed)
					data = CompressedStreamTools.read(path);
				else
					data = CompressedStreamTools.readCompressed(new FileInputStream(path));
				task.run();
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return false;
		}
	}

	private static class SaveTask implements IThreadedFileIO
	{
		NBTTagCompound data;
		File path;
		boolean compressed;

		public SaveTask(NBTTagCompound serializable, File path, boolean compressed)
		{
			this.data = serializable;
			this.path = path;
			this.compressed = compressed;
		}

		@Override
		public boolean writeNextIO()
		{
			try
			{
				if (compressed)
					CompressedStreamTools.writeCompressed(data, new FileOutputStream(path));
				else
					CompressedStreamTools.write(data, path);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

}
