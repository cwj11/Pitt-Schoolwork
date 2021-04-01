
public class Pattern {
	
	private int width;
	private int height;
	private int value;
	private String type;
	
	public Pattern(int width, int height, int value, String type) {
		this.width = width;
		this.height = height;
		this.value = value;
		this.type = type;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public int value() {
		return value;
	}
	
	public String type() {
		return type;
	}
	

}
