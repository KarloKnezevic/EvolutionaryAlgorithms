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
public abstract class PreklapajucaPopulacija<T extends Krajolik<?>> extends Populacija<T> {
	
	protected int brojDjece;

	public PreklapajucaPopulacija(int velicina, int brojDjece) { 
		super(velicina); 
		this.brojDjece = brojDjece;
	}

	@Override
	public void evoluiraj() {

		List<Jedinka<T>> privremeno = new ArrayList<Jedinka<T>>(jedinke.size() + brojDjece);
		int limit = jedinke.size();
		for (int i = 0; i < limit; i++) { privremeno.add(i, jedinke.get(i)); };
		selektor.postaviPopulaciju(jedinke);
		limit = jedinke.size() + brojDjece;
		for (int i = jedinke.size(); i < limit; i++) {
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
