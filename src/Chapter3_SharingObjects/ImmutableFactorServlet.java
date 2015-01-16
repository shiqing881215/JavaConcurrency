package Chapter3_SharingObjects;

import java.math.BigInteger;
import java.util.Arrays;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import Annotations.Annotation.ThreadSafe;
import Chapter2_ThreadSafe.FactorServlet;
import Common.Servlet;

/**
 * 
 * @author shiqing
 *
 *	Combine the Immutable object with Volatile key word to provide another version 
 *	thread-safe {@link FactorServlet}
 */
@ThreadSafe(description = "Immutable + Volatile makes another thread-safe version")
@SuppressWarnings("serial")
public class ImmutableFactorServlet extends Servlet {
	// Make it volatile, so the update on cache would be seen by other threads immediately
	private volatile ImmutableDataHolder cache = new ImmutableDataHolder(null, null);
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = cache.getFactor(i);
		if (factors == null) {
			factors = factor(i);
			// Update the cache to refers to another new immutable object
			cache = new ImmutableDataHolder(i, factors);
		}
	}
	
	// This is the immutable data holder
	// So there is no worry the lastNumber and lastFactors will be updated and mismatches
	public class ImmutableDataHolder {
		private final BigInteger lastNumber;
		private final BigInteger[] lastFactor;
		
		public ImmutableDataHolder(BigInteger lastNumber, BigInteger[] lastFactor) {
			this.lastNumber = lastNumber;
			this.lastFactor = Arrays.copyOf(lastFactor, lastFactor.length);
		}
		
		public BigInteger[] getFactor(BigInteger number) {
			if (lastNumber == null || !lastNumber.equals(number)) {
				return null;
			}
			
			return Arrays.copyOf(lastFactor, lastFactor.length);
		}
	}

}
