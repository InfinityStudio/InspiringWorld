package api.simplelib.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author ci010
 */
public class GuiUtil
{
	public static FontRenderer font()
	{
		return mc.fontRendererObj;
	}

	public static final Minecraft mc = Minecraft.getMinecraft();

	public static final TextureManager texture = mc.getTextureManager();

	public static final ResourceLocation inv = new ResourceLocation("textures/gui/container/inventory.png");

	public static final TextureInfo slot = new TextureInfo(
			new ResourceLocation("textures/gui/container/inventory.png"), 7, 141, 18, 18);

	public static void bindTexture(TextureInfo info)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(info.getTexture());
	}
}
