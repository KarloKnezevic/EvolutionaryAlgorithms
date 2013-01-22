/**
 * 
 */
package ea.util;

import java.util.List;


/**
 * @author Zlikavac32
 *
 */
public class KoloSrece<T extends KoloSrece.Racunljiv> {

	private List<T> skup;

	private double[] koloSrece;
	
	private RandomGenerator generator;
	
	public static interface Racunljiv {
		public double racunajVrijednost();
	}
	
	public KoloSrece(List<T> skup, RandomGenerator generator) { 
		this.skup = skup; 
		this.generator = generator;
		stvoriKoloSrece();
	}
	
	private void stvoriKoloSrece() {
		double suma = 0;
		double odmak = Double.POSITIVE_INFINITY;
		int limit = skup.size();
		for (int i = 0; i < limit; i++) { 
			double faktorDobrote = skup.get(i).racunajVrijednost();
			if (faktorDobrote < odmak) { odmak = faktorDobrote; }
		}
		
		odmak = (odmak < 0) ? -odmak : 0;
		
		suma = 0;
		
		for (int i = 0; i < limit; i++) { 
			suma += skup.get(i).racunajVrijednost() + odmak; 
		}
		
		double ukupnaSuma = suma;
		
		koloSrece = new double[limit];
				
		suma = 0;
		
		for (int i = 0; i < limit; i++) {
			suma += (skup.get(i).racunajVrijednost() + odmak) / ukupnaSuma;
			koloSrece[i] = suma;
		}
	}
	
	public T okreni() {
		double vrijednost = generator.vratiDouble();
		
		int l = 0;
		int d = skup.size() - 1;
		
		while (l < d) {
			int m = (l + d) / 2;
			if (koloSrece[m] > vrijednost) { d = m; }
			else { l = m + 1; }
		}
		
		return skup.get(l);
	}
	
}
