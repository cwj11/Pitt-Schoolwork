import java.util.ArrayList;

public class EdgeWeightedGraph {
	private int V;
	private int E;
	private ArrayList<Edge>[] adj;
	private ArrayList<City> cities;
	
	public EdgeWeightedGraph(ArrayList<City> cities) {
		V = cities.size();
		E = cities.size() * (cities.size() - 1);
		this.cities = new ArrayList<City>();
		adj = (ArrayList<Edge> []) new ArrayList[V];
		for(int i = 0; i < cities.size(); i++) {
			adj[i] = new ArrayList<Edge>();
			for(int j = 0; j < cities.size(); j++) {
				if(i != j)
					adj[i].add(new Edge(cities.get(i), cities.get(j)));
			}
		}
		this.cities = cities;
	}
	
	public void toMST(ArrayList<Edge> edges) {
		for(int i = 0; i < adj.length; i++) {
			adj[i] = new ArrayList<Edge>();
		}
		for(int i = 0; i < edges.size(); i++) {
			adj[edges.get(i).city1.num].add(edges.get(i));
			adj[edges.get(i).city2.num].add(edges.get(i));
		}
	}
	
	public ArrayList<Edge> edges(){
		ArrayList<Edge> e = new ArrayList<Edge>();
		for(int i = 0; i < adj.length; i++) {
			e.addAll(adj[i]);
		}
		return e;
	}
	
	public int V() {
		return V;
	}
	
	public ArrayList<Edge> adj(int v){
		return adj[v];
	}
	
	public City getCity(int c) {
		return cities.get(c);
	}
}
