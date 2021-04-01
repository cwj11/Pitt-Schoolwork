
public class City {
	public double x;
	public double y;
	public String name;
	public int num;
	private static int cityNum = 0;
	
	public City(String n, double x, double y) {
		this.x = x;
		this.y = y;
		name = n;
		num = cityNum++;
	}
}
