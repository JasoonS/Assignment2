/* Golfer entity in the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */

package golfGameExtra;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Golfer extends Thread {
	
	private static AtomicInteger noGolfers = new AtomicInteger(0); //shared amongst threads
	private static int ballsPerBucket; //shared amongst threads
	
	private boolean leave = false;//this new variable acts as a flag notifying when player should leave the golf range
	
	private final int myID; //unique identifier for the object
	private AtomicBoolean done;
	private Queue<golfBall> golferBucket;
	private BallStash sharedStash; //link to shared stash
	private Range sharedField; //link to shared field
	private Random swingTime;
	
	Golfer(BallStash stash, Range field, AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		done = doneFlag;
		golferBucket = new LinkedList<golfBall>();
		swingTime = new Random();
		myID=newGolfID();
	}

	//Never accessed concurrently
	private static int newGolfID() { 
		return noGolfers.incrementAndGet();
	}
	
	public static boolean removeGolfer(){
		if(noGolfers.get() < 1) return false;
		
		noGolfers.decrementAndGet();
		return true;
	}
	
	//helper method for clean code
	private boolean fullBucket() throws InterruptedException{
		return sharedStash.getBucketBalls(golferBucket, myID);
	}
	
	//Only set once, don't need to worry about concurrency.
	public static void setBallsPerBucket (AtomicInteger sizeBucket) {
		ballsPerBucket=sizeBucket.get();
	}
	
	//Never modified, no need to synchronize.
	private static int getBallsPerBucket () {
		return ballsPerBucket;
	}
	
	
	public void run() {
		
		while (done.get() != true) {
			
			//Need to lock on done to prevent read-act compound action (ie make it impossible to print this like once golf range closes).
			synchronized(done){
				if(done.get() || leave) return;
				
				System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+getBallsPerBucket()+" balls.");
			}
			
			try {
				//Return if the bucket was not filled due to closing time.
				if(!fullBucket() || leave) return;
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
			
			while(!golferBucket.isEmpty())
			{ //for every ball in bucket
			    try {
					sleep(swingTime.nextInt(2000));
					
					if(leave) return;
					hittBallOnRange(); //      swing
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return; //means the done flag has been set to true
	}

	private void hittBallOnRange() throws InterruptedException {
		sharedField.hitBallOntoField(golferBucket.remove(), myID);
	}

	public void makeLeave() {
		leave = true;
	}	
}
