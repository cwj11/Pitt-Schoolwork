import java.util.ArrayList;

public class EuclideanTSP {

	private static double[] dist;
	private static Edge[] edgeTo;
	private static boolean[]  marked;
	private static IndexMinPQ<Double> queue;
	private static EdgeWeightedGraph g;
	
	public static ArrayList<Edge> MST(ArrayList<City> cities){
		g = new EdgeWeightedGraph(cities);
		dist = new double[g.V()];
		marked = new boolean[g.V()];
		edgeTo = new Edge[g.V()];
		queue = new IndexMinPQ<Double>(g.V());
		
		for(int i = 0; i < dist.length; i++) {
			dist[i] = Double.MAX_VALUE;
		}
		
		dist[0] = 0.0;
		queue.insert(0, 0.0);
		while(!queue.isEmpty())
			visit(queue.delMin());
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < edgeTo.length; i++) {
			if(edgeTo[i] != null) {
				edges.add(edgeTo[i]);
			}
		}
		return edges;
		
	}
	
	private static void visit(int v) {
		marked[v] = true;
		for(Edge e: g.adj(v)) {
			int w = e.other(v);
			if(marked[w])
				continue;
			if(e.length < dist[w]) {
				edgeTo[w] = e;
				dist[w] = e.length;
				if(queue.contains(w))
					queue.changeKey(w, dist[w]);
				else
					queue.insert(w,  dist[w]);
			}
		}
	}
	
	public static ArrayList<City> MSTTour(ArrayList<Edge> edges){
		g.toMST(edges);
		marked = new boolean[g.V()];
		ArrayList<City> tour = new ArrayList<City>();
		preorder(tour, 0);
		tour.add(g.getCity(0));
		return tour;
		
		
	}
	
	private static void preorder(ArrayList<City> cities, int c) {
		marked[c] = true;
		cities.add(g.getCity(c));
		for(Edge e: g.adj(c)) {
			if(!marked[e.other(c)]) {
				preorder(cities, e.other(c));
			}
		}
	}
	
	
	
	public static double length(ArrayList<City> tour) {
		double length = 0.0;
		for(int i = 0; i < tour.size() - 1; i++) {
			length += Math.sqrt(Math.pow(Math.abs(tour.get(i).x-tour.get(i+1).x), 2) + Math.pow(Math.abs(tour.get(i).y-tour.get(i+1).y), 2));
		}
//		length += Math.sqrt(Math.pow(Math.abs(tour.get(0).x-tour.get(tour.size()-1).x), 2) + Math.pow(Math.abs(tour.get(0).y-tour.get(tour.size()-1).y), 2));
		return length;
	}
	
	
	
	
	public static double weight(ArrayList<Edge> mst) {
		double w = 0.0;
		for(Edge e: mst) {
			w += e.length;
		}
		return w;
	}
}
