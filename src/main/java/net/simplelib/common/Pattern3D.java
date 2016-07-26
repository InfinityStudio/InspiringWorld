package net.simplelib.common;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * The implementation of Pattern3D into BlockPos.
 *
 * @author ci010
 */
public class Pattern3D
{
	protected List<Vector3D> sub;

	/**
	 * @return if they list of block is connected.
	 */
	public boolean connected()
	{
		return this.connected(sub, Lists.<Vector3D>newArrayList(), sub.get(0), 1, 1);
	}

	private boolean connected(List<Vector3D> points, List<Vector3D> done, Vector3D current, int direction, int
			original)
	{
		if (!done.contains(current))
			done.add(current);
		Vector3D next = null;
		switch (direction)
		{
			case 1:
				next = current.offset(0, 0, 1);
				break;
			case 2:
				next = current.offset(1, 0, 0);
				break;
			case 3:
				next = current.offset(0, 0, -1);
				break;
			case 4:
				next = current.offset(-1, 0, 0);
				break;
			case 5:
				next = current.offset(0, 1, 0);
				break;
			case 6:
				next = current.offset(0, -1, 0);
		}
		if (points.contains(next) && !done.contains(next))
			this.connected(points, done, next, direction, direction);
		int nextDirection = (direction + 1) % 6;
		if ((nextDirection = nextDirection == 0 ? 6 : nextDirection) == original)
			return true;
		this.connected(points, done, current, nextDirection, original);
		return done.size() == points.size();
	}

	public List<Vector3D> buildConnection(Vector3D start, Predicate<Vector3D> predicate)
	{
		List<Vector3D> lst = Lists.newArrayListWithCapacity(1000000);
		this.buildConnection(lst, start, predicate, 1, 1);
		return lst;
	}

	private void buildConnection(List<Vector3D> lst, Vector3D current, Predicate<Vector3D>
			predicate, int direction, int original)
	{
		if (lst.size() > 1000000)
			return;
		if (!predicate.apply(current))
			return;
		if (!lst.contains(current))
			lst.add(current);
		Vector3D next = null;
		switch (direction)
		{
			case 1:
				next = current.offset(0, 0, 1);
				break;
			case 2:
				next = current.offset(1, 0, 0);
				break;
			case 3:
				next = current.offset(0, 0, -1);
				break;
			case 4:
				next = current.offset(-1, 0, 0);
				break;
			case 5:
				next = current.offset(0, 1, 0);
				break;
			case 6:
				next = current.offset(0, -1, 0);
		}
		if (!lst.contains(next))
			this.buildConnection(lst, next, predicate, direction, direction);
		int nextDirection = (direction + 1) % 6;
		if ((nextDirection = (nextDirection == 0 ? 6 : nextDirection)) == original)
			return;
		this.buildConnection(lst, current, predicate, nextDirection, original);
	}

	public Pattern3D(List<Vector3D> pos)
	{
		this.sub = pos;
		Collections.sort(this.sub);
		if (!this.connected())
			throw new IllegalArgumentException("The pattern is not continued!");
	}

	/**
	 * Transfer the pattern to a specific coordination.
	 *
	 * @param origin The block position will be transfer to.
	 * @return The actual block positions.
	 */
	public List<Vector3D> transferTo(Vector3D origin)
	{
		List<Vector3D> result = Lists.newArrayList();
		for (Vector3D pos : sub)
			result.add(pos.offset(origin.getX(), origin.getY(), origin.getZ()));
		return result;
	}

	public interface Vector3D extends Comparable<Vector3D>
	{
		int getX();

		int getY();

		int getZ();

		/**
		 * Get a new vector that has the offset to current vector.
		 *
		 * @param x The offset in x coordination.
		 * @param y The offset in y coordination.
		 * @param z The offset in z coordination.
		 * @return new vector that has a certain offset to current vector.
		 */
		Vector3D offset(int x, int y, int z);
	}
}
