package Chapter13_ExplicitLock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Chapter10_Deadlock.DynamicDeadlock;

/**
 *	This is to demo the explicit lock - ReentrantLock which implements the Lock interface
 *	Also refer to the {@link DynamicDeadlock} in Chapter10 
 *	To avoid the potential deadlock, we can also use this ReentrantLock to achieve 
 *
 *	For other method, please refer to the {@link Lock} interface
 *
 */
public class ReentrantLockDemo {
	public boolean transferMoney(final Account fromAccount, final Account toAccount, final String amount, long timeout, TimeUnit unit) 
			throws InsufficientFundsException, InterruptedException {
		long fixedDelay = getFixedDelayInNano(timeout, unit);
		long randMod = getRandomDelayModuleInNano(timeout, unit);
		long stopTime = System.nanoTime() + unit.toNanos(timeout);
		Random rnd = new Random();
		
		// Keep trying
		while (true) {
			// If get the lock tryLock will return true
			if (fromAccount.lock.tryLock()) {
				// If get the fromAccount lock
				try {
					if (toAccount.lock.tryLock()) {
						// If get the toAccount lock
						try {
							if (fromAccount.getBalance().compareTo(amount) < 0) {
								throw new InsufficientFundsException();
							} else {
								fromAccount.debit(amount);
								toAccount.credit(amount);
								return true;
							}
						} finally {
							// Release the toAccount lock in the final block
							toAccount.lock.unlock();
						}
					}
				} finally {
					// Release the fromAccount lock in the final block
					fromAccount.lock.unlock();
				}
			}
			
			if (System.nanoTime() > stopTime) {
				return false;
			}
			// Also notice here, wait for a RANDOM time to avoid the livelock
			TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextLong()%randMod);
		}
	}
	
	// Fake here
	private long getRandomDelayModuleInNano(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Fake here
	private long getFixedDelayInNano(long timeout, TimeUnit unit) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Lock stays here!!!!
	public class Account {
		// Explicit lock
		Lock lock;
		
		public Account() {
			// Instantiate with ReentrantLock
			lock = new ReentrantLock();
		}

		public String getBalance() {
			// Fake here
			return null;
		}

		public void credit(String amount) {
			// TODO Auto-generated method stub
			
		}

		public void debit(String amount) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@SuppressWarnings("serial")
	private class InsufficientFundsException extends Exception {
		
	}

	
}
