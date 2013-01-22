package ea.de;

import ea.util.RandomGenerator;

public class UniformniMutator extends Mutator<double[][]> {

	public UniformniMutator(double vjerojatnostMutacije, RandomGenerator generator) {
		super(vjerojatnostMutacije, generator);
	}

	@Override
	public void mutiraj(double[][] odrediste, double[][] donor) {
		
		for (int i = 0; i < odrediste.length; i++) {
			for (int j = 0; j < odrediste[i].length && generator.vratiDouble() < vjerojatnostMutacije; j++) {
				odrediste[i][j] -= donor[i][j];
			}
		}
		
	}

}
