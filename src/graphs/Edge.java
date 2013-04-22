package graphs;

public class Edge <T>{
	private T destination;
	private String name;
	private int weight;

	public Edge(T d, String n, int w){
		if (w<0)
			throw new IllegalArgumentException("Weight of edge may not be negative");
		destination = d;
		name = n;
		weight = w;
	}
	
	public T getDestination(){
		return destination;
	}
	
	public int getWeight(){
		return weight;
	}
	
	public void setWeight(int w){
		if (w<0)
			throw new IllegalArgumentException("Weight of edge may not be negative");
		weight = w;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return "till " + destination + " med " + name + " tar " + weight;
	}
}

