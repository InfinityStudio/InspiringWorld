package net.infstudio.inspiringworld.magic.repackage.net.simplelib.model.load;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * @author ci010
 */
public class ModelDataCollections
{
	Map<ResourceLocation, ModelData> modelDataMap;

	public class ModelData
	{
		private int[] bound, rotation;

		ModelData()
		{}

		void load(String domain, RawData.RawModelData data)
		{
			if (data.parent != null)
			{
				ModelData parent = modelDataMap.get(parse(domain, data.parent));
				if (data.bound != null)
					bound = data.bound;
				else bound = parent.bound;
				if (data.rotation != null)
					rotation = data.rotation;
				else rotation = parent.rotation;
			}
			else
			{
				bound = data.bound;
				rotation = data.rotation;
			}
		}
	}

	class AnimationData
	{

	}

	static ResourceLocation parse(String domain, String parent)
	{
		if (parent.contains(":"))
			return new ResourceLocation(parent);
		else return new ResourceLocation(domain, parent);
	}

}
