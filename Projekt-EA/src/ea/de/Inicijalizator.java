package ea.de;

import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

public interface Inicijalizator<T, E extends Krajolik<T>> {

	public List<Vektor<T, E>> inicijaliziraj(int velicina, RandomGenerator generator);
	
}
