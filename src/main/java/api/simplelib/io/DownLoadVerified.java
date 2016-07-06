package api.simplelib.io;

import net.minecraft.world.storage.IThreadedFileIO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * @author ci010
 */
public class DownLoadVerified extends DownloadTask implements Callable<File>, IThreadedFileIO
{
	private VerifyTest verify;

	public DownLoadVerified(File file, URL url, VerifyTest verify)
	{
		super(file, url);
		this.verify = verify;
	}

	@Override
	public File call() throws Exception
	{
		File f = super.call();
		if (!verify.apply(f))
		{
			FileUtils.deleteQuietly(f);
			throw new VerifyException(verify.type(), "Invalid file doesn't pass " + verify + " test!");
		}
		return f;
	}
}
