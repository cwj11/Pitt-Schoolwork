from search import *
from utils import *

class Graph:
    """A graph connects nodes (vertices) by edges (links). Each edge can also
    have a length associated with it. The constructor call is something like:
        g = Graph({'A': {'B': 1, 'C': 2})
    this makes a graph with 3 nodes, A, B, and C, with an edge of length 1 from
    A to B,  and an edge of length 2 from A to C. You can also do:
        g = Graph({'A': {'B': 1, 'C': 2}, directed=False)
    This makes an undirected graph, so inverse links are also added. The graph
    stays undirected; if you add more links with g.connect('B', 'C', 3), then
    inverse link is also added. You can use g.nodes() to get a list of nodes,
    g.get('A') to get a dict of links out of A, and g.get('A', 'B') to get the
    length of the link from A to B. 'Lengths' can actually be any object at
    all, and nodes can be any hashable object."""

    def __init__(self, graph_dict=None, directed=True):
        self.graph_dict = graph_dict or {}
        self.directed = directed
        if not directed:
            self.make_undirected()

    def make_undirected(self):
        """Make a digraph into an undirected graph by adding symmetric edges."""
        for a in list(self.graph_dict.keys()):
            for (b, dist) in self.graph_dict[a].items():
                self.connect1(b, a, dist)

    def connect(self, A, B, distance=1):
        """Add a link from A and B of given distance, and also add the inverse
        link if the graph is undirected."""
        self.connect1(A, B, distance)
        if not self.directed:
            self.connect1(B, A, distance)

    def connect1(self, A, B, distance):
        """Add a link from A to B of given distance, in one direction only."""
        self.graph_dict.setdefault(A, {})[B] = distance

    def get(self, a, b=None):
        """Return a link distance or a dict of {node: distance} entries.
        .get(a,b) returns the distance or None;
        .get(a) returns a dict of {node: distance} entries, possibly {}."""
        links = self.graph_dict.setdefault(a, {})
        if b is None:
            return links
        else:
            distance = links.get(b)
            delta_e = self.elevation[a] - \
                self.elevation[b]
            t =  distance / (15 - (0.01 * delta_e))
            return t

    def nodes(self):
        """Return a list of nodes in the graph."""
        s1 = set([k for k in self.graph_dict.keys()])
        s2 = set([k2 for v in self.graph_dict.values() for k2, v2 in v.items()])
        nodes = s1.union(s2)
        return list(nodes)

def UndirectedGraph(graph_dict=None):
    """Build a Graph where every edge (including future ones) goes both ways."""
    return Graph(graph_dict=graph_dict, directed=False)


def main():
    global romania_map
    romania_map = UndirectedGraph(dict(
        Arad=dict(Zerind=75, Sibiu=140, Timisoara=118),
        Bucharest=dict(Urziceni=85, Pitesti=101, Giurgiu=90, Fagaras=211),
        Craiova=dict(Drobeta=120, Rimnicu=146, Pitesti=138),
        Drobeta=dict(Mehadia=75),
        Eforie=dict(Hirsova=86),
        Fagaras=dict(Sibiu=99),
        Hirsova=dict(Urziceni=98),
        Iasi=dict(Vaslui=92, Neamt=87),
        Lugoj=dict(Timisoara=111, Mehadia=70),
        Oradea=dict(Zerind=71, Sibiu=151),
        Pitesti=dict(Rimnicu=97),
        Rimnicu=dict(Sibiu=80),
        Urziceni=dict(Vaslui=142)))

    romania_map.locations = dict(
        Arad=(91, 492), Bucharest=(400, 327), Craiova=(253, 288),
        Drobeta=(165, 299), Eforie=(562, 293), Fagaras=(305, 449),
        Giurgiu=(375, 270), Hirsova=(534, 350), Iasi=(473, 506),
        Lugoj=(165, 379), Mehadia=(168, 339), Neamt=(406, 537),
        Oradea=(131, 571), Pitesti=(320, 368), Rimnicu=(233, 410),
        Sibiu=(207, 457), Timisoara=(94, 410), Urziceni=(456, 350),
        Vaslui=(509, 444), Zerind=(108, 531))

    romania_map.elevation = dict(
        Arad=117, Bucharest=91, Craiova=100,
        Drobeta=200, Eforie=50, Fagaras=78,
        Giurgiu=159, Hirsova=140, Iasi=85,
        Lugoj=110, Mehadia=103, Neamt=50,
        Oradea=50, Pitesti=102, Rimnicu=250,
        Sibiu=132, Timisoara=56, Urziceni=129,
        Vaslui=95, Zerind=101)

	# test case
    find_path('Arad', 'Bucharest', 'astar_graph')

def find_path(source, destination, algorithm):
    romania_problem = GraphProblem(source, destination, romania_map)
    print(romania_map.locations(source))
    result = None
    if algorithm == 'astar_graph':
        
        pass
    elif algorithm == 'astar_tree':
        pass
    elif algorithm == 'id_astar':
        pass
    elif algorithm == 'id_dfs':
        pass
    else:
        print('Unkown algorithm')

    if not result is None:
        soln_path = [source] + result.solution()
        soln_cost = round(result.path_cost, 2)
        soln_nodes = result.nodesExpanded
        print ('algorithm: {} | path: {} | cost: {} | nodes: {}'.format(algorithm, soln_path,
            soln_cost, soln_nodes))
    return result

if __name__ == '__main__':
    global romania_map
    main()

def heuristic(source, destination):
    pass
