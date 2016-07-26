package net.infstudio.inspiringworld.magic.repackage.api.simplelib.world.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

/**
 * @author ci010
 */
public interface Region extends ICapabilitySerializable<NBTTagCompound>
{
	boolean addRegion(int x1, int z1, int x2, int z2);

	Rectangle newRectangle(int x1, int z1, int x2, int z2);

	boolean intersect(Rectangle2D rectangle2D);

	void removeAll();

	void removeRegion(int x1, int z1, int x2, int z2);

	boolean include(int x, int z);

	String getId();

	String getName();

	PathIterator pathIterator();

	int getHeight();

	int getDepth();

	void setHeight(int height);

	void setDepth(int depth);

}
