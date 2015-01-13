package Chapter2_ThreadSafe;

import Annotations.Annotation.ThreadUnsafe;

@ThreadUnsafe(
		description = "Not safe counter", 
		reason = "This is read-modify-write, counter may access by two threads")
public class CounterFactoryUnsafe {
	private long counter = 0;
	
	public long get() { return this.counter;}
	
	public void increat() {
		counter++;
	}
}
