package api.simplelib.utils;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class FileReference
{
	public static final File mc = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "mcDataDir");
	private static Map<String, FileReference> refers = Maps.newHashMap();
	protected static File assets = getDir(mc, "debug-reports");

	private static File logDir;
	private static File saveDir;
	private static File modDir;

	public static File getLog()
	{
		if (logDir == null)
			logDir = new File(mc, "logs");
		return logDir;
	}

	public static File getModsDir()
	{
		if (modDir == null)
			modDir = new File(mc, "mods");
		return modDir;
	}

	public static File getSave()
	{
		while (saveDir == null)
			saveDir = DimensionManager.getCurrentSaveRootDirectory();
		return saveDir;
	}

	public final File modFile, dirBlockState, dirModelBlock, dirModelItem, dirTextureBlock, dirTextureItem, dirLang;

	private FileReference(String id)
	{
		modFile = getDir(assets, id);
		dirBlockState = getDir(modFile, "blockstates");
		File dirModel = getDir(modFile, "models");
		dirModelBlock = getDir(dirModel, "block");
		dirModelItem = getDir(dirModel, "item");
		dirLang = getDir(modFile, "lang");
		File dirTexutre = getDir(modFile, "textures");
		dirTextureBlock = getDir(dirTexutre, "blocks");
		dirTextureItem = getDir(dirTexutre, "items");
	}

	public static FileReference getRefer(String modid)
	{
		if (!refers.containsKey(modid))
			refers.put(modid, new FileReference(modid));
		return refers.get(modid);

	}

	public static File getDir(File parent, String name)
	{
		File f = new File(parent, name);
		if (!f.exists())
			f.mkdirs();
		return f;
	}

	public static File getDir(File parent, String... path)
	{
		File f = parent;
		for (String s : path)
			f = getDir(f, s);
		return f;
	}
}
