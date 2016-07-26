package api.simplelib.utils;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;

/**
 * @author ci010
 */
public class WorldUtils
{
	public static Comparator<BlockPos> getComparator(EnumFacing.Axis axis)
	{
		switch (axis)
		{
			case X:
				return new Comparator<BlockPos>()
				{
					@Override
					public int compare(BlockPos o1, BlockPos o2)
					{
						return o1.getX() - o2.getX();
					}
				};
			default:
			case Y:
				return new Comparator<BlockPos>()
				{
					@Override
					public int compare(BlockPos o1, BlockPos o2)
					{
						return o1.getY() - o2.getY();
					}
				};
			case Z:
				return new Comparator<BlockPos>()
				{
					@Override
					public int compare(BlockPos o1, BlockPos o2)
					{
						return o1.getZ() - o2.getZ();
					}
				};
		}
	}

	/**
	 * author Mickey
	 */
	public static AxisAlignedBB getAABBFromEntity(Entity entity, double r)
	{
		return new AxisAlignedBB(entity.posX - r, entity.posY - r, entity.posZ - r, entity.posX + r, entity.posY + r,
				entity.posZ + r);
	}

	public static ChunkPos getAsChunkCoord(int x, int z)
	{
		return new ChunkPos(x >> 4, z >> 4);
	}

	public static ChunkPos getAsChunkCoord(BlockPos pos)
	{
		return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
	}

	public static ChunkPos getAsChunkCoord(Chunk chunk)
	{
		return new ChunkPos(chunk.xPosition, chunk.zPosition);
	}

	public static List<Chunk> getAllLoadingChunks(World world, Rectangle2D area)
	{
		List<Chunk> chunks = Lists.newArrayList();
		int minX = (int) area.getMinX() >> 4, maxX = (int) area.getMaxX() >> 4,
				minY = (int) area.getMinY() >> 4, maxY = (int) area.getMaxY() >> 4;
		for (int i = minX; i <= maxX; i++)
			for (int j = minY; j < maxY; j++)
			{
				IChunkProvider provider = world.getChunkProvider();
				Chunk chunk = provider.provideChunk(i, j);
				if (chunk != null)
					chunks.add(chunk);
			}
		return chunks;
	}

	public static List<ChunkPos> getAllLoadingChunkPos(World world, Rectangle2D area)
	{
		List<ChunkPos> chunks = Lists.newArrayList();
		int minX = (int) area.getMinX() >> 4, maxX = (int) area.getMaxX() >> 4,
				minY = (int) area.getMinY() >> 4, maxY = (int) area.getMaxY() >> 4;
		for (int i = minX; i <= maxX; i++)
			for (int j = minY; j < maxY; j++)
			{
				IChunkProvider provider = world.getChunkProvider();
				if (provider.getLoadedChunk(i, j) != null)
					chunks.add(new ChunkPos(i, j));
			}
		return chunks;
	}
}
