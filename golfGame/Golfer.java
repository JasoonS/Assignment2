package golfGame;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Golfer extends Thread {
	
	private static int noGolfers; //shared amongst threads
	private static int ballsPerBucket; //shared amongst threads
	
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
		noGolfers++;
		return noGolfers;
	}
	
	//helper method for clean code
	private boolean fullBucket() throws InterruptedException{
		return sharedStash.getBucketBalls(golferBucket, myID);
	}
	
	//Don't need to worry about
	public static void setBallsPerBucket (int noBalls) {
		ballsPerBucket=noBalls;
	}
	
	
	private static int getBallsPerBucket () {
		return ballsPerBucket;
	}
	
	
	public void run() {
		
		while (done.get() != true) {
			
			//Need to lock on done to prevent read-act compound action (ie make it impossible to print this like once golf range closes).
			synchronized(done){
				if(done.get()) return;
				
				System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+getBallsPerBucket()+" balls.");
			}
			
			try {
				//Return if the bucket wasn't filled due to closing time.
				if(!fullBucket()) return;
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}	
			
			for (int b=0;b<ballsPerBucket;b++)
			{ //for every ball in bucket
				
			    try {
					sleep(swingTime.nextInt(2000));
					hittBallOnRange();
//					System.out.windowprintln("Golfer #"+ myID + " hit ball #"+hittBallId+" onto field");	
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} //      swing
			    
				    
			    //!!wair for cart if necessary if cart there
			}
			
		}
		return;
	}

	
	private void hittBallOnRange() throws InterruptedException {
		sharedField.hitBallOntoField(golferBucket.remove(), myID);
	}	
}
