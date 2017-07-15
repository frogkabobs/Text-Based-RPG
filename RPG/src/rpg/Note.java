package rpg;

import java.util.Collections;
import java.util.StringJoiner;

public class Note extends Item {
	public String[] contents;
	int width;
	
	public Note(String name, int w, int h, String str) {
		super(name);
		width = w;
		contents = new String[h];
		for(int i = 0; i < h; i++) {
			int len = Math.min(w, str.length() - w*i);
			if(w*i < str.length()) replaceLine(i, str.substring(w*i, w*i + len));
			else contents[i] = String.join("", Collections.nCopies(w, " "));
		}
	}
	
	public void replaceLine(int i, String str) {
		int len = Math.min(width, str.length());
		contents[i] = str.substring(0, len) + String.join("", Collections.nCopies(width - len, " "));
	}
	@Override
	public String toString() {
		String tab = String.join("", Collections.nCopies((104 - width)/2, " "));
		StringJoiner res = new StringJoiner("\n" + tab);
		res.add("\n\n\n");
		res.add(" " + String.join("", Collections.nCopies(width, "_")));
		for(int i = 0; i < contents.length; i++) res.add("|" + contents[i] + "|");
		res.add("|" + String.join("", Collections.nCopies(width, "_")) + "|");
		return res.toString() + "\n";
	}//underline bottom row?

}
