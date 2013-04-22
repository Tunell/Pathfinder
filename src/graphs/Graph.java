package graphs;
import java.util.*;

public interface Graph <T>{
	public void add(T node);
	public void connect(T from, T to, String name, int weight);
	public void setConnectionWeight(T from, T to, String name, int weight);
	public Set<T> getNodes();
	public Set<Edge<T>> getEdgesFrom(T from);
	public List<Edge<T>> getEdgesBetween(T from, T to);
	public String toString();
	//public boolean pathExists(T from, T to);
}
