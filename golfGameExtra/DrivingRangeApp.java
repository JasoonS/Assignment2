/*The main class and driver for the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */
package golfGameExtra;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DrivingRangeApp {


	public static void main(String[] args) throws InterruptedException {
		
		AtomicBoolean done  =new AtomicBoolean(false);
		
		AtomicInteger noGolfers = new AtomicInteger(Integer.parseInt(args[0]));
		AtomicInteger sizeStash= new AtomicInteger(Integer.parseInt(args[1]));
		AtomicInteger sizeBucket= new AtomicInteger(Integer.parseInt(args[2]));
		BallStash sharedStash = new BallStash(sizeStash, sizeBucket, done);
		Range sharedField = new Range(sizeStash, done);
		Golfer.setBallsPerBucket(sizeBucket);
		GolferController golfers = new GolferController(noGolfers, sharedStash, sharedField, done);
		
		Bollie bollie = new Bollie(sharedStash, sharedField, done);
		
		KeyboardListener keyController = new KeyboardListener("Your golf game controller", done, golfers, noGolfers, sizeBucket, bollie, sizeStash);
		
		System.out.println("=======   River Club Driving Range Open  ========");
		System.out.println("======= Golfers:"+noGolfers+" balls: "+sizeStash+ " bucketSize:"+sizeBucket+"  ======");
		
		//start the concurrent golfers.
		golfers.start();
		
		//Starting Bollie
		bollie.start();
		
		return;
		
	}

}
