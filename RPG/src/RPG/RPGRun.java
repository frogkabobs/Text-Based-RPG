package RPG;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RPGRun extends Applet implements KeyListener, Runnable, MouseListener {
	private static final long serialVersionUID = 1L;
	public static final Dimension SCREEN_SIZE = new Dimension(1270, 662);
	public static final Dimension GAME_SIZE = new Dimension(1270, 662);
	public static final String TITLE = "RPG";
	public static final Color BACKGROUND_COLOR = Color.black;
	public static final Color FOREGROUND_COLOR = Color.white;
	public static final int TICK = 50;
	public static JFrame frame;
	public static JPanel textInPanel;
	public static JPanel textPanel;
	public static JTextField textIn;
	public static JTextArea text;
	public static FlowLayout layout;
	public static JLabel label;
	public static boolean isRunning = true;
	public Story story = new Story(this);
	public Player player = new Player();
	public final List<String> returnText = new LinkedList<String>();

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		String contents = textIn.getText();
		int selEnd = Math.max(textIn.getCaret().getDot(), textIn.getCaret().getMark());
		int selBegin = Math.min(textIn.getCaret().getDot(), textIn.getCaret().getMark());
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(e.isAltDown()) {
					if(e.isShiftDown()) {
						synchronized(returnText) {
							returnText.add("left");
							returnText.notify();
						}
					} else {
						textIn.setText(contents.substring(0, selBegin) + "\u2190" + contents.substring(selEnd));
						textIn.setCaretPosition(selBegin + 1);
					}
				}
				break;
			case KeyEvent.VK_UP:
				if(e.isAltDown()) {
					if(e.isShiftDown()) {
						synchronized(returnText) {
							returnText.add("up");
							returnText.notify();
						}
					} else {
						textIn.setText(contents.substring(0, selBegin) + "\u2191" + contents.substring(selEnd));
						textIn.setCaretPosition(selBegin + 1);
					}
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(e.isAltDown()) {
					if(e.isShiftDown()) {
						synchronized(returnText) {
							returnText.add("right");
							returnText.notify();
						}
					} else {
						textIn.setText(contents.substring(0, selBegin) + "\u2192" + contents.substring(selEnd));
						textIn.setCaretPosition(selBegin + 1);
					}
				}
				break;
			case KeyEvent.VK_DOWN:
				if(e.isAltDown()) {
					if(e.isShiftDown()) {
						synchronized(returnText) {
							returnText.add("down");
							returnText.notify();
						}
					} else {
						textIn.setText(contents.substring(0, selBegin) + "\u2193" + contents.substring(selEnd));
						textIn.setCaretPosition(selBegin + 1);
					}
				}
				break;
			case KeyEvent.VK_P: case KeyEvent.VK_U:
				if(e.isAltDown()) textIn.setText(textIn.getText() + e.getKeyChar());
				else break;
			case KeyEvent.VK_ENTER:
				synchronized(returnText) {
					returnText.add(textIn.getText());
					textIn.setText("");
					returnText.notify();
				}
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	public void resetText() {
		text.setText("");
	}
	
	public void resetText(int i) {
		pause(i);
		resetText();
	}
	
	public void textRoll(String str, int a, int b, int... x) {
		textRoll(text, str, a, b, x);
	}
	
	public void textRoll(JTextArea j, String str, int a, int b, int... x) {
		String prev = j.getText();
		switch(a) {
			case 0:	
				for(int i = 0; i < str.length(); i++) {
					j.append(str.substring(i, i + 1));
					pause(b);
				}
				break;
			case 1:
				for(int i = 0; i < str.split("\n").length; i++) {
					for(int k = 0; k < str.split("\n")[i].length(); k++) {
						j.append(str.split("\n")[i].substring(k, k + 1));
						pause(b);
					}
					j.append("\n");
					if(i < str.split("\n").length - 1) pause(x[0]);
				}
				break;
			case 2:
				for(int i = 0; i < str.split("\n").length; i++) {
					j.append(str.split("\n")[i] + "\n");
					pause(b);
				}
				break;
			case 3:
				int max = 0;
				for(int i = 0; i < str.split("\n").length; i++) max = Math.max(max, str.split("\n")[i].length());
				for(int i = 0; i < max; i++) {
					String s = "";
					for(int k = 0; k < str.split("\n").length; k++) s += str.split("\n")[k].substring(0, Math.min(i, str.split("\n")[k].length())) + "\n";
					j.setText(prev + s);
					pause(b);
				}
				break;
			case 4:
				ArrayList<Integer> ar = new ArrayList<Integer>();
				char[] c = str.toCharArray();
				for(int i = 0; i < str.length(); i++) if(" \n".indexOf(str.substring(i, i + 1)) == -1) {
					ar.add(i);
					c[i] = ' ';
				}
				Collections.shuffle(ar); 
				while(ar.size() > 0) {
					for(int k = 0; k < x[0] && ar.size() > 0; k++) {
						int z = ar.remove(0);
						c[z] = str.charAt(z);
					}
					j.setText(prev + new String(c));
					pause(b);
				}
				break;
		}
	}

	public static void main(String[] args) {
		RPGRun main = new RPGRun();
		text.setEditable(false);
		text.setPreferredSize(new Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height - 62));
		text.setBackground(BACKGROUND_COLOR);
		text.setForeground(FOREGROUND_COLOR);
		text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
		text.setLineWrap(true);
		textIn.setBorder(null);
		textIn.setPreferredSize(new Dimension(SCREEN_SIZE.width - 35, 35));
		textIn.setBackground(BACKGROUND_COLOR);
		textIn.setForeground(FOREGROUND_COLOR);
		textIn.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
		textIn.grabFocus();
		label.setText(">>");
	    label.setBackground(BACKGROUND_COLOR);
	    label.setForeground(FOREGROUND_COLOR);
	    label.setOpaque(true);
	    label.setPreferredSize(new Dimension(35, 35));
	    label.setMinimumSize(new Dimension(35, 35));
	    label.setMaximumSize(new Dimension(35, 35));
	    label.setFont(new Font("Monospaced", Font.PLAIN, 20));
	    layout.setVgap(0);
	    textPanel.setLayout(layout);
		textPanel.add(text);
	    textInPanel.setLayout((LayoutManager) new BoxLayout(textInPanel, BoxLayout.LINE_AXIS));
	    textInPanel.add(label);
	    textInPanel.add(textIn);
	    frame.setResizable(false);
	    frame.setLayout(layout);
	    frame.add(textPanel);
	    frame.add(textInPanel);
		frame.setSize(SCREEN_SIZE);
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(main);
		frame.setVisible(true);
		textIn.grabFocus();
		main.start();
	}
	
	public RPGRun() {
		setPreferredSize(SCREEN_SIZE);
		frame = new JFrame();
		textInPanel = new JPanel();
		textPanel = new JPanel();
		textIn = new JTextField();
		text = new JTextArea();
		layout = new FlowLayout(FlowLayout.LEADING, 0, 0);
		label = new JLabel();
	}
	
	public void start() {
		init();
		new Thread(this).start();
	}
	
	public void init() {
		setFocusable(true);
		textIn.addKeyListener(this);
		text.addMouseListener(this);
	}
	
	public void tick() {
		
	}
	
	public void go() {
		story.executeLevel();
	}
	
	public void run() {
		if(isRunning) {
			go();
		}
	}
	
	public void pause(int i) {
		try {
			Thread.sleep(i);
		} catch(InterruptedException e) {}
	}
	
	public void stop() {
		isRunning = false;
	}
}
