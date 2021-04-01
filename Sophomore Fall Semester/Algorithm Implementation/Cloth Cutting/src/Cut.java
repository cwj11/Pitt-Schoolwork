
public class Cut implements Comparable<Cut>{
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	
	public Cut(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public int x1() {
		return x1;
	}
	
	public int x2() {
		return x2;
	}
	
	public int y1() {
		return y1;
	}
	
	public int y2() {
		return y2;
	}
	
	public String toString() {
		return "[" + x1 + "," + y1 + "," + x2 + "," + y2 + "]";
	}
	
	public int compareTo(Cut other) {
		if(this.x2() - this.x1() > other.x2() - other.x1() || this.y2() - this.y1() > other.y2() - other.y1())
			return -1;
		else if(this.x2() - this.x1() < other.x2() - other.x1() || this.y2() - this.y1() < other.y2() - other.y1())
			return 1;
		else {
			if(this.y1() < other.y1())
				return -1;
			else if(this.y1() > other.y1())
				return 1;
			else {
				if(this.x1() < other.x1())
					return -1;
				else if(this.x1() > other.x1())
					return 1;
				else
					return 0;
			}
		}
	}
}
