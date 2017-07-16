package rpg;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Weapon extends Item { //add stamina reduction
	public String message;
	public double baseDmg;
	public double range;
	public double[][] criticals; //different types of crits and their probabilies
	ArrayList<Enchantment> enhancements; //do this (includes def ignoring)
	//add std miss chance?
	
	public Weapon(String name, String msg, double a, double b, double[]... c) {
		super(name);
		message = msg;
		baseDmg = a;
		range = b;
		criticals = c;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##");
		String crits = "";
		for(double[] c : criticals) {
			String chance = df.format(c[0]*100);
			String damage = df.format(c[1]*100);
			crits += "\n\tChance:" + String.join("", Collections.nCopies(6 - chance.length(), " ")) + chance
			+ "%\tDamage:" + String.join("", Collections.nCopies(6 - damage.length(), " ")) + damage + "%";
		}
		return "Message: " + String.format(message, "You", "[enemy]")
		+ "\nBase damage: " + baseDmg
		+ "\nVary: " + range
		+ "\nCriticals:" + crits;//add crits. add enhancements
	}
	
	public static final class Weapons {
		public static final Weapon fist = new Weapon("Punch", "%1$s punch %2$s", 1, 0, new double[]{.5, 2});
		public static final Weapon critStaff = new Weapon("Crit Staff", "", 37, 5, new double[]{.9, 1.1}, new double[]{.8, 1.2}, new double[]{.7, 1.3}, new double[]{.6, 1.4}, new double[]{.5, 1.5}, new double[]{.4, 1.6}, new double[]{.3, 1.7}, new double[]{.2, 1.8}, new double[]{.1, 1.9});
		public static final Weapon ratTail = new Weapon("Rat whip", "%1$s rat tail %2$s", 4, 1, new double[]{.5, 2});
	}
}