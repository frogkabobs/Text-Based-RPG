package rpg;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class Enemy extends Entity implements Interactive {
	public Weapon[][] attacks;
	public BiConsumer<RPGRun, Enemy> fight;
	public Probability<Item>[] drops;
	
	@SafeVarargs
	public Enemy(String str, int hp, double def, double flatDef, Weapon[][] atk, BiConsumer<RPGRun, Enemy> c, Probability<Item>... d) {
		name = str;
		health = hp;
		defense = def;
		flatDefense = flatDef;
		attacks = atk;
		fight = c;
		drops = d;
	}
	
	@SuppressWarnings("unchecked")
	public Enemy(String str, int hp, double def, double flatDef, Weapon[][] atk, BiConsumer<RPGRun, Enemy> c, ArrayList<Probability<Item>> d) {
		this(str, hp, def, flatDef, atk, c, d.toArray((Probability<Item>[]) new Object[d.size()]));
	}
	@Override
	public void interact(RPGRun r) {
		r.resetText();
		r.textRoll("You've encountered a " + name, 0, 30);
		r.resetText(1500);
		r.textRoll("Player:\t" + r.player.health + " HP\t" + r.player.defense + " Def\t" + r.player.flatDefense + " Flat Def | Stats [s] | Attack [a] | Pass [p]\n"
				+ name + ":\t" + health, 0, 30);
		fight.accept(r, this);
	}
	@SuppressWarnings("static-access")
	public static final class Enemies {
		public static final Enemy rat = new Enemy("Rat", 3, 0, 0, new Weapon[][]{{new Weapon("Bite", "%1$ bites %2$", 2, 1),
			new Weapon("Tail Whip", "%1$ tail whips %2$", 2.5, .5, new double[]{.3, 1.7})}}, (r, e) -> {
				r.story.userWait();
				while(e.health > 0) {
					switch(r.story.returnText.toLowerCase()) {
						case "a":
							r.text.setText(String.format(r.player.currWeapon.message, "You", "the rat")); // maybe lowercase
							r.player.attack(e);
							r.pause(1500);
					}
					if(e.health > 0) break;
					Weapon w = e.attacks[0][(int)(Math.random()*2)];
					r.text.setText(String.format(w.message, "The rat", "you"));
					e.currWeapon = w;
					e.attack(r.player);
					r.story.userWait();
				}
			}, new Probability<Item>(new SpecialItem("Rat Tail", "\nNotes:\n\t- Crafting item for the rat whip"), .5),
			new Probability<Item>(new SpecialItem("Rat Tail", ""), .5), new Probability<Item>(new SpecialItem("Rat Tooth", "\nNotes:\n\t- Crafting item for the rat whip"), 1d/6));
		
		public static final Enemy mutatedRat = new Enemy("Mutated Rat", 9, .2, 0, new Weapon[][]{{new Weapon("Bite", "The mutated rat bites you", 4, 1),
			new Weapon("Tail Whip", "The mutated rat tail whips you", 5, 1, new double[]{.3, 1.7})}}, (r, e) -> {
				
			}, new Probability<Item>(new SpecialItem("Rat Tooth", "\nNotes:\n\t- Crafting item for the rat whip"), 1));
		
		public static ArrayList<Pair<Enemy, double[]>> EnemyTable(Player p, int tier) {
			ArrayList<Pair<Enemy, double[]>> table = new ArrayList<Pair<Enemy, double[]>>();
			switch(p.chapter) {
				case 1:
					switch(tier) {
						case 1:
							table.add(new Pair<Enemy, double[]>(mutatedRat, new double[]{1, 1}));
							break;
						case 2:
							table.add(new Pair<Enemy, double[]>(rat, new double[]{1, 1}));
							break;
					}
			}
			return table;
		}
	}
}
