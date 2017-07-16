package rpg;

import java.io.Serializable;

public class Player extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	public boolean retarded;
	public int gold;
	public int xp;
	public int x;
	public int y;
	public int xDisp; //0 - 1
	public int yDisp; //0 - 3
	public int chapter;
	public Maze level;
	public Inventory inventory; //add energy + stamina (to all entities?)
	//make multiple hits wear them out
	
	public Player() {
		inventory = new Inventory();
		currWeapon = Weapon.Weapons.fist;
		health = maxHealth = 20;
		stamina = maxStamina = 20;
		mana = maxMana = 20;
		defense = 0;
		flatDefense = 0;
		chapter = -1;
		retarded = false;
	}
	
}
