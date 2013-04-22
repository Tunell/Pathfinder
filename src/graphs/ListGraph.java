package graphs;

import java.util.*;
public class ListGraph<T> implements Graph<T>{
	private Map<T, List<Edge<T>>> nodes = new HashMap<T, List<Edge<T>>>();
	
	public void add(T node){
		nodes.put(node, new ArrayList<Edge<T>>());
	}
	
	public void connect(T from, T to, String name, int weight){
		if(!nodes.containsKey(from) || !nodes.containsKey(to))
			throw new NoSuchElementException("Node does not exist in Graph during connect");
		List<Edge<T>> flist = nodes.get(from);
		for(Edge<T> temp : flist)
			if (temp.getDestination() == to && temp.getName() == name)
				throw new IllegalStateException("Edge allready exists");
		List<Edge<T>> tlist = nodes.get(to);
		Edge<T> e1 = new Edge<T>(to,name,weight);
		flist.add(e1);
		Edge<T> e2 = new Edge<T>(from,name,weight);
		tlist.add(e2);
	}
	
	public void setConnectionWeight(T from, T to, String name, int weight){
		if(!nodes.containsKey(from) || !nodes.containsKey(to))
			throw new NoSuchElementException("Node does not exist in Graph during connect");
		List<Edge<T>> flist = nodes.get(from);
		if (flist.contains(getEdgesBetween(from,to).get(0))){
			for(Edge<T> temp : flist){
				if (temp.getDestination().equals(to) && temp.getName().equals(name)){
					temp.setWeight(weight);
				}
			}
		}
		else throw new NoSuchElementException("Edge dosen't exists");
		List<Edge<T>> tlist = nodes.get(to);
		if (tlist.contains(getEdgesBetween(to,from).get(0))){
			for(Edge<T> temp : tlist)
				if (temp.getDestination().equals(from) && temp.getName().equals(name))
					temp.setWeight(weight);
		}
		else throw new NoSuchElementException("Edge dosen't exists");

	}
	
	public Set<T> getNodes(){
		Set<T> allNodes = nodes.keySet();
		Set<T> copyNodes = allNodes;
		return copyNodes;
	}
	
	public Set<Edge<T>> getEdgesFrom(T from){
		return new HashSet<Edge<T>>(nodes.get(from));
	}

	public List<Edge<T>> getEdgesBetween(T from, T to){
		List<Edge<T>> flist = nodes.get(from);
		List<Edge<T>> edgesBetween = new ArrayList<Edge<T>>();
		boolean foundElement = false;
		for(Edge<T> temp : flist)
			if (temp.getDestination() == to){
				edgesBetween.add(temp);
				foundElement = true;	
			}
		if (foundElement == false)
			throw new NoSuchElementException("Edge dosen't exists");
		return edgesBetween;
	}
	
	public String toString(){
		String str = "";
		for(Map.Entry<T, List<Edge<T>>> me : nodes.entrySet()){
			str += me.getKey() + ": ";
			for(Edge<T> e : me.getValue())
				str+= e.toString() + " ";
			str += "\n";
		}
		return str;
	}


	
}
