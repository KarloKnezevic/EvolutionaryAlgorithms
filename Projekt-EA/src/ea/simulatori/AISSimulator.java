/**
 * 
 */
package ea.simulatori;

import java.util.ArrayList;
import java.util.List;

import de.congrace.exp4j.Calculable;
import ea.Globalno;
import ea.ais.AntiTijelo;
import ea.ais.CLONALGPopulacija;
import ea.ais.DistribuiraniBitMutator;
import ea.ais.Inicijalizator;
import ea.ais.JedanBitMutator;
import ea.ais.JedanBitVarijablaMutator;
import ea.ais.Mutator;
import ea.ais.Populacija;
import ea.ais.RealnaVarijablaAntiTijelo;
import ea.ais.SAISPopulacija;
import ea.ais.gui.AISGUI;
import ea.util.FunkcijaKrajolik;
import ea.util.RandomGenerator;
import ea.util.RealniKrajolik;
import ea.util.XKorakaKriterijKraja;

/**
 * @author Zlikavac32
 *
 */
public class AISSimulator extends Simulator<List<AntiTijelo<byte[], double[], RealniKrajolik>>> {

//	private Cestica<Double[]> najbolje = null;
	
	public static final int MAKSIMUM = 1;
	
	public static final int MINIMUM = 0;

	public static final int JEDAN_BIT_MUTATOR = 0;

	public static final int JEDAN_BIT_VARIJABLA_MUTATOR = 1;

	public static final int DISTRIBUIRANI_BIT_MUTATOR = 2;

	public static final int SAIS_ALGORITAM = 0;

	public static final int CLONALG_ALGORITAM = 1;

	private XKorakaKriterijKraja<Populacija<byte[], double[], RealniKrajolik>> kriterijKraja;
	
	private FunkcijaKrajolik krajolik;

	private int brojGeneracija;

	private int brojAntiTijela;

	private AntiTijelo<byte[], double[], RealniKrajolik> najbolje;

	private int brojBitova;

	private double ro;

	private int brojKlonova;

	private int beta;

	private int brojNovih;

	private int algoritam;

	private int mutacija;

	public AISSimulator() { 
		randomGenerator = new RandomGenerator();
		krajolik = new FunkcijaKrajolik();
		krajolik.postaviVarijable(new String[] {
			"x", "y"
		});
	}

	public void koristeciSjeme(long sjeme) {
		randomGenerator.postaviSjeme(sjeme);
	}
	
	public void koristeciFunkciju(Calculable funkcija) {
		krajolik.postaviFunkciju(funkcija);
	}
	
	public void traziEkstrem(int ekstrem) {
		if (ekstrem != MAKSIMUM && ekstrem != MINIMUM) {
			throw new IllegalArgumentException("Ekstrem moze biti MAKSIMUM ili MINIMUM");
		}
		krajolik.postaviInvertiran(ekstrem == MINIMUM);
	}
	
	public void unutarGranica(double[] donjaGranica, double[] gornjaGranica) {
		krajolik.postaviDonjuGranicu(donjaGranica);
		krajolik.postaviGornjuGranicu(gornjaGranica);
	}
	
	public void saBrojemAntiTijela(int brojAntiTijela) {
		if (brojAntiTijela < 1) {
			throw new IllegalArgumentException("Broj antitijela mora biti veci ili jednak 1");
		}
		this.brojAntiTijela = brojAntiTijela;
	}

	public void uzBrojGeneracija(int brojGeneracija) { 
		this.brojGeneracija = brojGeneracija;
		kriterijKraja = new XKorakaKriterijKraja<Populacija<byte[], double[], RealniKrajolik>>(brojGeneracija); 
	}

	public void koristeciBrojBitova(int brojBitova) {
		this.brojBitova = brojBitova;
	}

	public void koristeciRo(double ro) {
		this.ro = ro;
	}

	public void uzBrojKlonova(int brojKlonova) {
		this.brojKlonova = brojKlonova;
	}

	public void uzBetu(int beta) {
		this.beta = beta;
	}

	public void uzBrojNovih(int brojNovih) {
		this.brojNovih = brojNovih;
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
		
		((AISGUI) gui).iscrtajFunkciju(krajolik);
		
		Mutator<byte[]> mutator = stvoriMutator();
		
		Populacija<byte[], double[], RealniKrajolik> populacija = stvoriPopulaciju(mutator);
		
		if (mutacija == DISTRIBUIRANI_BIT_MUTATOR) {
			((DistribuiraniBitMutator) mutator).postaviPopulaciju(populacija);
		}
		
		populacija.inicijaliziraj(new Inicijalizator<byte[], double[], RealniKrajolik>() {
			
			@Override
			public List<AntiTijelo<byte[], double[], RealniKrajolik>> inicijaliziraj(int velicina, RandomGenerator generator) {
				
				List<AntiTijelo<byte[], double[], RealniKrajolik>> vrati = new ArrayList<AntiTijelo<byte[],double[],RealniKrajolik>>(velicina);
								
				for (int i = 0; i < velicina; i++) {
					RealnaVarijablaAntiTijelo antiTijelo = new RealnaVarijablaAntiTijelo(2, brojBitova, krajolik);
					antiTijelo.inicijaliziraj(generator);
					vrati.add(antiTijelo);
				}
				
				return vrati;
			}
		});
		publish(populacija.vratiKopijuPopulacije());
		
		while (!kriterijKraja.jeKraj(null) && !Globalno.jeZaustavljen()) {
			if (Globalno.vratiBrzinu() > 0) { Thread.sleep(Globalno.vratiBrzinu()); }
			populacija.evoluiraj();
			publish(populacija.vratiKopijuPopulacije());
			najbolje = populacija.vratiGlobalnoNajbolje();
		}
		ispisiRjesenje();
	}
	
	private Mutator<byte[]> stvoriMutator() {
		switch (mutacija) {
			case JEDAN_BIT_MUTATOR :
				return new JedanBitMutator(randomGenerator);
			case JEDAN_BIT_VARIJABLA_MUTATOR :
				return new JedanBitVarijablaMutator(brojBitova, randomGenerator);
			case DISTRIBUIRANI_BIT_MUTATOR :
				return new DistribuiraniBitMutator(ro, randomGenerator);
		}
		return null;
	}

	private Populacija<byte[], double[], RealniKrajolik> stvoriPopulaciju(Mutator<byte[]> mutator) {
		switch (algoritam) {
			case SAIS_ALGORITAM :
				return new SAISPopulacija<byte[], double[], RealniKrajolik>(brojAntiTijela, brojKlonova, randomGenerator, mutator);
			case CLONALG_ALGORITAM :
				return new CLONALGPopulacija<byte[], double[], RealniKrajolik>(brojNovih, brojAntiTijela, beta, randomGenerator, mutator);
		}
		return null;
	}

	@Override
    protected void process(List<List<AntiTijelo<byte[], double[], RealniKrajolik>>> populacije) {
		List<AntiTijelo<byte[], double[], RealniKrajolik>> zadnja = populacije.get(populacije.size() - 1);
		((AISGUI) gui).iscrtajPopulaciju(zadnja);
        setProgress((int) ((((XKorakaKriterijKraja<?>) kriterijKraja).vratiBrojProteklihGeneracija() / (double) brojGeneracija ) * 100));
    }

	@Override
	public void ispisiRjesenje() {
		gui.zapisiUZapisnik("Najbolje rjesenje: " + najbolje);
	}

	public void koristeciAlgoritam(int algoritam) {
		this.algoritam = algoritam;
	}

	public void koristeciMuaciju(int mutacija) {
		this.mutacija = mutacija;
	}
	
	
}
