/*Bollie controller for the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */
package golfGameExtra;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bollie extends Thread{

	private AtomicBoolean done;  // flag to indicate when threads should stop
	private BallStash sharedStash; //link to shared stash
	private Range sharedField; //link to shared field
	private Random waitTime;
	private boolean mustNotOpperate;
	private Queue<golfBall> ballsCollected;
	
	Bollie(BallStash stash,Range field,AtomicBoolean doneFlag) {
		sharedStash = stash; //shared 
		sharedField = field; //shared
		done = doneFlag;
		waitTime = new Random();
		ballsCollected = new LinkedList<golfBall>();
	}
	
	public void run() {
		
		while (done.get()!=true) {
			while(mustNotOpperate){ 
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				sleep(waitTime.nextInt(1000));
				collectDeposit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}	
	}
	
	public void toggleAutoClean() throws InterruptedException {
		mustNotOpperate = !mustNotOpperate;
		if(mustNotOpperate) {
			System.out.println("Bollie will now rest unless you explicitly tell him to work.");
		} else {
			System.out.println("Bollie is back in full service. Expect to see him often.");
		}
	}
	
	public boolean getBollieStatus(){
		return mustNotOpperate;
	};

	public void forceBollieOpperation() throws InterruptedException {
		synchronized(sharedField){
			System.out.println("YOU HAVE FORCED BOLLIE TO WORK AGAINST HIS WILL. SHAME ON YOU!");
			collectDeposit();
		}
	}

	private void collectDeposit() throws InterruptedException {
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
