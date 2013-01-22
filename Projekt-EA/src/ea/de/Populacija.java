package ea.de;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

public abstract class Populacija<T, E extends Krajolik<T>> {
	
	protected List<Vektor<T, E>> vektori;
	
	protected Vektor<T, E> najbolje;
	
	protected int brojVektora;
	
	protected RandomGenerator generator;

	protected Mutator<T> mutator;

	protected Selektor<T, E> selektor;
	
	public Populacija(
		int brojVektora, RandomGenerator generator,
		Mutator<T> mutator, Selektor<T, E> selektor
	) {
		this.brojVektora = brojVektora;
		this.generator = generator;
		this.mutator = mutator;
		this.selektor = selektor;
	}

	public void inicijaliziraj(Inicijalizator<T, E> inicijalizator) {
		vektori = inicijalizator.inicijaliziraj(brojVektora, generator);
		azurirajGlobalnoNajbolje();
	}
		
	public Vektor<T, E> vratiLokalnoNajbolje() {
		if (vektori == null || vektori.size() < 1) { return null; }
		Vektor<T, E> najbolje = vektori.get(0);
		int kraj = vektori.size();
		for (int i = 1; i < kraj; i++) {
			Vektor<T, E> temp = vektori.get(i);
			if (temp.compareTo(najbolje) < 0) {
				najbolje = temp;
			}
		}
		return najbolje.kopiraj();
	}
	
	public Vektor<T, E> vratiLokalnoNajgore() {
		if (vektori == null || vektori.size() < 1) { return null; }
		Vektor<T, E> najgore = vektori.get(0);
		int kraj = vektori.size();
		for (int i = 1; i < kraj; i++) {
			Vektor<T, E> temp = vektori.get(i);
			if (temp.compareTo(najgore) > 0) {
				najgore = temp;
			}
		}
		return najgore.kopiraj();
	}
	
	protected void azurirajGlobalnoNajbolje() {
		Vektor<T, E> moguceNajbolje = vratiLokalnoNajbolje();
		if (najbolje == null || moguceNajbolje.compareTo(najbolje) < 0) {
			najbolje = moguceNajbolje;
		}
	}
	
	public Vektor<T, E> vratiGlobalnoNajbolje() {
		return najbolje.kopiraj();
	}
	
	public List<Vektor<T, E>> vratiPopulaciju() {
		return Collections.unmodifiableList(vektori);
	}
	
	public abstract void evoluiraj();

	public List<Vektor<T, E>> vratiKopijuPopulacije() {
		return new ArrayList<Vektor<T, E>>(vektori);
	}
}
