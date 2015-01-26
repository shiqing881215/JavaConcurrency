package Chapter5_BuildingBlocks;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Barrier is similar as Latch. They all wait for all the threads finish then process.
 * The key difference is :
 * Latch is single-use object, once it reaches its terminal state, it cannot be reset.
 * Barrier is waiting for all the threads come to a point at the same time, then success and pass,
 * then barrier is reset and wait for the next run.
 * 
 */
public class SynchronizerBarrier {
	// A class representing a system to calculate the cellular activities.
	// It divided the whole work into different several subparts, and each thread works on one of them.
	public class CellularAutomata {
		private final Board mainBoard;
		private final CyclicBarrier barrier;  // it called cyclic because of reusability
		private final Worker[] workers;
		
		public CellularAutomata(Board board) {
			this.mainBoard = board;
			int count = Runtime.getRuntime().availableProcessors();
			// Initialize the barrier with number of threads and
			// also an action which is executed when the barrier is successfully passed
			// but before the thread is released
			this.barrier = new CyclicBarrier(count, new Runnable() {
				@Override
				public void run() {
					mainBoard.commitNewValues();
				}
			});
			this.workers = new Worker[count];
			for (int i = 0; i < count; i++) {
				workers[i] = new Worker(mainBoard.getSubBoard(count, i));
			}
		}
		
		public class Worker implements Runnable {
			private final Board board;
			
			public Worker(Board board) {
				this.board = board;
			}
			
			@Override
			public void run() {
				while (!board.hasConverged()) {
					for (int x = 0; x < board.getMaxX(); x++) {
						for (int y = 0; y < board.getMaxY(); y++) {
							board.setNewValue(x, y, computeNewValue(x,y));
						}
					}
					
					try {
						// After finish own job, call await to wait for other threads
						// Just like tell the barrier "OK, this thread is done with its job"
						barrier.await();
					} catch (InterruptedException e) {  
						// This thread can be interrupted, we just return and commit nothing
						return;
					} catch (BrokenBarrierException e) {
						// If one of waiting threads is interrupted
						// We think the barrier is broken, all the other threads which call
						// await method will terminate with BrokenBarrierException
						// So we commit nothing here and return
						return;
					}
				}
			}

			// Generate new value in this position
			private Object computeNewValue(int x, int y) {
				// TODO Auto-generated method stub
				return null;
			}
		}
	}
	
	
	
	// Representing the work items
	public class Board {

		// Commit new value in this round
		public void commitNewValues() {
			// TODO Auto-generated method stub
		}

		public void setNewValue(int x, int y, Object computeNewValue) {
			// TODO Auto-generated method stub
		}

		public int getMaxY() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getMaxX() {
			// TODO Auto-generated method stub
			return 0;
		}

		// Whether this board work is done
		public boolean hasConverged() {
			// TODO Auto-generated method stub
			return false;
		}

		// Get the sub-work
		public Board getSubBoard(int count, int i) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
