package Chapter14_BuildingCustomSynchronizers;

public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
	// Condition Predicate : not-full  !isFull()
	// Condition Predicate : not-empty  !isEmpty()

	public BoundedBuffer(int capacity) {
		super(capacity);
	}

	// BLOCK until not full
	// Because the condition predicate, which is isFull() here, contains the state variable (count)
	// and this state variable is guarded by the lock which is condition queue object
	// You must hold the same lock of the condition queue object, which is BoundedBuffer here, before 
	// testing the condition predicate
	public synchronized void put(V v) throws InterruptedException {
		// Cause there is some defect in the wait
		// It's not guaranteed that the condition predicate is true when you get back from wait
		// So put in the while loop to test again the condition predicate when you back from wait
		while (isFull()) {
			// If enter here, it will release the lock and blocks the current thread
			// until the current thread is interrupted, timeout or waken up by notification
			// then it reacquire the lock before returning
			wait();
		}
		// So no matter in which situation, when we reach here, we should have the lock to do this operation
		doPut(v);
		// Notify other threads waiting on this buffer
		// This will also release the lock so other threads waiting on the queue and whose
		// condition predicate is true can reacquire the lock
		notifyAll();
	}
	
	// BLOCK unit not empty
	public synchronized V take() throws InterruptedException {
		while (isEmpty()) {
			wait();
		}
		V v = doTake();
		notifyAll();
		return v;
	}
}
