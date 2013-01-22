package ea.de;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

public abstract class Vektor<T, E extends Krajolik<T>> implements Comparable<Vektor<T, E>> {

	protected E krajolik;
	
	public Vektor(E krajolik) {
		this.krajolik = krajolik;
	}
	
	public abstract double racunajFaktorDobrote();
	
	public abstract T vratiVrijednost();
	
	public abstract void inicijaliziraj(RandomGenerator generator);
	
	public abstract void postaviVrijednost(T vrijednost);
	
	@Override
	public int compareTo(Vektor<T, E> drugi) {
		double dFD = drugi.racunajFaktorDobrote();
		double mFD = racunajFaktorDobrote();
		if (dFD < mFD) { return 1; }
		if (dFD > mFD) { return -1; }
		return 0;
	}
	
	public abstract Vektor<T, E> kopiraj();
	
	public E vratiKrajolik() {
		return krajolik;
	}

}
