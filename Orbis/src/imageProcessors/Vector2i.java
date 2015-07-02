package imageProcessors;

public class Vector2i {
	private int x, y;
	
	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i r){
		return new Vector2i(x + r.getX(), y + r.getY());
	}
	
	public Vector2i sub(Vector2i r){
		return new Vector2i(x - r.getX(), y - r.getY());
	}
	
	public Vector2i mult(int r){
		return new Vector2i(x * r, y * r);
	}
	
	public Vector2i div(int r){
		return new Vector2i(Math.round(x / r), Math.round(y / r));
	}
	
	public Vector2i div(float r){
		return new Vector2i(Math.round(x / r), Math.round(y / r));
	}
	
	public int dot(Vector2i r){
		return x*r.x + y*r.y;
	}
	
	public Vector2i normalize(){
		return this.div(getLength());
	}
	
	public float getLength(){
		return (float) Math.sqrt(getX() * getX() + getY() * getY());
	}
	
	public int getX(){
		return x;
	};
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public String toString() {
		return (this.x + ", " + this.y);
	}
	
}
