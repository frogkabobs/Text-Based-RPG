package rpg;

import java.util.Collections;
import java.util.function.BiFunction;

public class StatusEffect {
	public Effect effect;
	public int potency;
	
	public StatusEffect(String s, int p) {
		effect = Effect.valueOf(s);
		potency = p;
	}
	
	private enum Effect {
		vitality("Vitality", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply after every enemy turn
			e.health  = Math.min(e.maxHealth, p + e.health);
			return null;
		}),
		charm("Charm", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply every turn
			e.mana  = Math.min(e.maxMana, p + e.mana);
			return null;
		}),
		endurance("Endurance", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply every turn
			e.stamina  = Math.min(e.maxStamina, p + e.stamina);
			return null;
		}),
		superVitality("Supervitality", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply every turn
			e.health  = Math.min(e.maxHealth, (int)(e.maxHealth*p/50d) + e.health);
			return null;
		}),
		superCharm("Supercharm", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply every turn
			e.mana  = Math.min(e.maxMana, (int)(e.maxMana*p/50d) + e.mana);
			return null;
		}),
		superEndurance("Superendurance", (BiFunction<Entity, Integer, Void>) (e, p) -> { //apply every turn
			e.stamina  = Math.min(e.maxStamina, e.maxStamina*p/50 + e.stamina);
			return null;
		}),
		critInverse("Critical Inversion", (BiFunction<Double, Integer, Double>) (u, v) -> {return 1/u;});
		
		public String name;
		public BiFunction<Object, Integer, Object> function;
		
		@SuppressWarnings("unchecked")
		private <T, U> Effect(String s, BiFunction<T, Integer, U> f) {
			name = s;
			function = (BiFunction<Object, Integer, Object>) (u, v) -> {return (Object) f.apply((T) u, v);};
		}
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
