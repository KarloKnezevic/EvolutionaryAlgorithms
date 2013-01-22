/**
 * 
 */
package ea.ga;

import ea.util.KoloSrece;
import ea.util.Krajolik;

/**
 * 
 * @author Zlikavac32
 *
 */
public abstract class Jedinka<T extends Krajolik<?>> implements Comparable<Jedinka<T>>, KoloSrece.Racunljiv {

	protected Populacija<T> populacija;

	public Jedinka(Populacija<T> populacija) {
		this.populacija = populacija;
	}
	
	/**
	 * Racuna faktor dobrote za trenutnu jedinku.
	 * 
	 * @return Faktor dobrote
	 */
	public abstract double racunajFaktorDobrote();

	/**
	 * Kopira trenutnu jedinku.
	 * 
	 * @return Kopija jedinke
	 */
	public abstract Jedinka<T> kopiraj();
	
	/**
	 * Mutira trenutnu jedinku.
	 * 
	 * @param mutator Mutator koji se koristi
	 */
	public abstract void mutiraj(int mutator, double vjerojatnostMutacije);
	
	/**
	 * Mutira trenutnu jedinku za neku deltu.
	 * 
	 * @param mutator Mutator koji se koristi
	 * @param delta Delta koja odreduje nacin mutacije
	 * @param vjerojatnostMutacije 
	 */
	public abstract void mutiraj(int mutator, Object delta, double vjerojatnostMutacije);
	
	/**
	 * Rekombinira trenutnu jedinku sa partnerom koristeci rekombinator.
	 * 
	 * @param rekombinator Rekombinator koji se koristi
	 * @param partner Partner koji se koristi u rekombinaciji
	 */
	public abstract void rekombiniraj(int rekombinator, Jedinka<T> partner);

	/**
	 * Inicijalizira trenutnu jedinku.
	 */
	public abstract void inicijaliziraj();
	
	public abstract Object vratiVrijednost();

	@Override
	public int compareTo(Jedinka<T> strani) {
		if (strani == null) { return -1; }
		double mojFaktorDobrote = racunajFaktorDobrote();
		double straniFaktorDobrote = strani.racunajFaktorDobrote();
		if (mojFaktorDobrote > straniFaktorDobrote) { return -1; }
		else if (mojFaktorDobrote < straniFaktorDobrote) { return 1; }
		return 0;
	}
	
}
