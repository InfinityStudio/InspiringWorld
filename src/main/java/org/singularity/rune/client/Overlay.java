package org.singularity.rune.client;

import api.simplelib.Instance;
import api.simplelib.registry.ModHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HOTBAR;

/**
 * @author ci010
 */
@ModHandler
@SideOnly(Side.CLIENT)
public class Overlay extends Gui
{
	@Instance
	public static final Overlay INSTANCE = new Overlay();

	private boolean enable, reverse;
	private int currentX, currentY;
	private Polygon[] drawArr;
	private Polygon range;
	private float sensitivity = 0.3F;
	private float factor;

	public boolean isReverse()
	{
		return reverse;
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
		this.factor = sensitivity * (reverse ? -1 : 1);
	}

	public void setEnable(boolean enable)
	{
		if (this.enable != enable)
			if (!this.enable)
				this.currentX = currentY = 0;
		this.enable = enable;
	}

	public float getSensitivity()
	{
		return sensitivity;
	}

	public void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
		this.factor = sensitivity * (reverse ? -1 : 1);
	}

	{
		generate();
	}

	@SubscribeEvent
	public void mouse(MouseEvent event)
	{
		if (!enable) return;
		int x = (int) (currentX + event.getDx() * factor);
		int y = (int) (currentY - event.getDy() * factor);
		if (range.contains(x, y))
		{
			currentX = x;
			currentY = y;
		}
	}

	void generate()
	{
		int r = 40, eR = 25;
		int divide = 6;
		Polygon[] polygons = new Polygon[divide];
		int[] xPoints = new int[divide], yPoints = new int[divide];
		double deltaAngle = Math.PI * 2 / divide;
		double angle = 0F;
		int lastX = xPoints[0] = r, lastY = yPoints[0] = 0, lastEX = eR, lastEY = 0;
		for (int i = 0; i < polygons.length; i++)
		{
			angle += deltaAngle;
			polygons[i] = new Polygon(
					new int[]{lastEX, lastX,
							  xPoints[i] = lastX = (int) (Math.cos(angle) * r),
							  lastEX = (int) (Math.cos(angle) * eR)},
					new int[]{lastEY, lastY,
							  yPoints[i] = lastY = (int) (Math.sin(angle) * r),
							  lastEY = (int) (Math.sin(angle) * eR)},
					4);
		}
		this.range = new Polygon(xPoints, yPoints, divide);
		this.drawArr = polygons;
	}


	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post post)
	{
		if (!enable)
			return;
		if (post.getType() != HOTBAR)
			return;
		ScaledResolution resolution = post.getResolution();
		int currentX = this.currentX + resolution.getScaledWidth() / 2;
		int currentY = this.currentY + resolution.getScaledHeight() / 2;

		Tessellator instance = Tessellator.getInstance();
		VertexBuffer buffer = instance.getBuffer();
		GlStateManager.enableAlpha();
		GlStateManager.disableCull();
		for (Polygon polygon : drawArr)
		{
			buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
			int j = 0;
			buffer.pos(polygon.xpoints[j] + currentX, polygon.ypoints[j] + currentY, 0).color(1f, 0f, 0f, 0.5f)
					.endVertex();
			j = 3;
			buffer.pos(polygon.xpoints[j] + currentX, polygon.ypoints[j] + currentY, 0).color(1f, 0f, 0f, 0.5f)
					.endVertex();
			j = 1;
			buffer.pos(polygon.xpoints[j] + currentX, polygon.ypoints[j] + currentY, 0).color(1f, 0f, 0f, 0.5f)
					.endVertex();
			j = 2;
			buffer.pos(polygon.xpoints[j] + currentX, polygon.ypoints[j] + currentY, 0).color(1f, 0f, 0f, 0.5f)
					.endVertex();
			instance.draw();
		}
		GlStateManager.enableCull();
		GlStateManager.disableAlpha();

	}
}
