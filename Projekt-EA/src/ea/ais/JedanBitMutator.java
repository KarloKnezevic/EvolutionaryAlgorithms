package ea.ais;

import ea.util.RandomGenerator;

public class JedanBitMutator implements Mutator<byte[]> {
	
	protected RandomGenerator generator;

	public JedanBitMutator(RandomGenerator generator) {
		this.generator = generator;
	}
	
	@Override
	public byte[] mutiraj(AntiTijelo<byte[], ?, ?> antiTijelo) {
		byte[] podatci = antiTijelo.vratiReprezentaciju();
		int pozicija = generator.vratiInt(podatci.length);
		podatci[pozicija] = (byte) (1 - podatci[pozicija]);
		return podatci;
	}

	@Override
	public void inicijaliziraj() {
		//nista
	}

}
