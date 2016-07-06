package net.simplelib.common.registry;

import api.simplelib.utils.FileReference;
import api.simplelib.utils.Environment;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import api.simplelib.registry.ModHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ci010
 */
@ModHandler
public class ModelReporter
{
//	private static ModelResourceLocation MISS = new ModelResourceLocation("builtin/missing", "missing");

	@SubscribeEvent
	public void onModelPost(ModelBakeEvent event)
	{
		if (!Environment.debug())
			return;
		Map<ResourceLocation, ModelStates> map = Maps.newHashMap();
		List<ModelStates> list = Lists.newArrayList();
		Set<ModelResourceLocation> set = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(),
				"missingVariants");
//		IRegistry modelReg = event.modelRegistry;
//		Object missingModel = modelReg.getArgs(MISS);
		for (ModelResourceLocation missing : set)
		{
			ResourceLocation location = new ResourceLocation(missing.getResourceDomain(), missing.getResourcePath());
			if (map.containsKey(location))
				map.get(location).addVariant(missing.getVariant());
			else
			{
				ModelStates temp = new ModelStates(missing);
				map.put(location, temp);
			}
		}
		Gson gson = this.buildGson();
		try
		{
			for (final ModelStates state : list)
				if (state.vars.size() == 1 && state.vars.get(0).equals("inventory"))
					this.writeItem(state, gson);
				else
					this.writeBlock(state, gson);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		standard = null;
	}

	private void writeItem(ModelStates state, Gson gson) throws IOException
	{
		FileReference refer = FileReference.getRefer(state.domain());
		File modelItem = new File(refer.dirModelItem, state.path().concat(".json"));
		if (standard != null)
			FileUtils.write(new File(refer.dirModelItem, "standard.json"), gson.toJson(standard));
		standard = null;
		ItemModelSimpleInfo info = new ItemModelSimpleInfo();
		info.parent = state.domain().concat(":item/standard");
		info.texture = state.domain().concat(":item/").concat(state.path());
		FileUtils.write(modelItem, gson.toJson(info));
	}

	private void writeBlock(ModelStates state, Gson gson) throws IOException
	{
		FileReference refer = FileReference.getRefer(state.domain());
		File stateFile = new File(refer.dirBlockState, state.path().concat(".json"));
		File modelItem = new File(refer.dirModelItem, state.path().concat(".json"));
		FileUtils.write(stateFile, gson.toJson(state).replace('`', '='));
		//Replace to prevent gson to transform that to something strange....
		for (String s : state.varPath)
		{
			File modelFile = new File(refer.dirModelBlock, s.concat(".json"));
			BlockModelSimpleInfo info = new BlockModelSimpleInfo();
			info.texture = state.domain().concat(":").concat(s).concat("_").concat("texture");
			FileUtils.write(modelFile, gson.toJson(info));
		}
		ItemModelSimpleInfo info = new ItemModelSimpleInfo();
		info.parent = state.domain().concat(":").concat("block").concat("/").concat(state.path());
		info.thirdperson = new PersonView();
		info.thirdperson.rotation = new int[]{10, -45, 170};
		info.thirdperson.translation = new float[]{0f, 1.5f, -2.75f};
		info.thirdperson.scale = new float[]{0.375f, 0.375f, 0.375f};
		FileUtils.write(modelItem, gson.toJson(info));
	}

	private Gson buildGson()
	{
		GsonBuilder builder = new GsonBuilder()
				.registerTypeAdapter(ModelStates.class, new TypeAdapter<ModelStates>()
				{
					@Override
					public void write(JsonWriter out, ModelStates value) throws IOException
					{
						out.setIndent("  ");
						out.beginObject();
						out.name("variants").beginObject();
						for (int i = 0; i < value.vars.size(); ++i)
						{
							String var = value.vars.get(i);
							String varPth = value.varPath.get(i);
							var = var.replace('=', '`');
							out.name(var).beginObject();
							out.name("model").value(value.domain().concat(":").concat(varPth)).endObject();
						}
						out.endObject().endObject();
					}

					@Override
					public ModelStates read(JsonReader in) throws IOException {return null;}
				})
				.registerTypeAdapter(BlockModelSimpleInfo.class, new TypeAdapter<BlockModelSimpleInfo>()
				{
					@Override
					public void write(JsonWriter out, BlockModelSimpleInfo value) throws IOException
					{
						out.setIndent("  ");
						out.beginObject();
						out.name("parent").value(value.parent);
						out.name("textures").beginObject();
						out.name("all").value(value.texture);
						out.endObject().endObject();
					}

					@Override
					public BlockModelSimpleInfo read(JsonReader in) throws IOException {return null;}
				})
				.registerTypeAdapter(ItemModelSimpleInfo.class, new TypeAdapter<ItemModelSimpleInfo>()
				{
					@Override
					public void write(JsonWriter out, ItemModelSimpleInfo value) throws IOException
					{
						out.setIndent("  ");
						out.beginObject();
						if (value.parent != null)
							out.name("parent").value(value.parent);
						if (value.thirdperson != null || value.firstperson != null)
						{
							out.name("display").beginObject();
							if (value.thirdperson != null)
								this.write("thirdperson", out, value.thirdperson);
							if (value.firstperson != null)
								this.write("firstperson", out, value.firstperson);
							out.endObject();
						}
						if (value.texture != null)
						{
							out.name("texture").beginObject();
							out.name("layer0").value(value.texture);
							out.endObject();
						}
						out.endObject();
					}

					private void write(String name, JsonWriter out, PersonView view) throws IOException
					{
						out.setIndent("  ");
						out.name(name).beginObject();
						out.name("rotation").beginArray();
						for (int i : view.rotation)
							out.value(i);
						out.endArray();
						out.name("translation").beginArray();
						for (float i : view.translation)
							out.value(i);
						out.endArray();
						out.name("multiply").beginArray();
						for (float v : view.scale)
							out.value(v);
						out.endArray().endObject();
					}

					@Override
					public ItemModelSimpleInfo read(JsonReader in) throws IOException {return null;}
				});
		return builder.create();
	}

	private static class ItemModelSimpleInfo
	{
		String parent, texture;
		PersonView thirdperson, firstperson;
	}

	private static class PersonView
	{
		int[] rotation;
		float[] scale, translation;
	}

	private static class BlockModelSimpleInfo
	{
		String parent = "block/cube_all", texture;
	}

	private static ItemModelSimpleInfo standard = new ItemModelSimpleInfo()
	{
		{
			parent = "builtin/generated";
			thirdperson = new PersonView();
			thirdperson.rotation = new int[]{-90, 0, 0};
			thirdperson.translation = new float[]{0, 1, -3};
			thirdperson.scale = new float[]{0.55f, 0.55f, 0.55f};
			firstperson = new PersonView();
			firstperson.rotation = new int[]{0, -135, 25};
			firstperson.translation = new float[]{0, 4, 2};
			firstperson.scale = new float[]{1.7f, 1.7f, 1.7f};
		}
	};

	private static class ModelStates
	{
		private ResourceLocation location;
		List<String> vars;
		List<String> varPath;

		public ModelStates(ModelResourceLocation resourceLocation)
		{
			location = new ResourceLocation(resourceLocation.getResourceDomain(), resourceLocation.getResourcePath());
			vars = Lists.newArrayList();
			varPath = Lists.newArrayList();
			this.addVariant(resourceLocation.getVariant());
		}

		public ModelStates(String domain, String path)
		{
			vars = Lists.newArrayList();
			varPath = Lists.newArrayList();
		}

		public ModelStates addVariant(String var)
		{
			String path = location.getResourcePath();
			this.vars.add(var);
			int idx = var.indexOf("=");
			if (idx != -1)
				this.varPath.add(path.concat("_").concat(var.substring(var.indexOf("=") + 1)));
			else
				this.varPath.add(path);
			return this;
		}

		public String domain()
		{
			return location.getResourceDomain();
		}

		public String path()
		{
			return location.getResourcePath();
		}

		@Override
		public String toString()
		{
			return location.toString();
		}
	}

}
