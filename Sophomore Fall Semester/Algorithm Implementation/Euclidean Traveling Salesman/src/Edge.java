
public class Edge {
	public City city1;
	public City city2;
	public double length;
	
	public Edge(City c1, City c2) {
		city1 = c1;
		city2 = c2;
		length = Math.sqrt(Math.pow(Math.abs(city1.x-city2.x), 2) + Math.pow(Math.abs(city1.y-city2.y), 2));
	}
	
	public int other(int v) {
		if(v == city1.num)
			return city2.num;
		if(v == city2.num)
			return city1.num;
		return -1;
	}
}
