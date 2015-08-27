package golfGameExtra;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Golfer extends Thread {

	//Remember to ensure thread safety
	
	private AtomicBoolean done;
	
	private static int noGolfers; //shared amongst threads
	private static int ballsPerBucket; //shared amongst threads
	
	private int myID;
	
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

	private static int newGolfID() { 
		noGolfers++;
		return noGolfers;
	}
	
	private boolean fullBucket() throws InterruptedException{
		return sharedStash.getBucketBalls(golferBucket, myID);
	}
	
	public static void setBallsPerBucket (int noBalls) {
		ballsPerBucket=noBalls;
	}
	private static int getBallsPerBucket () {
		return ballsPerBucket;
	}
	public void run() {
		
		while (done.get() != true) {
			
			 
			System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+getBallsPerBucket()+" balls.");
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
					int hittBallId = hittBallOnRange();
//					System.out.println("Golfer #"+ myID + " hit ball #"+hittBallId+" onto field");	
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} //      swing
			    
				    
			    //!!wair for cart if necessary if cart there
			}
			
		}
		return;
	}

	private synchronized int hittBallOnRange() throws InterruptedException {
//		System.out.println("Golfer #"+ myID + "Bucket had - " + golferBucket.size() + "balls"); 
		int num = sharedField.hitBallOntoField(golferBucket.remove());
//		System.out.println("Golfer #"+ myID + "Now the bucket has - " + golferBucket.size() + "balls");
//		System.out.println("Golfer #"+ myID + " hit ball #"+num+" onto field");
		return num;
	}	
}
