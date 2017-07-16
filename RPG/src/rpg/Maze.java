package rpg;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.ArrayList;
import java.util.Arrays;
 
/*
 * recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 */
public class Maze {
	public final int x;
	public final int y;
	public final String s;
	public final int[][] maze;
	public final GameMode mode;
	public int[][] walls;
	public int[][] doors;
	public Interactive[][] interactives;
	public Player player;
	public String m = "P";
	public String n = "x ";
	public Light l;
	public int startx;
	public int starty;
	public int endx;
	public int endy;
	public int[][] path;
 
	public Maze(int a, int b, GameMode gm, String str, Player p) {
		x = a;
		y = b;
		s = str;
		mode = gm;
		player = p;
		maze = new int[x][y];
		walls = new int[x][y];
		doors = new int[x][y];
		interactives = new Interactive[x][y];
		generateMaze(0, 0);
		ArrayList<int[]> ar = new ArrayList<int[]>();
		for(int i = 0; i < maze.length; i++) for(int j = 0; j < maze[i].length; j++)
		if(maze[i][j] == 1 || maze[i][j] == 2 || maze[i][j] == 4 || maze[i][j] == 8) ar.add(new int[]{i, j, maze[i][j]});
		int[] startPos = ar.get((int)(Math.random()*ar.size()));
		startx = player.x = startPos[0];
		starty = player.y = startPos[1];
		player.xDisp = 2;
		player.yDisp = 0;
		ArrayList<DefaultMutableTreeNode> spots = getEndGameSpots(startx, starty, mode.end);
		DefaultMutableTreeNode end = spots.get((int)(Math.random()*spots.size()));
		endx = ((int[]) end.getUserObject())[0];
		endy = ((int[]) end.getUserObject())[1];
		path = Arrays.copyOf(end.getUserObjectPath(), end.getLevel(), int[][].class);
		interactives[startx][starty] = new Shop();
		interactives[endx][endy] = new End();
		generateInteractives();
		for(int i = 0; i < interactives.length; i++) for(int j = 0; j < interactives[i].length; j++) if(interactives[i][j] instanceof Interactive) System.out.println(i + ", " + j);
		System.out.println("chests");
		for(int i = 0; i < interactives.length; i++) for(int j = 0; j < interactives[i].length; j++) if(interactives[i][j] instanceof Chest) System.out.println(i + ", " + j);
		System.out.println("Enemies");
		for(int i = 0; i < interactives.length; i++) for(int j = 0; j < interactives[i].length; j++) if(interactives[i][j] instanceof Enemy) System.out.println(i + ", " + j);
	}
	
	public Maze(int a, int b, int c, int d, GameMode gm, String str, Player p) {
		x = a;
		y = b;
		s = str;
		mode = gm;
		player = p;
		maze = new int[x][y];
		walls = new int[x][y];
		doors = new int[x][y];
		interactives = new Interactive[x][y];
		List<DIR> dirs = Arrays.asList(DIR.values());
		if(c == 0) dirs.remove(DIR.W);
		if(c == x - 1) dirs.remove(DIR.E);
		if(d == 0) dirs.remove(DIR.N);
		if(d == y - 1) dirs.remove(DIR.S);
		DIR dir = dirs.get((int)(Math.random()*dirs.size()));
		maze[c][d] = dir.bit;
		maze[c + dir.dx][d + dir.dy] |= dir.opposite.bit;
		generateMaze(c + dir.dx, d + dir.dy);
		startx = player.x = c;
		starty = player.y = d;
		player.xDisp = 2;
		player.yDisp = 0;
		ArrayList<DefaultMutableTreeNode> spots = getEndGameSpots(startx, starty, mode.end);
		DefaultMutableTreeNode end = spots.get((int)(Math.random()*spots.size()));
		endx = ((int[]) end.getUserObject())[0];
		endy = ((int[]) end.getUserObject())[1];
		path = Arrays.copyOf(end.getUserObjectPath(), end.getLevel(), int[][].class);
		interactives[startx][starty] = new Shop();
		interactives[endx][endy] = new End();
		generateInteractives();
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
			for(int j = 0; j < split[i].length(); j++) if(Math.hypot(16d/7*(i - index[0]), j - index[1]) > l.radius) c[j] = ' ';
			res[i] = new String(c);
		}
		return String.join("\n", res) + "\n";
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < y; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {
				str += s + ((maze[j][i] & 1) == 0 ? s : (((~walls[j][i] & 1) == 0) ? n : ((player.x == j && player.y == i && player.yDisp == 1) ? m + " " : "  ")));
			}
			str += s+"\n";
			// draw the west edge
			for (int j = 0; j < x; j++) {
				String str1 = ((maze[j][i] & 8) == 0 ? s : (((~walls[j][i] & 8) == 0) ? n : "  ")) + ((j == startx && i == starty) ? "$ " : "  ");
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
	
	public int[][] getMoveMaze() {
		int[][] base = new int[maze.length][maze[0].length];
		for(int i = 0; i < maze.length; i++) for(int j = 0; j < maze[i].length; j++) base[i][j] = (maze[i][j] & ~walls[i][j]) & ~doors[i][j];
		return base;
	}
	
	public ArrayList<DefaultMutableTreeNode> getEndGameSpots(int a, int b, double w) {
		DefaultMutableTreeNode tree = getTree(a, b);
		ArrayList<DefaultMutableTreeNode> spots = new ArrayList<DefaultMutableTreeNode>();
		enumEndNodes(tree, spots, w);
		return spots;
	}
	
	private static void enumEndNodes(DefaultMutableTreeNode tree, ArrayList<DefaultMutableTreeNode> spots, double w) {
		enumNodes(tree, spots, (u, v) -> u.getChildCount() == 0 && u.getLevel() >= w*((DefaultMutableTreeNode) v.getRoot()).getDepth(), false);
	}
	
	public static void enumNodes(DefaultMutableTreeNode tree, ArrayList<DefaultMutableTreeNode> spots, BiPredicate<DefaultMutableTreeNode, DefaultMutableTreeNode> p, boolean b) {
		enumNodes(tree, t -> {spots.add(t);}, p, (u, v) -> u, b);
	}
	
	@SuppressWarnings("unchecked")
	public static void enumNodes(DefaultMutableTreeNode tree, Consumer<DefaultMutableTreeNode> function, BiPredicate<DefaultMutableTreeNode, DefaultMutableTreeNode> p, BiFunction<DefaultMutableTreeNode, DefaultMutableTreeNode, DefaultMutableTreeNode> nextTree, boolean b) {
		for(Enumeration<DefaultMutableTreeNode> e = tree.children(); e.hasMoreElements();) {
			DefaultMutableTreeNode node = e.nextElement();
			if(p.test(node, tree)) function.accept(node);
			if(b || !p.test(node, tree)) enumNodes(nextTree.apply(node, tree), function, p,  nextTree, b);
		}
	}
	
	public static int getNodeValue(DefaultMutableTreeNode tree, int[][] a) {
		return a[((int[]) tree.getUserObject())[0]][((int[]) tree.getUserObject())[1]];
	}
	
	public static <T> T getNodeValue(DefaultMutableTreeNode tree, T[][] a) {
		return a[((int[]) tree.getUserObject())[0]][((int[]) tree.getUserObject())[1]];
	}
	
	public DefaultMutableTreeNode getTree(int a, int b) {
		return getNode(a, b, null);
	}
	
	private DefaultMutableTreeNode getNode(int a, int b, DIR dir) {
		DefaultMutableTreeNode tree = new DefaultMutableTreeNode(new int[]{a, b});
		for(DIR d : DIR.values()) if(!d.opposite.equals(dir) && (~maze[a][b] & d.bit) == 0) tree.add(getNode(a + d.dx, b + d.dy, d));
		return tree;
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
	}
 
	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}
	
	public void generateInteractives() {
		ArrayList<DefaultMutableTreeNode> endNodes = new ArrayList<DefaultMutableTreeNode>();
		enumNodes(getTree(startx, starty), endNodes, (u, v) -> u.isLeaf() && (((int[])u.getUserObject())[0] != endx || ((int[])u.getUserObject())[0] != endy), false);
		for(DefaultMutableTreeNode t : endNodes) System.out.println(Arrays.toString((int[])t.getUserObject()));
		Collections.shuffle(endNodes);
		int k = 0;
		for(; k < (mode.min + Math.random()*.2)*endNodes.size(); k++) {
			int i = ((int[])endNodes.get(k).getUserObject())[0];
			int j = ((int[])endNodes.get(k).getUserObject())[1];
			interactives[i][j] = new Chest(player.chapter*(65 + (int)(Math.random()*20)), player.chapter*(115 + (int)(Math.random()*20)),
			player.chapter*(25 + (int)(Math.random()*10)), player.chapter*(40 + (int)(Math.random()*10)), player);
		}
		int rest = endNodes.size() - k;
		ArrayList<Pair<Enemy, double[]>> enemyProbs = Enemy.Enemies.EnemyTable(player, 1); //put boss at very end
		ArrayList<Integer> count = new ArrayList<Integer>();	//do dungeons
		System.out.println(player.chapter);
		System.out.println(enemyProbs.size());
		for(int m = 0; m < enemyProbs.size(); m++) {
			count.add((int)(rest*enemyProbs.get(m).second[0]));
			for(int n = 0; n < rest*enemyProbs.get(m).second[0]; n++) {
				int i = ((int[])endNodes.get(k).getUserObject())[0];
				int j = ((int[])endNodes.get(k).getUserObject())[1];
				interactives[i][j] = enemyProbs.get(m).first.clone();
				k++;
			}
			if(count.get(m) >= rest*enemyProbs.get(m).second[1]) {
				count.remove(m);
				enemyProbs.remove(m--);
			}
		}
		for(; k < endNodes.size(); k++) {
			if(enemyProbs.size() == 0) break;
			int i = ((int[])endNodes.get(k).getUserObject())[0];
			int j = ((int[])endNodes.get(k).getUserObject())[1];
			int num = (int)(Math.random()*enemyProbs.size());
			interactives[i][j] = enemyProbs.get(num).first.clone();
			count.set(num, count.get(num) + 1);
			if(count.get(num) >= rest*enemyProbs.get(num).second[1]) {
				count.remove(num);
				enemyProbs.remove(num);
			}
		}
		//make std enemies
		enemyProbs = Enemy.Enemies.EnemyTable(player, 2);
		ArrayList<int[]> spots = new ArrayList<int[]>();
		count = new ArrayList<Integer>();
		ArrayList<int[]> blacklist = new ArrayList<int[]>();
		ArrayList<int[]> possibleSpots = new ArrayList<int[]>();
		enumNodes(getTree(startx, starty), t -> {blacklist.add((int[])t.getUserObject());}, (u, v) -> u.getLevel() < 5, (u, v) -> u, false);
		for(int i = 0; i < interactives.length*interactives[0].length; i++) {
			boolean bool = interactives[i % interactives.length][i/interactives.length] == null;
			for(int[] j : blacklist) bool &= !Arrays.equals(j, new int[]{i % interactives.length, i/interactives.length});
			if(bool) possibleSpots.add(new int[]{i % interactives.length, i/interactives.length});
		}
		int distance = (int)(Math.random()*8);
		for(int[] i : possibleSpots) {
			distance++;
			if(distance > 3 && Math.random() < 1d/distance) {
				spots.add(i);
				distance = 0;
			}
		}
		Collections.shuffle(spots);
		k = 0; //fix stdenemies
		for(int m = 0; m < enemyProbs.size(); m++) {
			count.add((int)(spots.size()*enemyProbs.get(m).second[0]));
			for(int n = 0; n < spots.size()*enemyProbs.get(m).second[0]; n++) {
				interactives[spots.get(k)[0]][spots.get(k)[1]] = enemyProbs.get(m).first.clone();
				k++;
			}
			if(count.get(m) >= spots.size()*enemyProbs.get(m).second[1]) {
				count.remove(m);
				enemyProbs.remove(m--);
			}
		}
		for(; k < spots.size(); k++) {
			if(enemyProbs.size() == 0) break;
			int num = (int)(Math.random()*enemyProbs.size());
			interactives[spots.get(k)[0]][spots.get(k)[1]] = enemyProbs.get(num).first.clone();
			count.set(num, count.get(num) + 1);
			if(count.get(num) >= spots.size()*enemyProbs.get(num).second[1]) {
				count.remove(num);
				enemyProbs.remove(num);
			}
		}
	}
 
	public enum DIR {
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		public final int bit;
		public final int dx;
		public final int dy;
		public DIR opposite;
 
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
		
		public static DIR getDir(int i) {
			switch(i) {
				case 1:
					return N;
				case 2:
					return S;
				case 4:
					return E;
				case 8:
					return W;
			}
			return null;
		}
		
		public static DIR getDir(int[] a, int[] b) {
			switch(b[0] - a[0]) {
				case 0:
					switch(b[1] - a[1]) {
						case 1:
							return S;
						case -1:
							return N;
					}
					break;
				case 1:
					return E;
				case -1:
					return W;
			}
			return null;
		}
	}
}