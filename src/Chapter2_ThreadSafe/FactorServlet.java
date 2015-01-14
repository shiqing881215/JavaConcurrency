package Chapter2_ThreadSafe;

import java.math.BigInteger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import Annotations.Annotation.GuardeBy;
import Annotations.Annotation.ThreadSafe;
import Common.Servlet;

@SuppressWarnings("serial")
@ThreadSafe(description = "Scope your synchronized block.")
/**
 * @author shiqing
 *
 * This is to demo the tradeoff between safe and performance when using synchronization.
 * You can just simply add the synchronized keyword for the service method.
 * It covers all the mutable state change and will guarantee the thread safe.
 * But this will affect the performance a lot and will also subvert the servlet concept
 * which means can serve multiple threads at the same time.
 * 
 * So try to scope your synchronized block like use synchronized block only on the shared state
 * that would be accessed. Try to avoid include expensive statement in the synchronized block like
 * factor() method. 	
 */
public class FactorServlet extends Servlet {
	@GuardeBy(lockObject = "this") private BigInteger lastNumber;
	@GuardeBy(lockObject = "this") private BigInteger[] lastFactors;
	@GuardeBy(lockObject = "this") private long hits;
	@GuardeBy(lockObject = "this") private long cacheHits;
	
	public synchronized long getHit() { return this.hits; }
	public synchronized double getCacheRatio() {
		return (double)cacheHits/(double)hits;
	}
	
	@Override
	public void service(ServletRequest req, ServletResponse resp) {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = null;
		
		synchronized (this) {
			if (i.equals(lastNumber)) {
				cacheHits++;
				factors = lastFactors.clone();
			}
		}
		
		if (factors == null) {
			// This will consume a lot of time and also NOT change any SHARE state
			// exclude out of the synchronized block
			factors = factor(i);
			synchronized (this) {
				hits++;
				lastFactors = factors.clone();
				lastNumber = i;
			}
		}
		
		encodeIntoResponse(req, factors);
	}
}
