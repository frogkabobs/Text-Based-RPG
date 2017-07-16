package rpg;

import java.util.Collections;

public class StatusEffect {
	public Effect effect;
	public int potency;
	
	public StatusEffect(String s, int p) {
		effect = Effect.valueOf(s);
		potency = p;
	}
	
	@SuppressWarnings("unchecked")
	public static <T, U> U affect(Entity e, String s, T t) {
		for(StatusEffect se : e.statusEffects) if(se.effect.equals(Effect.valueOf(s))) return (U) Effect.valueOf(s).function.apply(t, se.potency);
		try {
			return (U) t;
		} catch(Exception ex) {
			return null;
		}
	}
	
	public static boolean hasEffect(Entity e, String s) {
		for(StatusEffect se : e.statusEffects) if(se.effect.equals(Effect.valueOf(s))) return true;
		return false;
	}
	
	private String romanNumeral(int a) {
		if(a == 0) return "";
		if(a >= 4000) return " " + a;
		String numerals = "IVXLCDM";
		String str = "";
		int b = a;
		for(int i = 0; b > 0; i += 2) {
			int c = b % 10;
			switch(c) {
				case 1: case 2: case 3:
					str = String.join("", Collections.nCopies(c, numerals.substring(i, i + 1))) + str;
				case 4:
					str = numerals.substring(i, i + 2) + str;
					break;
				case 5: case 6: case 7: case 8:
					str = numerals.substring(i + 1, i + 2) + String.join("", Collections.nCopies(c - 5, numerals.substring(i, i + 1))) + str;
					break;
				case 9:
					str = numerals.substring(i, i + 1) + numerals.substring(i + 2, i + 3) + str;
			}
			b /= 10;
		}
		return " " + str;
	}
	
	public String toString() {
		return effect.name + romanNumeral(potency);
	}
}
