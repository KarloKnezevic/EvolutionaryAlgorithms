package ea.de;

import ea.util.RandomGenerator;

public class BinomniMutator extends Mutator<double[][]> {

	public BinomniMutator(double vjerojatnostMutacije, RandomGenerator generator) {
		super(vjerojatnostMutacije, generator);
	}

	@Override
	public void mutiraj(double[][] odrediste, double[][] donor) {
		
		for (int i = 0; i < odrediste.length; i++) {
			for (int j = 0; j < odrediste[i].length; j++) {
				if (generator.vratiDouble() < vjerojatnostMutacije) {
					odrediste[i][j] -= donor[i][j];
				}
			}
		}
		
	}

}
