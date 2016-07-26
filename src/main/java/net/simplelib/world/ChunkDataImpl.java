package net.simplelib.world;

import api.simplelib.registry.ModTileEntity;
import api.simplelib.world.chunk.ChunkData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ci010
 */
@ModTileEntity
public class ChunkDataImpl extends TileEntity implements ChunkData
{
	public ChunkDataImpl()
	{
		super();
		this.blockType = null;
	}

	@Override
	public boolean canRenderBreaking()
	{
		return false;
	}

	@Override
	public int getBlockMetadata()
	{
		return 0;
	}

	@Override
	public void markDirty()
	{
		if (this.worldObj != null)
			this.worldObj.markChunkDirty(this.pos, this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return 0D;
	}

	@Override
	public double getDistanceSq(double x, double y, double z)
	{
		return 0;
	}

	@Override
	public Block getBlockType()
	{
		return Blocks.AIR;
	}

	@Override
	public void onChunkUnload()
	{
	}

	@Override
	public void onLoad()
	{
	}

	@Override
	public void invalidate()
	{}

	@Override
	public void validate()
	{}

	public void setPos(BlockPos posIn)
	{
		this.pos = new BlockPos(posIn.getX() << 4, 0, posIn.getZ() << 4);
		if (worldObj != null)
		{
			Chunk chunk = worldObj.getChunkFromBlockCoords(pos);
			chunk.getTileEntityMap().put(pos, this);
		}
	}

	public static ChunkDataImpl getChunkData(World world, BlockPos pos)
	{
		return getChunkData(world.getChunkFromBlockCoords(pos));
	}

	public static ChunkDataImpl getChunkData(World world, ChunkPos pos)
	{
		return getChunkData(world.getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos));
	}

	public static ChunkDataImpl getChunkData(Chunk chunk)
	{
		BlockPos pos = new BlockPos(chunk.xPosition << 4, 0, chunk.zPosition << 4);
		TileEntity tileEntity = chunk.getTileEntityMap().get(pos);
		if (tileEntity == null)
			chunk.getTileEntityMap().put(pos, tileEntity = new ChunkDataImpl());
		return (ChunkDataImpl) tileEntity;
	}

	@Override
	public NBTTagCompound getCustomData()
	{
		return this.getTileData();
	}
}
