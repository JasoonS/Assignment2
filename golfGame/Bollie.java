package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

	private AtomicBoolean done;  // flag to indicate when threads should stop
	
	private BallStash sharedStash; //link to shared stash
	private Range sharedField; //link to shared field
	private Random waitTime;

	//link to shared field
	Bollie(BallStash stash,Range field,AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		waitTime = new Random();
		done = doneFlag;
	}
	
	
	public void run() {
		
		//while True
		golfBall [] ballsCollected = new golfBall[sharedStash.getSizeStash()];
		while (done.get()!=true) {
			try {
				sleep(waitTime.nextInt(1000));
				System.out.println("*********** Bollie collecting balls   ************");	
				// sharedField.collectAllBallsFromField(ballsCollected);
				// collect balls, no golfers allowed to swing while this is happening
				sleep(1000);
				System.out.println("*********** Bollie adding balls to stash ************");	
				//sharedStash.addBallsToStash(ballsCollected,noCollected);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		    		}
		}	
}
