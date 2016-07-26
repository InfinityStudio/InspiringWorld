package api.simplelib;

import api.simplelib.io.IOSystem;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.*;
import io.netty.util.internal.chmv8.ForkJoinPool;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author ci010
 */
public class TaskExecutor implements ListeningScheduledExecutorService
{
	private ListeningScheduledExecutorService service =
			MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(2));

	private static TaskExecutor instance = new TaskExecutor();

	private TaskExecutor()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static TaskExecutor instance()
	{
		return instance;
	}

	@Override
	public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {return service.schedule(command, delay, unit);}

	@Override
	public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {return service.schedule(callable, delay, unit);}

	@Override
	public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {return service.scheduleAtFixedRate(command, initialDelay, period, unit);}

	@Override
	public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {return service.scheduleWithFixedDelay(command, initialDelay, delay, unit);}

	@Override
	public void shutdown() {service.shutdown();}

	@Override
	public List<Runnable> shutdownNow() {return service.shutdownNow();}

	@Override
	public boolean isShutdown() {return service.isShutdown();}

	@Override
	public boolean isTerminated() {return service.isTerminated();}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {return service.awaitTermination(timeout, unit);}

	@Override
	public <T> ListenableFuture<T> submit(Callable<T> task) {return service.submit(task);}

	@Override
	public <T> ListenableFuture<T> submit(Runnable task, T result) {return service.submit(task, result);}

	@Override
	public ListenableFuture<?> submit(Runnable task) {return service.submit(task);}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {return service.invokeAll(tasks);}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {return service.invokeAll(tasks, timeout, unit);}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {return service.invokeAny(tasks);}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {return service.invokeAny(tasks, timeout, unit);}

	@Override
	public void execute(Runnable command) {service.execute(command);}

	public ListenableFuture<?> submitIOTask(Runnable task)
	{
		final ListenableFutureTask<?> t = ListenableFutureTask.create(task, null);
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(new IThreadedFileIO()
		{
			@Override
			public boolean writeNextIO()
			{
				t.run();
				try
				{
					t.get();
					return true;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				return false;
			}
		});
		return t;
	}

	public <T> ListenableFuture<T> submitIOTask(final Callable<T> task)
	{
		final ListenableFutureTask<T> t = ListenableFutureTask.create(task);
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(new IThreadedFileIO()
		{
			@Override
			public boolean writeNextIO()
			{
				t.run();
				try
				{
					t.get();
					return true;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				return false;
			}
		});
		return t;
	}

	@SubscribeEvent
	public void onTick(TickEvent.WorldTickEvent event)
	{
		if (!event.world.isRemote && event.phase == TickEvent.Phase.END)
		{
			for (ITickableTask tickObj : tickObjs)
			{
				tickObj.tick();
				if (tickObj.isDone())
					removeQueue.add(tickObj);
			}
			while (!removeQueue.isEmpty())
				tickObjs.remove(removeQueue.poll());
		}
	}

	public ListenableFuture<?> submitRunningTask(Runnable command, int duration)
	{
		ListenableFutureTask<Object> task = ListenableFutureTask.create(Runnables.doNothing(), null);
		tickObjs.add(new TemperateTask(duration, command, task));
		return task;
	}

	public ListenableFuture<?> scheduleOneTimeTask(Runnable command, int delayTick)
	{
		ListenableFutureTask<Object> task = ListenableFutureTask.create(command, null);
		tickObjs.add(new DelayObj(delayTick, task));
		return task;
	}

	public <V> ListenableFuture<V> scheduleOneTimeTask(Callable<V> callable, int delayTick)
	{
		ListenableFutureTask<V> task = ListenableFutureTask.create(callable);
		tickObjs.add(new DelayObj(delayTick, task));
		return task;
	}

	private Set<ITickableTask> tickObjs = Sets.newConcurrentHashSet();
	private Queue<ITickableTask> removeQueue = Lists.newLinkedList();

	private class TemperateTask implements ITickableTask
	{
		int limit, current = 0;
		Runnable runnable;
		ListenableFutureTask task;
		boolean done;

		public TemperateTask(int limit, Runnable runnable, ListenableFutureTask task)
		{
			this.limit = limit;
			this.runnable = runnable;
			this.task = task;
		}

		@Override
		public boolean isDone()
		{
			return done;
		}

		@Override
		public void tick()
		{
			current++;
			runnable.run();
			if (current == limit)
			{
				done = true;
				task.run();
			}
		}
	}

	private class DelayObj implements ITickableTask
	{
		int limit, current = 0;
		ListenableFutureTask task;
		boolean done;

		public DelayObj(int limit, ListenableFutureTask task)
		{
			this.limit = limit;
			this.task = task;
		}

		public void tick()
		{
			current++;
			if (current == limit)
			{
				done = true;
				task.run();
			}
		}

		public boolean isDone()
		{
			return done;
		}
	}
}
