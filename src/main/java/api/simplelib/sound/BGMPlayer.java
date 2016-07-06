package api.simplelib.sound;

import api.simplelib.LoadingDelegate;
import api.simplelib.utils.FileReference;
import api.simplelib.Instance;
import api.simplelib.registry.ModHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simplelib.client.loading.ExternalResource;
import net.simplelib.client.loading.PackBase;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Just a simple bgm hook....
 *
 * @author ci010
 */
@SideOnly(Side.CLIENT)
@ModHandler
@LoadingDelegate
public class BGMPlayer implements IResourceManagerReloadListener
{
	@Instance
	private static BGMPlayer instance = new BGMPlayer();
	private ISound currentPlaying;
	private Map<String, Integer> loopTimeMap = new TreeMap<String, Integer>();

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		ExternalResource.register(new PackBase(FileReference.getDir(FileReference.mc, "bgm"))
		{
			@Override
			public Set<String> domain()
			{
				return Sets.newHashSet("bgm1", "bgm2");
			}
		});
	}

	private BGMPlayer()
	{
		this.enable(true);
	}

	private boolean isEnable;

	public static BGMPlayer instance()
	{
		return instance;
	}

	public boolean isPlaying()
	{
		return currentPlaying != null;
	}

	public boolean isEnable()
	{
		return isEnable;
	}

	public void enable(boolean isEnable)
	{
		this.isEnable = isEnable;
		if (isEnable)
			MinecraftForge.EVENT_BUS.register(this);
		else
			MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void playSound(PlaySoundEvent event)
	{
		if (event.getSound().getCategory() == SoundCategory.AMBIENT)
			event.setResultSound(null);
	}

	public void play(String name)
	{
		play(PositionedSoundRecord.getMusicRecord(new SoundEvent(new ResourceLocation("bgm1", name))));
	}

	public void playLoop(String name)
	{
		play(new SoundLoop(name));
	}

	private void play(ISound sound)
	{
		if (isEnable)
		{
			if (currentPlaying != null)
				Minecraft.getMinecraft().getSoundHandler().stopSound(currentPlaying);
			Minecraft.getMinecraft().getSoundHandler().playSound(
					currentPlaying = sound);
		}
	}

	private void prepareContinueLoop(SoundLoop soundLoop)
	{
		SoundCategory category = SoundCategory.getByName(soundLoop.nextChannel());
		SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
		handler.pauseSounds();
		handler.setSoundLevel(category, 0);
		handler.playSound(soundLoop.next());
	}

	public void stop()
	{
		if (isEnable)
		{
			Minecraft.getMinecraft().getSoundHandler().stopSound(this.currentPlaying);
		}
	}

	class SoundLoop implements ISound //, ITickableSound
	{
		private ResourceLocation location;
		private SoundLoop nextSound;
		private long current, offset;

		SoundLoop(String name)
		{
			this("bgm1", name);
		}

		SoundLoop(String channel, String name)
		{
			this.location = new ResourceLocation(channel, name);
			this.nextSound = new SoundLoop(nextChannel(), this.location.getResourcePath());
		}

		@Override
		public ResourceLocation getSoundLocation()
		{
			return location;
		}

		@Nullable
		@Override
		public SoundEventAccessor createAccessor(SoundHandler handler)
		{
			return null;
		}

		@Override
		public Sound getSound()
		{
			return null;
		}

		@Override
		public SoundCategory getCategory()
		{
			return null;
		}

		@Override
		public boolean canRepeat()
		{
			if (Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(nextSound))
			{
				SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
				SoundCategory category = SoundCategory.getByName(this.nextChannel());
				handler.setSoundLevel(category, 1);
			}
			return false;
		}

		@Override
		public int getRepeatDelay()
		{
			return 0;
		}

		@Override
		public float getVolume()
		{
			return 1;
		}

		@Override
		public float getPitch()
		{
			return 1;
		}

		@Override
		public float getXPosF()
		{
			return 0;
		}

		@Override
		public float getYPosF()
		{
			return 0;
		}

		@Override
		public float getZPosF()
		{
			return 0;
		}

		@Override
		public AttenuationType getAttenuationType()
		{
			return AttenuationType.NONE;
		}

		//		@Override
		public boolean isDonePlaying()
		{
			return false;
		}
//
//		@Override

		public void update()
		{
			if (++current == offset)
				prepareContinueLoop(this);
		}

		private SoundLoop next()
		{
			return nextSound;
		}

		private String nextChannel()
		{
			return this.location.getResourceDomain().equals("bgm1") ? "bgm2" : "bgm1";
		}
	}


	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		this.generateSoundJson(FileReference.getDir(FileReference.mc, "bgm"));
		this.loadLoopTime();
	}

	private void loadLoopTime()
	{
		File sounds = FileReference.getDir(FileReference.mc, "bgm", "sounds");
		File file = new File(sounds, "loop.properties");
		if (file.exists())
			try
			{
				List<String> strings = FileUtils.readLines(file);
				for (String string : strings)
				{
					String[] split = string.split("=");
					if (split.length == 2)
					{
						try
						{
							loopTimeMap.put(split[0].replace(" ", ""), Integer.parseInt(split[1].replace(" ", "")));
						}
						catch (NumberFormatException e)
						{/*TODO log*/}
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		else
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void generateSoundJson(File assetsDir)
	{
		File soundsDir = FileReference.getDir(assetsDir, "sounds");
		final List<ResourceLocation> locations = Lists.newArrayList();
		this.discoverSoundsFile(soundsDir, locations);
		Gson gson = new GsonBuilder().registerTypeAdapter(ResourceLocation[].class, new
				TypeAdapter<ResourceLocation[]>()
				{
					@Override
					public void write(JsonWriter out, ResourceLocation[] value) throws IOException
					{
						out.beginObject();
						for (ResourceLocation loc : value)//for bgm1 channel
						{
							String path = loc.getResourcePath();
							System.out.println(path.substring(path.lastIndexOf("/")));
							out.name(path.substring(path.lastIndexOf("/"))).beginObject();
							{
								out.name("category").value(loc.getResourceDomain());
								out.name("sounds").beginArray();
								{
									out.beginObject();
									out.name("name").value(path);
									out.name("stream").value(true);
									out.endObject();
								}
								out.endArray();
							}
							out.endObject();
						}
						out.endObject();
					}

					@Override
					public ResourceLocation[] read(JsonReader in) throws IOException
					{
						return null;
					}
				}
		).setPrettyPrinting().create();
		String json = gson.toJson(locations.toArray(), ResourceLocation[].class);
		if (json.isEmpty())
			return;
		try
		{
			FileUtils.write(new File(assetsDir, "sounds.json"), json);
		}
		catch (IOException e) {e.printStackTrace();}
	}

	private void discoverSoundsFile(File dir, final List<ResourceLocation> locations)
	{
		File[] musics = dir.listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				if (pathname.isDirectory())
				{
					discoverSoundsFile(pathname, locations);
					return false;
				}
				return pathname.getName().endsWith(".ogg");
			}
		});
		if (musics != null && musics.length != 0)
			for (File music : musics)
			{
				String path = music.getPath();
				path = path.replace(".\\bgm\\sounds\\", "").replace(".ogg", "").replace("\\", "/");
				locations.add(new ResourceLocation("bgm1", path));
				locations.add(new ResourceLocation("bgm2", path));
			}
	}

}
