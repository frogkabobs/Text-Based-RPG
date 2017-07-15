package rpg;

public class PlayerStats {
	public int health;
	public String name;
	public int x;
	public int y;
	public int xDisp; //0 - 1
	public int yDisp; //0 - 3
	public int chapter;
	public Maze level;
	public Inventory inventory;
	
	public PlayerStats() {
		inventory = new Inventory();
		health = 20;
		chapter = -1;
	}
}
