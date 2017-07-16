package rpg;

import java.util.function.BiFunction;

public enum Effect {
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
	critInverse("Critical Inversion", (BiFunction<Double, Integer, Double>) (u, v) -> {return 1/u;}),
	reflection("Reflection", null),
	critNegate("Critical Negation", null);
	
	public String name;
	public BiFunction<Object, Integer, Object> function;
	
	@SuppressWarnings("unchecked")
	private <T, U> Effect(String s, BiFunction<T, Integer, U> f) {
		name = s;
		function = (BiFunction<Object, Integer, Object>) (u, v) -> {return (Object) f.apply((T) u, v);};
	}
}
