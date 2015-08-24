package golfGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DrivingRangeApp {


	public static void main(String[] args) throws InterruptedException {
		AtomicBoolean done  =new AtomicBoolean(false);
		AtomicBoolean bollieCollecting = new AtomicBoolean(false);

		//read these in as command line arguments instead of hard coding
		int noGolfers = Integer.parseInt(args[0]);
		int sizeStash= Integer.parseInt(args[1]);
		int sizeBucket= Integer.parseInt(args[2]);
		BallStash sharedStash = new BallStash(sizeStash, sizeBucket, bollieCollecting);
		Range sharedField = new Range(sizeStash, bollieCollecting);
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
		Bollie bollie = new Bollie(sharedStash, sharedField, done);
		bollie.start();
		
		//for testing, just run for a bit
		Thread.sleep(10000);// this is an arbitrary value - you may want to make it random
		done.set(true);
		System.out.println("=======  River Club Driving Range Closing ========");

		
	}

}
