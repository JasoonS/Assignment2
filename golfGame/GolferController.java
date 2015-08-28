/* Controller of the collection of Golfer entities in the Driving Range.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */

package golfGame;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GolferController {
	AtomicBoolean done;
	BallStash sharedStash; 
	Range sharedField;
	ArrayList<Golfer> golfers = new ArrayList<Golfer>();
	
	GolferController(int numGolfers, BallStash stash, Range field, AtomicBoolean doneFlag){
		this.sharedField = field;
		this.sharedStash = stash;
		this.done = doneFlag;
		
		for(int i=0; i<numGolfers; i++) {
			golfers.add(new Golfer(this.sharedStash, this.sharedField, this.done));
		}
	}
	
	public void start(){
		//Starting the concurrent processes
		for(Golfer golfer : golfers) {
			golfer.start();
		}
	}
}
