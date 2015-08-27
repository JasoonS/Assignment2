package golfGame;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

	private AtomicBoolean done;  // flag to indicate when threads should stop
	private BallStash sharedStash; //link to shared stash
	private Range sharedField; //link to shared field
	private Random waitTime;
	
	Bollie(BallStash stash,Range field,AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		done = doneFlag;
		waitTime = new Random();
	}
	
	
	public void run() {
		
		Queue<golfBall> ballsCollected = new LinkedList<golfBall>();
		
		while (done.get()!=true) {
			try {
				sleep(waitTime.nextInt(1000));
				
				//Big block to synchronize, but strategic. Gives Bollie full priority. 
				//    Synchronized handles 'waiting' and 'notify' of other threads automatically.
				synchronized(sharedField){
					
					synchronized(done) {
						if (done.get()) break;
						//print contained in synchronization, so safe from interleaving prints (from a change in state of done and from swinging golfers)
						System.out.println("*********** Bollie collecting balls   ************");	
					}
					
					sharedField.collectAllBallsFromField(ballsCollected);
				
					sleep(1000);// no golfers allowed to swing while this is happening
					
					System.out.println("*********** Bollie adding balls to stash ************");	
					
					sharedStash.addBallsToStash(ballsCollected);
					
					sharedField.notifyAll();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}	
	}	
}
