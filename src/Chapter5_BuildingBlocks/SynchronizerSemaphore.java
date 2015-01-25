package Chapter5_BuildingBlocks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 *	Semaphore is used to control the access to a resource at the same time.
 *	It manages a set of virtual permits, initial number of permit is set in constructor.
 *	Activities can request permit (if none block) and return it when they are done. 
 *
 *	Good places to use : database connection pool, add bound for collection
 *
 *	Binary semaphore, which is initialized with 1, can be used as mutex.
 */
public class SynchronizerSemaphore {
	public class BoundedHashSet<T> {
		private final Set<T> set;
		private final Semaphore sem;
		
		public BoundedHashSet(int bound) {
			set = Collections.synchronizedSet(new HashSet<T>());
			// Initialize the semaphore number with bound
			sem = new Semaphore(bound);
		}
		
		public boolean add(T t) throws InterruptedException {
			// Acquire a permit, block if none left
			sem.acquire();
			boolean isSuccess = false;
			try {
				isSuccess = set.add(t);
				// Return success or fail
				return isSuccess;
			} finally {
				// do final check to make sure if not insert successfully
				// we return the permit
				if (!isSuccess) {
					sem.release();
				}
			}
		}
		
		public boolean remove(Object o) {
			boolean isSuccess = set.remove(o);
			if (isSuccess) {
				// Only success then release
				sem.release();
			}
			return isSuccess;
		}
	}
}
