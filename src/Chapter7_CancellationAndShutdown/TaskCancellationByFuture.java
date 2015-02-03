package Chapter7_CancellationAndShutdown;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskCancellationByFuture {
	public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
		Future<?> task = Executors.newCachedThreadPool().submit(r);
		try {
			task.get(timeout, unit);
		} catch (TimeoutException e) {
			// task will be cancelled below
		} catch (ExecutionException e) {
			// exception in the task; rethrow
			// pop up the exception to the caller surface
			throw launderThrowable(e.getCause());
		} finally {
			// Cancel the task in final block
			// No pain if task already completed
			// Cancel if it is still running
			task.cancel(true);
		}
	}

	private static InterruptedException launderThrowable(Throwable cause) {
		// TODO Auto-generated method stub
		return null;
	}
}
