package net.simplelib.font;

import api.simplelib.utils.ColorIntUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.simplelib.HelperMod;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author ci010
 */
public class FontRendererCustom extends FontRenderer
{
	public FontRendererCustom()
	{
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft
				.getMinecraft().renderEngine, false);
		this.onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
	}

	@Override
	protected float renderDefaultChar(int ch, boolean italic)
	{
		if (ch >= 'a' && ch <= 'z')
		{
			float width = charWidth[ch] * 1.9F;
			float widthFix = width - 0.01F;
			float offsetTexX = 0.1F, offsetTexY = 0.13F;

			GlStateManager.enableBlend();
			bindTexture(getResourceLocation((char) ch));
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

			//WTF?????
//			GL11.glTexCoord2f(0 + offsetTexX, 1 - offsetTexY);
//			GL11.glVertex3f(this.posX, this.posY, 0.0F);
//			GL11.glTexCoord2f(0 + offsetTexX, 0 + offsetTexY);
//			GL11.glVertex3f(this.posX, this.posY + 7.99F, 0.0F);
//			GL11.glTexCoord2f(1 - offsetTexX, 1 - offsetTexY);
//			GL11.glVertex3f(this.posX + widthFix - 1.0F, this.posY, 0.0F);
//			GL11.glTexCoord2f(1 - offsetTexX, 0 + offsetTexY);
//			GL11.glVertex3f(this.posX + widthFix - 1.0F, this.posY + 7.99F, 0.0F);

			GL11.glTexCoord2f(0 + offsetTexX, 0 + offsetTexY);
			GL11.glVertex3f(this.posX, this.posY, 0.0F);
			GL11.glTexCoord2f(0 + offsetTexX, 1 - offsetTexY);
			GL11.glVertex3f(this.posX, this.posY + FONT_HEIGHT, 0.0F);
			GL11.glTexCoord2f(1 - offsetTexX, 0 + offsetTexY);
			GL11.glVertex3f(this.posX + widthFix - 1.0F, this.posY, 0.0F);
			GL11.glTexCoord2f(1 - offsetTexX, 1 - offsetTexY);
			GL11.glVertex3f(this.posX + widthFix - 1.0F, this.posY + FONT_HEIGHT, 0.0F);

			GL11.glEnd();

			return width;
		}
		else
			return super.renderDefaultChar(ch, italic);
	}

	public ResourceLocation getResourceLocation(char c)
	{
		return new ResourceLocation(HelperMod.MODID, "textures/font/" + c + ".png");
	}
}
