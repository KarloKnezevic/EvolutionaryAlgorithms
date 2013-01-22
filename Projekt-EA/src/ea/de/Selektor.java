package ea.de;

import ea.util.Krajolik;
import ea.util.Par;
import ea.util.RandomGenerator;

public interface Selektor<T, E extends Krajolik<T>> {

	public Par<Vektor<T, E>, T> selektiraj(int indeks, Populacija<T, E> populacija, RandomGenerator generator);
	
}
