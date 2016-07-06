package net.simplelib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy
{
	public Side getGameSide()
	{
		return Side.SERVER;
	}

	public Side getCallerSide()
	{
		return FMLCommonHandler.instance().getEffectiveSide();
	}

	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	public World getWorld(int dimension)
	{
		return DimensionManager.getWorld(dimension);
	}

	public String getLanguageCode(EntityPlayer player)
	{
		if (player instanceof EntityPlayerMP)
		{
			return ReflectionHelper.getPrivateValue(EntityPlayerMP.class, ((EntityPlayerMP) player), "translator");
		}
		return "en_US";
	}

	public void registerModel(Block block)
	{

	}

	public void registerModel(Item item)
	{

	}
}
