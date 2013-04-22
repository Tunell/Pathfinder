package graphs;
public class Dijkstras<T>{
	private int weight = Integer.MAX_VALUE-20;
	private boolean settled = false;
	private String name;
	private T origin;
	
	public Dijkstras(){		
	}
	
	public String getName(){
		return name;
	}
	
	public T getOrigin(){
		return origin;
	}
	
	public void setOrigin(String name, T o){
		this.name = name;
		this.origin = o;
	}
	
	public int getNodeWeight(){
		return weight;
	}
	
	public void setNodeWeight(int value){
		this.weight = value;
	}
	
	public boolean settled(){
		return settled;
	}
	
	public void settle(){
		this.settled = true;
	}
}