package Chapter10_Deadlock;

import java.util.HashSet;
import java.util.Set;

import Annotations.Annotation.GuardeBy;

/**
 *	Even though there is no explicit call of two locks in the same method.
 *	There is possibility that a single call with one explicitly lock can result in multiple lock
 *	requirement between cooperating objects. 
 *
 *	This could be resolved by using "open call" which is shrinking synchronized block
 *
 */
public class CooperatingDeadlock {
	class Taxi {
		@GuardeBy(lockObject = "This")
		private String location, destination;
		private final Dispatcher dispatcher;
		
		public Taxi(Dispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}
		
		public synchronized String getLocation() {
			return location;
		}
		
		// Though this method is explicitly asks for the lock of this, which is Taxi
		// it also asks for the lock of dispatcher
		public synchronized void setLocation(String newLocation) {
			this.location = newLocation;
			// If reaching the destination, notify the system current taxi is available
			if (newLocation.equals(destination)) {
				// here we need the lock of dispatcher
				dispatcher.notifyAvailable(this);
			}
		}
		
		/*
		This is the way to use shrinking synchronized block (which is called open calls) to avoid this 
		potential deadlock 
		 
		public void setLocation(String newLocation) {
			boolean isReachiedDestination = false;
			synchronized (this) {
				this.location = newLocation;
				isReachiedDestination = newLocation.equals(destination);
			}
			if (isReachiedDestination) {
				dispatcher.notifyAvailable(this);
			}
		}
		*/
	}
	
	// Represent the system, include a bunch of taxi
	class Dispatcher {
		@GuardeBy(lockObject = "This")
		private final Set<Taxi> taxis;
		@GuardeBy(lockObject = "This")
		private final Set<Taxi> availableTaxis;
		
		public Dispatcher() {
			taxis = new HashSet<Taxi>();
			availableTaxis = new HashSet<Taxi>();
		}
		
		public synchronized void notifyAvailable(Taxi taxi) {
			availableTaxis.add(taxi);
		}
		
		// Draw the image of all taxis
		// Though this method is just explicitly asks for the lock of "this" which is Dispatch
		// it also asks for the lock of each taxi
		public synchronized Image getImage() {
			Image image = new Image();
			for (Taxi t : taxis) {
				// here we need the lock of t to get the location
				image.drawTaxiOnMap(t.getLocation());
			}
			return image;
		}
		
		/*
		This is also the way to do open call
		 
		public Image getImage() {
			Image image = new Image();
			Set<Taxi> copy;
			synchronized (this) {
				copy = new HashSet<Taxi>(taxis);
			}
			
			for (Taxi t : copy) {
				// here we need the lock of t to get the location
				image.drawTaxiOnMap(t.getLocation());
			}
			return image;
		}
		*/
		
		class Image {
			public void drawTaxiOnMap(String location) {
				
			}
		}
	}
}
