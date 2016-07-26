package net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLContainerHolder;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.IOUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author ci010
 */
public class ExternalResource implements IResourcePack, FMLContainerHolder
{
	private ModContainer container;

	private static Map<String, Pack> delegate = Maps.newHashMap();

	public static void register(Pack pack)
	{
		for (String s : pack.domain())
			delegate.put(s, pack);
	}

	public ExternalResource(ModContainer container)
	{
		this.container = container;
	}

	@Override
	public InputStream getInputStream(ResourceLocation location)
	{
		if (!delegate.containsKey(location.getResourceDomain()))
			return null;
		return delegate.get(location.getResourceDomain()).getInputStream(location);
	}

	@Override
	public boolean resourceExists(ResourceLocation location)
	{
		if (!delegate.containsKey(location.getResourceDomain()))
			return false;
		return delegate.get(location.getResourceDomain()).resourceExists(location);
	}

	public Set<String> getResourceDomains()
	{
		return delegate.keySet();
	}

	@Override
	public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String
			metadataSectionName) throws IOException
	{
		TypeUtils.cast(new ByteArrayInputStream(("{\n" +
				" \"pack\": {\n" +
				"   \"description\": \"dummy FML pack for " + container.getName() + "\",\n" +
				"   \"pack_format\": 1\n" +
				"}\n" +
				"}").getBytes(Charsets.UTF_8)));
		JsonObject jsonobject = null;
		BufferedReader bufferedreader = null;

		try
		{
			bufferedreader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(("{\n" +
					" \"pack\": {\n" +
					"   \"description\": \"dummy FML pack for " + container.getName() + "\",\n" +
					"   \"pack_format\": 1\n" +
					"}\n" +
					"}").getBytes(Charsets.UTF_8))));
			jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
		}
		catch (RuntimeException runtimeexception)
		{
			throw new JsonParseException(runtimeexception);
		}
		finally
		{
			IOUtils.closeQuietly(bufferedreader);
		}

		return metadataSerializer.parseMetadataSection(metadataSectionName, jsonobject);
	}

	private InputStream getRealInputStream(ResourceLocation location)
	{
		try
		{
			ZipFile zipfile = new ZipFile(
					new File(this.container.getSource(),
							String.format("%s/%s/%s/", "assets", location.getResourceDomain(), location.getResourcePath())));
			ZipEntry zipentry = zipfile.getEntry("assets/pack.mcmeta");
			return zipfile.getInputStream(zipentry);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BufferedImage getPackImage() throws IOException
	{
		return Minecraft.getMinecraft().getResourcePackRepository().getResourcePackInstance().getPackImage();
	}

	@Override
	public String getPackName()
	{
		return "extra";
	}

	@Override
	public ModContainer getFMLContainer()
	{
		return container;
	}
}
