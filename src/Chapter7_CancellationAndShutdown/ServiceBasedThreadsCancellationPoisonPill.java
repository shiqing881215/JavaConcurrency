package Chapter7_CancellationAndShutdown;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * By using "Poison Pill" to tell queue when to shut donw.
 * This is a demo for single producer and single consumer.
 * You can extend to multiple producer and consumers, but you need to update by receiving/submitting 
 * N poison pills. 
 *
 */
public class ServiceBasedThreadsCancellationPoisonPill {
	public class IndexingService {
		private final File POISON = new File("");
		private final BlockingQueue<File> queue;
		private final CrawlerThread producer = new CrawlerThread();
		private final IndexerThread consumer = new IndexerThread();
		private final File root;
		
		public IndexingService(BlockingQueue<File> queue, File root) {
			this.queue = queue;
			this.root = root;
		}
		
		public void start() {
			producer.start();
			consumer.start();
		}
		
		// Interrupt the producer thread and insert the poison file
		public void stop() {
			producer.interrupt();
		}
		
		// This should be called after the stop method to wait for the consumer thread to die
		public void awaitTermination() throws InterruptedException {
			consumer.join();
		}

		// Producer
		public class CrawlerThread extends Thread {
			public void run() {
				try {
					crawl(root);
				} catch (InterruptedException e) {
					// Not care here, we just want to insert the poison in the queue
				} finally {
					while (true) {
						try {
							// put the poison file and break out
							queue.put(POISON);
							break;
						} catch (InterruptedException e) {
							// Just retry until we succeeding in inserting the poison file
						}
					}
				}
			}
			
			public void crawl(File file) throws InterruptedException {
				// do crawl stuff
			}
			
		}
		
		// Consumer
		public class IndexerThread extends Thread {
			public void run() {
				while (true) {
					try {
						// This will guarantee all the tasks which already submitted will be processed
						File f = queue.take();
						if (f == POISON) {
							break;
						}
						indexFile(f);
					} catch (InterruptedException e) {
						// retry
					}
				}
				
			}
			
			public void indexFile(File f) {
				// do index stuff
			}
		}
	}
}
