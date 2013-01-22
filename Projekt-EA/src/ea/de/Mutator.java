package ea.de;

import ea.util.RandomGenerator;

public abstract class Mutator<T> {

	protected double vjerojatnostMutacije;
	
	protected RandomGenerator generator;
	
	public Mutator(double vjerojatnostMutacije, RandomGenerator generator) {
		this.vjerojatnostMutacije = vjerojatnostMutacije;
		this.generator = generator;
	}
	
	public abstract void mutiraj(T odrediste, T donor);
	
}
