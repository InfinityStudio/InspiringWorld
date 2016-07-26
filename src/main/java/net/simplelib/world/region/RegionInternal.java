package net.simplelib.world.region;

import api.simplelib.seril.NBTBases;
import api.simplelib.seril.NBTTagBuilder;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * @author ci010
 */
class RegionInternal
{
	static
	{
		CapabilityManager.INSTANCE.register(RegionManagerImpl.class, new Capability.IStorage<RegionManagerImpl>()
		{
			@Override
			public NBTBase writeNBT(Capability<RegionManagerImpl> capability, RegionManagerImpl instance, EnumFacing side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<RegionManagerImpl> capability, RegionManagerImpl instance, EnumFacing side, NBTBase nbt)
			{

			}
		}, new Callable<RegionManagerImpl>()
		{
			@Override
			public RegionManagerImpl call() throws Exception
			{
				return null;
			}
		});
	}

	static class ShapeDummy implements Shape, PathIterator
	{
		int rule;

		public ShapeDummy(int rule, LinkedList<Token> tokenList)
		{
			this.rule = rule;
			this.tokenList = tokenList;
		}

		public ShapeDummy() {}

		@Override
		public Rectangle getBounds() {return null;}

		@Override
		public Rectangle2D getBounds2D() {return null;}

		@Override
		public boolean contains(double x, double y) {return false;}

		@Override
		public boolean contains(Point2D p) {return false;}

		@Override
		public boolean intersects(double x, double y, double w, double h) {return false;}

		@Override
		public boolean intersects(Rectangle2D r) {return false;}

		@Override
		public boolean contains(double x, double y, double w, double h) {return false;}

		@Override
		public boolean contains(Rectangle2D r) {return false;}

		@Override
		public PathIterator getPathIterator(AffineTransform at) {return this;}

		@Override
		public PathIterator getPathIterator(AffineTransform at, double flatness) {return this;}

		@Override
		public int getWindingRule() {return rule;}

		LinkedList<Token> tokenList;
		Token current;

		@Override
		public boolean isDone()
		{
			return !tokenList.isEmpty();
		}

		@Override
		public void next()
		{
			current = tokenList.pop();
		}

		@Override
		public int currentSegment(float[] coords)
		{
			for (int i = 0; i < current.data.length; i++)
				coords[i] = current.data[i];
			return current.type;
		}

		@Override
		public int currentSegment(double[] coords)
		{
			for (int i = 0; i < current.data.length; i++)
				coords[i] = current.data[i];
			return current.type;
		}
	}

	static class Token
	{
		int type;
		int[] data;

		Token(int type, int[] data)
		{
			this.type = type;
			this.data = data;
		}
	}

	static Area fromNBT(NBTTagCompound compound)
	{
		NBTTagList itr = compound.getTagList("itr", NBTBases.COMPOUND);
		int rule = compound.getInteger("type");
		LinkedList<Token> linkedList = new LinkedList<Token>();
		for (int i = 0; i < itr.tagCount(); i++)
		{
			NBTTagCompound tag = itr.getCompoundTagAt(i);
			int segType = tag.getInteger("type");
			int[] datas = tag.getIntArray("data");
			linkedList.add(new Token(segType, datas));
		}
		return new Area(new ShapeDummy(rule, linkedList));
	}

	static NBTTagCompound toNBT(Area area)
	{
		NBTTagBuilder builder = NBTTagBuilder.create();
		PathIterator iterator = area.getPathIterator(null);
		builder.addInt("type", iterator.getWindingRule());
		float[] data = new float[6];
		while (!iterator.isDone())
		{
			int[] ints = new int[6];
			int i = iterator.currentSegment(data);
			for (int j = 0; j < data.length; j++)
				ints[j] = (int) data[j];
			builder.addInt("type", i);
			builder.addIntArray("data", ints);
		}
		return builder.build();
	}
}
