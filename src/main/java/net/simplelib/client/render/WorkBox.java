package net.simplelib.client.render;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author ci010
 */
public class WorkBox implements ClientRenderer.Work
{
	private Vector3d a, b;
	private LinkedList<RenderWorker> workers = Lists.newLinkedList();

	public WorkBox(BlockPos a, BlockPos b)
	{
		this.a = ClientRenderUtils.getRealPositionX(a);
		this.b = ClientRenderUtils.getRealPositionX(b);
	}

	public WorkBox addFirst(RenderWorker worker)
	{
		workers.addFirst(worker);
		return this;
	}

	public WorkBox addAfter(RenderWorker target, RenderWorker newWorker)
	{
		ListIterator<RenderWorker> iterator = workers.listIterator();
		while (iterator.hasNext())
			if (iterator.next() == target)
				iterator.add(newWorker);
		return this;
	}

	public WorkBox addBefore(RenderWorker target, RenderWorker newWorker)
	{
		ListIterator<RenderWorker> iterator = workers.listIterator();
		while (iterator.hasNext())
			if (iterator.next() == target)
			{
				iterator.previous();
				iterator.add(newWorker);
			}
		return this;
	}

	public WorkBox addLast(RenderWorker worker)
	{
		workers.addLast(worker);
		return this;
	}

	@Override
	public void render(RenderGlobal context, float partialTicks)
	{
		for (RenderWorker worker : workers)
			worker.render(context, partialTicks, a.x, a.y, a.z, b.x, b.y, b.z);
	}

	public interface RenderWorker
	{
		void render(RenderGlobal context, float partialTicks, double minX, double minY, double minZ, double maxX,
					double maxY, double maxZ);
	}

	public static final RenderWorker FULL_BOX =
			new RenderWorker()
			{
				@Override
				public void render(RenderGlobal context, float partialTicks, double minX, double minY, double minZ,
								   double maxX, double maxY, double maxZ)
				{
					Tessellator instance = Tessellator.getInstance();
					VertexBuffer vertex = instance.getBuffer();
					vertex.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION);
					vertex.pos(minX, minY, minZ).endVertex();
					vertex.pos(minX, maxY, minZ).endVertex();

					vertex.pos(maxX, minY, minZ).endVertex();
					vertex.pos(maxX, maxY, minZ).endVertex();

					vertex.pos(maxX, minY, maxZ).endVertex();
					vertex.pos(maxX, maxY, maxZ).endVertex();

					vertex.pos(minX, minY, maxZ).endVertex();
					vertex.pos(minX, maxY, maxZ).endVertex();

					vertex.pos(minX, minY, minZ).endVertex();
					vertex.pos(minX, maxY, minZ).endVertex();
					instance.draw();

					vertex.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
					vertex.pos(minX, maxY, minZ).endVertex();
					vertex.pos(maxX, maxY, minZ).endVertex();
					vertex.pos(maxX, maxY, maxZ).endVertex();
					vertex.pos(minX, maxY, maxZ).endVertex();
					vertex.pos(minX, minY, minZ).endVertex();
					vertex.pos(maxX, minY, minZ).endVertex();
					vertex.pos(maxX, minY, maxZ).endVertex();
					vertex.pos(minX, minY, maxZ).endVertex();
					instance.draw();
				}
			},
			EMPTY_BOX = new RenderWorker()
			{
				@Override
				public void render(RenderGlobal context, float partialTicks, double minX, double minY, double minZ,
								   double maxX, double maxY, double maxZ)
				{
					Tessellator instance = Tessellator.getInstance();
					VertexBuffer vertex = instance.getBuffer();

					vertex.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
					vertex.pos(minX, minY, minZ).endVertex();
					vertex.pos(minX, maxY, minZ).endVertex();
					vertex.pos(maxX, maxY, minZ).endVertex();
					vertex.pos(maxX, minY, minZ).endVertex();
					instance.draw();

					vertex.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
					vertex.pos(minX, minY, maxZ).endVertex();
					vertex.pos(minX, maxY, maxZ).endVertex();
					vertex.pos(maxX, maxY, maxZ).endVertex();
					vertex.pos(maxX, minY, maxZ).endVertex();
					instance.draw();

					vertex.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
					vertex.pos(minX, minY, minZ).endVertex();
					vertex.pos(minX, minY, maxZ).endVertex();

					vertex.pos(minX, maxY, minZ).endVertex();
					vertex.pos(minX, maxY, maxZ).endVertex();

					vertex.pos(maxX, maxY, minZ).endVertex();
					vertex.pos(maxX, maxY, maxZ).endVertex();

					vertex.pos(maxX, minY, minZ).endVertex();
					vertex.pos(maxX, minY, maxZ).endVertex();
					instance.draw();
				}
			};
}
