package ea.ais;

import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

public interface Inicijalizator<R, E, T extends Krajolik<E>> {

	public List<AntiTijelo<R, E, T>> inicijaliziraj(int velicina, RandomGenerator generator);
	
}
