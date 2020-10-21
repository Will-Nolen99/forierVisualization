import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class FourierVisual extends PApplet {

    // The argument passed to main must match the class name
    public static void main(String[] args) {
        PApplet.main("FourierVisual");

    }

    // method for setting the size of the window
    public void settings(){
        size(800, 500);
    }

    // identical use to setup in Processing IDE except for size()
    public void setup(){
        background(55);
        stroke(255);
        strokeWeight(1); 
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
    
    
    final float radiusConstant = 50;
    float time = 0;
    final int iterations = 2;
    
    ArrayList<Float> wavePoints = new ArrayList<Float>();
    
    
    // identical use to draw in Processing IDE
    public void draw(){
    	
    	//Start with background to clear canvas
    	background(55);
    	
    	translate(width/5, height/2);
    	
    	
    	
    	float prevX = 0, prevY = 0;
    	float x = 0, y = 0;

    	//Draw each circle and the point of the circle
    	for(int i = 0; i < iterations; i++) {
    		
        	float term = i * 2 + 1;
        	float radius = radiusConstant * (4/ (term * PI));
        	
    		x += radius * cos(term * time);
    		y += radius * sin(term * time);

    		
        	noFill();
        	strokeWeight(1);
        	stroke(255, 100);
        	
        	circle(prevX, prevY, radius * 2);
        	
        	
        	strokeWeight(3);
        	stroke(255);
        	point(x, y);
        	
        	
        	strokeWeight(1);
        	line(prevX, prevY, x, y);
        	
        	prevX = x;
        	prevY = y;
    		
    	}
    	
    	//line from end of last circle to where the wave is being drawn
    	stroke(255, 100);
    	line(prevX, prevY, width/5, prevY);
    	
    	
    	translate(width/5, 0);
    	
    	// draw the wave resulting from the sum of sinusoids
    	
    	wavePoints.add(0, prevY);
    	
    	stroke(255);
    	beginShape();
		
    	for(int i = 0; i < wavePoints.size(); i++) {
    		
    		float pointY = wavePoints.get(i);
    		vertex(i / 2, pointY);

    	}
    	
		endShape();

		
    	//remove points not on screen to save performance
		// 1000 is how many points it takes to reach the end of the screen
    	if(wavePoints.size() > 1000) {
    		
    		wavePoints.remove(wavePoints.size() - 1);
    		
    	}
    	
    	
    	
    	double dt = 0.01;
    	time += dt;
    	
    }
}