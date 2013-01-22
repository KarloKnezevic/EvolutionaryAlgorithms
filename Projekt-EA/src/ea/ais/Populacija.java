package ea.ais;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ea.util.Krajolik;
import ea.util.RandomGenerator;

public abstract class Populacija<R, E, T extends Krajolik<E>> {
	
	protected int brojAntiTijela;
	
	protected RandomGenerator generator;
	
	protected List<AntiTijelo<R, E, T>> antiTijela;
	
	protected AntiTijelo<R, E, T> najbolje;
	
	public Populacija(int brojAntiTijela, RandomGenerator generator) {
		this.brojAntiTijela = brojAntiTijela;
		this.generator = generator;
	}
	
	public void inicijaliziraj(Inicijalizator<R, E, T> inicijalizator) {
		antiTijela = inicijalizator.inicijaliziraj(brojAntiTijela, generator);
		azurirajGlobalnoNajbolje();
	}
		
	public AntiTijelo<R, E, T> vratiLokalnoNajbolje() {
		if (antiTijela == null || antiTijela.size() < 1) { return null; }
		AntiTijelo<R, E, T> najbolje = antiTijela.get(0);
		int kraj = antiTijela.size();
		for (int i = 1; i < kraj; i++) {
			AntiTijelo<R, E, T> temp = antiTijela.get(i);
			if (temp.compareTo(najbolje) < 0) {
				najbolje = temp;
			}
		}
		return najbolje.kopiraj();
	}
	
	public AntiTijelo<R, E, T> vratiLokalnoNajgore() {
		if (antiTijela == null || antiTijela.size() < 1) { return null; }
		AntiTijelo<R, E, T> najgore = antiTijela.get(0);
		int kraj = antiTijela.size();
		for (int i = 1; i < kraj; i++) {
			AntiTijelo<R, E, T> temp = antiTijela.get(i);
			if (temp.compareTo(najgore) > 0) {
				najgore = temp;
			}
		}
		return najgore.kopiraj();
	}
	
	protected void azurirajGlobalnoNajbolje() {
		AntiTijelo<R, E, T> moguceNajbolje = vratiLokalnoNajbolje();
		if (najbolje == null || moguceNajbolje.compareTo(najbolje) < 0) {
			najbolje = moguceNajbolje;
		}
	}
	
	public AntiTijelo<R, E, T> vratiGlobalnoNajbolje() {
		return najbolje.kopiraj();
	}
	
	public List<AntiTijelo<R, E, T>> vratiPopulaciju() {
		return Collections.unmodifiableList(antiTijela);
	}
	
	public abstract void evoluiraj();

	public List<AntiTijelo<R, E, T>> vratiKopijuPopulacije() {
		return new ArrayList<AntiTijelo<R,E,T>>(antiTijela);
	}
	
}
