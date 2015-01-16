package Chapter3_SharingObjects;

import Annotations.Annotation.ThreadSafe;

/**
 * 
 * @author shiqing
 *
 *	When a variable is defined as volatile, 
 *	which means all the update on this variable will be visible to other thread. 
 *	(That¡¯s the reason why it called volatile) 
 *	This is a little like lighter version of synchronized. 
 *	But remember, volatile just guarantee the visibility among threads, 
 *	but synchronized can guarantee both the visibility and atomicity. 
 *	The most common use is in like variable for completion, interruption, status flag.
 */
@ThreadSafe(description = "Volatile key word will make all threads always see the most up-to-date value")
public class VolatileExample {
	// Yes, sure we can use synchronized, this is just lighter and simple
	volatile boolean shouldExit;
	
	public void check() {
		while (!shouldExit) {
			// do something
		}
	}
	
	// Any thread set the value on shouldExit will be seen by other thread read the value
	// in the future
	public void setExit() {
		shouldExit = true;
	}
}
