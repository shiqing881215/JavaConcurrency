package Chapter10_Deadlock;

/**
 * 
 * Here we use the hashcode to guarantee the lock ordering.
 * In the real situation, if an account has something unique, like account number
 * we can use that to compare and that's easier to implement (which the tie situation will not happen)
 *
 */
public class DynamicDeadlock {
	// This is used during the special case that two account has the same hashcode
	// we need to guarantee the order, so we use this extra lock to make sure only one thread
	// can get both account locks at the same time
	private static final Object tieLock = new Object();
	
	/**
	 * This looks good, get the fromAccount lock first, then the toAccount
	 * seems no lock ordering issue
	 * But we cannot guarantee how the external world calls this.
	 * They may call with the totally opposite parameter which can lead to the deadlock
	 * 
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @throws InsufficientFundsException 
	 */
	public void transferMoney(final Account fromAccount, final Account toAccount, final String amount) 
			throws InsufficientFundsException {
		// Just a helper class wrapper the transfer logic
		class Helper {
			public void transfer() throws InsufficientFundsException {
				if (fromAccount.getBalance().compareTo(amount) < 0) {
					throw new InsufficientFundsException();
				} else {
					fromAccount.debit(amount);
					toAccount.credit(amount);
				}
			}
		}
		
		// This will return  Object.hashCode and we use this to guarantee the order
		int fromHash = System.identityHashCode(fromAccount);
		int toHash = System.identityHashCode(toAccount);
		
		if (fromHash < toHash) {
			synchronized (fromAccount) {
				synchronized (toAccount) {
					new Helper().transfer();
				}
			}
		} else if (fromHash > toHash) {
			synchronized (toAccount) {
				synchronized (fromAccount) {
					new Helper().transfer();
				}
			}
		} else {  // the situation that two account has the same hash
			synchronized (tieLock) {
				// The order below doesn't matter
				// Coz the above line guarantee only one thread can reaches here
				synchronized (toAccount) {
					synchronized (fromAccount) {
						new Helper().transfer();
					}
				}
			}
		}
	}
	
	private class Account {

		public String getBalance() {
			// Fake here
			return null;
		}

		public void credit(String amount) {
			// TODO Auto-generated method stub
			
		}

		public void debit(String amount) {
			// TODO Auto-generated method stub
			
		}
	}
	
	@SuppressWarnings("serial")
	private class InsufficientFundsException extends Exception {
		
	}
}
