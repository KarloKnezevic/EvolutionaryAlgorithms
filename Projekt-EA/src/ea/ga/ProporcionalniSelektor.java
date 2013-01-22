/**
 * 
 */
package ea.ga;

import java.util.List;

import ea.util.KoloSrece;
import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class ProporcionalniSelektor<T extends Krajolik<?>> extends Selektor<T> {


	public ProporcionalniSelektor(RandomGenerator generator) {
		super(generator);
	}

	protected KoloSrece<Jedinka<T>> koloSrece;
	
	@Override
	public void postaviPopulaciju(List<Jedinka<T>> populacija) {
		super.postaviPopulaciju(populacija);
		koloSrece = new KoloSrece<Jedinka<T>>(populacija, randomGenerator);
	}
	
	/**
	 * @see ea.ga.Selektor#vratiSljedecuJedinku()
	 */
	@Override
	public Jedinka<T> vratiSljedecuJedinku() {
		return koloSrece.okreni();
	}

}
