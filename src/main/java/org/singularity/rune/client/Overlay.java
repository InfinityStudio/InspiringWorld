package org.singularity.rune.client;

import api.simplelib.registry.ModHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.HOTBAR;

/**
 * @author ci010
 */
@ModHandler
public class Overlay extends Gui
{
	private int currentX, currentY, widthHalf = 5, heightHalf = 5;
	private ScaledResolution resolution;
	Polygon polygon = new Polygon(new int[]{0, 5, 7}, new int[]{5, 10, 0}, 3);


	@SubscribeEvent
	public void mouse(MouseEvent event)
	{
		int x = currentX + event.getDx();
		int y = currentY + event.getDy();
		if (polygon.contains(x, y))
		{
			currentX = x;
			currentY = y;
		}
	}

	boolean draw;

	@SubscribeEvent
	public void render(RenderGameOverlayEvent.Post post)
	{
		if (post.getType() != HOTBAR)
			return;

		ScaledResolution resolution = post.getResolution();
		if (this.resolution == null)
		{
			currentX = resolution.getScaledWidth() / 2;
			currentY = resolution.getScaledHeight() / 2;
			this.resolution = resolution;
			polygon.translate(currentX, currentY);
		}
		else if (this.resolution.getScaledHeight() != resolution.getScaledHeight() ||
				this.resolution.getScaledWidth() != resolution.getScaledWidth())
		{
			polygon.translate(-currentX, -currentY);
			this.resolution = resolution;
			currentX = resolution.getScaledWidth() / 2;
			currentY = resolution.getScaledHeight() / 2;
			polygon.translate(currentX, currentY);
		}
		if (!draw)
		{
			System.out.println(currentX);
			System.out.println(currentY);
			for (int xpoint : polygon.xpoints)
				System.out.println(xpoint);
			for (int ypoint : polygon.ypoints)
				System.out.println(ypoint);
			draw = true;
		}
		Gui.drawRect(currentX, currentY, currentX + widthHalf, currentY + heightHalf, 0);
	}
}
