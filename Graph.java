package graph;

/* Tsipora Stone
 * 114110213
 * 0108
 * I pledge on my honor that I have not given or received
 * any unauthorized assistance on this assignment*/

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

// manipulates a graph using HashMaps 
public class Graph<V> {

	private HashMap<V, HashMap<V, Integer>> vertices = new HashMap<V, HashMap<V, Integer>>(); 

	public Graph() {

	}

	// creates a new vertex and returns true
	public boolean addVertex(V vertexToAdd) {
		if (vertexToAdd == null) {
			throw new IllegalArgumentException();
		}
		if (vertices.containsKey(vertexToAdd)) {
			return false;
		}
		vertices.put(vertexToAdd, new HashMap<V, Integer>());
		return true;
	}

	// checks if the vertex exists in the vertex map
	public boolean isVertex(V vertexToCheck) {
		if (vertexToCheck == null) {
			throw new IllegalArgumentException();
		}
		return vertices.containsKey(vertexToCheck);
	}

	// returns an independent collection with the vertices in the graph
	public Collection<V> getVertices() {
		Collection<V> copy = new HashSet<>();
		// makes an independent collection
		for (V vertex: vertices.keySet()) {
			copy.add(vertex);
		}
		return copy;
	}

	// removes the vertex from the graph and its edges and returns true
	public boolean removeVertex(V vertexToRemove) {
		if (vertexToRemove == null) {
			throw new IllegalArgumentException();
		}
		if (isVertex(vertexToRemove)) { 
			// iterates through the vertices and then the edges of each vertex
			Collection<V> removed = new HashSet<>();
			for (V vertex: vertices.keySet()) {
				if(vertices.get(vertex).keySet().contains(vertexToRemove)) {
					removeEdge(vertex, vertexToRemove);
				}
			}
			vertices.remove(vertexToRemove);
			return true;
		}
		return false;
	}

	// adds an edge to the vertex in the graph and returns true
	public boolean createEdge(V from, V to, int cost) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		if (cost >= 0) {
			// adds vertex from to graph if it doesn't exist
			if (!(isVertex(from))) {
				addVertex(from);
			}
			// adds vertex to to graph if it doesn't exist
			if (!(isVertex(to))) {
				addVertex(to);
			}
			// finds the vertex that equals from in the graph and adds edge
			for (V vertex: vertices.keySet()) {
				if (vertex.equals(from)) {
					vertices.get(from).put(to, cost);
					return true;
				}
			}
		}
		return false;
	}

	// returns the cost of the edge between from and to
	public int costOfEdge(V from, V to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		int cost = -1;
		if (isVertex(from) && isVertex(to) && vertices.get(from).containsKey(to)) {
			cost = vertices.get(from).get(to);
		}
		return cost;
	}

	// changes the edge cost at vertex from to the new edge cost and returns true
	public boolean changeEdgeCost(V from, V to, int newCost) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		if (newCost >= 0 && isVertex(from) && isVertex(to) 
				&& vertices.get(from).containsKey(to)) {
			vertices.get(from).put(to, newCost);
			return true;
		}
		return false;
	}

	// removes the edge between vertices from and to
	public boolean removeEdge(V from, V to) {
		if (from == null || to == null) {
			throw new IllegalArgumentException();
		}
		if (isVertex(from) && isVertex(to) && vertices.get(from).containsKey(to)) {
			vertices.get(from).remove(to);
			return true;
		}
		return false;
	}

	// returns a collection of the neighbors of the vertex in the graph
	public Collection<V> neighborsOfVertex(V sourceVertex) {
		if (sourceVertex == null) {
			throw new IllegalArgumentException();
		}
		Collection<V> neighbors = new HashSet<>();
		if (isVertex(sourceVertex)) {
			for (V vertex: vertices.get(sourceVertex).keySet()) {
				neighbors.add(vertex);
			}
		}
		return neighbors;
	}

	// returns a collection of all the reachable vertices from the a vertex
	public Collection<V> reachable(V from) {
		if (from == null) {
			throw new IllegalArgumentException();
		}
		Collection<V> reachableVertices = new HashSet<>();
		int sizeDif = 1;
		if (isVertex(from)) {
			reachableVertices.add(from);
			// while all the vertices haven't been added to the collection
			while (sizeDif != 0) {
				int begSize = reachableVertices.size();
				HashSet<V> added = new HashSet<>();
				// adds all the neighbors of the vertex to the collection and iterates
				// through those vertices 
				for (V vertex: reachableVertices) {
					added.addAll(neighborsOfVertex(vertex));
				}
				reachableVertices.addAll(added);
				// updates sizeDif 
				sizeDif = reachableVertices.size() - begSize;
			}
		}
		return reachableVertices;
	}

	// returns a new graph with the vertices and edges from the old graph
	// included in verticesForNewGraph collection
	public Graph<V> divideGraph(Collection<V> verticesForNewGraph) {
		if (verticesForNewGraph == null) {
			throw new IllegalArgumentException();
		}
		Graph<V> newGraph = new Graph<>();
		HashSet<V> removed = new HashSet<>();
		// goes through verticesForNewGraph and adds those vertices to the 
		// new graph
		for (V vertex: verticesForNewGraph) {
			if (vertices.containsKey(vertex)) {
				newGraph.addVertex(vertex);
				for (V v: vertices.get(vertex).keySet()) {
					if (verticesForNewGraph.contains(v)) {
						newGraph.createEdge(vertex, v, vertices.get(vertex).get(v));
					}
				}
			}
		}
			// removes the vertices from the old graph
			for (V vertexToRemove: verticesForNewGraph) {
				if (vertices.containsKey(vertexToRemove)) {
					this.removeVertex(vertexToRemove);
				}
			}
			return newGraph;
		}
	}
