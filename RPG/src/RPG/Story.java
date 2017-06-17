package RPG;

import java.util.Collections;

public class Story {
	public RPGRun r;
	public Maze level;
	public int chapter = 0;
	public String returnText = " ";
	public boolean yn;
	int option;
	
	public Story(RPGRun run) {
		r = run;
	}
	
	@SuppressWarnings("static-access")
	public void executeLevel() {
		switch(chapter) {
			case 0:
				level = new Maze(20, 10, "+ ", r.player); //*
				r.pause(1000);
				r.textRoll("You wake up in the dark, disheveled and disoriented.\n"
						+ "You try to remember how you got here, but you can't seem to remember anything at all.\n"
						+ "You don't even know your name.\n", 1, 30, 1500);
				r.pause(1000);
				r.text.append("\n\tChoose a name for yourself\n");
				userWait();
				r.player.name = returnText;
				returnText = "";
				r.resetText();
				r.textRoll("The name " + r.player.name + " has a good ring to it, and you decide to call yourself that for the time being.\n"
						+ "You start to look around at the moist stone walls and the dark corridor in front of you.\n"
						+ "As you stand up you hear a rustle and see a note fall off your chest.\n", 1, 30, 1500);
				r.pause(1000);
				r.text.append("\n\tPress [p] to pick up the note\n"); // */
				Note note = new Note("Note", 46, 8, "");
				note.replaceLine(4, "                      run.");
				r.player.inventory.add(note); //*
				while(!returnText.equalsIgnoreCase("p")) userWait();
				r.resetText();
				itemAccess(r.player.inventory.get(0));
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
				level.l = (Light)r.player.inventory.get(1);
				r.player.inventory.add(new MagicMap("Magic Map", level));
				r.player.inventory.add(new SpecialItem("Matches", "")); //*
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
				itemAccess(r.player.inventory.get(2));
				r.textRoll(level.torchString(), 3, 20, 5);
				userWait();
				returnText = "";
				r.resetText();
				r.textRoll("It seems that the map only shows the area around where you are that's lit by the torch.\n"
						+ "However, this map allows you to see through walls, which is far better than just looking ahead.\n", 1, 30, 1500);
				r.text.append("\n\tTry moving [\u2190\u2191\u2192\u2193] while looking at the map (press [u] to open the map)\n");
				while(!returnText.equalsIgnoreCase("u")) userWait();
				r.resetText();//* /
				openMap();
				//r.textRoll(level.toString(), 3, 20, 5);
		}
		if(chapter <= 20) executeLevel();
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
	
	public void openMap() {
		itemAccess(r.player.inventory.get(2));
		userWait();
		while(!returnText.isEmpty()) {
			int val = level.maze[r.player.x][r.player.y];
			switch(returnText) {
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
			itemAccess(r.player.inventory.get(2));
			userWait();
		}
		returnText = "";
	}
	
	@SuppressWarnings("static-access")
	public void itemAccess(Item i) {
		r.text.setText("Item: "+ i.name + String.join("", Collections.nCopies(73 - i.name.length(), " ")) + "Press 'Enter' to exit\n" + i.toString()); //make it so you pass item in there and it automatically displays
	}
}