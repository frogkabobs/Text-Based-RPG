package rpg;

import java.util.ArrayList;

public abstract class Entity {
	public String name;
	public int maxHealth;
	public int health;
	public int maxMana;
	public int mana;
	public double maxStamina;
	public double stamina;
	public double defense; //0 - 1
	public double flatDefense;
	public Weapon currWeapon;
	public ArrayList<StatusEffect> statusEffects = new ArrayList<StatusEffect>();;
	
	public void attack(Entity e) { //add slow multi hit display (maybe in enhancements?)
		double dmg = currWeapon.baseDmg - currWeapon.range + Math.random()*currWeapon.range*(2 + stamina/maxStamina - e.stamina/e.maxStamina);
		if(!StatusEffect.hasEffect(e, "critNegate")) for(double[] c : currWeapon.criticals) if(Math.random() < c[0]) dmg *= StatusEffect.<Double, Double>affect(e, "critInverse", c[1]);
		if(StatusEffect.hasEffect(e, "reflection")) health -= Math.max(1, Math.round((1 - defense)*dmg - flatDefense));
		else e.health -= Math.max(1, Math.round((1 - e.defense)*dmg - e.flatDefense));
	}
	/* avg damage:
		double dmg = baseDmg;
		for(double[] c : criticals) dmg *= 1 - c[0] + c[0]*c[1];
	*/
}
