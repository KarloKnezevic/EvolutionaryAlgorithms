/**
 * 
 */
package ea.ais;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Zlikavac32
 *
 */
public class SAISPopulacija<R, E, T extends Krajolik<E>> extends Populacija<R, E, T> {

	protected int brojKlonova;
	
	protected Mutator<R> mutator;
	
	public SAISPopulacija(int brojAntiTijela, int brojKlonova, RandomGenerator generator, Mutator<R> mutator) {
		super(brojAntiTijela, generator);
		this.brojKlonova = brojKlonova;
		this.mutator = mutator;
	}

	/**
	 * @see ea.ais.Populacija#evoluiraj()
	 */
	@Override
	public void evoluiraj() {
		if (antiTijela == null) { throw new IllegalArgumentException("Antitijela moraju biti inicijalizirana prije pokretanja evolucije"); }
		int velicina = antiTijela.size();
		List<AntiTijelo<R, E, T>> klonovi = new ArrayList<AntiTijelo<R, E, T>>(brojKlonova * velicina + velicina);
		if (mutator != null) { mutator.inicijaliziraj(); }
		klonovi.addAll(antiTijela);
		for (int i = 0; i < velicina; i++) {
			AntiTijelo<R, E, T> temp = antiTijela.get(i);
			for (int j = 0; j < brojKlonova; j++) {
				//Odmah cemo i mutirati
				AntiTijelo<R, E, T> klon = temp.kopiraj();
				klon.mutiraj(mutator);
				klonovi.add(klon);
			}
		}
		Collections.sort(klonovi);
		antiTijela.clear();
		for (int i = 0; i < velicina; i++) {
			antiTijela.add(klonovi.get(i));
		}
		azurirajGlobalnoNajbolje();
	}

}
