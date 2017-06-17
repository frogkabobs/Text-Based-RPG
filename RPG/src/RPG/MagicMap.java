package RPG;

public class MagicMap extends Item{
	Maze maze;
	
	public MagicMap(String str, Maze m) {
		super(str);
		maze = m;
	}
	
	public String toString() {
		return maze.torchString();
	}
}
