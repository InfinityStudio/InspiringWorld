package net.simplelib.world.region;

import api.simplelib.capabilities.Capabilities;
import api.simplelib.seril.NBTBases;
import api.simplelib.utils.WorldUtils;
import api.simplelib.seril.NBTTagBuilder;
import api.simplelib.world.region.Region;
import api.simplelib.world.region.RegionEvent;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Set;

/**
 * @author ci010
 */
public class RegionImpl implements Region
{
	private RegionManagerImpl manager;
	private final String id;

	private Area area;
	private int height = 50, depth = 30;

	private String name;

	private CapabilityDispatcher dispatcher;
	private Set<ChunkPos> occupyChunk = Sets.newHashSet();

	public RegionImpl(String id, RegionManagerImpl manager)
	{
		this.id = id;
		this.manager = manager;
		this.area = new Area();
		this.dispatcher = Capabilities.gatherCapabilities(new RegionEvent.AttachCapability(this));
	}


	@Override
	public boolean addRegion(int x1, int z1, int x2, int z2)
	{
		Rectangle rectangle = newRectangle(x1, z1, x2, z2);
		List<Chunk> chunks = WorldUtils.getAllLoadingChunks(manager.getWorld(), rectangle);
		if (manager.onRegionExpand(this, chunks, rectangle))
		{
			this.area.add(new Area(rectangle));
			for (Chunk chunk : chunks)
				occupyChunk.add(WorldUtils.getAsChunkCoord(chunk));
			return true;
		}
		return false;
	}

	@Override
	public Rectangle newRectangle(int x1, int z1, int x2, int z2)
	{
		int x = Math.min(x1, x2), z = Math.min(z1, z2), xSize = Math.abs(x1 - x2), zSize = Math.abs(z1 - z2);
		return new Rectangle(x, z, xSize, zSize);
	}

	@Override
	public boolean intersect(Rectangle2D rectangle2D)
	{
		if (!this.area.intersects(rectangle2D))
			return false;
		PathIterator itr = rectangle2D.getPathIterator(null);
		double[] container = new double[6];
		while (!itr.isDone())
		{
			int i = itr.currentSegment(container);
			if (i == PathIterator.SEG_LINETO || i == PathIterator.SEG_MOVETO)
			{
				if (this.area.contains(container[0], container[1]))
					return true;
			}
		}
		return false;
	}

	@Override
	public void removeAll()
	{
		Rectangle bounds = this.area.getBounds();
		area.reset();
		List<Chunk> chunks = WorldUtils.getAllLoadingChunks(manager.getWorld(), bounds);
		manager.onRegionSubtract(this, chunks);
		this.occupyChunk.clear();
	}

	@Override
	public void removeRegion(int x1, int z1, int x2, int z2)
	{
		Rectangle re = newRectangle(x1, z1, x2, z2);
		area.subtract(new Area(re));
		List<Chunk> chunks = WorldUtils.getAllLoadingChunks(manager.getWorld(), re);
		manager.onRegionSubtract(this, chunks);
		for (Chunk chunk : chunks)
			this.occupyChunk.remove(WorldUtils.getAsChunkCoord(chunk));
	}

	@Override
	public boolean include(int x, int z)
	{
		return area.contains(x, z);
	}

	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public PathIterator pathIterator()
	{
		return area.getPathIterator(null);
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public int getDepth()
	{
		return depth;
	}

	@Override
	public void setHeight(int height)
	{
		this.height = height;
	}

	@Override
	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	public RegionImpl setName(String nameKey)
	{
		this.name = nameKey;
		return this;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return dispatcher != null && dispatcher.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return dispatcher == null ? null : dispatcher.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagBuilder build = NBTTagBuilder.create()
				.addString("name", name)
				.addTag("area", RegionInternal.toNBT(area))
				.addTag("capabilities", dispatcher.serializeNBT());
		NBTTagList lst = new NBTTagList();
		for (ChunkPos ChunkPos : this.occupyChunk)
			lst.appendTag(new NBTTagIntArray(new int[]{ChunkPos.chunkXPos, ChunkPos.chunkZPos}));
		return build.addTag("chunks", lst).build();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		name = nbt.getString("name");
		area = RegionInternal.fromNBT(nbt.getCompoundTag("area"));
		dispatcher.deserializeNBT(nbt.getCompoundTag("capabilities"));
		NBTTagList chunks = nbt.getTagList("chunks", NBTBases.INT_ARRAY);
		for (int i = 0; i < chunks.tagCount(); i++)
		{
			int[] arr = chunks.getIntArrayAt(i);
			this.occupyChunk.add(new ChunkPos(arr[0], arr[1]));
		}
	}

	//	class HeightBase implements Height
//	{
//		Table<Integer, Integer, RangeBase> rangeBaseTable = HashBasedTable.create();
//		int low, high;
//
//		@Override
//		public int getLowest()
//		{
//			return low;
//		}
//
//		@Override
//		public int getHighest()
//		{
//			return high;
//		}
//
//		@Override
//		public void set(RangeBase rangeBase, RangeBase rangeBaseX, RangeBase rangeBaseZ)
//		{
//			if (low > rangeBase.getLow())
//				low = rangeBase.getLow();
//
//		}
//
//		@Override
//		public RangeBase get(int x, int z)
//		{
//			return null;
//		}
//	}
//
//	interface Height
//	{
//		Height Default = new Height()
//		{
//			RangeBase r = new RangeBase(40, 80);
//
//			@Override
//			public int getLowest()
//			{
//				return 80;
//			}
//
//			@Override
//			public int getHighest()
//			{
//				return 40;
//			}
//
//			@Override
//			public void set(RangeBase rangeBase, RangeBase rangeBaseX, RangeBase rangeBaseZ)
//			{
//			}
//
//			@Override
//			public RangeBase get(int x, int z)
//			{
//				return r;
//			}
//		};
//
//		int getLowest();
//
//		int getHighest();
//
//		void set(RangeBase rangeBase, RangeBase rangeBaseX, RangeBase rangeBaseZ);
//
//		RangeBase get(int x, int z);
//	}
}
