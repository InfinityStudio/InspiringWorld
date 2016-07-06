package net.simplelib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.simplelib.CommonProxy;

public class ClientProxy extends CommonProxy
{
	@Override
	public String getLanguageCode(EntityPlayer player)
	{
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public World getWorld(int dimension)
	{
		return Minecraft.getMinecraft().theWorld.provider.getDimension() == dimension ?
				Minecraft.getMinecraft().theWorld : null;
	}

	@Override
	public Side getGameSide()
	{
		return Side.CLIENT;
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}


}
