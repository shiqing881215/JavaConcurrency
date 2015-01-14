package Chapter2_ThreadSafe;

/**
 *	Intrinsic lock has the Reentrency feature
 *	which means same thread can access the same object lock which it already holds before
 *	for multiple times. 
 *	This brings us a lot of benefit.
 *	
 */
public class Reentrancy {
	/**
	 * Consider if we don't have the reentrency here.
	 * Thread A get the lock of an widget object and call the doSomething method.
	 * When it reaches to the super.doSomething(), it will not allow to get the same lock again.
	 * It must wait the lock to release, but this lock is hold by thread A and it will never finish.
	 * So this will cause the deadlock. 
	 * 
	 * Reentrency makes this work.
	 */
	public class Widget {
		public synchronized void doSomething() {}
	}
	
	public class LoggingWidget extends Widget {
		@Override
		public synchronized void doSomething() {
			// do something special
			
			super.doSomething();
		}
	}
}
