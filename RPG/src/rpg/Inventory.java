package rpg;

import java.util.ArrayList;
import java.util.Arrays;

public class Inventory {
	ArrayList<ArrayList<Pair<Item, Integer>>> inv; //create subinv class to replace ArrayList<Pair<Item, Integer>>?
	
	public Inventory() {
		inv = new ArrayList<ArrayList<Pair<Item, Integer>>>();
	}
	
	public void add(Item i) {
		for(ArrayList<Pair<Item, Integer>> ar : inv) if(ar.get(0).first.getClass().equals(i.getClass())) {
			for(Pair<Item, Integer> p : ar) if(p.first.equals(i)) {
				p.second++;
				return;
			}
			ar.add(new Pair<Item, Integer>(i, 1));
			return;
		}
		inv.add(new ArrayList<Pair<Item, Integer>>(Arrays.asList(new Pair<Item, Integer>(i, 1))));
	}
	
	public ArrayList<Pair<Item, Integer>> get(int a) {
		return inv.get(a);
	}
	
	public Item get(int a, int b) {
		return inv.get(a).get(b).first;
	}
	
	public boolean remove(Item i) {
		for(ArrayList<Pair<Item, Integer>> ar : inv) if(ar.get(0).first.getClass().equals(i.getClass()))
		for(Pair<Item, Integer> p : ar) if(p.first.equals(i)) {
			if(--p.second == 0) ar.remove(p);
			if(ar.size() == 0) inv.remove(ar);
			return true;
		}
		return false;
	}
	
	public void clear() {
		inv.clear();
	}
}
