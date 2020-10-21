package forierVisualization;
import processing.core.PApplet;

public class forierVisual extends PApplet {

    // The argument passed to main must match the class name
    public static void main(String[] args) {
        PApplet.main("UsingProcessing");

    }

    // method for setting the size of the window
    public void settings(){
        size(500, 500);
    }

    // identical use to setup in Processing IDE except for size()
    public void setup(){
        background(0);
        stroke(255);
        strokeWeight(10); 
    }
    

    
    
    /**
     * Draw variable declaration
     * *************************
     * 
     * Since draw loops continually there is no way of initializing variable inside as they will be set to that each iteration.
     * The only way around this is with global variables.
     * To keep organization up all global variables used in draw will be declared below this comment.
     * 
     * 
     */

    
    
    
    // identical use to draw in Prcessing IDE
    public void draw(){
    	
    	
    }
}