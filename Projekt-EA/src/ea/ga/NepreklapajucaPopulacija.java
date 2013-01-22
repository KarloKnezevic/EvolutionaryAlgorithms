/**
 * 
 */
package ea.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ea.util.Krajolik;

/**
 * @author Zlikavac32
 *
 */
public abstract class NepreklapajucaPopulacija<T extends Krajolik<?>> extends PreklapajucaPopulacija<T> {
	

	public NepreklapajucaPopulacija(int velicina, int brojDjece) { 
		super(velicina, brojDjece); 
		if (brojDjece < velicina) {
			throw new IllegalArgumentException("Nepreklapajuca populacija mora imat broj djece veci ili jedank velicini populacije");
		}
	}
	
	@Override
	public void evoluiraj() {
		List<Jedinka<T>> privremeno = new ArrayList<Jedinka<T>>(brojDjece);
		int limit = brojDjece;
		selektor.postaviPopulaciju(jedinke);
		for (int i = 0; i < limit; i++) {
			Jedinka<T> jedinka = selektor.vratiSljedecuJedinku().kopiraj();
			if (koristiRekombinaciju) { jedinka.rekombiniraj(rekombinator, selektor.vratiSljedecuJedinku().kopiraj()); }
			if (koristiMutaciju) { jedinka.mutiraj(mutator, vjerojatnostMutacije); }
			privremeno.add(i, jedinka);
		}
		
		Collections.sort(privremeno);
		limit = jedinke.size();
		for (int i = 0; i < limit; i++) { jedinke.set(i, privremeno.get(i)); }
		obnoviGlobalnoNajbolje();
	}

}
