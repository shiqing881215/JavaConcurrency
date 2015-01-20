package Chapter4_ComposingObjects;

import java.util.Vector;

import Annotations.Annotation.ThreadSafe;

@SuppressWarnings("serial")
@ThreadSafe(description = "Extends thread-safe class to add feature")
public class AddFuncToThreadSafeClass1<T> extends Vector<T>{
	/*
	 * Use synchronized this will use the same lock as Vector(thread-safe), which is this
	 */
	public synchronized boolean putIfAbsent(T t) {
		boolean absent = !contains(t);
		if (absent) {
			add(t);
			return true;
		}
		return false;
	}
}
