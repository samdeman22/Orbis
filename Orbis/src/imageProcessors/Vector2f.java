package imageProcessors;

public class Vector2f {
private float x, y;
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2f add(Vector2f r){
		return new Vector2f(x + r.getX(), y + r.getY());
	}
	
	public Vector2f sub(Vector2f r){
		return new Vector2f(x - r.getX(), y - r.getY());
	}
	
	public Vector2f mult(float r){
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f div(float r){
		return new Vector2f(x / r, y / r);
	}
	
	
	public Vector2f normalize(){
		return this.div(getLength());
	}
	
	public float getLength(){
		return (float) Math.sqrt(getX() * getX() + getY() * getY());
	}
	
	public float getX(){
		return x;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public String toString() {
		return (this.x + ", " + this.y);
	}
}
