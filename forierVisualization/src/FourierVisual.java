import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


import processing.core.PApplet;
import processing.core.PVector;

public class FourierVisual extends PApplet {

	
	
	
	
    // The argument passed to main must match the class name
    public static void main(String[] args){
        PApplet.main("FourierVisual");

    }

    // method for setting the size of the window
    public void settings(){
        size(1000, 1000);
    }

    // identical use to setup in Processing IDE except for size()
    public void setup(){
        background(black);
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
    
    //colors
    final int purple = color(125, 122, 188);
    final int red = color(222, 60, 75);
    final int green = color(19, 111, 99);
    final int blue = color(38, 237, 247);
    final int black = color(40, 38, 44);
    
    boolean draw = false;
    
    
    
    
    
    ArrayList<PVector> wavePoints = new ArrayList<PVector>();
    ArrayList<Float> signalY = new ArrayList<Float>();
    ArrayList<Float> signalX = new ArrayList<Float>();
    ArrayList<Map<String, Float>> fourierY = new ArrayList<Map<String, Float>>();
    ArrayList<Map<String, Float>> fourierX = new ArrayList<Map<String, Float>>();
    ArrayList<PVector> mousePoints = new ArrayList<PVector>();
    
    
    
    // identical use to draw in Processing IDE
    public void draw(){
    	
 
    	
    	
    	if(draw) {
	    	//Start with background to clear canvas
	    	background(black);
	    	
	    	//translate(100, height/2);
	
	
	    	PVector coordsY = epiCycles(100, 500, HALF_PI, fourierY);
	    	
	    	PVector coordsX = epiCycles(500, 100, 0, fourierX);
	    	
	    	
	    	PVector intersect = new PVector();
	    	
	    	intersect.x = coordsX.x;
	    	intersect.y = coordsY.y;
	    	
	    	//line from end of last circle to where the wave is being drawn
	    	stroke(blue, 50);
	    	line(coordsY.x, coordsY.y, intersect.x, intersect.y);
	    	line(coordsX.x, coordsX.y, intersect.x, intersect.y);
	    	
	    	
	    	
	    	// draw the wave resulting from the sum of sinusoids
	    	
	    	wavePoints.add(intersect);
	    	
	    	strokeWeight(2);
	    	stroke(purple);
	    	beginShape();
			
	    	for(int i = 0; i < wavePoints.size(); i++) {
	    		
	    		PVector point = wavePoints.get(i);
	    		vertex(point.x, point.y);
	
	    	}
	    	
			endShape();
	    	
	    	
	    	
	    	float dt =  TWO_PI / fourierY.size() ;
	    	time += dt;
	    	
	    	if(time > TWO_PI) {
	    		wavePoints.clear();
	    		time = 0;
	    	}
	    	
    	}else {
    		
    		if(mouseButton == LEFT) {
    			PVector pnt = new PVector();
        		pnt.y = mouseY;
        		pnt.x = mouseX;
        		
        		mousePoints.add(pnt);
    		}
    		background(black);
    		

    		//instructions
    		textSize(32);
    		String instruction1 = "Hold left click to draw.";    	
    		String instruction2 = "Drawing will be shifted so stay away from edges";  
    		String instruction3 = "Drawing can not have gaps.";
    		String instruction4 = "Right click to submit drawing";  
    		
    		text(instruction1, 50, 650);
    		text(instruction2, 50, 750);
    		text(instruction3, 50, 850);
    		text(instruction4, 50, 950);

    		
    		noFill();
    		stroke(red, 50);
    		strokeWeight(3);
    		beginShape();
    		for(int i = 0; i < mousePoints.size(); i++){
    			PVector p = mousePoints.get(i);
    			vertex(p.x, p.y);
    		}
    		
    		endShape();
    		

    		
    		
    		
    		if(mouseButton == RIGHT) {
    			draw = true;

        		for(int i = 0; i < mousePoints.size(); i++){
        			PVector p = mousePoints.get(i);
        			signalX.add(p.x);
        			signalY.add(p.y);
        		}
        		 fourierY = dft(signalY);
                 fourierX = dft(signalX);
                 
                 fourierY = sortArrayList(fourierY);
                 fourierX = sortArrayList(fourierX);
                 
    			
    		}
    		
           
    		
    		
    		
    	}
    }
    
    
    public PVector epiCycles(float x, float y, float rotation, ArrayList<Map<String, Float>> fourier) {
    	
    	
    	//Draw each circle and the point of the circle
    	for(int i = 1; i < fourier.size(); i++) {
    		
    		
    		float prevX = x;
    		float prevY = y;
    		
    		Map<String, Float> dict = fourier.get(i);
    		
    		float freq = dict.get("frequency");
    		float radius = dict.get("amplitude");
    		float phase = dict.get("phase");
    		
    	
        	
    		x += radius * cos(freq * time + phase + rotation);
    		y += radius * sin(freq * time + phase + rotation);

    		
        	noFill();
        	strokeWeight(1);
        	stroke(green);
        	
        	circle(prevX, prevY, radius * 2);
        	
        	strokeWeight(1);
        	stroke(red);
        	line(prevX, prevY, x, y);
        	
       	
        	strokeWeight(3);
        	stroke(red);
        	point(x, y);
        	  		
    	}
    	
    	
    	PVector vector = new PVector();
    	vector.x = x;
    	vector.y = y;
    	
    	return vector;
    	
    }
    
    
    
    /**
     * method of a discrete Fourier transform or dft
     * 
     */
    public static ArrayList<Map<String, Float>> dft(ArrayList<Float> signal){
    	
    	ArrayList<Map<String, Float>> dictionary = new ArrayList<Map<String, Float>>();
    	
    	int len = signal.size();
    	
    	for(int k = 0; k < len; k++) {
    		
    		float re = 0;
    		float im = 0;
    		
    		for(int n = 0; n < len; n++) {
    			
    			float phi = (TWO_PI * k * n) / len;
    			
    			re += signal.get(n) * cos(phi);
    			im -= signal.get(n) * sin(phi);
    			
    		}
    		
    		re = re / len;
    		im = im / len;
    		
    		
    		Map<String, Float> element = new HashMap<String, Float>();
    		
    		
    		float freq = k;
    		
    		element.put("real", re);
    		element.put("imaginary", im);
    		element.put("amplitude", sqrt(re * re + im * im));
    		element.put("phase", atan2(im, re));
    		element.put("frequency", freq);
    		
    		dictionary.add(element);
    		

    	}
    	

    	return dictionary;
    }
    
    
    public ArrayList<Map<String, Float>> sortArrayList(ArrayList<Map<String, Float>> unsorted) {
    	
    	ArrayList<Map<String, Float>> temp = new ArrayList<Map<String, Float>>();
    	

    		
    		while(unsorted.size() > 0) {
    			int lowestIndex = 0;
    			Map<String, Float> lowMap = unsorted.get(0);
    			
    			for(int i = 0; i < unsorted.size(); i++) {
    				Map<String, Float> compare = unsorted.get(i);
    				
    				if(compare.get("amplitude") > lowMap.get("amplitude")) {
    					lowMap = compare;
    					lowestIndex = i;
    				}
    			}
    			temp.add(unsorted.remove(lowestIndex));
    		
    	}
    	
    	return temp;
    }
    
    
}