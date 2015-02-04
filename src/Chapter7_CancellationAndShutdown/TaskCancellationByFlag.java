package Chapter7_CancellationAndShutdown;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import Annotations.Annotation.GuardeBy;
import Annotations.Annotation.ThreadSafe;

/**
 * 
 * One way to do the cancellation is to use some flag.
 * Like the isCancelled here 
 *
 */
public class TaskCancellationByFlag {
	@ThreadSafe(description = "Protect by intrinsic lock")
	public class PrimeGenerator implements Runnable {
		@GuardeBy(lockObject = "this")
		private final List<BigInteger> primes = new ArrayList<BigInteger>();
		// Use volatile here to make it visible to other threads as flag
		private volatile boolean isCancelled;
		
		@Override
		public void run() {
			BigInteger b = BigInteger.ONE;
			while (!isCancelled) {
				b = b.nextProbablePrime();
				synchronized (this) {
					primes.add(b);
				}
			}
		}
		
		public void cancel() {
			isCancelled = true;
		}
		
		public synchronized List<BigInteger> getPrimes() {
			return primes;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		PrimeGenerator pg = new TaskCancellationByFlag().new PrimeGenerator();
		new Thread(pg).start();
		
		try {
			Thread.sleep(1000);
		} finally {
			pg.cancel();
		}
		
		// I try it on my local mac and it reaches to 97771 in one second
		System.out.println(pg.getPrimes());
	}
	
}
