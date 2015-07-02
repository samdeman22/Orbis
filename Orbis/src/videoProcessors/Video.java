package videoProcessors;

import java.awt.image.BufferedImage;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class Video {
	
	private int width, height, frames;
	private double fps;
	private FFmpegFrameGrabber grab;
	
	public Video(String videoDir){
		grab = new FFmpegFrameGrabber(videoDir);
		if (grab == null){
			System.err.println("failed to create OpenCV frameGrabber");
			return;
		}
		start();
		width = grab.getImageWidth();
		height = grab.getImageHeight();
		frames = grab.getLengthInFrames();
		fps = grab.getFrameRate();
		stop();
//		try {
//			grab.start();
//			width = grab.getImageWidth();
//			height = grab.getImageHeight();
//			frames = grab.getLengthInFrames();
//			video = new BufferedImage[frames];
//			
//			for (int i = 0; i < frames; i++){
//				BufferedImage frame = grab.grab().getBufferedImage();
//				if (frame.equals(null)){
//					System.err.println("Failed to grab frame " + i);
//					return;
//				}
//				
//				video[i] = frame;
//			}
//			
//			grab.stop();
//			
//		} catch(Exception e) {
//			System.err.println("Exception! " + e.getMessage());
//		}

		
	}
	
	public BufferedImage grab(){
		try {
			return grab.grab().getBufferedImage();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void start(){
		try {
			grab.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			grab.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getFrame() {
		return grab.getFrameNumber();
	}
	
	public void setFrame(int frame) {
		try {
			grab.setFrameNumber(frame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getFrames(){
		return frames;
	}
	
	public double getFrameRate(){
		return fps;
	}
	
	public double getLength(){
		return grab.getLengthInTime() / 1000000;
	}
}
