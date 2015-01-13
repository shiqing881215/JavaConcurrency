package Chapter2_ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

import Annotations.Annotation.ThreadSafe;

@ThreadSafe(description = "Use java built in thread safe class to handle this")
public class CounterFactorySafe {
	private AtomicLong counter = new AtomicLong(0);
	
	public long get() { return counter.get();}
	
	public void increat() {
		counter.incrementAndGet();
	}
}
