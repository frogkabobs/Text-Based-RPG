package rpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class Story {
	public RPGRun r;
	public String returnText = " ";
	public boolean yn;
	int option;
	
	public Story(RPGRun run) {
		r = run;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public void executeLevel() {
		switch(r.player.chapter) {
			case -1:
				option = 0;
				r.text.setText("\nChoose your gamemode:\n");
				for(int i = 0; i < GameMode.values().length; i++) r.text.append("\n\t" + ((i == option) ? ">" : " ") + GameMode.values()[i].toString() + "\n");
				userWait();
				outerLoop:
				while(!returnText.isEmpty()) {
					switch(returnText) {
						case "up":
							option--;
							break;
						case "down":
							option++;
							break;
						default:
							for(int i = 0; i < GameMode.values().length; i++) if(GameMode.values()[i].toString().equalsIgnoreCase(returnText))  {
								option = i;
								break outerLoop;
							}
					}
					option = Math.floorMod(option, GameMode.values().length);
					r.text.setText("\nChoose your gamemode:\n");
					for(int i = 0; i < GameMode.values().length; i++) r.text.append("\n\t" + ((i == option) ? ">" : " ") + GameMode.values()[i].toString() + "\n");
					userWait();
				}
				r.text.setText("\nGo through the tutorial?\n\n\t>Yes\t No\n");
				yn = true;
				userWait();
				while(!returnText.isEmpty()) {
					String str = r.text.getText();
					switch(returnText) {
						case "Yes": case "yes": case "left":
							yn = true;
							r.text.setText(str.substring(0, str.length() - 9) + ">Yes\t No\n");
							break;
						case "No": case "no": case "right":
							yn = false;
							r.text.setText(str.substring(0, str.length() - 9) + " Yes\t>No\n");
							break;
					}
					userWait();
				}
				r.player.level = new Maze(20, 10, GameMode.values()[option], "+ ", r.player);
				r.resetText();
				if(!yn) {
					r.player.chapter += 2;
					break;
				}
				r.player.chapter++;
				r.player.level = new Maze(20, 10, GameMode.values()[option], "+ ", r.player);
			case 0: //add option to have story on/off. Trade all gains for keys
				ArrayList<DefaultMutableTreeNode> split = new ArrayList<DefaultMutableTreeNode>();
				Maze.enumNodes(r.player.level.getTree(r.player.level.startx, r.player.level.starty), split,
						(u, v) -> Maze.getNodeValue(u, r.player.level.maze) != (Maze.getNodeValue((DefaultMutableTreeNode) u.getRoot(), r.player.level.maze)
								| Maze.DIR.getDir(Maze.getNodeValue((DefaultMutableTreeNode) u.getRoot(), r.player.level.maze)).opposite.bit), false);
				for(Enumeration<DefaultMutableTreeNode> e = split.get(0).children(); e.hasMoreElements();) split.add(e.nextElement());
				ArrayList<int[]> splitPoints = new ArrayList<int[]>();
				for(int i = 0; i < split.size(); i++) splitPoints.add((int[]) split.get(i).getUserObject());
				for(int i = 1; i < split.size(); i++) { 
					int cx = splitPoints.get(i)[0];
					int cy = splitPoints.get(i)[1];
					for(Maze.DIR dir : Maze.DIR.values()) if((~r.player.level.maze[cx][cy] & dir.bit) == 0) { 
						int nx = cx + dir.dx;
						int ny = cy + dir.dy;
						if(nx != splitPoints.get(0)[0] || ny != splitPoints.get(0)[1]) {
							r.player.level.walls[cx][cy] |= dir.bit;
							r.player.level.walls[nx][ny] |= dir.opposite.bit;
						}
					}
				} // */
				r.pause(1000);/*
				r.textRoll("You wake up in the dark, disheveled and disoriented.\n"
						+ "You try to remember how you got here, but you can't seem to remember anything at all.\n"
						+ "You don't even know your name.\n", 1, 30, 1500);
				r.pause(1000);
				r.text.append("\n\tChoose a name for yourself\n");
				if(r.player.retarded) {
					r.pause(2000);
					r.player.name = "Retarded";
					r.player.retarded = false;
				} else {
					userWait();
					r.player.name = returnText;
				}
				r.resetText();
				r.textRoll("The name " + r.player.name + " has a good ring to it, and you decide to call yourself that for the time being.\n"
						+ "You start to look around at the moist stone walls and the dark corridor in front of you.\n"
						+ "As you stand up you hear a rustle and see a note fall off your chest.\n", 1, 30, 1500);
				r.pause(1000);
				returnText = "";
				r.text.append("\n\tPress [p] to pick up the note\n"); // */
				Note note = new Note("Note", 46, 8, "");
				note.replaceLine(4, "                      run.");
				r.player.inventory.add(note); /*
				while(!returnText.equalsIgnoreCase("p")) userWait();
				r.resetText();
				itemAccess(r.player.inventory.get(0, 0));
				userWait();
				returnText = "";
				r.resetText();
				r.textRoll("You're a bit fazed by the ominous note as a chill runs down your spine,\n"
						+ "but you try to not give it much mind.\n", 0, 30);
				r.pause(1500);
				r.textRoll("You're more curious about where you even are.\n"
						+ "You look around some more and find a piece of paper titled \"Magic Map\", a torch, and some matches.\n", 1, 30, 1500);
				r.pause(1000); // */
				r.player.inventory.add(new Light("Torch", 8));
				r.player.level.l = (Light)r.player.inventory.get(1, 0);
				r.player.inventory.add(new MagicMap("Magic Map", r.player.level));
				r.player.inventory.add(new SpecialItem("Matches", "               \\\\\\\\    \\\\\\\\        ////\n"
						+ "                \\\\\\\\    \\\\\\\\      ////\n"
						+ "                 \\\\\\\\    \\\\\\\\    //// \n"
						+ "                  \\  \\    \\  \\  /  /\n"
						+ "                   \\  \\    \\  \\/  /\n"
						+ "                    \\  \\    \\  \\ / \n"
						+ "                     \\  \\    \\  \\\n"
						+ "                      \\  \\  / \\  \\\n"
						+ "                       \\  \\/  /\\  \\\n"
						+ "                        \\  \\ /  \\  \\\n"
						+ "                         \\  \\    \\  \\\n"
						+ "                        / \\  \\    \\  \\\n"
						+ "                       /  /\\  \\    \\  \\\n"
						+ "                      /  /  \\  \\    \\  \\\n"
						+ "                     /  /    \\  \\    \\  \\\n"
						+ "                    /  /      \\  \\    \\  \\")); /*
				r.text.append("\n\tLight the matches?\t>Yes\t No\n");
				yn = true;
				userWait();
				while(!returnText.isEmpty()) {
					String str = r.text.getText();
					switch(returnText) {
						case "Yes": case "yes": case "left":
							yn = true;
							r.text.setText(str.substring(0, str.length() - 9) + ">Yes\t No\n");
							break;
						case "No": case "no": case "right":
							yn = false;
							r.text.setText(str.substring(0, str.length() - 9) + " Yes\t>No\n");
							break;
					}
					userWait();
				}
				r.resetText();
				if(yn) {
					r.textRoll("You try to strike a match on the damp wall, but it does not light because the wall is too wet.\n", 0, 30);
					r.pause(1500);
				}
				r.textRoll("You decide to look around some more and manage to find some rough, dry rocks in the corner.\n", 0, 30);
				r.pause(1000);
				r.text.append("\n\tPress [u] to use the rocks for striking the matches\n");
				while(!returnText.equalsIgnoreCase("u")) userWait();
				r.resetText();
				r.textRoll("You strike a match across the rocks and it suddenly ignites.\n"
						+ "You now have a fire.\n", 0, 30, 1500);
				r.text.append("\n\t>Light the torch on fire\t Light the map on fire\n");
				yn = true;
				userWait();
				while(!returnText.isEmpty()) {
					String str = r.text.getText();
					switch(returnText) {
						case "Torch": case "torch": case "left":
							yn = true;
							r.text.setText(str.substring(0, str.length() - 48) + ">Light the torch on fire\t Light the map on fire\n");
							break;
						case "Map": case "map": case "right":
							yn = false;
							r.text.setText(str.substring(0, str.length() - 48) + " Light the torch on fire\t>Light the map on fire\n");
					}
					userWait();
				}
				if(!yn) {
					r.text.setText("You're retarded.");
					r.pause(2000);
					r.resetText();
					r.player.retarded = true;
					r.player.inventory.clear(); //use serialization
					break;
				}
				r.resetText();
				r.textRoll("You touch the lit match to the torch and it immediately catches fire.\n"
						+ "You can now see the map.\n", 1, 20, 1500);
				r.pause(1000);
				r.text.append("\n\tPress [u] to look at the map\n");
				while(!returnText.equalsIgnoreCase("u")) userWait();
				r.resetText();
				itemAccess(r.player.inventory.get(2, 0));
				//r.textRoll(level.torchString(), 3, 20, 5);
				userWait();
				returnText = "";
				r.resetText();
				r.textRoll("It seems that the map only shows the area around where you are that's lit by the torch.\n"
						+ "However, this map allows you to see through walls, which is far better than just looking ahead.\n", 1, 30, 1500);
				r.text.append("\n\tTry moving [\u2190\u2191\u2192\u2193] while looking at the map (press [u] to open the map)\n");
				while(!returnText.equalsIgnoreCase("u")) userWait();
				r.resetText(); // */
				openItem(r.player.inventory.get(2, 0)); //have shop owner help you destroy walls (make walls doors? pay for all to be destroyed)
				//it appears you're trapped
				//why hello there *unnerving smile*
				//robot
				//I don't remeber you being here
				//$ for shop/start
				//r.textRoll(level.toString(), 3, 20, 5);
				//r.player.chapter++;
				yn = true;
			case 1:
				if(yn) generateMaze();
				else {
					Note note1 = new Note("Note", 46, 8, "");
					note1.replaceLine(4, "                      run.");
					r.player.inventory.add(note1);
					r.player.inventory.add(new Light("Torch", 8));
					r.player.level.l = (Light)r.player.inventory.get(1, 0);
					r.player.inventory.add(new MagicMap("Magic Map", r.player.level));
				}
				openItem(r.player.inventory.get(2, 0)); //menu?
		}
		if(r.player.chapter <= 20) executeLevel();
	}
	
	public void generateMaze() {
		r.player.level = new Maze(r.player.level.x + 4, r.player.level.y + 2, r.player.level.endx, r.player.level.endy, r.player.level.mode, "+ ", r.player);
	}
	
	public void userWait() {
		synchronized(r.returnText) {
			while(r.returnText.isEmpty()) {
	        	try {
	        		r.returnText.wait();
	        	} catch (InterruptedException e) {}
	    	}
			returnText = r.returnText.remove(0);
		}
	}
	
	public void openItem(Item i) {
		itemAccess(i, 4, 20, 1);
		userWait();
		if(i instanceof Interactive) ((Interactive) i).interact(r);
		returnText = "";
	}
	
	@SuppressWarnings("static-access")
	public void itemAccess(Item i) { //flat item access
		r.text.setText("Item: "+ i.name + String.join("", Collections.nCopies(73 - i.name.length(), " ")) + "Press 'Enter' to exit\n" + i.toString()); //make it so you pass item in there and it automatically displays
	}
	public void itemAccess(Item i, int a, int b, int... x) {
		r.textRoll("Item: "+ i.name + String.join("", Collections.nCopies(73 - i.name.length(), " ")) + "Press 'Enter' to exit\n", 5, 30);
		r.textRoll(i.toString(), a, b, x);
	}
}