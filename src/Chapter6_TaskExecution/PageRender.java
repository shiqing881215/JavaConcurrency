package Chapter6_TaskExecution;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Consider the situation that you want to render a page with some images.
 * 1) If you do it sequentially, each time when we reach an image that needs downloading, the whole
 *    page is frozen.
 * 2) If we use two threads, one is rendering the text (DOM), the other one is used to download the 
 *    image. This is better. How to do? Use future and executor. Can we do better? This will show 
 *    all the image at once when they all download. Could we show each image once they are finished 
 *    downloading instead of waiting for all the image finishes?
 * 3) Using the CompletionService, which combines the functionality of Executor and BlockingQueue. 
 * 
 *
 */
public class PageRender {
	private final ExecutorService executor;
	
	public PageRender(ExecutorService executor) {
		this.executor = executor;
	}
	
	public void renderPage(CharSequence source) {
		List<ImageInfo> infos = scanForImage(source);
		// Instantiate the completion service by passing an executor parameter
		CompletionService<ImageData> completionService = 
				new ExecutorCompletionService<ImageData>(executor);
		for (final ImageInfo info : infos) {
			// Submit each download task as a callable to the executor
			// At the time of submit this callable task, it starts to do this task
			// which is downloading the image automatically, if it finishes, it will put
			// in the internal queue of the completionService
			completionService.submit(new Callable<ImageData>() {
				@Override
				public ImageData call() throws Exception {
					ImageData image = downloadImage(info);
					return image;
				}

				// Download the image data
				private ImageData downloadImage(ImageInfo info) {
					return null;
				}
			});
		}
		
		renderText(source);
		
		try {
			for (int i = 0; i < infos.size(); i++) {
				// If any of the image finish downloading, render it, otherwise wait it.
				Future<ImageData> f = completionService.take();
				ImageData image = f.get();
				renderImage(image);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			// TODO: handle exception
		}
		
		
		// Below is the version NOT using CompletionService
		// The disadvantage is it must wait for all the image download finish then render them together at once
		/*
		Callable<List<ImageData>> task = new Callable<List<ImageData>>() {
			@Override
			public List<ImageData> call() throws Exception {
				List<ImageData> result = new ArrayList<ImageData>();
				for (ImageInfo info : infos) {
					result.add(downloadImage(info));
				}
				
				return result;
			}
		};
		Future<List<ImageData>> f = executor.submit(task);
		try {
			// This will block until all the images get downloaded.
			List<ImageData> images = f.get();
		}
		*/
		
	}
	
	private List<ImageInfo> scanForImage(CharSequence source) {
		// scan the image info from the html source file
		return null;
	}
	
	// Render DOM
	private void renderText(CharSequence source) {
		
	}
	
	// Render the image
	private void renderImage(ImageData image) {
		
	}
	
	// ImagInfo java POJO class
	public class ImageInfo {
		
	}
	
	// ImageData java POJO class
	public class ImageData {
		
	}
}
