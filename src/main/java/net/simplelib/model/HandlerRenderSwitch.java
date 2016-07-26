package net.simplelib.model;

import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import api.simplelib.registry.ModHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;

/**
 * @author ci010
 */
@ModHandler
@LoadingDelegate
@SideOnly(Side.CLIENT)
public class HandlerRenderSwitch
{
	@Instance
	public static final HandlerRenderSwitch instance = new HandlerRenderSwitch();

	private static WeakReference<TickEvent.RenderTickEvent> renderTick;

	@Mod.EventHandler
	public void init(FMLPreInitializationEvent event)
	{
		Minecraft.getMinecraft().getRenderManager();
//		RenderingRegistry.registerEntityRenderingHandler();
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{
		renderTick = new WeakReference<TickEvent.RenderTickEvent>(event);
	}

	public static TickEvent.RenderTickEvent getLastRednerTick()
	{
		return renderTick.get();
	}
}
