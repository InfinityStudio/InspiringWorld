package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.collect.Lists;

import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * @author ci010
 */
public class GridLayoutBuilder
{
	private int offset, numberOfElements;
	private List<Vector2f> lst = Lists.newArrayList();
	private Vector2f iconSize, containerSize, realCompSize;
	private boolean changed;
	private static String name = "Grid";

	public GridLayoutBuilder(Vector2f containerSize, Vector2f iconSize, int leastOffset, int numberOfElements)
	{
		this.setContainerSize(containerSize).setIconSize(iconSize).setOffset(leastOffset).setNumberOfElements
				(numberOfElements);
		this.make();
	}

	public GridLayoutBuilder setOffset(int offset)
	{
		this.offset = offset;
		this.changed = true;
		return this;
	}

	public GridLayoutBuilder setNumberOfElements(int numberOfElements)
	{
		this.numberOfElements = numberOfElements;
		this.changed = true;
		return this;
	}

	public GridLayoutBuilder setIconSize(Vector2f iconSize)
	{
		this.iconSize = iconSize;
		this.changed = true;
		return this;
	}

	public GridLayoutBuilder setContainerSize(Vector2f containerSize)
	{
		this.containerSize = containerSize;
		this.changed = true;
		return this;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof GridLayoutBuilder)
		{
			GridLayoutBuilder o = (GridLayoutBuilder) obj;
			return o.offset == this.offset && o.iconSize.equals(this.iconSize)
					&& o.numberOfElements == this.numberOfElements && o.containerSize.equals(this.containerSize);
		}
		return super.equals(obj);
	}

	private void make()
	{
		this.changed = false;
		lst.clear();
		this.realCompSize = new Vector2f(iconSize.x + offset, iconSize.y + offset);
		int line = 1, countInOneLine = numberOfElements / line, perLength = (int) (containerSize.x / countInOneLine);
		while (perLength < (iconSize.x + offset))
		{
			++line;
			countInOneLine = numberOfElements / line;
			perLength = (int) (containerSize.x / countInOneLine);
		}
		int remainder = numberOfElements % line;
		float leftX = this.calLeftX(countInOneLine);
		for (int i = 1; i <= line; ++i)
			for (int j = 1; j <= countInOneLine; ++j)
				lst.add(new Vector2f(leftX + j * realCompSize.x, offset + i * realCompSize.y));
		leftX = this.calLeftX(remainder);
		for (int i = 1; i <= remainder; ++i)
			lst.add(new Vector2f(leftX + i * realCompSize.x, offset + (line + 1) * realCompSize.y));
	}

	public CompiledLayout build()
	{
		if (changed)
			this.make();
		return new CompiledLayout()
		{
			private Vector2f[] pos;

			{
				pos = new Vector2f[lst.size()];
				pos = lst.toArray(pos);
			}

			@Override
			public Vector2f getPos(int i)
			{
				return lst.get(i);
			}

			@Override
			public String getName()
			{
				return name;
			}
		};
	}

	private float calLeftX(int numInOneLine)
	{
		float center = containerSize.x / 2;
		if (numInOneLine % 2 == 1)
			return (center - iconSize.x / 2) - numInOneLine / 2 * realCompSize.x;
		else
			return (center - offset / 2 - iconSize.x) - (numInOneLine / 2 - 1) * realCompSize.x;
	}
}
