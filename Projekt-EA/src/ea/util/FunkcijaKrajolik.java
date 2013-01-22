/**
 * 
 */
package ea.util;

import de.congrace.exp4j.Calculable;

/**
 * @author Zlikavac32
 *
 */
public class FunkcijaKrajolik extends RealniKrajolik {

	protected boolean invertiran;

	private Calculable funkcija;
	
	private String[] varijable;

	@Override
	public double racunajFaktorDobrote(double[] vrijednost) {
		double[] novo = new double[vrijednost.length];
		for (int i = 0; i < vrijednost.length; i++) { novo[i] = vrijednost[i]; }
		double tempVrijednost = racunajVrijednost(novo);
		return invertiran ? -tempVrijednost : tempVrijednost;
	}

	@Override
	public boolean jeValjanaVrijednost(double[] vrijednost) {
		for (int i = 0; i < vrijednost.length; i++) {
			if (vrijednost[i] > gornjaGranica[i] || vrijednost[i] < donjaGranica[i]) { return false; }
		}
		return true;
	}
		
	/**
	 * Odreduje da li je funkcija invertirana ili ne, tj. odreduje da li trazimo minimum ili maksimum.
	 * 
	 * @param invertiran True ako je funkcija invertirana, false ako ne
	 */
	public void postaviInvertiran(boolean invertiran) { this.invertiran = invertiran; }
	
	public void postaviVarijable(String[] varijable) { this.varijable = varijable; }

	@Override
	public synchronized double racunajVrijednost(double[] vrijednost) { 
		for (int i = 0; i < vrijednost.length; i++) {
			funkcija.setVariable(varijable[i], vrijednost[i]);
		}
		return funkcija.calculate();
	}

	public void postaviFunkciju(Calculable f) {
		funkcija = f;
	}


}
