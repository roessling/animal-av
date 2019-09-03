package generators.network;

public class Graphnode {
	private static int nextId = 0;
	
	private String label;
	private final int id;

	public static void resetId() {
		nextId = 0;
	}
	
	
	public Graphnode(String lable){
		this.label = lable;
		id = nextId++;
	}
	
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String lable) {
		this.label = lable;
	}
	

	public int getId() {
		return id;
	}

	
}
