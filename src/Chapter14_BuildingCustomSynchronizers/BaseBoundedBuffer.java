package Chapter14_BuildingCustomSynchronizers;

import Annotations.Annotation.GuardeBy;
import Annotations.Annotation.ThreadSafe;

@ThreadSafe(description = "Base class mplements a classic array-based circular buffer " +
		"where the buffer state variables (buf, head, tail, and count) are guarded by the buffer¡¯s intrinsic lock")
public abstract class BaseBoundedBuffer<V> {
	@GuardeBy(lockObject = "this") private final V[] buf;
	@GuardeBy(lockObject = "this") private int tail;
	@GuardeBy(lockObject = "this") private int head;
	@GuardeBy(lockObject = "this") private int count;
	
	@SuppressWarnings("unchecked")
	public BaseBoundedBuffer(int capacity) {
		this.buf = (V[]) new Object[capacity];
	}
	
	protected synchronized final void doPut(V v) {
		buf[tail] = v;
		if (++tail == buf.length) {
			tail = 0;
		}
		count++;
	}
	
	protected synchronized final V doTake() {
		V v = buf[head];
		buf[head] = null;
		if (++head == buf.length) {
			head = 0;
		}
		count--;
		return v;
	}
	
	protected synchronized final boolean isFull() {
		return count == buf.length;
	}
	
	protected synchronized final boolean isEmpty() {
		return count == 0;
	}
}
