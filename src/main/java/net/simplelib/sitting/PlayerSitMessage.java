package net.simplelib.sitting;

import api.simplelib.network.AbstractServerMessage;
import api.simplelib.network.ModMessage;
import api.simplelib.network.NBTCoder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author CI010
 */

@ModMessage
public class PlayerSitMessage extends AbstractServerMessage<NBTTagCompound>
{
	public PlayerSitMessage()
	{super(new NBTCoder());}


	public PlayerSitMessage(float offset, BlockPos pos, Block block)
	{
		super(new NBTCoder());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		nbt.setFloat("offset", offset);
		nbt.setInteger("block", Block.getIdFromBlock(block));
		this.delegate.set(nbt);
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
	{
		System.out.println("inject sit message");
		int x = data.getInteger("x"), y = data.getInteger("y"), z = data.getInteger("z");
		SitHandler.sitOnBlock(player.worldObj, new BlockPos(x, y, z), player, Block.getBlockById(data
				.getInteger("block")));
		return null;
	}
}
