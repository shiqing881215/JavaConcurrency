package Chapter8_ApplyingThreadPools;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *	Thread starvation happens when in the thread pool
 *	you current thread is waiting for the result of other task
 *	but there is no available threads to execute other tasks 
 *
 */
public class ImplicitCoupling_ThreadStarvationDeadlock {
	// We just create a single thread in the thread pool
	ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public class RenderPageClass implements Callable<String> {
		@Override
		public String call() throws Exception {
			Future<String> header, footer;
			// Submit two other tasks in this task to get the footer and header
			header = exec.submit(new LoadFileTask("header.html"));
			footer = exec.submit(new LoadFileTask("footer.html"));
			String page = renderBody();
			// Wait for header and footer come back and return
			// But this will get the DEADLOCK!
			// Cause no thread will be available to execute the header and footer task
			return header.get() + page + footer.get();
		}

		// Render page body
		private String renderBody() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	// Task standing for load a local html template file
	public class LoadFileTask implements Callable<String> {
		public LoadFileTask(String string) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
