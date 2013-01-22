package ea.ais;

import ea.util.RandomGenerator;

public class JedanBitVarijablaMutator implements Mutator<byte[]> {

	protected RandomGenerator generator;
	
	protected int brojBitovaPoVarijabli;
	
	public JedanBitVarijablaMutator(int brojBitovaPoVarijabli, RandomGenerator generator) {
		this.generator = generator;
		this.brojBitovaPoVarijabli = brojBitovaPoVarijabli;
	}
	
	@Override
	public byte[] mutiraj(AntiTijelo<byte[], ?, ?> antiTijelo) {
		byte[] podatci = antiTijelo.vratiReprezentaciju();
		int brojVarijabli = podatci.length / brojBitovaPoVarijabli;
		if (brojVarijabli * brojBitovaPoVarijabli != podatci.length) {
			throw new IllegalArgumentException("Nema dovoljno bitova za sve varijable");
		}
		for (int i = 0; i < brojVarijabli; i++) {
			int pozicija = generator.vratiInt(brojBitovaPoVarijabli);
			int odmak = i * brojBitovaPoVarijabli + pozicija;
			podatci[odmak] = (byte) (1 - podatci[odmak]);
		}
		return podatci;
	}

	@Override
	public void inicijaliziraj() {
		//Nista
	}
}
