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
public class CLONALGPopulacija<R, E, T extends Krajolik<E>> extends Populacija<R, E, T> {

	protected int beta;
	
	protected Mutator<R> mutator;
	
	protected int brojNovih;
	
	public CLONALGPopulacija(int brojNovih, int brojAntiTijela, int beta, RandomGenerator generator, Mutator<R> mutator) {
		super(brojAntiTijela, generator);
		this.beta = beta;
		this.mutator = mutator;
		this.brojNovih = brojNovih;
	}

	/**
	 * @see ea.ais.Populacija#evoluiraj()
	 */
	@Override
	public void evoluiraj() {
		if (antiTijela == null) { throw new IllegalArgumentException("Antitijela moraju biti inicijalizirana prije pokretanja evolucije"); }
		int velicina = antiTijela.size();
		Collections.sort(antiTijela);
		List<AntiTijelo<R, E, T>> klonovi = new ArrayList<AntiTijelo<R, E, T>>(velicina * velicina / 2 + velicina + brojNovih);
				
		klonovi.addAll(antiTijela);

		if (mutator != null) { mutator.inicijaliziraj(); }
		
		for (int i = 0; i < velicina; i++) {
			AntiTijelo<R, E, T> temp = antiTijela.get(i);
			for (int j = (int) beta * velicina / (i + 1); j > 0; j--) {
				//Odmah cemo i mutirati
				AntiTijelo<R, E, T> klon = temp.kopiraj();
				klon.mutiraj(mutator);
				klonovi.add(klon);
			}
		}
		
		AntiTijelo<R, E, T> temp = klonovi.get(0).kopiraj();
		
		for (int i = 0; i < brojNovih; i++) {
			temp.inicijaliziraj(generator);
			klonovi.add(temp.kopiraj());
		}
		
		Collections.sort(klonovi);
		
		antiTijela.clear();
		
		for (int i = 0; i < velicina; i++) {
			antiTijela.add(klonovi.get(i));
		}
		azurirajGlobalnoNajbolje();
	}

}
