/**
 * 
 */
package ea.pso;

import java.text.DecimalFormat;
import java.util.Arrays;

import ea.util.RandomGenerator;
import ea.util.RealniKrajolik;

/**
 * @author Zlikavac32
 *
 */
public class RealnaVarijablaCestica extends Cestica<double[]> {

	protected int brojVarijabli;
	
	protected RealniKrajolik krajolik;
	
	protected double faktorDobrote;
	
	protected double najboljiFaktorDobrote;
	
	protected double[] staro;
	
	protected static DecimalFormat format = new DecimalFormat("0.000000");
	
	public RealnaVarijablaCestica(
		int brojVarijabli, RealniKrajolik krajolik, Susjedstvo<double[]> susjedstvo, 
		BrzinaKalkulator<double[]> brzinaKalkulator
	) { 
		super(krajolik, susjedstvo, brzinaKalkulator); 
		this.krajolik = krajolik;
		this.brojVarijabli = brojVarijabli;
	}

	/**
	 * @see ea.pso.Cestica#jeValjanaVrijednost(java.lang.Object)
	 */
	@Override
	public boolean jeValjanaVrijednost(double[] vrijednost) { return krajolik.jeValjanaVrijednost(vrijednost); }

	/**
	 * @see ea.pso.Cestica#racunajFaktorDobrote()
	 */
	@Override
	public double racunajFaktorDobrote() { return faktorDobrote; }
	
	protected void azurirajFaktorDobrote() {
		faktorDobrote = krajolik.racunajFaktorDobrote(vrijednost);
	}

	@Override
	public RealnaVarijablaCestica kopiraj() {
		RealnaVarijablaCestica kopija = new RealnaVarijablaCestica(
			brojVarijabli, krajolik, susjedstvo, brzinaKalkulator
		);
		kopija.brzina = Arrays.copyOf(brzina, brzina.length);
		kopija.vrijednost = Arrays.copyOf(vrijednost, vrijednost.length);
		kopija.najboljiFaktorDobrote = najboljiFaktorDobrote;
		kopija.faktorDobrote = faktorDobrote;
		kopija.staro = staro == null ? null : Arrays.copyOf(staro, staro.length);
		return kopija;
	}

	@Override
	public void inicijaliziraj(RandomGenerator generator) {
		vrijednost = new double[brojVarijabli];
		brzina = new double[brojVarijabli];
		double[] donjaGranica = brzinaKalkulator.vratiDonjuGranicu();
		double[] gornjaGranica = brzinaKalkulator.vratiGornjuGranicu();
		for (int i = 0; i < brojVarijabli; i++) {
			brzina[i] = generator.vratiDouble() * (gornjaGranica[i] - donjaGranica[i]) + donjaGranica[i];
			vrijednost[i] = generator.vratiDouble() * (krajolik.vratiGornjuGranicu()[i] - krajolik.vratiDonjuGranicu()[i])
				+ krajolik.vratiDonjuGranicu()[i];
		}
		azurirajFaktorDobrote();
		obnoviNajboljuVrijednost();
	}

	@Override
	public void evoluiraj(RandomGenerator generator) {
		brzina = brzinaKalkulator.izracunajBrzinu(this, generator);
		staro = vrijednost;
		double[] novaVrijednost = new double[vrijednost.length];
		for (int i = 0; i < brojVarijabli; i++) {
			novaVrijednost[i] = vrijednost[i] + brzina[i];
		}
		if (jeValjanaVrijednost(novaVrijednost)) {
			vrijednost = novaVrijednost;
			azurirajFaktorDobrote();
			obnoviNajboljuVrijednost();
		}
	}

	protected void obnoviNajboljuVrijednost() {
		if (najboljaVrijednost == null || faktorDobrote > najboljiFaktorDobrote) {
			najboljaVrijednost = Arrays.copyOf(vrijednost, vrijednost.length);
			najboljiFaktorDobrote = faktorDobrote;
		}
	}

	@Override
	public double[] vratiVrijednost() {
		return Arrays.copyOf(vrijednost, vrijednost.length);
	}

	@Override
	public double[] vratiNajboljuVrijednost() {
		return Arrays.copyOf(najboljaVrijednost, najboljaVrijednost.length);
	}
	
	@Override
	public String toString() {
		StringBuilder graditelj = new StringBuilder("Cestica na lokaciji (");
		graditelj.append(format.format(vrijednost[0]));
		graditelj.append(", ");
		graditelj.append(format.format(vrijednost[1]));
		graditelj.append(") sa funkcijskom vrijednoscu ");
		double[] tempVrijednost = new double[vrijednost.length];
		for (int i = 0; i < vrijednost.length; i++) { tempVrijednost[i] = vrijednost[i]; }
		graditelj.append(format.format(krajolik.racunajVrijednost(tempVrijednost)));
		return graditelj.toString();
	}

	@Override
	public double[] vratiStaruVrijednost() {
		return Arrays.copyOf(staro, staro.length);
	}

}
