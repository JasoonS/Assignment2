/*Golf Ball Stash for the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */
package golfGame;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BallStash {
	private final int sizeStash;
	private final int sizeBucket;
	private AtomicBoolean done;
	
	//This stash variable determines state of this object
	private BlockingQueue<golfBall> stash; 
	
	BallStash(int sizeStash, int sizeBucket, AtomicBoolean done){
		this.sizeStash = sizeStash;
		this.sizeBucket = sizeBucket;
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
					if(done.get()) return false; //ok since done is atomic.
					//waits if the stash is not large enough to safely full a bucket
					stash.wait();
				}
				
				//loop through filling bucket to capacity
				for(int i = 0; i < sizeBucket; i++) {
					golferBucket.add(stash.take());
				}
				
				//get the size of the stash while still synchronized to the stash
				int stashSize = stash.size();		
				
				//NOTE: I included this in the stash lock so that it prints the remaining number of balls in the correct order.
				//		if this is not important to you, putting this outside the stash lock is completely safe.
				//This print needs to be here to prevent interleaving at closing time
				//  - synchronized on done to prevent interleaving
				synchronized(done) {
					if(done.get()) return false;
					
					System.out.println("<<< Golfer #"+ myID + " filled bucket with          "+sizeBucket+" balls (remaining stash="+ stashSize +")");
				}
			}//See the 'NOTE' above for reason of this closing brackets placement
			
			//return true as clearly the transaction of balls was successful.
			return true;
		}
	
	//No protection mechanism as BlockingQueue's are themselves atomic
	//Returns number of balls in Stash()
	private int getBallsInStash() {
		return stash.size();
	}
	
	public void addBallsToStash(Queue<golfBall> ballsCollected) throws InterruptedException {
		synchronized(stash){
			
			//transfer all the balls into the stash
			while(ballsCollected.size() > 0) {
				golfBall ball = ballsCollected.remove();
				stash.put(ball);
			}

			//notify all threads waiting to get bucket of balls
			stash.notifyAll();
		}
	}
}
