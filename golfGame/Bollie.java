/*Bollie controller for the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */
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
	Queue<golfBall> ballsCollected = new LinkedList<golfBall>();//this is the temporary storage for Bollie
	
	Bollie(BallStash stash,Range field,AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		done = doneFlag;
		waitTime = new Random();
	}
	
	public void run() {
		
		while (done.get()!=true) {
			try {
				
				sleep(waitTime.nextInt(1000));
				
				collectDeposit();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}	
	}	
	
	private void collectDeposit() throws InterruptedException{
		//Big block to synchronize, but strategic. Gives Bollie full priority. 
		//    Synchronized handles 'waiting' and 'notify' of other threads automatically.
		synchronized(sharedField){
			
			synchronized(done) {
				if (done.get()) return;
				//print contained in synchronization, so safe from interleaving prints (from a change in state of done and from swinging golfers)
				System.out.println("*********** Bollie collecting balls   ************");	
			}
			
			sharedField.collectAllBallsFromField(ballsCollected);
		
			sleep(1000);// no golfers allowed to swing while this is happening
			
			System.out.println("*********** Bollie adding balls to stash ************");	
			
			sharedStash.addBallsToStash(ballsCollected);
			
			sharedField.notifyAll();
		}
	}
}
