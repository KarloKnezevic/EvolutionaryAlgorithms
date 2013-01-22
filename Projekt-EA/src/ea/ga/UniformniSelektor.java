/**
 * 
 */
package ea.ga;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class UniformniSelektor<T extends Krajolik<?>> extends Selektor<T> {


	public UniformniSelektor(RandomGenerator generator) {
		super(generator);
	}

	@Override
	public Jedinka<T> vratiSljedecuJedinku() {
		return populacija.get(randomGenerator.vratiInt(populacija.size()));
	}

}
