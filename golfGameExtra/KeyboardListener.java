/* Controller of the Swing GUI and the event handler for keyboarn inputs.
 * 
 * CSC2002S - Assignment2
 * @author	Jason Smythe
 */

package golfGameExtra;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyboardListener extends JFrame implements KeyListener{

    private AtomicBoolean done;
    private AtomicInteger noGolfers;
    private AtomicInteger sizeBucket;
    private AtomicInteger stashSize;
    private GolferController golfers;
    private Bollie bollie;
    private JPanel panel;
    private JPanel mainPanel;
    private JPanel instrPanel;
    private JLabel instruc1;
    private JLabel instruc2;
    private JLabel instruc3;
    private JLabel instruc4;
    private JLabel instruc5;
    private JLabel instruc6;
    private JLabel instruc7;
    private JLabel openLabel;
    private JLabel openDisplay;
    private JLabel golfersLabel;
    private JLabel golfersDisplay;
    private JLabel bucketLabel;
    private JLabel bucketDisplay;
    private JLabel bollieLabel;
    private JLabel bollieDisplay;

    public KeyboardListener (String name, AtomicBoolean done, GolferController golfers, AtomicInteger noGolfers, AtomicInteger sizeBucket, Bollie bollie, AtomicInteger sizeStash) {
    	super(name);
    	this.done = done;
    	this.golfers = golfers;
    	this.sizeBucket = sizeBucket;
    	this.noGolfers = noGolfers;
    	this.stashSize = sizeStash;
    	this.bollie = bollie;
        addKeyListener(this);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
               System.exit(0);
            }        
         }); 
        this.setSize(600,400);
        
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,20)); 
        mainPanel.setLayout(new GridLayout(2,1));
        
        instruc1 = new JLabel("Instructions(keyboard commands):",JLabel.LEFT);
        instruc2 = new JLabel("a - adds a golfer;                                               r - removes a golfer;",JLabel.LEFT);
        instruc3 = new JLabel("i - increase bucket size;                                   d - decrease bucket size;",JLabel.LEFT);
        instruc4 = new JLabel("t - toggle Bollies auto collect functionality;  c - make Bollie collect balls; ",JLabel.LEFT);
        instruc5 = new JLabel("s - close the range (but be carefull! Can only be pressed once)",JLabel.LEFT);
        instruc6 = new JLabel("See the console, and live monitor (below) for the results of your actions",JLabel.LEFT);
        instruc7 = new JLabel("*************************************************************************",JLabel.LEFT);
        instrPanel = new JPanel();
        instrPanel.setLayout(new GridLayout(7,1));
        instrPanel.add(instruc1);
        instrPanel.add(instruc2);
        instrPanel.add(instruc3);
        instrPanel.add(instruc4);
        instrPanel.add(instruc5);
        instrPanel.add(instruc6);
        instrPanel.add(instruc7);
        
        openLabel = new JLabel("Current Status of Driving Range:");
        openDisplay = new JLabel("Open For Business",JLabel.RIGHT);
        golfersLabel = new JLabel("How many golfers are currently at the range:",JLabel.LEFT);
        golfersDisplay = new JLabel("There are " + noGolfers.get() + " currently",JLabel.RIGHT);
        bucketLabel = new JLabel("What is the bucket allowance for each golfer?",JLabel.LEFT);
        bucketDisplay = new JLabel("Each golfer may have " + sizeBucket.get() + " balls in bucket",JLabel.RIGHT);
        bollieLabel = new JLabel("Is Bollie in opperation?",JLabel.LEFT);
        bollieDisplay = new JLabel("Bollie is in opperation",JLabel.RIGHT);
        panel = new JPanel();
        panel.setLayout(new GridLayout(8,1));
        panel.add(openLabel);
        panel.add(openDisplay);
        panel.add(golfersLabel);
        panel.add(golfersDisplay);
        panel.add(bucketLabel);
        panel.add(bucketDisplay);
        panel.add(bollieLabel);
        panel.add(bollieDisplay);
        
        this.add(mainPanel);
        
        mainPanel.add(instrPanel);
        
        mainPanel.add(panel);
        
        GridLayout layout = new GridLayout(0,3);
        
        
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case 65:
			addGolfer();
			break;
		case 68:
			decreaseBucketSize();
			break;
		case 67:
			try {
				forceBollieToCollect();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			break;
		case 73:
			increaseBucketSize();
			break;
		case 82:
			removeGolfer();
			break;
		case 83:
			stopGolfSimulation();
			break;
		case 84:
			try {
				toggleBollie();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			break;
		}
	}
	
	private void stopGolfSimulation(){
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} 
		synchronized(done){
			done.set(true);
			System.out.println("=======  River Club Driving Range Closing ========");
			openDisplay.setText("Range is Closed Now");
		}
	}
	
	private void removeGolfer(){
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} 
		golfers.removeGolfer();
		if (noGolfers.get() < 1) return;
		golfersDisplay.setText("There are " + noGolfers.decrementAndGet() + " currently");
	}
	
	private void addGolfer(){
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		}
		golfers.addGolfer();
		golfersDisplay.setText("There are " + noGolfers.incrementAndGet() + " currently");
	}
	
	private void increaseBucketSize(){
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} else if(sizeBucket.get() == stashSize.get()) {
			System.out.println("You cannot have a bucket larger than the total number of balls, you DODO.");
			return;
		}
		bucketDisplay.setText("Each golfer may have " + sizeBucket.incrementAndGet() + " balls in bucket");
		System.out.println("Managment are very Generous, increasing bucket size. New bucket size: " + sizeBucket.get());
	}
	
	private void decreaseBucketSize(){
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} else if(sizeBucket.get() < 2) {
			System.out.println("You cannot have a bucket of size 0, you DODO.");
			return;
		}
		bucketDisplay.setText("Each golfer may have " + sizeBucket.decrementAndGet() + " balls in bucket");
		System.out.println("Managment are being stingy, decreasing bucket size. New bucket size: " + sizeBucket.get());
	}
	
	private void toggleBollie() throws InterruptedException{
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} 
		bollie.toggleAutoClean();
		String keyword = "";
		if(bollie.getBollieStatus()) keyword = "NOT ";
		bollieDisplay.setText("Bollie is " + keyword + "in oppearation");
	}
	
	private void forceBollieToCollect() throws InterruptedException{
		if(done.get()){
			System.out.println("We are closed, sorry! Please restart the simulation.");
			return;
		} 
		bollie.forceBollieOpperation();
	}
}
