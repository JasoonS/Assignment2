/* Controller of the collection of Golfer entities in the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */

package golfGameExtra;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GolferController {
	AtomicBoolean done;
	BallStash sharedStash; 
	Range sharedField;
	ArrayList<Golfer> golfers = new ArrayList<Golfer>();
	
	GolferController(AtomicInteger numGolfers, BallStash stash, Range field, AtomicBoolean doneFlag){
		this.sharedField = field;
		this.sharedStash = stash;
		this.done = doneFlag;
		
		for(int i=0; i<numGolfers.get(); i++) {
			golfers.add(new Golfer(this.sharedStash, this.sharedField, this.done));
		}
	}
	
	public void start(){
		//Starting the concurrent processes
		for(Golfer golfer : golfers) {
			golfer.start();
		}
	}
	
	public void addGolfer(){

		Golfer golfer = new Golfer(this.sharedStash, this.sharedField, this.done);
		System.out.println("We can rejoice, a new golfer has joined the club! Welcome Golfer #" + golfer.getId());
		golfer.start();
		golfers.add(golfer);
		
	}
	
	public void removeGolfer(){
		if(golfers.size()<1){
			System.out.println("THERE ARE NO MORE GOLFERS TO REMOVE. I THINK I WILL CRY NOW!");
			return;
		}
		
		Golfer toRemove = golfers.get(golfers.size()-1);
		toRemove.makeLeave();
		Golfer.removeGolfer();
		golfers.remove(golfers.size()-1);
		System.out.println("OH NO! We are loosing a golfer. Goodbye Golfer #" + toRemove.getId());
	}
}
