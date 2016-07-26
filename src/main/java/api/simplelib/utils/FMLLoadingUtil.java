package api.simplelib.utils;

import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author ci010
 */
public class FMLLoadingUtil
{
	/**
	 * @param modid The mod's id.
	 * @return The ModContainer referred by that mod id.
	 */
	public static ModContainer getModContainer(String modid)
	{
		return Loader.instance().getIndexedModList().get(modid);
	}

	/**
	 * Warning, this method break into the Loader and LoadController. It will change the current active ModContainer.
	 *
	 * @param container The mod container will be changed into.
	 */
	public static void setActiveContainer(ModContainer container)
	{
		ReflectionHelper.setPrivateValue(LoadController.class,
				(LoadController) ReflectionHelper.getPrivateValue(Loader.class,
						Loader.instance(),
						"modController"),
				container,
				"activeContainer");
	}
}
