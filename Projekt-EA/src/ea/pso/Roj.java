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
public abstract class Roj<T> {
	
	protected Cestica<T>[] cestice;
	
	protected Cestica<T> najbolje;
	
	protected RandomGenerator generator;
	
	protected int brojCestica;

	protected Krajolik<T> krajolik;

	protected SusjedstvoGraditelj<T> susjedstvoGraditelj;

	protected BrzinaKalkulator<T> brzinaKalkulator;
	
	public abstract void inicijaliziraj();
	
	public Roj(
		int brojCestica, RandomGenerator generator, Krajolik<T> krajolik, SusjedstvoGraditelj<T> susjedstvoGraditelj,
		BrzinaKalkulator<T> brzinaKalkulator 
	) {
		this.brojCestica = brojCestica;
		this.generator = generator;
		this.krajolik = krajolik;
		this.susjedstvoGraditelj = susjedstvoGraditelj;
		this.brzinaKalkulator = brzinaKalkulator;
	}
	
	public Cestica<T>[] vratiCestice() {
		return Arrays.copyOf(cestice, cestice.length);
	}
	
	public Cestica<T> vratiLokalnoNajgore() {
		if (cestice == null || cestice.length < 1) { return null; }
		Cestica<T> najgore = cestice[0];
		for (int i = 1; i < cestice.length; i++) {
			if (cestice[i].compareTo(najgore) > 0) { najgore = cestice[i]; }
		}
		return najgore.kopiraj();
	}

	public Cestica<T> vratiLokalnoNajbolje() {
		if (cestice == null || cestice.length < 1) { return null; }
		Cestica<T> najbolje = cestice[0];
		for (int i = 1; i < cestice.length; i++) {
			if (cestice[i].compareTo(najbolje) < 0) { najbolje = cestice[i]; }
		}
		return najbolje.kopiraj();
	}
	
	protected void obnoviGlobalnoNajbolje() {
		Cestica<T> moguceNajbolje = vratiLokalnoNajbolje();
		if (najbolje == null || moguceNajbolje.compareTo(najbolje) < 0) { najbolje = moguceNajbolje; }
	}
	
	public Cestica<T> vratiGlobalnoNajbolje() { return najbolje; }

	public abstract void evoluiraj();
	
}
