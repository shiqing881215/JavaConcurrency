package Common;

import java.math.BigInteger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 *	Template servlet class  
 *	@author shiqing
 */
public abstract class Servlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	public abstract void service(ServletRequest req, ServletResponse resp);
		
	public BigInteger extractFromRequest(ServletRequest req) {
		// omit logic
		return null;
	}
	
	/*
	 * This method will consume a lot of time
	 */
	public BigInteger[] factor(BigInteger i) {
		// omit logic
		return null;
	}
	
	public void encodeIntoResponse(ServletRequest req, BigInteger[] response) {
		// omit logic
	}
}
