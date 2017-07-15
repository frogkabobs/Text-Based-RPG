package rpg;

public class MagicMap extends Item implements Interactive {
	Maze maze;
	
	public MagicMap(String str, Maze m) {
		super(str);
		maze = m;
	}
	
	public String toString() {
		return maze.torchString();
	}
	
	public void interact(RPGRun r) {
		while(!r.story.returnText.isEmpty()) {
			int val = maze.getMoveMaze()[r.player.x][r.player.y];
			switch(r.story.returnText) {
				case "left":
					if(r.player.yDisp == 0) {
						switch(r.player.xDisp) {
							case 3:
							case 1:
								r.player.xDisp--;
								break;
							case 2:
								if((~val & 8) == 0) r.player.xDisp--;
								break;
							case 0:
								r.player.xDisp = 3;
								r.player.x--;
								break;
						}
					}
					break;
				case "right":
					if(r.player.yDisp == 0) {
						switch(r.player.xDisp) {
							case 3:
								r.player.xDisp = 0;
								r.player.x++;
								break;
							case 2:
								if((~val & 4) == 0) r.player.xDisp++;
								break;
							case 1:
							case 0:
								r.player.xDisp++;
								break;
						}
					}
					break;
				case "up":
					if(r.player.xDisp == 2) {
						if(r.player.yDisp == 0) {
							if((~val & 1) == 0) r.player.yDisp = 1;
						} else {
							r.player.yDisp = 0;
							r.player.y--;
						}
					};
					break;
				case "down":
					if(r.player.xDisp == 2) {
						if(r.player.yDisp == 1) {
							r.player.yDisp = 0;
						}
						else if((~val & 2) == 0) {
							r.player.yDisp = 1;
							r.player.y++;
						}
					};
					break;
			}
			r.story.itemAccess(this);
			r.story.userWait();
		}
	}
}
