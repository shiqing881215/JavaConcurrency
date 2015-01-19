package Chapter4_ComposingObjects;

import Annotations.Annotation.GuardeBy;
import Annotations.Annotation.ThreadSafe;

/**
 * 
 * @author shiqing
 * 
 * Java monitor pattern encapsulate all its mutable states and guarded them with the 
 * object's own intrinsic lock.
 *
 */
@ThreadSafe(description = "Java monitor pattern - convention")
public class JavaMonitorPattern1 {
	/**
	 * In this case, we have one mutable state value, and all access to it is through
	 * methods in object which is guarded by synchronized.
	 */
	@GuardeBy(lockObject = "this")private long value = 0;
	
	public synchronized long getValue() {
		return value;
	}
	
	public synchronized long increase() {
		return ++value;
	}
}
