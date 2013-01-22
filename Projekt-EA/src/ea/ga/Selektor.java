/**
 * 
 */
package ea.ga;

import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public abstract class Selektor<T extends Krajolik<?>> {
	
	protected List<Jedinka<T>> populacija;
	
	protected RandomGenerator randomGenerator;
	
	public Selektor(RandomGenerator generator) {
		this.randomGenerator = generator;
	}
	
	public void postaviPopulaciju(List<Jedinka<T>> populacija) {
		this.populacija = populacija;
	}
	
	public abstract Jedinka<T> vratiSljedecuJedinku();

}
