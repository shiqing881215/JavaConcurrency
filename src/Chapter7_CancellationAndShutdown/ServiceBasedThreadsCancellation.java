package Chapter7_CancellationAndShutdown;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import Annotations.Annotation.GuardeBy;

/**
 * 
 * Demo for how to deal with a service with multiple threads combined with it.
 * 
 * Consider the log line in the application. PrintWriter is thread-safe and easy to insert
 * into the code you want to log, but the bad thing is this affect the performance.
 * 
 * So we come out with a logService which has another thread gathering the log request from app,
 * then write to the output. So this is a multiple producers and single consumer model.
 * 
 * Plan to use BlockingQueue, several things need to consider :
 * 1) When we want to stop the service, we send the interrupt signal to the log thread
 * 2) If we already send the interrupt signal to log service, no matter it's shutting down,
 * 	  already shut down or not yet. We don't allow any other request to submit new log request.
 * 	  To achieve this, we set the isShutDown flag.
 * 3) If there is some task in the queue, when we receive the shutdown signal, we want to process
 * 	  those before we go to shut down. 
 * 	  To achieve this, we use reservation to remember how taks are left in the queue
 *
 */
public class ServiceBasedThreadsCancellation {
	public class LogService {
		private final BlockingQueue<String> queue;
		private final LoggerThread loggerThread;
		private final PrintWriter writer;
		@GuardeBy(lockObject = "this")
		private boolean isShutDown;
		@GuardeBy(lockObject = "this")
		private int reservation;
		
		public LogService(BlockingQueue<String> queue, LoggerThread loggerThread, PrintWriter writer) {
			this.queue = queue;
			this.loggerThread = loggerThread;
			this.writer = writer;
		}
		
		// Called by logService
		public void start() {
			loggerThread.start();
		}
		
		// Called by logService
		public void stop() {
			// set the shutdown flag first in the synchronized block 
			synchronized (this) {
				isShutDown = true;
			}
			// send the interrupt signal 
			loggerThread.interrupt();
		}
		
		// Called by any threads need logging
		public void log(String msg) throws InterruptedException {
			synchronized (this) {
				// if already shut down, no longer allow submit task
				if (isShutDown) {
					throw new IllegalStateException();
				}
				// if not shutdown. update the reservation number also in the synchronized block
				reservation++;
			}
			queue.put(msg);
		}
		
		public class LoggerThread extends Thread {
			// Log the message
			public void run() {
				try {
					while (true) {
						try {
							synchronized (LogService.this) {
								// If shut down and no left task in the queue
								// All is done and we should break the while loop
								if (isShutDown && reservation == 0) {
									break;
								}
							}
							String message = queue.take();
							synchronized (LogService.this) {
								// update left task number
								reservation--;
							}
							writer.println(message);
						} catch (InterruptedException ignored) {
							// just ignore in this while loop, keep going
							// coz we already set the isShutdonw boolean
							// the only time we will step out is break line
						}
					}
				} finally {
					writer.close();
				}
			}
		}
	}
}
