package rpg;

public enum GameMode {
	Easy(2d/3, .25), Medium(3d/4, .2), Hard(9d/10, .15);
	public final double end;
	public final double min;
	
	GameMode(double d, double m) {
		end = d;
		min = m;
	}
}
