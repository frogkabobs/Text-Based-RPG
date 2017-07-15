package rpg;

public class Probability<T> { //extends Pair<T, Double>?
	public T item;
	public double probability;
	
	public Probability(T i, double p) {
		item = i;
		probability = p;
	}
	
	public boolean execute() {
		return Math.random() < probability;
	}
}
