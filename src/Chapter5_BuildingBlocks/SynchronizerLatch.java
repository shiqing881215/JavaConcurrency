package Chapter5_BuildingBlocks;

import java.util.concurrent.CountDownLatch;

/**
 *	Latch plays like a gate block all the threads.
 *	When the latch reaches its terminal state, all the thread can pass.
 *   
 *  {@link CountDownLatch} is initialized with a positive value. And the await() 
 *  method will block the thread. CountDown() method minus the state, and when it reaches
 *  0, which is the terminal state. All threads can pass. 
 *
 */
public class SynchronizerLatch {
	public long caculateExecutionTime (int threadNumber, final Runnable longTimeTask) throws InterruptedException {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(threadNumber);
		
		for (int i = 0; i < threadNumber; i++) {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						// No thread can start execute the task, they all await here
						startGate.await();
						try {
							longTimeTask.run();
						} finally {
							// When task is done, count down 1 on the endGate
							endGate.countDown();
						}
					} catch (InterruptedException e) { }
				}
			};
			// It will not start immediately until the line 40 is called
			t.start();
		}
		
		long start = System.currentTimeMillis();
		startGate.countDown();
		// Await here until all the threads finish the job 
		endGate.await();
		long end = System.currentTimeMillis();
		
		return end-start;
	}
}
