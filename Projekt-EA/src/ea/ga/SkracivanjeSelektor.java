/**
 * 
 */
package ea.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class SkracivanjeSelektor<T extends Krajolik<?>> extends Selektor<T> {

	int skracivanje;
	
	public SkracivanjeSelektor(int skracivanje, RandomGenerator randomGenerator) {
		super(randomGenerator);
		this.skracivanje = skracivanje; 
	}
	
	@Override
	public void postaviPopulaciju(List<Jedinka<T>> populacija) {
		List<Jedinka<T>> nova = new ArrayList<Jedinka<T>>(populacija.size());
		int limit = populacija.size();
		for (int i = 0; i < limit; i++) { nova.add(i, populacija.get(i)); }
		Collections.sort(nova);
		super.postaviPopulaciju(nova);
	}
	
	/* (non-Javadoc)
	 * @see ea.ga.Selektor#vratiSljedecuJedinku()
	 */
	@Override
	public Jedinka<T> vratiSljedecuJedinku() {
		return populacija.get(randomGenerator.vratiInt(skracivanje));
	}

}
