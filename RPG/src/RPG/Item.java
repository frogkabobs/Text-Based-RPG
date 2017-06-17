package RPG;

public abstract class Item {
String name;
	
	public Item(String str) {
		name = str;
	}
	
	abstract public String toString();
}
