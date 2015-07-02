
import java.awt.image.BufferedImage;

import imageProcessors.*;
import static imageProcessors.IPOP.*;

public class TestComponent {

	public static void main(String[] args) {	
		System.out.println("hello world");
		
		BufferedImage img1 = null;
		BufferedImage img2 = null;
		//get an image or something
		int w = img1.getWidth(), h = img1.getHeight();
		
		//blobTrack();
		//circleTrack();
	}
	
	private static void blobTrack(int[] B, int[] F, int w, int h){
		int T = 20;
		Vector2f centroid = centroid(IPOP.subtractBackground(B, F, T), w, h);
		System.out.println(centroid.toString());
	}
	
	private static void circleTrack(){
		CannyEdgeDetector canny = null;
		HoughCircle hough = null;
	}
	
}
