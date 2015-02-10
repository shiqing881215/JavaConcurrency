package Chapter8_ApplyingThreadPools;

import java.util.concurrent.ThreadFactory;

/**
 *	Implements ThreadFactory to provide the customization during
 *	thread initialization 
 *
 */
public class CustomThreadFactory_Factory implements ThreadFactory {
	private final String poolName;
	
	public CustomThreadFactory_Factory(String poolName) {
		this.poolName = poolName;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		return new CustomThreadFactory_Thread(r, poolName);
	}
}
