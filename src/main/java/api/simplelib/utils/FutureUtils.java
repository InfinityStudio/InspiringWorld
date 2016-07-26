package api.simplelib.utils;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

/**
 * @author ci010
 */
public class FutureUtils
{
	public static void addDoneTask(ListenableFuture future, Runnable task, Executor executor)
	{
		if (future.isDone())
			task.run();
		else
			future.addListener(task, executor);
	}
}
