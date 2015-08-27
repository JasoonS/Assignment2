package golfGame;


public class golfBall {
	
	//noBalls does not require synchronization as it is private, inaccessible (no public getters and setters),
	// and only ever used in the constructor where it it is modified only in a sequential manner (only by one thread).
	// Thus i opted not to make it an atomicInteger
	private static int noBalls;
	private final int myID;
	
	golfBall() {
		myID=noBalls;
		incID();
	}
	
	//returns the balls ID
	public int getID() {
		return myID;		
	}
	
	//increments the field used as an id.
	private static void  incID() {
		noBalls++;
	}
	
}
