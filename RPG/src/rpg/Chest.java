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
	
	public Chest(int gMin, int gMax, int xpMin, int xpMax, Player p) {
		gold = gMin + (int) (Math.random()*(gMax - gMin + 1));
		xp = xpMin + (int) (Math.random()*(xpMax - xpMin + 1));
		for(Probability<Item> ip : lootTable(p)) if(ip.execute()) loot.add(ip.item);
	}
	
	public static ArrayList<Probability<Item>> lootTable(Player player) { //todo
		ArrayList<Probability<Item>> itemProbs = new ArrayList<Probability<Item>>();
		switch(player.chapter) {
			
		}
		return itemProbs;
	}
	@SuppressWarnings("static-access")
	@Override
	public void interact(RPGRun r) { //make this immediately display chest contents
		r.text.setText("You've found a chest\n\n\n\tGold: " + gold + "\n\n\tXp: " + xp);
		r.player.gold += gold;
		r.player.xp += xp;
		r.pause(1500);
		r.story.userWait();
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean disappear() {
		// TODO Auto-generated method stub
		return true;
	}
}
