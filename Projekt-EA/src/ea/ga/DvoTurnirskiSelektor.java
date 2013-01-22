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
public class DvoTurnirskiSelektor<T extends Krajolik<?>> extends Selektor<T> {

	public DvoTurnirskiSelektor(RandomGenerator generator) {
		super(generator);
	}

	/**
	 * @see ea.ga.Selektor#vratiSljedecuJedinku()
	 */
	@Override
	public Jedinka<T> vratiSljedecuJedinku() {
		Jedinka<T> prva = populacija.get(randomGenerator.vratiInt(populacija.size()));
		Jedinka<T> druga = populacija.get(randomGenerator.vratiInt(populacija.size()));
		return (prva.racunajFaktorDobrote() > druga.racunajFaktorDobrote()) ? prva : druga;
	}

}
