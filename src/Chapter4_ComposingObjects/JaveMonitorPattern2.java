package Chapter4_ComposingObjects;

import Annotations.Annotation.GuardeBy;

/**
 * 
 * @author shiqing
 *
 * Java monitor patter is convention, any lock object can be used to guard an object state
 * if it's used consistently.
 * 
 * Some benefit of using a private lock is you are not exposing this lock to the client side.
 * So you can avoid the risk incorrectly using lock from client side.
 */
public class JaveMonitorPattern2 {
	@GuardeBy(lockObject = "myLock") private long value;
	// Object private lock, combined with this JaveMonitorPattern2 object. 
	// Because each JavaMonitorPattern2 object will only have one this myLock and immutable
	// so get this myLock is equal to get "this" lock
	private final Object myLock = new Object();
	
	public long getValue() {
		synchronized (myLock) {
			return value;
		}
	}
}
