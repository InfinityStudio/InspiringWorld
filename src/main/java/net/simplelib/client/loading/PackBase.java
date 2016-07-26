package net.simplelib.client.loading;

import net.minecraft.util.ResourceLocation;

import java.io.*;

/**
 * @author ci010
 */
public abstract class PackBase implements Pack
{
	private File file;

	public PackBase(File file)
	{
		this.file = file;
	}

	@Override
	public InputStream getInputStream(ResourceLocation location)
	{
		try
		{
			return new BufferedInputStream(new FileInputStream(new File(file, location.getResourcePath())));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean resourceExists(ResourceLocation location)
	{
		return (new File(file, location.getResourcePath())).isFile();
	}
}
