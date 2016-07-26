package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io;

import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.world.storage.IThreadedFileIO;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author ci010
 */
public class DownloadTask implements Callable<File>, IThreadedFileIO
{
	private File file;
	private URL url;
	ListenableFutureTask<File> future;

	public DownloadTask(File file, URL url)
	{
		this.file = file;
		this.url = url;
		this.future = ListenableFutureTask.create(this);
	}

	@Override
	public File call() throws Exception
	{
		ReadableByteChannel channel = Channels.newChannel(url.openStream());
		FileOutputStream cacheFile = new FileOutputStream(file);
		cacheFile.getChannel().transferFrom(channel, 0, Integer.MAX_VALUE);
		cacheFile.close();
		return file;
	}

	@Override
	public boolean writeNextIO()
	{
		future.run();
		try
		{
			future.get();
			return true;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
