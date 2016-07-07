package org.singularity.rune.client;

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

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HOTBAR;

/**
 * @author ci010
 */
@ModHandler
@SideOnly(Side.CLIENT)
public class Overlay extends Gui
{
	private int currentX, currentY, widthHalf = 20, heightHalf = 20;
	private ScaledResolution resolution;
	Polygon[] arr;
	Polygon range;
	float sensitivity = 0.3F;

	{
		generate();
	}

	@SubscribeEvent
	public void mouse(MouseEvent event)
	{
		int x = (int) (currentX + event.getDx() * sensitivity);
		int y = (int) (currentY - event.getDy() * sensitivity);
		if (range.contains(x, y))
		{
			currentX = x;
			currentY = y;
		}
	}

	void generate()
	{
		int r = 40, eR = 20;
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
		for (int i = 0; i < divide; i++)
		{
			System.out.println(xPoints[i] + " " + yPoints[i]);
		}
		System.out.println(range.contains(25, 37));
		this.arr = polygons;
	}

	boolean d;

	byte time = 0;

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post post)
	{
		if (post.getType() != HOTBAR)
			return;


		ScaledResolution resolution = post.getResolution();
//		if (this.resolution == null || this.resolution.getScaledHeight() != resolution.getScaledHeight() ||
//				this.resolution.getScaledWidth() != resolution.getScaledWidth())
//		{
//			this.resolution = resolution;
//		}

		int currentX = this.currentX + resolution.getScaledWidth() / 2;
		int currentY = this.currentY + resolution.getScaledHeight() / 2;

		if (!d)
		{
			System.out.println(currentX);
			System.out.println(currentY);
		}


//		GlStateManager.pushMatrix();

//		GlStateManager.translate(10, 10, 0);
//		GL11.glColor3f(1F, 0F, 0F);

//		GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
//		GL11.glVertex3f(0, 0, 0);
//		GL11.glVertex3f(100, 70, 0);
//		GL11.glVertex3f(0, 100, 0);
//		GL11.glEnd();
//
//		GlStateManager.glBegin(GL11.GL_QUADS);
//		GL11.glVertex2f(0 + 100, 0 + 100);
//		GL11.glVertex2f(0 + 100, 10 + 100);
//		GL11.glVertex2f(10 + 100, 10 + 100);
//		GL11.glVertex2f(10 + 100, 0 + 100);
//		GL11.glEnd();

//		GlStateManager.popMatrix();

//		GL11.glBegin(GL11.GL_TRIANGLES);
//		GL11.glColor4f(0F, 1F, 0F, 0.5F);
//		GL11.glVertex2f((currentX + 10) / resolution.getScaledWidth(), currentY / resolution.getScaledHeight());
//		GL11.glColor4f(0F, 1F, 0F, 0.5F);
//		GL11.glVertex2f(currentX / resolution.getScaledWidth(), (currentY + 10) / resolution.getScaledHeight());
//		GL11.glColor4f(0F, 1F, 0F, 0.5F);
//		GL11.glVertex2f(currentX / resolution.getScaledWidth(), currentY / resolution.getScaledHeight());
//		GL11.glEnd();

//		buffer.pos(currentX + 10, currentY, 0).color(0f, 1f, 0f, 0.5f).endVertex();
//		buffer.pos(currentX, currentY + 10, 0).color(0f, 1f, 0f, 0.5f).endVertex();
//		buffer.pos(currentX, currentY, 0).color(0f, 1f, 0f, 0.5f).endVertex();
//		instance.draw();
		Tessellator instance = Tessellator.getInstance();
		VertexBuffer buffer = instance.getBuffer();


//		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//		buffer.pos(currentX + 10, currentY + 10, 0).color(1f, 0f, 1, 0.5f).endVertex();
//		buffer.pos(currentX + 5, currentY - 10, 0).color(1f, 0f, 1, 0.5f).endVertex();
//		buffer.pos(currentX - 5, currentY - 10, 0).color(1f, 0f, 1, 0.5f).endVertex();
//		buffer.pos(currentX - 10, currentY + 10, 0).color(1f, 0f, 1, 0.5f).endVertex();
//		instance.draw();
		for (Polygon polygon : arr)
		{
//			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//			int x = polygon.xpoints[0] + currentX, y = polygon.ypoints[0] + currentX;
//			buffer.pos(x + 10, y + 10, 0).color(1f, 0f, 0f, 0.5f).endVertex();
//			buffer.pos(x + 10, y - 10, 0).color(1f, 0f, 0f, 0.5f).endVertex();
//			buffer.pos(x - 10, y - 10, 0).color(1f, 0f, 0f, 0.5f).endVertex();
//			buffer.pos(x - 10, y + 10, 0).color(1f, 0f, 0f, 0.5f).endVertex();
//			instance.draw();

//			if (!d)
//				System.out.println((x + currentX) + " " + (y + currentY));
//			if (time++ == 240)
//			{
//				time = 0;
//			System.out.println();
//			for (int i = 0; i < polygon.npoints; i++)
//				System.out.println((polygon.xpoints[i] + "+" + currentX + "=" + (polygon.xpoints[i] + currentX))
//						+ " " +
//						(polygon.ypoints[i] + "+" + currentY + "=" + (polygon.ypoints[i] + currentY)));
//			System.out.println();
//			}
//			glDisable(GL_CULL_FACE);
//			glBegin(GL_QUADS);
//			glVertex2f(223, 137);
//			glVertex2f(233, 154);
//			glVertex2f(194, 154);
//			glVertex2f(204, 137);
//			glEnd();
//			glEnable(GL_CULL_FACE);

//			glBegin(GL_QUAD_STRIP);
//			for (int i = 0; i < polygon.npoints; i++)
//				glVertex2f(polygon.xpoints[i] + currentX, polygon.ypoints[i] + currentY);
//			glEnd();

//			GlStateManager.disableCull();
			GlStateManager.enableAlpha();
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
			GlStateManager.disableAlpha();
//			GlStateManager.enableCull();


//			GlStateManager.disableCull();
//			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
//			for (int i = polygon.npoints - 1; i > 0; i--)
//				buffer.pos(polygon.xpoints[i] + currentX, polygon.ypoints[i] + currentY, 0).color(1f, 0f, 0f, 0.5f)
//						.endVertex();
//			instance.draw();
//			GlStateManager.enableCull();
		}
		if (!d)
			d = true;
	}
}
