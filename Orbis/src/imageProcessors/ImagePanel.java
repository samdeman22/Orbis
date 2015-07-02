package imageProcessors;


//import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.Ellipse2D;
//import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	//preview items
	private boolean circle;
	private boolean center;
	private int radius, drawX, drawY;
	
	public ImagePanel(){
		image = null;
		radius = 10;	//default preview values
		drawX = 0;
		drawY = 0;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		g.clearRect(0, 0, getWidth(), getHeight());
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(new Color(0xFFFFFF));
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		
//		if (circle)
//			g2d.draw(new Ellipse2D.Double(x, y, radius, radius));
//		if (center)
//			g2d.draw(new Line2D.Double(x-3, y-3, x+3, y+3));
	}
	
	public int getRadius(){
		return radius;
	}
	
	public void setRadius(int radius){
		this.radius = radius;
	}
	
	public int getX(){
		return drawX;
	}
	
	public void setX(int x){
		this.drawX = x;
	}
	
	public int getY(){
		return drawY;
	}
	
	public void setY(int y){
		this.drawY = y;
	}
	
	public void setCenterPreview(boolean r){
		center = r;
	}
	
	public boolean getCenterPreview(){
		return center;
	}
	
	public void setCirclePreview(boolean r){
		circle = r;
	}
	
	public boolean getCirclePreview(){
		return circle;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public void setImage(BufferedImage bufferedImage) {
		image = bufferedImage;
		//drawX = image.getWidth() / 2;
		//drawY = image.getHeight() / 2;
		repaint();
	}

}
