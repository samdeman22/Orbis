package imageProcessors;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

public final class IPOP {
	
	private IPOP(){
	}
	
	//constants for methods of grey-scaling
	public static final int AVERAGE = 1, DESATURATION = 2, DECOMPOSITION_MAX = 3, DECOMPOSITION_MIN = 4, RED = 5, GREEN = 6, BLUE = 7;
	
	public static int[] bufferedImageToArray(BufferedImage img){
		int w = img.getWidth(), h = img.getHeight();
		int[] imgArray = new int[w*h];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, imgArray, 0, w);
		
	      try {
	          pg.grabPixels();
	      } catch (InterruptedException e) {
	          System.err.println("interrupted waiting for pixels!");
	          return null;
	      }
	      if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
	          System.err.println("image fetch aborted or errored");
	          return null;
	      }
		
		return imgArray;
	}
	
	public static BufferedImage arrayToBufferedImage(int[] pixels, int w, int h){
		BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		img.getRaster().setDataElements(0, 0, w, h, pixels);
		return img;
	}
	
	public static BufferedImage bufferedImageARGBtoRGB(BufferedImage argb){
		int[] intargb = bufferedImageToArray(argb);
		for (int i = 0; i < intargb.length; i++){
			int red = (intargb[i] >> 16) & 0xff;
			int green = (intargb[i] >> 8) & 0xff;
			int blue = (intargb[i]) & 0xff;
			intargb[i] = combinedRGB(red, green, blue);
		}
		return arrayToBufferedImage(intargb, argb.getWidth(), argb.getHeight());
	}
	
	//replaces the pixel with 'colour'
	public static int[] setPixel(int[] pixels, int w, int h, int x, int y, int colour){
		if (((x >= 0) && (x <= w)) && ((y >= 0) && (y <= h)))
			pixels[w*y + x] = colour;
		return pixels;
	}
	
	//adds addition onto the current value of pixel, rather than changing the pixel entirely
	public static int[] addPixel(int[] pixels, int w, int h, int x, int y, int addition){
		if (((x >= 0) && (x < w)) && ((y >= 0) && (y < h)))
			pixels[w*y + x] += addition;
		return pixels;
	}
	
	//get the individual 8 bit values from the single integer value that BufferedImage uses
	public static int[] pixelRGB(int value){
		return new int[] {(value >> 16) & 0xff, (value >> 8) & 0xff, value & 0xff};
	}
	
	//convert three 8-bit values into the single integer value type that BufferedImage is using
	public static int combinedRGB(int red, int green, int blue){
		return ((red&0x0ff)<<16)|((green&0x0ff)<<8)|(blue&0x0ff);
	}
	
	//these methods are just to make the code easier to read, instead of pixel[0] you would write red(pixel), pixel[1] becomes green(pixel) etc..
	public static int red(int[] pixel){
		return pixel[0];
	}
	
	public static int green(int[] pixel){
		return pixel[1];
	}
	
	public static int blue(int[] pixel){
		return pixel[2];
	}
	
	public static int maxRGB(int r, int g, int b){
		if (r > g && r > b){
			return r;
		}else if(g > r && g > b){
			return g;
		}else{
			return b;
		}
	}
	
	public static int minRGB(int r, int g, int b){
		if (r < g && r < b){
			return r;
		}else if(g < r && g < b){
			return g;
		}else{
			return b;
		}
	}
	
	public static int[] convertToIntArray(byte[] input)
	{
	    int[] ret = new int[input.length];
	    for (int i = 0; i < input.length; i++)
	    {
	        ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
	    }
	    return ret;
	}

	
	public static int[] opGreyscale(int[] pixels, int w, int h, int method){
		switch(method){
		
		case AVERAGE:
			for (int i = 0; i < pixels.length; i++){
				int grey = (pixelRGB(pixels[i])[0] + pixelRGB(pixels[i])[1] + pixelRGB(pixels[i])[2])/3;
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case DESATURATION:
			for (int i = 0; i < pixels.length; i++){
				int max = maxRGB(pixelRGB(pixels[i])[0], pixelRGB(pixels[i])[1], pixelRGB(pixels[i])[2]);
				int min = minRGB(pixelRGB(pixels[i])[0], pixelRGB(pixels[i])[1], pixelRGB(pixels[i])[2]);
				
				int grey = (max + min)/2;
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case DECOMPOSITION_MAX:
			for (int i = 0; i < pixels.length; i++){
				int grey = maxRGB(pixelRGB(pixels[i])[0], pixelRGB(pixels[i])[1], pixelRGB(pixels[i])[2]);
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case DECOMPOSITION_MIN:
			for (int i = 0; i < pixels.length; i++){
				int grey = minRGB(pixelRGB(pixels[i])[0], pixelRGB(pixels[i])[1], pixelRGB(pixels[i])[2]);
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case RED:
			for (int i = 0; i < pixels.length; i++){
				int grey = pixelRGB(pixels[i])[0];
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case GREEN:
			for (int i = 0; i < pixels.length; i++){
				int grey = pixelRGB(pixels[i])[1];
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
			
		case BLUE:
			for (int i = 0; i < pixels.length; i++){
				int grey = pixelRGB(pixels[i])[2];
				pixels[i] = combinedRGB(grey, grey, grey);
			}
			break;
		}
		
		return pixels;
	}
	
	public static int[] opGreyscale(BufferedImage img, int w, int h, int method){
		int[] pixels = bufferedImageToArray(img);
		pixels = opGreyscale(pixels, w, h, method);
		return pixels;
	}
	
	private static int[] opThreshold(int[] pixels, int[] minThresh, int[] maxThresh){
		for (int i = 0; i < pixels.length; i++){
//			int red = red(pixelRGB(pixels[i]));
//			int green = green(pixelRGB(pixels[i]));
//			int blue = blue(pixelRGB(pixels[i]));
			if ((blue(pixelRGB(pixels[i])) >= blue(minThresh) && blue(pixelRGB(pixels[i])) <= blue(maxThresh)) && (green(pixelRGB(pixels[i])) >= green(minThresh) && green(pixelRGB(pixels[i])) <= green(maxThresh)) && (red(pixelRGB(pixels[i])) >= red(minThresh) && red(pixelRGB(pixels[i])) <= red(maxThresh))){
				pixels[i] = 0xFFFFFF;
			} else {
				pixels[i] = 0;
			}
		}
		return pixels;
	}
	
	public static int[] opThreshold(int[] pixels, int colour, int plusOrMinus){
		int[] minThresh = new int[] {red(pixelRGB(colour)) - plusOrMinus, green(pixelRGB(colour)) - plusOrMinus, blue(pixelRGB(colour)) - plusOrMinus};
		int[] maxThresh = new int[] {red(pixelRGB(colour)) + plusOrMinus, green(pixelRGB(colour)) + plusOrMinus, blue(pixelRGB(colour)) + plusOrMinus};
		for (int i = 0; i < 3; i++){
			if(minThresh[i] < 0)
				minThresh[i] = 0;
			if(maxThresh[i] > 255)
				maxThresh[i] = 255;
		}
		return opThreshold(pixels, minThresh, maxThresh);
	}
	
	public static int[] opThreshold(BufferedImage img, int[] minThresh, int[] maxThresh){
		return opThreshold(bufferedImageToArray(img), minThresh, maxThresh);
	}
	
	public static int[] opThreshold(BufferedImage img, int colour, int plusOrMinus){
		return opThreshold(bufferedImageToArray(img), colour, plusOrMinus);
	}
	
	public static int[] opCannyEdge(int[] pixels, int w, int h){
		return opCannyEdge(arrayToBufferedImage(pixels, w, h));
	}
	
	public static int[] opCannyEdge(BufferedImage img){
		BufferedImage edges = null;
		CannyEdgeDetector canny = new CannyEdgeDetector();
		canny.setEdgesImage(edges);
		canny.setSourceImage(img);
		canny.process();
		return bufferedImageToArray(canny.getEdgesImage());
	}
	
	/**
	 * Generates a series of accumulator arrays through different radii using the Hough transform for circles, and then fills the arrays with votes. <b>The algorithm <i>expects</i> the input image to be binary. Any pixel that is white (0xFFFFFF) will be analysed, the rest will not.</b>
	 * @param pixels - the image in the form int[] to begin with
	 * @param w - width
	 * @param h - height
	 * @param minRadius - the minimum suggested radius
	 * @param maxRadius - the maximum suggested radius
	 */
	public static int[] opHoughCircle(int[] pixels, int w, int h, int minRadius, int maxRadius) {
		int[][] accumulator = new int[maxRadius - minRadius + 1][pixels.length];
		int max = 0;
		int[] maxima = null;
		for (int k = 0; k < accumulator.length; k++){
			for (int i = 0; i < h; i++){
				for (int j = 0; j < w; j++){
					int[] colour = pixelRGB(pixels[i*w + j]);
					if (colour[0] == 255 && colour[1] == 255 && colour[2] == 255){
	//					accumulator = drawAdditiveCircle(accumulator, w, h, j, i, minRadius, maxRadius, 0x010101, true);
						//accumulator[k] = drawAdditiveCircle(accumulator[k], w, h, j, i, 0, minRadius + k, 1, false);
						accumulator[k] = drawAdditiveCircle(accumulator[k], w, h, j, i, minRadius + k, 1);
						//System.out.println("circle - radius: " + (minRadius + k) + "- added at " + j + "," + i);
					}
				}
			}
			maxima = findLocalMaxima(accumulator[k], w, h);
			if (accumulator[k][maxima[1]*w + maxima[0]]  > max){
				max = accumulator[k][maxima[1]*w + maxima[0]];
				maxK = k;
			}
			//System.out.println("most likely circle at " + maxMaxima[0] + "," + maxMaxima[1] + " - radius: " + (minRadius + maxK));
		}
		return accumulator[maxK];
	}
	
	/**
	 * Generates a series of accumulator arrays through different radii using the Hough transform for circles, and then fills the arrays with votes. Also with added <b>ImagePanel</b> to animate to.
	 * @param pixels - the image in the form int[] to begin with
	 * @param w - width
	 * @param h - height
	 * @param minRadius - the minimum suggested radius
	 * @param maxRadius - the maximum suggested radius
	 * @param ip - the ImagePanel of which to send images through each circle drawing phase
	 * @return the int[] form of the 2D accumulator space with the votes filled in. Where 1 = 1 vote, 2 = 2 votes etc...
	 * @see ImagePanel
	 */
	public static int[] opHoughCircle(int[] pixels, int w, int h, int minRadius, int maxRadius, ImagePanel ip, boolean debug) {
		int[][] accumulator = new int[maxRadius - minRadius + 1][pixels.length];
		int max = 0;
		int[] maxMaxima = null;
		int[] maxima = null;
		for (int k = 0; k < accumulator.length; k++){
			for (int i = 0; i < h; i++){
				for (int j = 0; j < w; j++){
					int[] colour = pixelRGB(pixels[i*w + j]);
					if (colour[0] == 255 && colour[1] == 255 && colour[2] == 255){
	//					accumulator = drawAdditiveCircle(accumulator, w, h, j, i, minRadius, maxRadius, 0x010101, true);
						//accumulator[k] = drawAdditiveCircle(accumulator[k], w, h, j, i, 0, minRadius + k, 1, false);
						accumulator[k] = drawAdditiveCircle(accumulator[k], w, h, j, i, minRadius + k, 1);
						ip.setImage(arrayToBufferedImage(accumulator[k], w, h));
						System.out.println("circle - radius: " + (minRadius + k) + "- added at " + j + "," + i);
					}
				}
			}
			maxima = findLocalMaxima(accumulator[k], w, h);
			if (accumulator[k][maxima[1]*w + maxima[0]]  > max){
				max = accumulator[k][maxima[1]*w + maxima[0]];
				maxMaxima = maxima;
				maxK = k;
			}
			System.out.println("most likely circle at " + maxMaxima[0] + "," + maxMaxima[1] + " - radius: " + (minRadius + maxK));
		}
		if (debug){
			return drawCircle(accumulator[maxK], w, h, maxMaxima[0], maxMaxima[1], minRadius + maxK, minRadius + maxK, 0xFF0000, true, 4);
		}
		else{
			return accumulator[maxK];
		}


	}
	
	public static int[] drawAdditiveCircle(int[] pixels, int w, int h, int x, int y, int r0, int r, int addition, boolean filled){
		for(int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				int dx = j - x;
				int dy = i - y;
				int distance = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				if(filled){
					if (distance <= r && distance >= r0)
						pixels[i*w + j] += addition;
				}else{
					if (distance == r)
						pixels[i*w + j] += addition;
				}
				//there has to be a faster way
//				if (filled){
//					if (dx <= r && dx >= r0 && dy <= r && dy >= r0)
//						pixels[i*w + j] += addition;
//				} else {
//					if (dy == r && dx == r){
//						pixels[i*w + j] += addition;
//					}
//				}
//				//http://en.wikipedia.org/wiki/Midpoint_circle_algorithm
//				int x1 = r;
//				int y1 = 0;
//				int radiusError = 1-x;
//				
//				while (x >= y){
//					pixels[w*(y1 + y) + (x1 + x)] += addition;
//					pixels[w*(x1 + y) + (y1 + x)] += addition;
//					pixels[w*(y1 + y) + (-x1 + x)] += addition;
//					pixels[w*(x1 + y) + (-y1 + x)] += addition;
//					pixels[w*(-y1 + y) + (-x1 + x)] += addition;
//					//pixels[w*(-x1 + y) + (-y1 + x)] += addition;
//					pixels[w*(-y1 + y) + (x1 + x)] += addition;
//					//pixels[w*(-x1 + y) + (y1 + x)] += addition;
//					y1++;
//					if (radiusError < 0)
//						radiusError += 2 * y + 1;
//					else {
//						x--;
//						radiusError += 2 * (y-x+1);
//					}
//				}
			}
		}
		return pixels;
	}
	
	public static int[] drawAdditiveCircles(int[] pixels, int w, int h, int r0, int r, int addition, boolean filled, int[][] circles){
		//similar to original circle drawing algorithm, however it draws based on how many circles are within the range
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				for(int k = 0; k < circles.length; k++){
					int dx = j - circles[k][0];
					int dy = i - circles[k][1];
					int distance = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
					if (filled){
						if (distance <= r && distance >= r0)
							pixels[i*w + j] += addition;
					}else{
						if (distance == r)
							pixels[i*w + j] += addition;
					}
				}
			}
		}
		return pixels;
	}
	static int maxK = 0;
	public static int[] drawAdditiveCircles(int[] pixels, int w, int h, int r0, int r, int addition, boolean filled, int[][] circles, ImagePanel ip){
		//similar to original circle drawing algorithm, however it draws based on how many circles are within the range
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				for(int k = 0; k < circles.length; k++){
					int dx = j - circles[k][0];
					int dy = i - circles[k][1];
					int distance = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
					if (filled){
						if (distance <= r && distance >= r0)
							pixels[i*w + j] += addition;
					}else{
						if (distance == r)
							pixels[i*w + j] += addition;
					}
					ip.setImage(arrayToBufferedImage(pixels, w, h));
				}
			}
		}
		return pixels;
	}
	
	public static int[] drawCircle(int[] pixels, int w, int h, int x, int y, int r0, int r, int colour, boolean filled){
		for(int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				int dx = j - x;
				int dy = i - y;
				int distanceFromCentre = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				if(filled){
					if (distanceFromCentre <= r && distanceFromCentre >= r0)
						pixels[i*w + j] = colour;
				}else{
					if (distanceFromCentre == r)
						pixels[i*w + j] = colour;
				}
			}
		}
		return pixels;
	}
	
	public static int[] drawCircle(int[] pixels, int w, int h, int x, int y, int r0, int r, int colour, boolean centre, int centreSize){
		for(int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				int dx = j - x;
				int dy = i - y;
				int distanceFromCentre = (int)Math.round(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				if(distanceFromCentre == r || (distanceFromCentre <= centreSize && (dx == 0 || dy == 0) && centre)){
					pixels[i*w + j] = colour;
				}
			}
		}
		return pixels;
	}
	
	/**
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param x0
	 * @param y0
	 * @param r
	 * @param addition
	 * @return a circle, using the midpoint algorithm by adding "addition" to the value
	 */
	public static int[] drawAdditiveCircle(int[] pixels, int w, int h, int x0, int y0, int r, int addition){
		int x = r;
		int y = 0;
		int radiusError = 1-x;
		while (x >= y){
			addPixel(pixels, w, h, x + x0, y + y0, addition);
			addPixel(pixels, w, h, y + x0, x + y0, addition);
			addPixel(pixels, w, h, -x + x0, y + y0, addition);
			addPixel(pixels, w, h, -y + x0, x + y0, addition);
			addPixel(pixels, w, h, -x + x0, -y + y0, addition);
			addPixel(pixels, w, h, -y + x0, -x + y0, addition);
			addPixel(pixels, w, h, x + x0, -y + y0, addition);
			addPixel(pixels, w, h, y + x0, -x + y0, addition);
			y++;
			if (radiusError < 0){
				radiusError += 2 * y + 1;
			}else{
				x--;
				radiusError += 2 * (y - x + 1);
			}
		}
		
		return pixels;
	}
	
	/**
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param x0
	 * @param y0
	 * @param r
	 * @param colour
	 * @return a circle, using the midpoint algorithm by replacing the pixel value with "colour".
	 */
	public static int[] drawCircle(int[] pixels, int w, int h, int x0, int y0, int r, int colour){
		int x = r;
		int y = 0;
		int radiusError = 1-x;
		while (x >= y){
			setPixel(pixels, w, h, x + x0, y + y0, colour);
			setPixel(pixels, w, h, y + x0, x + y0, colour);
			setPixel(pixels, w, h, -x + x0, y + y0, colour);
			setPixel(pixels, w, h, -y + x0, x + y0, colour);
			setPixel(pixels, w, h, -x + x0, -y + y0, colour);
			setPixel(pixels, w, h, -y + x0, -x + y0, colour);
			setPixel(pixels, w, h, x + x0, -y + y0, colour);
			setPixel(pixels, w, h, y + x0, -x + y0, colour);
			y++;
			if (radiusError < 0){
				radiusError += 2 * y + 1;
			}else{
				x--;
				radiusError += 2 * (y - x + 1);
			}
		}
		
		return pixels;
	}
	
	/**
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the coordinates of the maximum value in pixels[]
	 */
	public static int[] findLocalMaxima(int[] pixels, int w, int h, int x1, int y1, int x2, int y2){
		int [] maxima = new int[2];
		maxima[0] = 0;
		maxima[1] = 0;
		int maximum = 0;
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				if ((i > y1 && i < y2 && j > x1 && j < x2) & (pixels[i*w + j] >= maximum)){
					maximum = pixels[i*w + j];
					maxima[0] = j;
					maxima[1] = i;
				}
			}
		}
		return maxima;
	}
	
	/**
	 * 
	 * @param pixels
	 * @param w
	 * @param h
	 * @return the coordinates of the maximum value in pixels[] in the form of an int[2]
	 */
	public static int[] findLocalMaxima(int[] pixels, int w, int h){
		int [] maxima = new int[2];
		maxima[0] = 0;
		maxima[1] = 0;
		int maximum = 0;
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				if (pixels[i*w + j] >= maximum){
					maximum = pixels[i*w + j];
					maxima[0] = j;
					maxima[1] = i;
				}
			}
		}
		return maxima;
	}
	
	/**
	 * 
	 * @param background
	 * @param frame
	 * @param threshold
	 * @return returns an image (frame) subtracted by another (background) but more than the threshold value, any less, and the pixel is set to 0.
	 */
	public static int[] subtractBackground(int[] background, int[] frame, int threshold) {
		int[] next = new int[background.length];
		for (int i = 0; i < background.length; i++){
			if (background[i] - frame[i] < threshold){
				next[i] = frame[i];
			} else {
				next[i] = 0;
			}
		}
		return next;
	}
	
	
	/**
	 * @param data
	 * @param w
	 * @param h
	 * @return a Vector2f representing the centroid (midpoint) of a collection of integers (with respect to the width and height of the image in question) which are not zero.
	 */
	public static Vector2f centroid(int[] data, int w, int h){
		int X = 0, Y = 0, n = 0;
		for (int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				if (data[i*h + j] != 0){ 
					X += j;
					Y += i;
					n++;
				}
			}
		}
		
		X /= n;
		Y /= n;
		return new Vector2f(X,Y);
	}
	
	public static int firstInstanceOf(int[] data, int instance) {
		for (int i : data)
			if (i == instance)
				return i;
		return -1;	//error instance
	}
	
	public static int firstInstanceOf(int[] data, int w, int h, int instance, int y){
		for (int i = 0; i < w; i++){
			if (y <= h && data[y*w + i] == instance)
				return i;
		}
		return -1;	//error instance
	}
	
	public static Vector2i firstInstanceOf(int[] data, int w, int h, int instance) {
		for (int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				if (data[i*w + j] == instance)
					return new Vector2i(j, i);
			}
		}
		return null; //error instance
	}
	
	public static int occurrences(int[] data, int instance) {
		int n = 0;
		for (int i = 0; i <data.length; i++)
			if (data[i] >= instance)
				n++;
		return n;
	}
	
	public static int occurrences(int[] data, int w, int h, int instance) {
		int n = 0;
		for (int i = 0; i < w; i++)
			if (data[i] == instance)
				n++;
		return n;
	}
	
	public static int occurrences(int data[], int instance, int tolerence) {
		int[] thresh = opThreshold(data, instance, tolerence);
		int n = 0;
		for (int i = 0; i < data.length; i++)
			if (thresh[i] != 0)
				n++;
		return n;
	}
	
	public static int occurrences(int[] data, int w, int h, int instance, int tolerence, int y) {
		int[] thresh = opThreshold(data, instance, tolerence);
		int n = 0;
		for (int i = 0; i < w; i++){
			if (thresh[y*w + i] != 0)
				n++;
		}
		return n;
	}
	
	
}