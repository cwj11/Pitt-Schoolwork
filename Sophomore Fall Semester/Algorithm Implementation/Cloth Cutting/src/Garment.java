
public class Garment implements Comparable<Garment> {
	private Pattern pattern;
	private int x;
	private int y;
	
	public Garment(Pattern pattern, int x, int y){
		this.pattern = pattern;
		this.x = x;
		this.y = y;
	}
	
	public int x() {
		return x;
	}
	
	
	public int y() {
		return y;
	}
	
	public Pattern pattern() {
		return pattern;
	}
	
	public String toString() {
		return "[" + pattern.type() + "," + x + "," + y + "]";
	}
	
	public int compareTo(Garment other) {
		if(this.y() < other.y())
			return -1;
		else if(this.y() > other.y())
			return 1;
		else {
			if(this.x() < other.x())
				return -1;
			else if(this.x() > other.x())
				return 1;
			else
				return 0;
		}
	}
}
