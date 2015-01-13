package Annotations;

import java.lang.annotation.Documented;

/**
 * @author shiqing
 */
public interface Annotation {
	@Documented
	public @interface ThreadSafe {
		String description();
	}
	
	@Documented
	public @interface ThreadUnsafe {
		String description();
		String reason();
	}
}
