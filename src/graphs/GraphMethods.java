package graphs;

import java.util.*;
import java.util.Map.Entry;

public class GraphMethods<T>{
	private Map<T, Dijkstras<T>> searchNodes = new HashMap<T, Dijkstras<T>>();
	private List<T> fastPath = new ArrayList<T>();
	private List<T> tempFastPath = new ArrayList<T>();
	private List<Edge<T>> edgePath = new ArrayList<Edge<T>>();
	private int totalWeight;

	public boolean pathExists(Graph<T> g, T from, T to) {
		Set<T> visited = new HashSet<T>();
		depthFirstSearch(g, from, visited);
		return visited.contains(to);
	}
	
	private void depthFirstSearch(Graph<T> g, T from, Set<T> visited){
		visited.add(from);
		for( Edge<T> i : g.getEdgesFrom(from))
			if (!visited.contains(i.getDestination()))
				depthFirstSearch(g, i.getDestination(),visited);
	}
	
	public List<Edge<T>> shortestPath(Graph<T> g, T from, T to){
		if(pathExists(g,from,to)){
			for( T i : g.getNodes())
				searchNodes.put(i,new Dijkstras<T>());
			searchNodes.get(from).setNodeWeight(0);
			searchNodes.get(from).setOrigin(from.toString(), from);
			T iter = from;
			while ( !(searchNodes.get(to).settled()) ){
				searchNodes.get(iter).settle();
				//jämför nuvarande weight värde hos to och jämför det med from+edge.getweight och spara det.
				for( Edge<T> j : g.getEdgesFrom( iter ) ){
					T i = j.getDestination();
					if(!(searchNodes.get(i) == null) && !(searchNodes.get(i).settled() )){
						int testWeight = searchNodes.get(iter).getNodeWeight() + j.getWeight();
						if( testWeight < searchNodes.get(i).getNodeWeight() ){
							searchNodes.get(i).setOrigin(searchNodes.get(iter).toString(),iter);	
							searchNodes.get(i).setNodeWeight(testWeight);
						}
					}
				}
					//plocka fram den med lägst weight som inte är settled och sätt den.
					int minstaWeight = Integer.MAX_VALUE;
					T minsta = null;
					for(Entry<T, Dijkstras<T>> i : searchNodes.entrySet())
						if( i.getValue().getNodeWeight() < minstaWeight && !(i.getValue().settled())){
							minstaWeight = i.getValue().getNodeWeight();
							minsta = i.getKey();
						}
					iter = minsta;
			}
			//adera alla nodes av den snabbaste vägen till en lista
			//detta är gjort på ett oerhört klumpigt sätt, men det funkar och jag hinner inte bygga om det.
			iter = to;
			tempFastPath.add(to);
			while( iter != from ){
				iter = searchNodes.get(iter).getOrigin();
				tempFastPath.add(iter);
			}
			//vänd på listan av nodes
			for( int i = tempFastPath.size()-1; i >= 0; i-- ){
				fastPath.add(tempFastPath.get(i));
			}
			//gör en lista av Edges

			totalWeight = 0;
			for( int i = 0; i < fastPath.size()-1; i++ ){
				T f = fastPath.get(i);
				T t = fastPath.get(i+1);
				Edge<T> shortestWay = null;
				int lowestWeight = Integer.MAX_VALUE;
				for( Edge<T> j : g.getEdgesBetween( f,t ) ){
					if( j.getWeight() < lowestWeight){
						lowestWeight = j.getWeight();
						shortestWay = j;
					}
					else;
					
				}

				totalWeight +=shortestWay.getWeight();
				edgePath.add(shortestWay);
			}
			return edgePath;// borde även retunera en total Weight vilket jagi nte hunnit
		}
		//throw new det finns ingen väg exception
		List<Edge<T>> error = new ArrayList<Edge<T>>();
		return error;
	}
	
	public String getTotalWeight(){
		return "Totalt: "+ totalWeight;
	}
	
	//String writeShortestPath
}
