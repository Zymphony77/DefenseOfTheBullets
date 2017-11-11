package utility;

public class Pair {
	public double first, second;
	
	public Pair() {
		first = 0;
		second = 0;
	}
	
	public Pair(double first, double second) {
		this.first = first;
		this.second = second;
	}
	
	public Pair(Pair tmp) {
		this.first = tmp.first;
		this.second = tmp.second;
	}
}

