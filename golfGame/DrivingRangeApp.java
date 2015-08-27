package golfGame;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp {


	public static void main(String[] args) throws InterruptedException {
		AtomicBoolean done  =new AtomicBoolean(false);
		Random openTime = new Random();
		
//		int noGolfers = Integer.parseInt(args[0]);
//		int sizeStash= Integer.parseInt(args[1]);
//		int sizeBucket= Integer.parseInt(args[2]);
		int noGolfers = 20;
		int sizeStash= 200;
		int sizeBucket= 5;
		BallStash sharedStash = new BallStash(sizeStash, sizeBucket, done);
		Range sharedField = new Range(sizeStash, done);
		Golfer.setBallsPerBucket(sizeBucket);
		GolferController golfers = new GolferController(noGolfers, sharedStash, sharedField, done);
		
		System.out.println("=======   River Club Driving Range Open  ========");
		System.out.println("======= Golfers:"+noGolfers+" balls: "+sizeStash+ " bucketSize:"+sizeBucket+"  ======");
		
		//start the concurrent golfers.
		golfers.start();
		
		//Starting Bollie
		Bollie bollie = new Bollie(sharedStash, sharedField, done);
		bollie.start();
		
		//Opens for at least 1000ms, plus a random number
		Thread.sleep(1000 + openTime.nextInt(30000));
		synchronized(done){
			done.set(true);
			System.out.println("=======  River Club Driving Range Closing ========");
		}
		return;
		
	}

}
