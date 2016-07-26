package net.infstudio.inspiringworld.magic.repackage.net.simplelib;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * @author ci010
 */
public class LibPlugin implements IFMLLoadingPlugin
{
	private static File src;

	public static File getSource()
	{
		return src;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		src = (File) data.get("coremodLocation");
	}

	@Override
	public String getModContainerClass()
	{
		return LibModContainer.class.getName();
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

}
