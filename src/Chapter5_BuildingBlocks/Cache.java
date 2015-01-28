package Chapter5_BuildingBlocks;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 
 * Building a concurrent high performance cache
 *
 * Several thing you need to keep in mind : 
 * 1) How to make cache - use a map
 * 2) How to make it thread-safe - lock the map
 * 3) If I lock the map, all the threads need to wait - use ConcurrentHashMap
 * 4) How to avoid compute duplicate key because of two threads both check value not in cache 
 *    - use Future to insert the key into cache first and calculate the value later
 * 5) How to deal some really rare situation even with above defender - use putIfAbsent
 */
public class Cache {
	// Interface defining a method, given an input and return an output
	public interface Computable<I, O> {
		O compute(I arg) throws InterruptedException;
	}
	
	// An expansive computing which takes a long time and we really want to cache
	public class ExpansiveFunction implements Computable<String, BigInteger> {
		@Override
		public BigInteger compute(String arg) throws InterruptedException {
			// Doing some real long expansive computing here
			return new BigInteger(arg);
		}
	}

	// A wrapper class to hold expensive function and act as a cache by composing a map
	public class Memoizer<I, O> implements Computable<I, O> {
		// Using ConcurrentHashMap instead of HashMap, so all the threads can read and write this
		// cache at the same time, no need to use intrinsic lock to lock the compute() method - that's very inefficient
		// 
		// Also in order to avoid duplicate compute the same value which may happen when two threads ask for 
		// the same key, no cache, compute at same time and then insert.
		// Use Future to put in the cache first then execute it after, so threads will see this item
		// in the cache immediately after insert, but may need wait for a while on get() call to wait
		// for the other thread finish the computing
		private final ConcurrentHashMap<I, Future<O>> cache = new ConcurrentHashMap<I, Future<O>>();
		private final Computable<I, O> c;
		
		public Memoizer(Computable<I, O> c) {
			this.c = c;
		}

		@Override
		public O compute(final I arg) throws InterruptedException {
			// Include in the infinite loop
			// until get the answer and also try again hit some exception
			while (true) {
				Future<O> f = cache.get(arg);
				if (f == null) {
					// Create the expansive compute task could be used by the future
					Callable<O> eval = new Callable<O>() {
						@Override
						public O call() throws Exception {
							return c.compute(arg);
						}
					};
					
					FutureTask<O> ft = new FutureTask<O>(eval);
					
					// Put in the cache first, so other threads can see it immediately
					// Also here notice, two threads may reach here at a very low possibility, but that's possible
					// which may still end we put both in the compute twice
					// so use this putIfAbsent to really make sure we just do once!
					f = cache.putIfAbsent(arg, ft);
					if (f == null) {
						f = ft;
						// Run this expansive call after we insert into the cache first
						ft.run();
					}
				}
				
				try {
					// This may return immediately or for a while
					// But what we can sure either the cache is already has the value or the value
					// is under computation by some other threads inserting the same key before current thread
					return f.get();
				} catch (CancellationException e) {
					cache.remove(arg);
				} catch (ExecutionException e) {
//					throw launderThrowable(e);
				}
			}
		}
	}
	
}
