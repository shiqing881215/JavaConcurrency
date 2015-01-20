package Chapter4_ComposingObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Annotations.Annotation.ThreadSafe;

@ThreadSafe(description = "Composite thread-safe class and use same lock")
public class AddFuncToThreadSafeClass2<T> {
	public List<T> list = Collections.synchronizedList(new ArrayList<T>());
	
	/*
	 * You can NOT declare the synchronized for the whole method.
	 * Because that will use the different lock with list.
	 */
	public boolean putIfAbsent(T t) {
		synchronized (list) {   // Use the same lock here
			boolean absent = !list.contains(t);
			if (absent) {
				list.add(t);
			}
			return absent;
		}
	}
}
