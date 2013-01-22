/**
 * 
 */
package ea.pso;

import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class OgranicavajucaBrzinaKalkulator implements BrzinaKalkulator<double[]> {

	private double c1;
	
	private double c2;
	
	private double[] donjaGranica;
	
	private double[] gornjaGranica;
	
	private double K;

	public OgranicavajucaBrzinaKalkulator(double c1, double c2, double[] donjaGranica, double gornjaGranica[]) {
		this.c1 = c1;
		this.c2 = c2;
		this.donjaGranica = donjaGranica;
		this.gornjaGranica = gornjaGranica;
		double fi = c1 + c2;
		if (fi < 4) {
			throw new IllegalArgumentException("Faktori c1 i c2 zbrojeni moraju biti veci ili jednaki 4");
		}
		K = 2 / Math.abs(2 - fi - Math.sqrt(fi * fi - 4 * fi));
	}
	
	@Override
	public double[] izracunajBrzinu(Cestica<double[]> cestica,
			RandomGenerator generator) {
		
		double[] staraBrzina = cestica.vratiBrzinu();
		
		double[] brzina = new double[staraBrzina.length];
		double[] trenutno = cestica.vratiVrijednost();
		double[] osobnoNajbolje = cestica.vratiNajboljuVrijednost();
		double[] globalnoNajbolje = cestica.vratiSusjedstvo().vratiNajbolju().vratiVrijednost();
		
		for (int i = 0; i < brzina.length; i++) {
			brzina[i] = K * (staraBrzina[i] + c1 * generator.vratiDouble() * (
				osobnoNajbolje[i] - trenutno[i]
			) +  c2 * generator.vratiDouble() * (
					globalnoNajbolje[i] - trenutno[i]
			));
			if (brzina[i] < donjaGranica[i]) { brzina[i] = donjaGranica[i]; }
			else if (brzina[i] > gornjaGranica[i]) { brzina[i] = gornjaGranica[i]; }
		}
		
		return brzina;
	}

	@Override
	public void zavrsiKrug() {
		//Nista
	}

	@Override
	public double[] vratiDonjuGranicu() {
		return donjaGranica;
	}

	@Override
	public double[] vratiGornjuGranicu() {
		return gornjaGranica;
	}

}
