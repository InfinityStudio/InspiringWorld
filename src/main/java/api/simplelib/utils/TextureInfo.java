package api.simplelib.utils;

import net.minecraft.util.ResourceLocation;

/**
 * This class contains the basic info of a texture.
 * Regardless its position on the screen, this class only focuses on where the resource is, and which part of the
 * resource location is the texture.
 * <p>I hope this will reduce the pain caused by only 256x256 size of texture allowed in Minecraft.
 *
 * @author ci010
 */
public class TextureInfo
{
	private ResourceLocation location;
	private int u, v, width, height;

	public TextureInfo(ResourceLocation location, int u, int v, int width, int height)
	{
		this.location = location;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}

	public ResourceLocation getTexture()
	{
		return location;
	}

	public int getU()
	{
		return u;
	}

	public int getV()
	{
		return v;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

}
