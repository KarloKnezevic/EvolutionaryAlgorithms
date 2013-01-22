/**
 * 
 */
package ea.ga;

import java.util.ArrayList;
import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public abstract class Populacija<T extends Krajolik<?>> {
	
	protected List<Jedinka<T>> jedinke;
	
	protected Object delta;
	
	protected int mutator;
	
	protected boolean koristiMutaciju = false;
	
	protected boolean koristiRekombinaciju = false;
	
	protected int rekombinator;

	protected double vjerojatnostMutacije;
	
	protected Selektor<T> selektor;

	protected T krajolik;

	protected RandomGenerator generator;

	protected Jedinka<T> najbolje;
	
	public Populacija(int velicina) {
		jedinke = new ArrayList<Jedinka<T>>(velicina);
		for (int i = 0; i < velicina; i++) { jedinke.add(null); }
	}

	public Jedinka<T> vratiLokalnoNajbolje() {
		if (jedinke.size() == 0) { return null; }
		Jedinka<T> najbolje = jedinke.get(0);
		int limit = jedinke.size();
		for (int i = 1; i < limit; i++) {
			if (jedinke.get(i).compareTo(najbolje) < 0) {
				najbolje = jedinke.get(i);
			}
		}
		
		return najbolje.kopiraj();
	}

	public Jedinka<T> vratiLokalnoNajgore() {
		if (jedinke.size() == 0) { return null; }
		Jedinka<T> najgore = jedinke.get(0);
		int limit = jedinke.size();
		for (int i = 1; i < limit; i++) {
			if (jedinke.get(i).compareTo(najgore) > 0) {
				najgore = jedinke.get(i);
			}
		}
		
		return najgore.kopiraj();
	}
	
	protected void obnoviGlobalnoNajbolje() {
		Jedinka<T> moguceNajbolje = vratiLokalnoNajbolje();
		if (najbolje == null || moguceNajbolje.compareTo(najbolje) < 0) { najbolje = moguceNajbolje; }
	}
	
	public Jedinka<T> vratiGlobalnoNajbolje() { return najbolje.kopiraj(); }
	
	public void koristiMutaciju(int mutator) {
		koristiMutaciju = true;
		this.mutator = mutator;
	}
	
	public void koristiRekombinaciju(int rekombinator) {
		koristiRekombinaciju = true;
		this.rekombinator = rekombinator;
	}
	
	public void postaviMutacijskuDeltu(Object delta) { this.delta = delta; }
	
	@Override
	public String toString() {
		StringBuilder graditelj = new StringBuilder();
		graditelj.append("sa jedinkama\n[\n");
		int limit = jedinke.size();
		for (int i = 0; i < limit; i++) {
			graditelj.append("\t");
			graditelj.append(jedinke.get(i));
			graditelj.append("\n");
		}
		graditelj.append("]");
		return graditelj.toString();
	}
	
	public List<Jedinka<T>> vratiJedinke() {
		List<Jedinka<T>> vrati = new ArrayList<Jedinka<T>>(jedinke.size());
		int limit = jedinke.size();
		for (int i = 0; i < limit; i++) { vrati.add(i, jedinke.get(i)); }
		return vrati;
	}

	public void postaviVjerojatnostMutacije(double vjerojatnostMutacije) {
		this.vjerojatnostMutacije = vjerojatnostMutacije;
	}
	
	public void postaviSelektor(Selektor<T> selektor) {
		this.selektor = selektor;
	}
	
	public Selektor<T> vratiSelektor() { return selektor; }
	
	public abstract void evoluiraj();
	
	public abstract void inicijaliziraj();
	
	public T vratiKrajolik() { return krajolik; }
	
	public RandomGenerator vratiGenerator() { return generator; }
	
	public void postaviGenerator(RandomGenerator generator) { this.generator = generator; }
	
	public void postaviKrajolik(T krajolik) { this.krajolik = krajolik; }
	
}
