package RPG;

public class SpecialItem extends Item {
	String stats;
	
	public SpecialItem(String name, String s) {
		super(name);
		stats = s;
	}
	
	public String toString() {
		return stats;
	}

}
