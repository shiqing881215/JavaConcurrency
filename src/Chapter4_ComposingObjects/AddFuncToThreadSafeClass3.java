package Chapter4_ComposingObjects;

import java.util.List;

import Annotations.Annotation.ThreadSafe;

@ThreadSafe(description = "Composition, not pushlishing and own intrinsic lock")
public abstract class AddFuncToThreadSafeClass3<T> implements List<T> {
	// This is private and final, and not publishing, no one outside can change it.
	// We don't care it's thread-safe or not, all its atomic is guaranteed by this class intrinsic lock.
	private final List<T> list;
	
	public AddFuncToThreadSafeClass3(List<T> list) { this.list = list; }
	
	// Intrinsic lock
	public synchronized boolean putIfAbsent(T t) {
		boolean absent = !contains(t);
		if (absent) {
			add(t);
		}
		return absent;
	}
	
	public synchronized void clear() {
		list.clear();
	}
	
	// More ...
}
