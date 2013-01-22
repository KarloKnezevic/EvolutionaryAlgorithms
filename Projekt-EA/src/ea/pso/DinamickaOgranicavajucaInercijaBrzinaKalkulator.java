/**
 * 
 */
package ea.pso;

import ea.util.RandomGenerator;

/**
 * @author Zlikavac32
 *
 */
public class DinamickaOgranicavajucaInercijaBrzinaKalkulator implements BrzinaKalkulator<double[]> {

	private double c1;
	
	private double c2;
	
	private double[] donjaGranica;
	
	private double[] gornjaGranica;

	private double inercija;
	
	private int trenutniKorak = 0;
	
	private int brojKoraka;

	private double minInercija;

	private double maksInercija;

	public DinamickaOgranicavajucaInercijaBrzinaKalkulator(double c1, double c2, double[] donjaGranica, double gornjaGranica[], double minInercija, double maksInercija, int brojKoraka) {
		this.c1 = c1;
		this.c2 = c2;
		this.donjaGranica = donjaGranica;
		this.gornjaGranica = gornjaGranica;
		this.brojKoraka = brojKoraka;
		if (minInercija > 1 || minInercija < 0) { throw new IllegalArgumentException("Minimum faktora inercije mora biti u rasponu [0, 1]"); }
		this.minInercija = minInercija;
		if (minInercija > 1 || minInercija < 0) { throw new IllegalArgumentException("Maksimum faktora inercije mora biti u rasponu [0, 1]"); }
		if (maksInercija < minInercija) { throw new IllegalArgumentException("Maksimum faktora inercije mora biti veci od minimuma"); }
		this.maksInercija = maksInercija;
		azurirajFaktorInercije();
	}
	
	private void azurirajFaktorInercije() {
		inercija = (trenutniKorak <= brojKoraka) ? (trenutniKorak / brojKoraka * (minInercija - maksInercija) + maksInercija) : minInercija;
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
		trenutniKorak++;
		azurirajFaktorInercije();
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
