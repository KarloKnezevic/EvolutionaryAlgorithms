package ea.de;

import ea.util.Par;
import ea.util.Krajolik;
import ea.util.RandomGenerator;

import java.util.List;
import java.util.ArrayList;

public class DEPopulacija<T, E extends Krajolik<T>> extends Populacija<T, E> {

	private Vektor<T, E> lokalnoNajbolje;

	public DEPopulacija(
		int brojVektora, RandomGenerator generator,
		Mutator<T> mutator, Selektor<T, E> selektor
	) {
		super(brojVektora, generator, mutator, selektor);
	}

	@Override
	public void evoluiraj() {
		List<Vektor<T, E>> novi = new ArrayList<Vektor<T, E>>(brojVektora);
		
		for (int i = 0; i < brojVektora; i++) {
			Vektor<T, E> temp = vektori.get(i);
			Par<Vektor<T, E>, T> parNoviIDonor = selektor.selektiraj(i, this, generator);
			mutator.mutiraj(parNoviIDonor.prvi.vratiVrijednost(), parNoviIDonor.drugi);
			parNoviIDonor.prvi.postaviVrijednost(parNoviIDonor.prvi.vratiVrijednost());
			if (parNoviIDonor.prvi.compareTo(temp) < 0) {
				novi.add(parNoviIDonor.prvi);
			} else { novi.add(temp); }
		}
		
		vektori = novi;
				
		azurirajLokalnoNajbolje();
		azurirajGlobalnoNajbolje();
	}
	
	protected void azurirajLokalnoNajbolje() {
		lokalnoNajbolje = super.vratiLokalnoNajbolje();
	}
	
	@Override
	public void inicijaliziraj(Inicijalizator<T, E> inicijalizator) {
		vektori = inicijalizator.inicijaliziraj(brojVektora, generator);
		azurirajLokalnoNajbolje();
		azurirajGlobalnoNajbolje();
	}
	
	@Override
	public Vektor<T, E> vratiLokalnoNajbolje() {
		return lokalnoNajbolje.kopiraj();
	}

}
