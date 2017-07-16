package rpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class Enemy extends Entity implements Interactive {
	public Weapon[][] attacks; //make this an arraylist 
	public BiConsumer<RPGRun, Enemy> fight;
	public ArrayList<Probability<Item>> drops; //change to pairof item and max drops
	
	@SafeVarargs
	public Enemy(String str, int hp, int m, double s, double def, double flatDef, Weapon[][] atk, BiConsumer<RPGRun, Enemy> c, Probability<Item>... d) {
		name = str;
		health = maxHealth = hp;
		mana = maxMana = m;
		stamina = maxStamina = s;
		defense = def;
		flatDefense = flatDef;
		attacks = atk;
		fight = c;
		drops = new ArrayList<Probability<Item>>(Arrays.asList(d));
	}
	
	public Enemy(String str, int hp, int m, double s, double def, double flatDef, Weapon[][] atk, BiConsumer<RPGRun, Enemy> c, ArrayList<Probability<Item>> d) {
		this(str, hp, m, s, def, flatDef, atk, c);
		drops = d;
	}
	@Override
	public void interact(RPGRun r) {
		r.resetText();
		r.textRoll("You've encountered a " + name, 0, 30);
		r.resetText(1500);
		r.textRoll(getFightText(r), 0, 30);
		fight.accept(r, this);
	}
	@SuppressWarnings("static-access")
	public static final class Enemies {
		public static final Enemy rat = new Enemy("Rat", 3, 0, 6, 0, 0, new Weapon[][]{{new Weapon("Bite", "%1$s bites %2$s", 2, 1),
			new Weapon("Tail Whip", "%1$s tail whips %2$s", 2.5, .5, new double[]{.3, 1.7})}}, (r, e) -> {
				while(e.health > 0) {
					boolean cont = true;
					while(cont) {
						r.story.userWait();
						switch(r.story.returnText.toLowerCase()) {
							case "a":
								r.text.setText(String.format(r.player.currWeapon.message, "You", "the rat")); // maybe lowercase
								r.player.attack(e);
								r.pause(1500);
								cont = false;
								break;
						}
					}
					if(e.health <= 0) break; 
					Weapon w = e.attacks[0][(int)(Math.random()*2)];
					r.text.setText(String.format(w.message, "The rat", "you"));
					e.currWeapon = w;
					e.attack(r.player);
					r.pause(1500);
					r.text.setText(e.getFightText(r));
				}//do enemy kill here
			}, new Probability<Item>(new SpecialItem("Rat Tail", "\nNotes:\n\t- Crafting item for the rat whip"), .5),
			new Probability<Item>(new SpecialItem("Rat Tail", ""), .5), new Probability<Item>(new SpecialItem("Rat Tooth", "\nNotes:\n\t- Crafting item for the rat whip"), 1d/6));
		
		public static final Enemy mutatedRat = new Enemy("Mutated Rat", 6, 0, 15, .2, 0, new Weapon[][]{{new Weapon("Bite", "%1$s bites %2$s", 3, 1),
			new Weapon("Tail Whip", "%1$s tail whips %2$s", 3, 1, new double[]{.3, 1.7})}}, (r, e) -> {
				while(e.health > 0) {
					boolean cont = true;
					while(cont) {
						r.story.userWait();
						switch(r.story.returnText.toLowerCase()) {
							case "a":
								r.text.setText(String.format(r.player.currWeapon.message, "You", "the mutated rat")); // maybe lowercase
								r.player.attack(e);
								r.pause(1500);
								cont = false;
								break;
						}
					}
					if(e.health <= 0) break; 
					Weapon w = e.attacks[0][(int)(Math.random()*2)];
					r.text.setText(String.format(w.message, "The mutated rat", "you"));
					e.currWeapon = w;
					e.attack(r.player);
					r.pause(1500);
					r.text.setText(e.getFightText(r));
				}//do enemy kill here
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
	
	public Enemy clone() {
		return new Enemy(name, maxHealth, maxMana, maxStamina, defense, flatDefense, attacks, fight, drops);
	}
	
	public String getFightText(RPGRun r) {
		return "Stats [s] | Attack [a] | Pass [p]\nHealth: " + r.player.health + "/" + r.player.maxHealth + " | Mana: " + r.player.mana + "/" + r.player.maxMana + " | Stamina: "  + (int)r.player.stamina
		+ "/" + (int)r.player.maxStamina + " | Defense: " + r.player.defense + " | Flat Defense: " + r.player.flatDefense + "\n"
		+ name + ":\nHP: " + health + "/" + maxHealth;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean disappear() {
		// TODO Auto-generated method stub
		return health <= 0;
	}
}
