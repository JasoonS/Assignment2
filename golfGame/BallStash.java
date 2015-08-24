package golfGame;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BallStash {
	//static variables
	private final int sizeStash;
	private final int sizeBucket;
	private AtomicBoolean bollieCollecting;
	
	//ADD variables: a collection of golf balls, called stash
	private BlockingQueue<golfBall> stash; 
	
	BallStash(int sizeStash, int sizeBucket, AtomicBoolean bollieCollecting){
		this.sizeStash = sizeStash;
		this.sizeBucket = sizeBucket;
		this.bollieCollecting = bollieCollecting;
		stash = new ArrayBlockingQueue<golfBall>(sizeStash);
		
		//fill the stash with golfBalls.
		for(int i = 0; i < sizeStash; i++){
			stash.add(new golfBall());
		}
	}
	
	//ADD methods:
	//getBucketBalls
	// addBallsToStash
	// getBallsInStash - return number of balls in the stash
	
	//Not synchronized as sizeBucket is final
	public int getSizeBucket () {
		return sizeBucket;
	}
	//Not synchronized as sizeStash is final
	public int getSizeStash () {
		return sizeStash;
	}
	public synchronized void getBucketBalls(Queue<golfBall> golferBucket) throws InterruptedException {
		
		golfBall[] bucket = new golfBall[getSizeBucket()];
			
		System.out.println("Stash is size: " + getBallsInStash());
		
		while(getBallsInStash() < getSizeBucket()) {
			wait();
		}
		for(int i = 0; i < sizeBucket; i++) {			
			golferBucket.add(stash.take());
		} 
	}
	private synchronized int getBallsInStash() {
		return stash.size();
	}

	public synchronized void addBallsToStash(Queue<golfBall> ballsCollected, int noCollected) throws InterruptedException {
		
		bollieCollecting.set(false);
			
		for(int i = 0; i < noCollected; i++) {
			golfBall ball = ballsCollected.remove();
			System.out.println("BOLLIE DEPOSITING:::This ball has an ID of: " + ball.getID());
			stash.put(ball);
		}
		
		notifyAll();
	}
	
	
}
