package ea.de;

import java.text.DecimalFormat;
import java.util.Arrays;

import ea.util.PolinomKrajolik;
import ea.util.RandomGenerator;

public class KoeficijentiVektor extends Vektor<double[][], PolinomKrajolik> {

	protected double faktorDobrote;
	
	protected double[][] koeficijenti;
	
	protected int brojVarijabli;
	
	protected static DecimalFormat format = new DecimalFormat("0.000000");
	
	protected int redPolinoma;

	public KoeficijentiVektor(int brojVarijabli, int redPolinoma, PolinomKrajolik krajolik) {
		super(krajolik);
		this.brojVarijabli = brojVarijabli;
		this.redPolinoma = redPolinoma;
	}

	@Override
	public double racunajFaktorDobrote() {
		return faktorDobrote;
	}
	
	protected void azurirajFaktorDobrote() {
		faktorDobrote = krajolik.racunajFaktorDobrote(koeficijenti);
	}

	@Override
	public double[][] vratiVrijednost() {
		//Ne bi bilo lose napraviti kopiju tako da se izvana ne moze promijeniti stanje,
		//no nema veze sad
		return koeficijenti;
	}

	@Override
	public void inicijaliziraj(RandomGenerator generator) {
		double[] dg = krajolik.vratiDonjuGranicu();
		double[] gg = krajolik.vratiGornjuGranicu();
		koeficijenti = new double[brojVarijabli][redPolinoma + 1];
		for (int i = 0; i < brojVarijabli; i++) {
			for (int j = 0; j <= redPolinoma; j++) {
				koeficijenti[i][j] = generator.nextDouble() * (
					gg[i] - dg[i]
				) + dg[i];
			}
		}
		azurirajFaktorDobrote();
	}

	@Override
	public void postaviVrijednost(double[][] vrijednost) {
		//Nije najsigurnije, ali dobro
		this.koeficijenti = vrijednost;
		azurirajFaktorDobrote();
	}

	@Override
	public KoeficijentiVektor kopiraj() {
		KoeficijentiVektor novi = new KoeficijentiVektor(brojVarijabli, redPolinoma, krajolik);
		
		double[][] noviKoeficijenti = Arrays.copyOf(koeficijenti, koeficijenti.length);
		
		for (int i = 0; i < noviKoeficijenti.length; i++) {
			noviKoeficijenti[i] = Arrays.copyOf(koeficijenti[i], koeficijenti[i].length);
		}
		
		novi.koeficijenti = noviKoeficijenti;
		novi.faktorDobrote = faktorDobrote;
		
		return novi;
	}
	
	@Override
	public String toString() {
		StringBuilder graditelj = new StringBuilder(100);
		
		graditelj.append("Polinom [\n");
		
		for (int i = 0; i < brojVarijabli; i++) {
			graditelj.append("\tvarijabla ");
			graditelj.append(i + 1);
			graditelj.append(" [ ");
			for (int j = 0; j <= redPolinoma; j++) {
				graditelj.append(format.format(koeficijenti[i][j]));
				graditelj.append(" ");
			}
			graditelj.append("]\n");
		}
		
		graditelj.append("]");
		
		return graditelj.toString();
	}

}
