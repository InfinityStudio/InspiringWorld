package net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.render;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModHandler;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;

/**
 * @author ci010
 */
@ModHandler
@SideOnly(Side.CLIENT)
public class ClientRenderer
{
	private HashSet<Work> works = Sets.newLinkedHashSet();

	public static ClientRenderer instance()
	{
		return instance;
	}

	private static final ClientRenderer instance = new ClientRenderer();

	private ClientRenderer()
	{
	}

	@SubscribeEvent
	public void onRender(RenderWorldLastEvent event)
	{
		for (Work work : works)
			work.render(event.getContext(), event.getPartialTicks());
	}

	public void remove(Work work)
	{
		works.remove(work);
	}

	public void add(Work work)
	{
		works.add(work);
	}

	interface Work
	{
		void render(RenderGlobal context, final float partialTicks);
	}
}
