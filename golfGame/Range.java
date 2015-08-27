package golfGame;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Range {
	private AtomicBoolean done;
	private BlockingQueue<golfBall> stash;
	
	Range(int sizeStash, AtomicBoolean done) {
		this.done = done;
		stash = new ArrayBlockingQueue<golfBall>(sizeStash);
	}
	
	//add a golf ball to the range's queue
	public synchronized void hitBallOntoField(golfBall golfBall, int myID) throws InterruptedException {
		stash.add(golfBall);
		System.out.println("Golfer #"+ myID + " hit ball #"+ golfBall.getID() +" onto field");
	}

	//method for Bollie to collect all balls from the stash
	public synchronized void collectAllBallsFromField(Queue<golfBall> ballsCollected) throws InterruptedException {
		//add balls from stash one by one by to Bollies private stash.
		while((done.get() != true) && !stash.isEmpty()){	
			golfBall ball = stash.take();
			ballsCollected.add(ball);	
		}
	}
}
