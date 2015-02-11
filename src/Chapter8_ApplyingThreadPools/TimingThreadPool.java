package Chapter8_ApplyingThreadPools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * 
 * This is the demo to show how to extends {@link ThreadPoolExecutor}
 * and override the hooks it provides.
 *
 */
public class TimingThreadPool extends ThreadPoolExecutor {
	// Use the ThreadLocal to store the start time, so in the after hook the same thread can still see it 
	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private final Logger logger = Logger.getLogger("TimingThreadPool");
	// Number of finish task in this pool
	private final AtomicLong numTask = new AtomicLong();
	// Total time used for all the tasks
	private final AtomicLong totalTime = new AtomicLong();
	
	// You can actually ignore this constructor....
	public TimingThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		logger.fine(String.format("Thread : %s  start %s", t, r));
		startTime.set(System.nanoTime());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		try {
			long endTime = System.nanoTime();
			long runTime = endTime - startTime.get();
			numTask.incrementAndGet();
			totalTime.addAndGet(runTime);
			logger.fine(String.format("Thread %s ends %s  time:%d ns", t, r, runTime));
		} finally {
			super.afterExecute(r, t);
		}
	}

	@Override
	protected void terminated() {
		try {
			logger.fine(String.format("Terminated : avg time : %d ns", totalTime.get()/numTask.get()));
		} finally {
			super.terminated();
		}
	}
}
