package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Golfer extends Thread {

	//remeber to ensure thread saftey
	
	private AtomicBoolean done; 
	private AtomicBoolean cartOnField;
	
	private static int noGolfers; //shared amoungst threads
	private  static int ballsPerBucket=4; //shared amoungst threads
	
	private int myID;
	
	private golfBall  [] golferBucket;
	private BallStash sharedStash; //link to shared stash
	private Range sharedField; //link to shared field
	private Random swingTime;
	
	
	
	Golfer(BallStash stash,Range field, AtomicBoolean cartFlag, AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		cartOnField = cartFlag; //shared
		done = doneFlag;
		golferBucket = new golfBall[ballsPerBucket];
		swingTime = new Random();
		myID=newGolfID();
	}

	public  static int newGolfID() { 
		noGolfers++;
		return noGolfers;
	}
	
	public static void setBallsPerBucket (int noBalls) {
		ballsPerBucket=noBalls;
	}
	public static int getBallsPerBucket () {
		return ballsPerBucket;
	}
	public void run() {
		
	while (done.get()!=true) {
		
	    
			 
			System.out.println(">>> Golfer #"+ myID + " trying to fill bucket with "+getBallsPerBucket()+" balls.");
			// sharedStash.getBucketBalls
			System.out.println("<<< Golfer #"+ myID + " filled bucket with          "+getBallsPerBucket()+" balls");
			
		
		for (int b=0;b<ballsPerBucket;b++)
		{ //for every ball in bucket
			
		    try {
				sleep(swingTime.nextInt(2000));
				// sharedField.hitBallOntoField
				System.out.println("Golfer #"+ myID + " hit ball #"+golferBucket[b].getID()+" onto field");	
				
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //      swing
		    
			    
		    //!!wair for cart if necessary if cart there
		}
		
	      
	}
	}	
}
