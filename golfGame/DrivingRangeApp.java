package golfGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp {


	public static void main(String[] args) throws InterruptedException {
		AtomicBoolean done  =new AtomicBoolean(false);
		AtomicBoolean bollieCollecting = new AtomicBoolean(false);

		Random openTime = new Random();

		//read these in as command line arguments instead of hard coding
//		int noGolfers = Integer.parseInt(args[0]);
//		int sizeStash= Integer.parseInt(args[1]);
//		int sizeBucket= Integer.parseInt(args[2]);
		int noGolfers = 20;
		int sizeStash= 200;
		int sizeBucket= 5;
		BallStash sharedStash = new BallStash(sizeStash, sizeBucket, bollieCollecting, done);
		Range sharedField = new Range(sizeStash, bollieCollecting, done);
		Golfer.setBallsPerBucket(sizeBucket);
		
		//initialize shared variables
		ArrayList<Golfer> golfers = new ArrayList<Golfer>();
		for(int i=0; i<noGolfers; i++) {
			golfers.add(new Golfer(sharedStash, sharedField, done));
		}
		
		System.out.println("=======   River Club Driving Range Open  ========");
		System.out.println("======= Golfers:"+noGolfers+" balls: "+sizeStash+ " bucketSize:"+sizeBucket+"  ======");
		
		//Starting the concurrent processes
		for(Golfer golfer : golfers) {
			golfer.start();
		}
		
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
