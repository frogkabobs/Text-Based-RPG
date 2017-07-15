package rpg;

import java.util.ArrayList;

public class Chest implements Interactive {
	public int gold;
	public int xp;
	public ArrayList<Item> loot = new ArrayList<Item>();
	
	@SafeVarargs
	public Chest(int gMin, int gMax, int xpMin, int xpMax, Probability<Item>... i) {
		gold = gMin + (int) (Math.random()*(gMax - gMin + 1));
		xp = xpMin + (int) (Math.random()*(xpMax - xpMin + 1));
		for(Probability<Item> ip : i) if(ip.execute()) loot.add(ip.item);
	}
	
	@SuppressWarnings("unchecked")
	public Chest(int gMin, int gMax, int xpMin, int xpMax, Player p) {
		this(gMin, gMax, xpMin, xpMax, LootTable(p).toArray((Probability<Item>[]) new Object[LootTable(p).size()]));
	}
	
	public static ArrayList<Probability<Item>> LootTable(Player player) { //todo
		ArrayList<Probability<Item>> itemProbs = new ArrayList<Probability<Item>>();
		switch(player.chapter) {
			
		}
		return itemProbs;
	}
	@Override
	public void interact(RPGRun r) { //make this immediately display chest contents
		r.text.setText("You've found a chest\n\tPress 'o' to open the chest");
		r.story.userWait();
		while(!r.story.returnText.equalsIgnoreCase("o")) r.story.userWait();
		
		while(!r.story.returnText.isEmpty()) {
			
		}
	}
}
