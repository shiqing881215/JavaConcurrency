package Chapter5_BuildingBlocks;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 
 * @author shiqing
 * 
 * FutureTask is similar as Latch.
 * It performs as a get until all the job is done and then you get the answer.
 * 
 * Something about Callable : 
 * A task that returns a result and may throw an exception. 
 * Implementors define a single method with no arguments called call.
 * The Callable interface is similar to Runnable, 
 * in that both are designed for classes whose instances are potentially executed by another thread. 
 * A Runnable, however, does not return a result and cannot throw a checked exception.
 *
 */
public class SynchronizerFutureTask {
	// We will create an inside class here called PreLoader
	// which can preload some heavy task ahead and get the data back when the task is done
	// Otherwise just wait for the task to be done.
	public class PreLoader {
		private FutureTask<ProductInfo> future = 
				new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
					@Override
					public ProductInfo call() throws DataLoadException {
						return loadProductInfo();
					}
				});
		
		private Thread thread = new Thread(future);
		
		// This is the method we should call ahead to "Preload" the task
		// which means letting the task to run ahead of time
		public void start() { 
			// This will call the future and then call the callable to call the call() method
			thread.start(); 
		}
		
		// This is the method you should call when you want the result
		// It may block or get the result back immediately based on whether the job is done or not
		public ProductInfo get() throws InterruptedException, DataLoadException {
			try {
				// This is the based on whehter the job is done or not
				return future.get();
			} catch (ExecutionException e) {
				Throwable cause = e.getCause();
				if (cause instanceof DataLoadException) {
					throw (DataLoadException) cause;
				} else {
					throw launderThrowable(cause);
				}
			}
		}
		
		private RuntimeException launderThrowable(Throwable cause) {
			if (cause instanceof RuntimeException) {
				return (RuntimeException) cause;
			} else if (cause instanceof Error) {
				throw (Error) cause;
			} else { 
				throw new IllegalStateException("Not checked", cause); 
			}
		}
		
	}
	
	public class ProductInfo {
		// some info here
	}
	
	@SuppressWarnings("serial")
	public class DataLoadException extends Exception {
		
	}
	
	public ProductInfo loadProductInfo() throws DataLoadException {
		// do some heavy job here
		// just return null here for simplicity
		return null; 
	}
	
	// Sample use
	public static void main(String[] args) throws InterruptedException, DataLoadException {
		PreLoader preloader = new SynchronizerFutureTask().new PreLoader();
		// start the heavy work ahead
		preloader.start();
		/*
		 * Do something else here
		 */
		// Then go back the and check the result, this may block or not
		preloader.get();
	}
} 
