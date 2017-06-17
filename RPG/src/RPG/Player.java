package RPG;

import java.util.ArrayList;

public class Player {
	public int health;
	public String name;
	public int x;
	public int y;
	public int xDisp; //0 - 1
	public int yDisp; //0 - 3
	public ArrayList<Item> inventory = new ArrayList<Item>();
	
	
	public Player() {
		health = 20;
	}
	
}
