package RPG;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
 
/*
 * recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 */
public class Maze {
	private final int x;
	private final int y;
	private final String s;
	public final int[][] maze;
	public Player player;
	public String m = "P";
	public Light l;
 
	public Maze(int a, int b, String str, Player p) {
		x = a;
		y = b;
		s = str;
		player = p;
		maze = new int[x][y];
		generateMaze(0, 0);
	}

	public String[] splitString() {
		return toString().split("\n");
	}
	
	public String torchString() {
		String[] split = splitString();
		int[] index = null;
		for(int i = 0; i < split.length; i++) if(split[i].contains(m)) index = new int[]{i, split[i].indexOf(m)};
		String[] res = new String[split.length];
		for(int i = 0; i < split.length; i++) {
			char[] c = split[i].toCharArray();
			for(int j = 0; j < split[i].length(); j++) if(Math.hypot(2*(i - index[0]), j - index[1]) > l.radius) c[j] = ' ';
			res[i] = new String(c);
		}
		return String.join("\n", res) + "\n";
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < y; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {
				str += s + ((maze[j][i] & 1) == 0 ? s : ((player.x == j && player.y == i && player.yDisp == 1) ? m + " " : "  "));
			}
			str += s+"\n";
			// draw the west edge
			for (int j = 0; j < x; j++) {
				String str1 = ((maze[j][i] & 8) == 0 ? s : "  ") + "  ";
				if(player.x == j && player.y == i && player.yDisp == 0) str += str1.substring(0, player.xDisp) + m + str1.substring(player.xDisp + 1);
				else str += str1;
			}
			str += s+"\n";
		}
		// draw the bottom line
		for (int j = 0; j < x; j++) {
			str += s+s;
		}
		str += s+"\n";
		return str;
	}
 
	private void generateMaze(int cx, int cy) {
		DIR[] dirs = DIR.values();
		Collections.shuffle(Arrays.asList(dirs));
		for (DIR dir : dirs) {
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;
			if (between(nx, x) && between(ny, y)
					&& (maze[nx][ny] == 0)) {
				maze[cx][cy] |= dir.bit;
				maze[nx][ny] |= dir.opposite.bit;
				generateMaze(nx, ny);
			}
		}
		ArrayList<int[]> ar = new ArrayList<int[]>();
		for(int i = 0; i < maze.length; i++) for(int j = 0; j < maze[i].length; j++)
		if(maze[i][j] == 1 || maze[i][j] == 2 || maze[i][j] == 4 || maze[i][j] == 8) ar.add(new int[]{i, j, maze[i][j]});
		int[] startPos = ar.get((int)(Math.random()*ar.size()));
		player.x = startPos[0];
		player.y = startPos[1];
		player.xDisp = 2;
		player.yDisp = 0;
	}
 
	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}
 
	private enum DIR {
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		private final int bit;
		private final int dx;
		private final int dy;
		private DIR opposite;
 
		// use the static initializer to resolve forward references
		static {
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}
 
		private DIR(int bit, int dx, int dy) {
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	}; 
}