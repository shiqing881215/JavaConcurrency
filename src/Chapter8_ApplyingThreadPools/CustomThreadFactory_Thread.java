package Chapter8_ApplyingThreadPools;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomThreadFactory_Thread extends Thread {
	private static final String DEFAULT_NAME = "MyThread";
	private static volatile boolean debugeLifecycle = false;
	private static final AtomicInteger created = new AtomicInteger();
	private static final AtomicInteger alive = new AtomicInteger();
	private static final Logger logger = Logger.getAnonymousLogger();
	
	public CustomThreadFactory_Thread(Runnable r) {
		this(r, DEFAULT_NAME);
	}

	public CustomThreadFactory_Thread(Runnable r, String name) {
		// Override name and created increase
		super(r, name + "-" + created.incrementAndGet());
		// Override uncaught exception handler
		setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.log(Level.SEVERE, t.getName(), e);
			}
		});
	}

	@Override
	public void run() {
		// copy value to local variable to make sure it keeps consistent all through the method
		boolean debug = debugeLifecycle;
		if (debug) logger.log(Level.FINE, "Enterring : " + getName());
		try {
			// Alive increase
			alive.incrementAndGet();
			super.run();
		} finally {
			// Alive decrease
			alive.decrementAndGet();
			if (debug) logger.log(Level.FINE, "Exiting : " + getName());
		}
	}
	
	public int getThreadsCreated() {
		return created.get();
	}
	
	public int getThreadsAlive() {
		return alive.get();
	}
	
	public boolean getDebug() {
		return debugeLifecycle;
	}
	
	public static void setDebug(boolean debug) {
		debugeLifecycle = debug;
	}
}
