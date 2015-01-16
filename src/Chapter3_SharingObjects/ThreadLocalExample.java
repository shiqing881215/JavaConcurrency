package Chapter3_SharingObjects;

import java.util.Set;

import Annotations.Annotation.ThreadSafe;
import Chapter3_SharingObjects.ThreadLocalExample.DriverManager.Connection;

/**
 * 
 * @author shiqing
 *
 *	ThreadLocal can hold the value that is specific to this thread on this thread stack.
 *	This is one way to ensure thread confinement.
 */
@ThreadSafe(description = "ThreadLocal to store data on thread local stack")
public class ThreadLocalExample {
	// First time when call this, it will initialize the value with call to initialValue() method
	private static ThreadLocal<Connection> connectionHolder = 
			new ThreadLocal<Connection>() {
		public Connection initialValue() {
			return DriverManager.getConnection("db_url");
		}
	};
	
	// This will return the value dedicated combined with this thread
	public static Connection getConnection() {
		return connectionHolder.get();
	}
	
	
	// Fake db driver class
	public static class DriverManager {
		Set<Connection> pool;
		
		public static Connection getConnection(String dbUrl) {
			// Assign a new connection
			return null;
		}
		
		// Fake conneciton class
		public class Connection {
			
		}
	}
}
