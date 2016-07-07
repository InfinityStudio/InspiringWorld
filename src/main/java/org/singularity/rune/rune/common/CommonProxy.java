package org.singularity.rune.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Darkhighness on 2016/7/6.
 */
public class CommonProxy extends net.simplelib.CommonProxy
{
	public CommonProxy()
	{
	}

	@Override
	public Side getGameSide()
	{
		return super.getGameSide();
	}

	@Override
	public Side getCallerSide()
	{
		return super.getCallerSide();
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return super.getClientPlayer();
	}

	@Override
	public World getWorld(int dimension)
	{
		return super.getWorld(dimension);
	}

	@Override
	public void registerModel(Block block)
	{
		super.registerModel(block);
	}

	@Override
	public String getLanguageCode(EntityPlayer player)
	{
		return super.getLanguageCode(player);
	}

	@Override
	public void registerModel(Item item)
	{
		super.registerModel(item);
	}
}
