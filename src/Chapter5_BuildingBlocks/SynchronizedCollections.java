package Chapter5_BuildingBlocks;

import java.util.Vector;

/**
 * 
 * @author shiqing
 *
 * Attention Vector is a synchronized collection from Java. 
 * All the issue listed below could be resolved by using Concurrent Collections in Java, 
 * say, for example for this one, use CopyAndWriteArrayList
 */
public class SynchronizedCollections<T> {
	
	/*
	 * The following three are all thread-unsafe
	 * Though Vector is a thread safe class, these are all compound action.
	 * When you single call size() get() remove() it will guarantee the atomicity of this action.
	 * But when you combine action together, this may fail.
	 * e.g Thread A execute getLastUnsafe first line and get lastIndex = 10, 
	 * 	   then thread B execute both lines of deleteLastUnsafe and remove the 10th element.
	 * 	   Then thread A executes the second line and it will fail.
	 * Keep in mind this is all because you combine all the atomic action into compound action.
	 * (Same for the iterator) 
	 */
	
	public T getLastUnsafe(Vector<T> vector) {
		int lastIndex = vector.size()-1;
		return vector.get(lastIndex);
	}
	
	public void deleteLastUnsafe(Vector<T> vector) {
		int lastIndex = vector.size()-1;
		vector.remove(lastIndex);
	}
	
	// The size may change during the iterator by other threads
	public void interateUnsafe(Vector<T> vector) {
		for (int i = 0; i < vector.size(); i++) {
			// do something
			vector.get(i);
		}
	}
	
	
	// Thread safe
	public T getLastSafe(Vector<T> vector) {
		synchronized (vector) {
			int lastIndex = vector.size()-1;
			return vector.get(lastIndex);
		}
	}
	
	public void deleteLastSafe(Vector<T> vector) {
		synchronized (vector) {
			int lastIndex = vector.size()-1;
			vector.remove(lastIndex);
		}
	}
	
	// The size may change during the iterator by other threads
	public void interateSafe(Vector<T> vector) {
		synchronized (vector) {
			for (int i = 0; i < vector.size(); i++) {
				// do something
				vector.get(i);
			}
		}
	}
}
