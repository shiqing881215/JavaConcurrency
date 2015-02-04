package Chapter7_CancellationAndShutdown;

import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 
 * {@link ExecutorService} provides with shutdown() and shutdownNow() methods.
 * We can make use of those to achieve our shut down goal.
 * 
 * But instead of letting the application own the ExecutorService, it's good to have
 * our own service wrap around the ExecutorService and so that way we can have our own life-cycle. 
 *
 */
public class ServiceBasedThreadsCancellationExecutorService {
	public class LogService {
		private final ExecutorService exec = Executors.newSingleThreadExecutor();
		private final PrintWriter writer;
		
		public LogService(PrintWriter writer) {
			this.writer = writer;
		}
		
		public void start() {
			//...
		}
		
		public void stop() throws InterruptedException {
			try {
				exec.shutdown();
				exec.awaitTermination(1000, TimeUnit.MILLISECONDS);
			} finally {
				writer.close();
			}
		}
		
		public void log(String msg) {
			try {
				exec.execute(new LogTask(msg));
			} catch (RejectedExecutionException ignored) {}
		}
		
		public class LogTask implements Runnable {
			public LogTask(String msg) {
				//...
			}
			
			@Override
			public void run() {
				// Log things here
			}
		}
	}
}
