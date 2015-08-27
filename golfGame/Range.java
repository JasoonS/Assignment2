package golfGame;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range {
	private final int sizeStash;
	private AtomicBoolean bollieCollecting;
	private AtomicBoolean done;

	private BlockingQueue<golfBall> stash;
	
	Range(int sizeStash, AtomicBoolean bollieCollecting, AtomicBoolean done) {
		this.sizeStash = sizeStash;
		this.bollieCollecting = bollieCollecting;
		this.done = done;
		stash = new ArrayBlockingQueue<golfBall>(sizeStash);
	}
	
	public void hitBallOntoField(golfBall golfBall, int myID) throws InterruptedException {
		synchronized(stash){
			while(bollieCollecting.equals(true)) {//if there is no cart on the field
				this.wait();
			}
	
			stash.add(golfBall);
			
			System.out.println("Golfer #"+ myID + " hit ball #"+ golfBall.getID() +" onto field");
		}
	}

	public int collectAllBallsFromField(Queue<golfBall> ballsCollected) throws InterruptedException {
		synchronized(stash) {
			int ballsRetrieved = getBallsOnRange();
			while((done.get() != true) && !stash.isEmpty()){	
				golfBall ball = stash.take();
				
				ballsCollected.add(ball);	
			}
			
			return ballsRetrieved;
		}
	}
	
	private synchronized int getBallsOnRange() {
		return stash.size();
	}

}
