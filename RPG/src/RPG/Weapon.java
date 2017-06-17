package RPG;

public class Weapon extends Item{
	public int baseDmg;
	
	public Weapon(String name, int a) {
		super(name);
		baseDmg = a;
	}
	
	public String toString() {
		return "Base damage: " + baseDmg;
	}
}
