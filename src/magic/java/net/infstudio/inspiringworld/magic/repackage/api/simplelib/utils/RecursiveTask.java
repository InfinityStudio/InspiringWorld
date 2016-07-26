package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import io.netty.util.internal.chmv8.ForkJoinTask;

/**
 * Just try to port to java 6....
 * @author ci010
 */
public abstract class RecursiveTask<V> extends ForkJoinTask<V>
{
	private static final long serialVersionUID = 5232453952276485270L;

	/**
	 * The result of the computation.
	 */
	V result;

	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	protected abstract V compute();

	public final V getRawResult()
	{
		return result;
	}

	protected final void setRawResult(V value)
	{
		result = value;
	}

	/**
	 * Implements execution conventions for RecursiveTask.
	 */
	protected final boolean exec()
	{
		result = compute();
		return true;
	}

}
