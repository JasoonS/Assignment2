package golfGameExtra;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BallStash {
	private final int sizeStash;
	private final int sizeBucket;
	private AtomicBoolean bollieCollecting;
	private AtomicBoolean done;
	
	//This stash variable determines state of this object
	private BlockingQueue<golfBall> stash; 
	
	BallStash(int sizeStash, int sizeBucket, AtomicBoolean bollieCollecting, AtomicBoolean done){
		this.sizeStash = sizeStash;
		this.sizeBucket = sizeBucket;
		this.bollieCollecting = bollieCollecting;
		this.done = done;
		stash = new ArrayBlockingQueue<golfBall>(sizeStash);
		
		//fill stash with the correct number of golfBalls.
		for(int i = 0; i < sizeStash; i++){
			stash.add(new golfBall());
		}
	}
	
	//No protection mechanism as sizeBucket is final
	public int getSizeBucket () {
		return sizeBucket;
	}
	
	//No protection mechanism as sizeStash is final
	public int getSizeStash () {
		return sizeStash;
	}
	
	//this method safely refills a bucket of balls for the Golfer.
	public boolean getBucketBalls(Queue<golfBall> golferBucket, int myID) throws InterruptedException {
		//this object's state is only dependent on the stash variable 
		//  (the other variables are not related to the object's state and are either shared or constant)
		synchronized(stash){
			while(getBallsInStash() < getSizeBucket()) {
				if(done.get()) return false;
				//waits if the stash is not large enough to safely full a bucket
				stash.wait();
			}
			
			//loop through filling bucket to capacity
			for(int i = 0; i < sizeBucket; i++) {
				golferBucket.add(stash.take());
				if(done.get()) return false;
			}
			
			//This print needs to be here to prevent interleaving at closing time
			System.out.println("<<< Golfer #"+ myID + " filled bucket with          "+sizeBucket+" balls");
			
			//return as clearly the
			return true;
		}
	}
	
	//No protection mechanism as BlockingQueue's are themselves atomic
	//Returns number of balls in Stash()
	private int getBallsInStash() {
		return stash.size();
	}

	
	public void addBallsToStash(Queue<golfBall> ballsCollected, int noCollected) throws InterruptedException {
		synchronized(stash){
			bollieCollecting.set(false);
					
			for(int i = 0; i < noCollected; i++) {
				golfBall ball = ballsCollected.remove();
				stash.put(ball);
			}
			
			stash.notifyAll();
		}
	}
}
