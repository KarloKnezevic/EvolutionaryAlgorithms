/**
 * 
 */
package ea.ga;

import ea.util.RealniKrajolik;


/**
 * @author Zlikavac32
 *
 */
public class GenotipPreklapajucaPopulacija extends PreklapajucaPopulacija<RealniKrajolik> {

	int brojBitova;
	
	/**
	 * @param velicina
	 * @param brojDjece
	 */
	public GenotipPreklapajucaPopulacija(int velicina, int brojDjece, int brojBitova) {
		super(velicina, brojDjece);
		this.brojBitova = brojBitova;
	}

	/**
	 * @see ea.ga.Populacija#inicijaliziraj()
	 */
	@Override
	public void inicijaliziraj() {
		int limit = jedinke.size();
		for (int i = 0; i < limit; i++) {
			RealnaVarijablaGenotipJedinka jedinka = new RealnaVarijablaGenotipJedinka(this, brojBitova);
			jedinka.inicijaliziraj();
			jedinke.set(i, jedinka);
		}
	}

}
