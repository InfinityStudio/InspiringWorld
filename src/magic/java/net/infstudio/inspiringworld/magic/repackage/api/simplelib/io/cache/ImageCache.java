package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author ci010
 */
@SideOnly(Side.CLIENT)
public class ImageCache
{
	public static final ImageCache INSTANCE = new ImageCache();
	private Map<ResourceLocation, ITextureObject> mapTextureObjects = ReflectionHelper.getPrivateValue
			(TextureManager.class, Minecraft.getMinecraft().getTextureManager(), "mapTextureObjects");
	private TextureManager manager = Minecraft.getMinecraft().getTextureManager();
	private Set<ResourceLocation> loading = Sets.newHashSet();

	private ImageCache() {}

	/**
	 * Cache an image into game.
	 *
	 * @param location The resource location of this image will be identified in game.
	 * @param url      The image URL.
	 */
	public void cacheImage(final ResourceLocation location, final URL url)
	{
		if (CacheSystem.INSTANCE.isPending(location))
			return;
		if (loading.contains(location))
			return;
		final ListenableFuture<File> cache = CacheSystem.INSTANCE.get(location, url);
		if (cache.isDone())
		{
			try
			{
				this.load(location, cache.get());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();
			}
		}
		else
			cache.addListener(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						load(location, cache.get());
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					catch (ExecutionException e)
					{
						e.printStackTrace();
					}
				}
			}, CacheSystem.INSTANCE.service);
	}

	/**
	 * Remove the image from game, but not remove it on disk. Still, next time you to re-get it.
	 *
	 * @param location
	 */
	public void unloadImage(ResourceLocation location)
	{
		ITextureObject itextureobject = manager.getTexture(location);
		if (itextureobject != null)
		{
			TextureUtil.deleteTexture(itextureobject.getGlTextureId());
			mapTextureObjects.remove(location);
		}
		loading.remove(location);
	}

	/**
	 * Remove the image from game. The get file will be also removed.
	 *
	 * @param location
	 */
	public void removeImage(ResourceLocation location)
	{
		unloadImage(location);
		CacheSystem.INSTANCE.remove(location);
	}

	private void load(final ResourceLocation location, final File file)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					BufferedImage image = ImageIO.read(file);
					int id = TextureUtil.glGenTextures();
					TextureUtil.uploadTextureImage(id, image);
					TextureImage obj = new TextureImage(id);
					mapTextureObjects.put(location, obj);
					loading.add(location);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Clear all loading get.
	 */
	public void clear()
	{
		HashSet<ResourceLocation> set = Sets.newHashSet(loading);
		for (ResourceLocation location : set)
			removeImage(location);
	}

	private class TextureImage implements ITextureObject
	{
		int id;

		TextureImage(int id)
		{
			this.id = id;
		}

		@Override
		public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_)
		{}

		@Override
		public void restoreLastBlurMipmap()
		{}

		@Override
		public void loadTexture(IResourceManager resourceManager) throws IOException
		{}

		@Override
		public int getGlTextureId()
		{
			return id;
		}
	}
}
