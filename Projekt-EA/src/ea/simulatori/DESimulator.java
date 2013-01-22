/**
 * 
 */
package ea.simulatori;

import java.util.ArrayList;
import java.util.List;

import de.congrace.exp4j.Calculable;

import ea.Globalno;
import ea.de.BinomniMutator;
import ea.de.DEPopulacija;
import ea.de.Inicijalizator;
import ea.de.KoeficijentiVektor;
import ea.de.Mutator;
import ea.de.NajboljiSelektor;
import ea.de.Populacija;
import ea.de.RandomSelektor;
import ea.de.Selektor;
import ea.de.UniformniMutator;
import ea.de.Vektor;
import ea.de.gui.DEGUI;
import ea.util.FunkcijaKrajolik;
import ea.util.PolinomKrajolik;
import ea.util.RandomGenerator;
import ea.util.XKorakaKriterijKraja;

/**
 * @author Zlikavac32
 *
 */
public class DESimulator extends Simulator<Vektor<double[][], PolinomKrajolik>> {
	
	public static final int NAJBOLJI_SELEKTOR = 0;

	public static final int RANDOM_SELEKTOR = 1;

	public static final int UNOFIRMNA_MUTACIJA = 0;

	public static final int BINOMNA_MUTACIJA = 1;

	private int brojVektora;
	
	private int mutacija;
	
	private double vjerojatnostMutacije;

	private int selektor;
	
	private XKorakaKriterijKraja<Populacija<?, ?>> kriterijKraja;
	
	private int brojGeneracija;

	protected FunkcijaKrajolik krajolik;

	protected Populacija<double[][], PolinomKrajolik> populacija;

	private int brojParova;

	private double donjaGranicaKoeficijenata;

	private int redPolinoma;

	private int brojUzoraka;

	private double gornjaGranicaKoeficijenata;
	
	private PolinomKrajolik polinomKrajolik;

	private double faktorTezine;

	private Vektor<double[][], PolinomKrajolik> najboljaJedinka;

	public DESimulator() { 
		krajolik = new FunkcijaKrajolik();
		krajolik.postaviVarijable(new String[] { "x" });
		randomGenerator = new RandomGenerator();
	}
	
	public void koristeciSjeme(long sjeme) { randomGenerator.postaviSjeme(sjeme); }
	
	public void koristeciFunkciju(Calculable f) { 
		((FunkcijaKrajolik) krajolik).postaviFunkciju(f);
	}
	
	public void unutarGranica(double dolje, double gore) { 
		if (dolje > gore) { throw new IllegalArgumentException("Donja granica je veca od gornje");	}
		((FunkcijaKrajolik) krajolik).postaviDonjuGranicu(new double[] { dolje });
		((FunkcijaKrajolik) krajolik).postaviGornjuGranicu(new double[] { gore });
	}
	
	public void saVelicinomPopulacije(int velicina) { 
		brojVektora = velicina;
	}
	
	public void koristeciMutaciju(int mutacija) { 
		if (mutacija != UNOFIRMNA_MUTACIJA && mutacija != BINOMNA_MUTACIJA) {
			throw new IllegalArgumentException(
				"Podrzane mutacije su UNOFIRMNA_MUTACIJA i BINOMNA_MUTACIAJ"
			);
		}
		this.mutacija = mutacija;
	}
	
	public void uzVjerojatnostMutacije(double vjerojatnostMutacije) { 
		if (vjerojatnostMutacije < 0 || vjerojatnostMutacije > 1) {
			throw new IllegalArgumentException("Vjerojatnost mutacija mora biti u rasponu [0, 1]");
		}
		this.vjerojatnostMutacije = vjerojatnostMutacije;
	}
	
	public void koristeciSelektor(int selektor) { 
		if (selektor != RANDOM_SELEKTOR && selektor != NAJBOLJI_SELEKTOR) {
			throw new IllegalArgumentException("Podrzani selektoru su NAJBOLJI_SELEKTOR, RANDOM_SELEKTOR");
		}
		this.selektor = selektor;
	}
	
	public void uzBrojGeneracija(int brojGeneracija) { 
		this.brojGeneracija = brojGeneracija;
		kriterijKraja = new XKorakaKriterijKraja<Populacija<?, ?>>(brojGeneracija); 
	}

	public void saBrojemParova(int brojParova) {
		this.brojParova = brojParova;
	}

	public void koristeciDonjuGranicuKoeficijenata(double donjaGranicaKoeficijenata) {
		this.donjaGranicaKoeficijenata = donjaGranicaKoeficijenata;
	}

	public void uzRedPolinoma(int redPolinoma) {
		if (redPolinoma < 0) {
			throw new IllegalArgumentException("Red polinoma mora biti broj veci ili jedank 0");
		}
		this.redPolinoma = redPolinoma;
	}

	public void saBrojemUzoraka(int brojUzoraka) {
		if (brojUzoraka < 1) {
			throw new IllegalArgumentException("Broj uzoraka mora biti broj veci od 0");
		}
		this.brojUzoraka = brojUzoraka;
	}

	public void koristeciGornjuGranicuKoeficijenata(double gornjaGranicaKoeficijenata) {
		this.gornjaGranicaKoeficijenata = gornjaGranicaKoeficijenata;
	}

	public void uzFaktorTezine(double faktorTezine) {
		if (faktorTezine < 0 || faktorTezine > 2) {
			throw new IllegalArgumentException("Faktor tezine mora biti u rasponu [0, 2]");
		}
		this.faktorTezine = faktorTezine;
	}
	

	@Override
	protected Void doInBackground() 
		throws Exception {
		
		try { simuliraj(); }
		catch (InterruptedException e) { Thread.currentThread().interrupt(); }
		catch (Exception e) {
			gui.zapisiUZapisnikGresku(e.getMessage()); 
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void simuliraj()
		throws InterruptedException {
				
		Selektor<double[][], PolinomKrajolik> selektor = stvoriSelektor();
		Mutator<double[][]> mutator = stvoriMutator();
		
		double[][] tocke = stvoriVrijednostiVarijabli();
		
		polinomKrajolik = new PolinomKrajolik(krajolik, tocke);
		polinomKrajolik.postaviDonjuGranicu(new double[] { donjaGranicaKoeficijenata });
		polinomKrajolik.postaviGornjuGranicu(new double[] { gornjaGranicaKoeficijenata });
		
		Populacija<double[][], PolinomKrajolik> populacija = 
			new DEPopulacija<double[][], PolinomKrajolik>(brojVektora, randomGenerator, mutator, selektor);
		
		populacija.inicijaliziraj(new Inicijalizator<double[][], PolinomKrajolik>() {
			
			@Override
			public List<Vektor<double[][], PolinomKrajolik>> inicijaliziraj(
				int velicina, RandomGenerator generator
			) {
				List<Vektor<double[][], PolinomKrajolik>> vrati = 
					new ArrayList<Vektor<double[][],PolinomKrajolik>>(velicina);
				
				for (int i = 0; i < velicina; i++) {
					KoeficijentiVektor vektor = new KoeficijentiVektor(1, redPolinoma, polinomKrajolik);
					vektor.inicijaliziraj(generator);
					vrati.add(vektor);
				}
				
				return vrati;
			}
		});
		
		((DEGUI) gui).nacrtajFunkciju(krajolik);
		//((DEGUI) gui).iscrtajTocke(tocke, krajolik);
		
		while (!kriterijKraja.jeKraj(populacija) && !Globalno.jeZaustavljen()) {
			if (Globalno.vratiBrzinu() > 0) { Thread.sleep(Globalno.vratiBrzinu()); }
			populacija.evoluiraj();
			publish(populacija.vratiLokalnoNajbolje());
			najboljaJedinka = populacija.vratiGlobalnoNajbolje();
		}
		
		ispisiRjesenje();
		
	}

	@Override
	protected void process(
			List<Vektor<double[][], PolinomKrajolik>> vektori
	) {
		((DEGUI) gui).iscrtajAproksimaciju(vektori.get(vektori.size() - 1));
		setProgress((int) ((((XKorakaKriterijKraja<?>) kriterijKraja).vratiBrojProteklihGeneracija() / (double) brojGeneracija ) * 100));
    }
	
	private Mutator<double[][]> stvoriMutator() {
		switch (mutacija) {
			case BINOMNA_MUTACIJA :
				return new BinomniMutator(vjerojatnostMutacije, randomGenerator);
			case UNOFIRMNA_MUTACIJA :
				return new UniformniMutator(vjerojatnostMutacije, randomGenerator);
		}
		return null;
	}

	private Selektor<double[][], PolinomKrajolik> stvoriSelektor() {
		switch (selektor) {
			case NAJBOLJI_SELEKTOR :
				return new NajboljiSelektor<PolinomKrajolik>(faktorTezine, brojParova);
			case RANDOM_SELEKTOR :
				return new RandomSelektor<PolinomKrajolik>(faktorTezine	, brojParova);
		}
		return null;
	}

	private double[][] stvoriVrijednostiVarijabli() {
		double[][] vrijednost = new double[brojUzoraka + 1][1];
		
		double dolje = krajolik.vratiDonjuGranicu()[0];
		double gore = krajolik.vratiGornjuGranicu()[0];
		
		double odmak = (gore - dolje) / brojUzoraka;
		
		for (int i = 0; i <= brojUzoraka; i++) {
			vrijednost[i][0] = dolje;
			dolje += odmak;
		}
		
		return vrijednost;
	}

	@Override
	public void ispisiRjesenje() {
		gui.zapisiUZapisnik("Najbolje rjesenje: " + najboljaJedinka);
	}

}
