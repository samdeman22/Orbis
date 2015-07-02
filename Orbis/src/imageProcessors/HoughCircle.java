package imageProcessors;

import java.awt.image.BufferedImage;

public class HoughCircle {
	// "input"s
	private int[] pixels;
	private final int w, h, minRadius, maxRadius;
	
	// "output"s
	private Vector2i centre;
	private int radius;
	private int[][] accumulator;
	private int k;
	
	private int x0, x, y0, y;
	
	int max = 0, maxK = 0;
	int[] maxMaxima = null, maxima = null;
	
	public HoughCircle(int w, int h, int minRadius, int maxRadius){ 
		this.w = w;
		this.h = h;
		this.minRadius = minRadius;
		this.maxRadius = maxRadius;
		pixels = null;
		centre = null;
		radius = -1;	//null
	}
	
	public void process(){
		if (pixels == null)
			System.err.println("Hough Transformation Error: null image");
		
		accumulator = new int[maxRadius - minRadius + 1][pixels.length];
		max = 0;
		maxK = 0;
		maxMaxima = null;
		maxima = null;
		
		// for each pixel between x0, x, y0, y in each accumulator
		// pixels outside x0, x, y0, y will not be considered
		for (k = 0; k < accumulator.length; k++){
			for (int i = y0; i < y; i++){
				for (int j = x0; j < x; j++){
					
					int[] colour = IPOP.pixelRGB(pixels[i*w + j]);
					
					if (colour[0] == 255 && colour[1] == 255 && colour[2] == 255){
						accumulator[k] = IPOP.drawAdditiveCircle(accumulator[k], w, h, j, i, minRadius + k, 1);
						//System.out.println("circle - radius: " + (minRadius + k) + "- added at " + j + "," + i);
					}
				}
			}
			
			maxima = IPOP.findLocalMaxima(accumulator[k], w, h, x0, y0, x, y);

			if (accumulator[k][maxima[1]*w + maxima[0]]  > max){
				max = accumulator[k][maxima[1]*w + maxima[0]];
				maxMaxima = maxima;
				maxK = k;
			}else{
				
			}
		}
		
		if (maxMaxima == null)
			maxMaxima = maxima;
		
		radius = minRadius + maxK;
		centre = new Vector2i(maxMaxima[0], maxMaxima[1]);
	}
	
	public void setBounds(int x0, int x, int y0, int y) {
		this.x0 = x0;
		this.x = x;
		this.y0 = y0;
		this.y = y;
	}
	
	public Vector2i getCentre(){
		return centre;
	}
	
	public int getRadius(){
		return radius;
	}
	
//	public int[] getAccumulator(){
//		return accumulator[k];
//	}
//	
//	public BufferedImage getAccumulator(){
//		return IPOP.arrayToBufferedImage(accumulator[k], w, h);
//	}
	
	//it is assumed that the width and height will not change, 
	public void setImage(BufferedImage img){
		pixels = IPOP.bufferedImageToArray(img);
	}
	
	public void setImage(int[] pixels){
		this.pixels = pixels;
	}
	
}
