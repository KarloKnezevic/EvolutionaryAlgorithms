/**
 * 
 */
package ea.pso;

import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class DinamickaInercijaBrzinaKalkulator implements BrzinaKalkulator<double[]> {

	private double c1;
	
	private double c2;
	
	private double[] donjaGranica;
	
	private double[] gornjaGranica;

	private double inercija;

	private double faktorSmanjenjaInercije;

	public DinamickaInercijaBrzinaKalkulator(double c1, double c2, double[] donjaGranica, double gornjaGranica[], double inercija, double faktorSmanjenjaInercije) {
		this.c1 = c1;
		this.c2 = c2;
		this.donjaGranica = donjaGranica;
		this.gornjaGranica = gornjaGranica;
		if (inercija > 1 || inercija < 0) { throw new IllegalArgumentException("Faktor inercije mora biti u rasponu [0, 1]"); }
		this.inercija = inercija;
		if (faktorSmanjenjaInercije > 1 || faktorSmanjenjaInercije < 0) { throw new IllegalArgumentException("Faktor smanjenja inercije mora biti u rasponu [0, 1]"); }
		this.faktorSmanjenjaInercije = faktorSmanjenjaInercije;
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
			brzina[i] = inercija * staraBrzina[i] + c1 * generator.vratiDouble() * (
				osobnoNajbolje[i] - trenutno[i]
			) +  c2 * generator.vratiDouble() * (
					globalnoNajbolje[i] - trenutno[i]
			);
			if (brzina[i] < donjaGranica[i]) { brzina[i] = donjaGranica[i]; }
			else if (brzina[i] > gornjaGranica[i]) { brzina[i] = gornjaGranica[i]; }
		}
		
		return brzina;
	}

	@Override
	public void zavrsiKrug() {
		inercija *= faktorSmanjenjaInercije;
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
