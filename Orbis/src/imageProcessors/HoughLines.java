package imageProcessors;

import java.awt.image.BufferedImage;

public class HoughLines {
	private int w, h, cullRate; // rough guidelines
	private int[] pixels;
	private int[] accumulator;
	private float angle, tolerence, precision;
	
	//outputs
	private double r, t;
	
	public HoughLines(int w, int h, float angle, float tolerence, float precision, int cullRate) {
		this.w = w;
		this.h = h;
		this.angle = angle;
		this.tolerence = tolerence;
		this.precision = precision;
		this.cullRate = cullRate;
	}
	
	public void process(int n) {
		if (pixels != null) {
			System.err.println("Hough Transformation Error: null image");
		}else if (n < 1){
			System.err.println("Hough Transformation Error: number of lines to find < 1");
		}else {
			accumulator = new int[pixels.length];
			int max = 0;
			for (int i = 0; i < h; i++){
				for (int j = 0; j < w; j++){
					int[] colour = IPOP.pixelRGB(pixels[i*w + j]);
					if (colour[0] == 255 && colour[1] == 255 && colour[2] == 255){
						//go through all the angles, specified by the angle, tolerence and precision parameters
						for (float theta = angle - tolerence; theta <= angle + tolerence; theta += 1/precision){
							//consider a line being drawn with the particular
							int r_ij = 0;
						}
					}
				}
			}
		}
	}
	
	public void setImage(BufferedImage img){
		if (img != null) {
			this.pixels = IPOP.bufferedImageToArray(img);
		}
	}
	
	public void setImage(int[] pixels){
		this.pixels = pixels;
	}
	
	public double getDistanceFromCentre(){
		return r;
	}
	
	public double getTheta(){
		return t;
	}
}
