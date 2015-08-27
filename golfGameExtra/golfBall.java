package golfGameExtra;


public class golfBall {
	//add mechanisms for thread saftey
	private static int noBalls;
	private final int myID;
	
	golfBall() {
		myID=noBalls;
		incID();
	}
	
	public int getID() {
		return myID;		
	}
	
	private static void  incID() {
		noBalls++;
	}
	
}
