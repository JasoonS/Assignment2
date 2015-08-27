package golfGameExtra;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range {
	private final int sizeStash;
	private BlockingQueue<golfBall> stash;
	private AtomicBoolean bollieCollecting;
	private AtomicBoolean done;
	
	Range(int sizeStash, AtomicBoolean bollieCollecting, AtomicBoolean done) {
		this.sizeStash = sizeStash;
		this.bollieCollecting = bollieCollecting;
		this.done = done;
		stash = new ArrayBlockingQueue<golfBall>(sizeStash);
	}
	
	public synchronized int hitBallOntoField(golfBall golfBall) throws InterruptedException {
		
		while(bollieCollecting.equals(true)) {//if there is no cart on the field
			wait();
		}

		stash.add(golfBall);
		
		return golfBall.getID();
		
	}

	public synchronized int collectAllBallsFromField(Queue<golfBall> ballsCollected) throws InterruptedException {
		int ballsRetrieved = getBallsOnRange();
		while((done.get() != true) && !stash.isEmpty()){
			
			golfBall ball = stash.take();
//			System.out.println("BOLLIE COLLECTING:::This ball has an ID of: " + ball.getID());
//			System.out.println("Stash now has: " + stash.size() + " with a 'isEmpty' value of " + stash.isEmpty());
//			System.out.println("Bollie is now carrying: " + ballsCollected.size() + " but should be " + ballsRetrieved);
			
			ballsCollected.add(ball);
			
		}
		
		return ballsRetrieved;
	}
	
	private synchronized int getBallsOnRange() {
		return stash.size();
	}

}
