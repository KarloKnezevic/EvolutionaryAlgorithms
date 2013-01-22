/**
 * 
 */
package ea.pso;

import java.util.Arrays;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public abstract class Cestica<T> implements Comparable<Cestica<T>> {
	
	protected T vrijednost;
	
	protected Susjedstvo<T> susjedstvo;
	
	protected double[] brzina;
	
	protected Krajolik<T> krajolik;
	
	protected BrzinaKalkulator<T> brzinaKalkulator;
	
	protected T najboljaVrijednost;
	
	public Cestica(Krajolik<T> krajolik, Susjedstvo<T> susjedstvo, BrzinaKalkulator<T> brzinaKalkulator) {
		this.krajolik = krajolik;
		this.susjedstvo = susjedstvo;
		this.brzinaKalkulator = brzinaKalkulator;
	}
	
//	/**
//	 * Postavlja vrijednost na novu. U slucaju da nije valjana, baca se iznimka
//	 * @param vrijednost
//	 */
//	public void postaviVrijednost(T vrijednost) {
//		if (!jeValjanaVrijednost(vrijednost)) { throw new IllegalArgumentException("Vrijednost '" + vrijednost + "' nije valjana"); }
//		this.vrijednost = vrijednost;
//	}
		
	public double[] vratiBrzinu() { return Arrays.copyOf(brzina, brzina.length); }
	
	public abstract boolean jeValjanaVrijednost(T vrijednost);
	
	public abstract T vratiVrijednost();
	
	public abstract T vratiStaruVrijednost();
	
	public abstract T vratiNajboljuVrijednost();
	
	/**
	 * Racuna faktor dobrote za trenutnu jedinku.
	 * 
	 * @return Faktor dobrote
	 */
	public abstract double racunajFaktorDobrote();

	@Override
	public int compareTo(Cestica<T> strani) {
		if (strani == null) { return -1; }
		double mojFaktorDobrote = racunajFaktorDobrote();
		double straniFaktorDobrote = strani.racunajFaktorDobrote();
		if (mojFaktorDobrote > straniFaktorDobrote) { return -1; }
		else if (mojFaktorDobrote < straniFaktorDobrote) { return 1; }
		return 0;
	}
	
	public Susjedstvo<T> vratiSusjedstvo() { return susjedstvo; }
	
	public abstract void inicijaliziraj(RandomGenerator generator);
	
	public abstract Cestica<T> kopiraj();
	
	public abstract void evoluiraj(RandomGenerator generator);
		
}
