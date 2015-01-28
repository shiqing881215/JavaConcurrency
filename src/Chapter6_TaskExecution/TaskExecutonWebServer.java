package Chapter6_TaskExecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 
 * Using task executor both provide the concurrency and also the threads management.
 *
 */
public class TaskExecutonWebServer {
	// Limit the number of threads in the pool
	private static final int THREADS_NUM = 100;
	// Executors include bunch of factory and util methods
	private static final Executor exec = Executors.newFixedThreadPool(THREADS_NUM);  
	
	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while (true) {
			final Socket connection = socket.accept();
			Runnable task = new Runnable() {
				@Override
				public void run() {
					handleRequest(connection);
				}
			};
			
			// This will assign a thread and do the runnable task
			exec.execute(task);
		}
	}
	
	public static void handleRequest(Socket conn) {}
	
	
	/**
	 * Do not limit your thought that Executor can only do this concurrent stuff.
	 * It's a framework and it can mimic a lot of things
	 */
	// Mimic task-per-thread, no thread management/limitation
	public class TaskPerThread implements Executor {
		@Override
		public void execute(Runnable command) {
			new Thread(command).start();
		}
	}

	// Mimic single thread, don't create new thread, just use current thread
	public class SingleThread implements Executor {
		@Override
		public void execute(Runnable command) {
			command.run();
		}
	}
}
