package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ci010
 */
public class TrivialThread
{
	private static ExecutorService workingGroup = Executors.newCachedThreadPool();

	public static ExecutorService getExecutor()
	{
		return workingGroup;
	}

	public void newTask(final Callable<?> task, final int time)
	{
		final ListenableFutureTask<?> futureTask = ListenableFutureTask.create(task);
		Futures.addCallback(futureTask, new FutureCallback<Object>()
		{
			int current;

			@Override
			public void onSuccess(@Nullable Object result)
			{}

			@Override
			public void onFailure(@ParametersAreNonnullByDefault Throwable t)
			{
				if (++current < time)
					workingGroup.submit(futureTask);
			}
		}, workingGroup);
		workingGroup.submit(futureTask);
	}
}
