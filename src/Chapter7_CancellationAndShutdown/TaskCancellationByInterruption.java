package Chapter7_CancellationAndShutdown;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * When we change to use BlockingQueue, there maybe some issue
 *
 */
public class TaskCancellationByInterruption {
	public class PrimeGenerator extends Thread {
		private final BlockingQueue<BigInteger> primes;
		
		public PrimeGenerator(BlockingQueue<BigInteger> primes) {
			this.primes = primes;
		}
		
		public void run() {
			BigInteger b = BigInteger.ONE;
			try {
				// Cannot use the isCancel flag here
				// Coz the producer thread may wait at the put method
				// If the consumer thread doesn't create any space, it will always wait there
				// and never check the flag
				while (!Thread.currentThread().isInterrupted()) {
					primes.put(b.nextProbablePrime());
				}
			} catch (InterruptedException e) {
				// Allow exit when interrupt in the put block waiting
			}
		}
		
		// In here, it sends the interrupt signal instead of setting the flag
		public void cancel() {
			// From Thread
			interrupt();
		}
	}
}
