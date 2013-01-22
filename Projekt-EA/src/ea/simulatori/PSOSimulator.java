/**
 * 
 */
package ea.simulatori;

import java.util.List;

import de.congrace.exp4j.Calculable;
import ea.Globalno;
import ea.pso.BrzinaKalkulator;
import ea.pso.Cestica;
import ea.pso.DinamickaInercijaBrzinaKalkulator;
import ea.pso.DinamickaOgranicavajucaInercijaBrzinaKalkulator;
import ea.pso.GlobalnoSusjedstvo;
import ea.pso.KonstantnaInercijaBrzinaKalkulator;
import ea.pso.LokalnoSusjedstvo;
import ea.pso.OgranicavajucaBrzinaKalkulator;
import ea.pso.Roj;
import ea.pso.StandardniBrzinaKalkulator;
import ea.pso.StandradniRealnaVarijablaRoj;
import ea.pso.Susjedstvo;
import ea.pso.SusjedstvoGraditelj;
import ea.util.FunkcijaKrajolik;
import ea.util.RandomGenerator;
import ea.util.XKorakaKriterijKraja;

import ea.pso.gui.PSOGUI;


/**
 * @author Zlikavac32
 *
 */
public class PSOSimulator extends Simulator<Cestica<double[]>[]> {

//	private Cestica<Double[]> najbolje = null;
	
	public static final int MAKSIMUM = 1;
	
	public static final int MINIMUM = 0;

	public static final int STANDARDNI_BRZINA_KALKULATOR = 0;

	public static final int INERCIJA_BRZINA_KALKULATOR = 1;

	public static final int DINAMICKA_INERCIJA_BRZINA_KALKUALTOR = 2;

	public static final int DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR = 3;

	public static final int OGRANICAVAJUCA_BRZINA_KALKULATOR = 4;

	public static final int GLOBALNO_SUSJEDSTVO = 0;

	public static final int LOKALNO_SUSJEDSTVO = 1;

	private XKorakaKriterijKraja<Roj<?>> kriterijKraja;
	
	private FunkcijaKrajolik krajolik;

	private int brojGeneracija;

	private int brojCestica;

	private double inercija;

	private double faktorSmanjenjaInercije;

	private double[] graniceInercije;

	private int brojKorakaInercije;

	private int brojOkolnihCestica;

	private double[] donjaGranicaBrzine;

	private double[] gornjaGranicaBrzine;

	private int susjedstvo;

	private int brzinaKalkulator;

	private Cestica<double[]> najbolje;

	private double c1;

	private double c2;
	
	public PSOSimulator() { 
		randomGenerator = new RandomGenerator();
		krajolik = new FunkcijaKrajolik();
		krajolik.postaviVarijable(new String[] {
			"x", "y"
		});
	}



	public void koristeciC1IC2(double c1, double c2) {
		this.c1 = c1;
		this.c2 = c2;
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
	public void saBrojemCestica(int brojCestica) {
		if (brojCestica < 1) {
			throw new IllegalArgumentException("Broj cestica mora biti veci ili jednak 1");
		}
		this.brojCestica = brojCestica;
	}
	
	public void koristeciInerciju(double inercija) {
		this.inercija = inercija;
	}
	
	public void koristeciFaktorSmanjenjaInercije(double faktorSmanjenjaInercije) {
		this.faktorSmanjenjaInercije = faktorSmanjenjaInercije;
	}
	
	public void koristeciGraniceInercije(double[] graniceInercije) {
		this.graniceInercije = graniceInercije;
	}
	public void koristeciBrojKorakaInercije(int brojKorakaInercije) {
		if (brojKorakaInercije < 0) {
			throw new IllegalArgumentException("Broj koraka inercije mora biti veci ili jednak 0");
		}
		this.brojKorakaInercije = brojKorakaInercije;
	}
	public void koristeciBrojOkolnihCestica(int brojOkolnihCestica) {
		if (brojOkolnihCestica < 0) {
			throw new IllegalArgumentException("Broj okolnih cestica mora biti veci ili jednak 0");
		}
		this.brojOkolnihCestica = brojOkolnihCestica;
	}
	public void uzGraniceBrzine(double[] donjaGranicaBrzine, double[] gornjaGranicaBrzine) {
		this.donjaGranicaBrzine = donjaGranicaBrzine;
		this.gornjaGranicaBrzine = gornjaGranicaBrzine;
	}
	
	public void uzBrojGeneracija(int brojGeneracija) {
		if (brojGeneracija < 0) {
			throw new IllegalArgumentException("Broj generacija mora biti veci ili jednak 0");
		}
		this.brojGeneracija = brojGeneracija;
		
	}
	public void koristeciKalkulatorBrzine(int brzinaKalkulator) {
		if (
			brzinaKalkulator != STANDARDNI_BRZINA_KALKULATOR && brzinaKalkulator != INERCIJA_BRZINA_KALKULATOR &&
			brzinaKalkulator != DINAMICKA_INERCIJA_BRZINA_KALKUALTOR && brzinaKalkulator != DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR &&
			brzinaKalkulator != OGRANICAVAJUCA_BRZINA_KALKULATOR
		) {
			throw new IllegalArgumentException(
				"Podrzani kalkulatori brzina su STANDARDNI_BRZINA_KALKULATOR,  INERCIJA_BRZINA_KALKULATOR, DINAMICKA_INERCIJA_BRZINA_KALKUALTOR, "
				+ "DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR i OGRANICAVAJUCA_BRZINA_KALKULATOR"
			);
		}
		this.brzinaKalkulator = brzinaKalkulator;		
	}
	public void koristeciSusjedstvo(int susjedstvo) {
		if (susjedstvo != GLOBALNO_SUSJEDSTVO && susjedstvo != LOKALNO_SUSJEDSTVO) {
			throw new IllegalArgumentException("Susjedstvo mora biti GLOBALNO_SUSJEDSTVO ili LOKALNO_SUSJEDSTVO");
		}
		this.susjedstvo = susjedstvo;
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
		
		((PSOGUI) gui).iscrtajFunkciju(krajolik);
		
		Roj<double[]> roj = new StandradniRealnaVarijablaRoj(brojCestica, randomGenerator, 2, krajolik, vratiSusjedstvoGraditelj(), vratiGeneratorBrzine());
		
		roj.inicijaliziraj();
		
		kriterijKraja = new XKorakaKriterijKraja<Roj<?>>(brojGeneracija);
		while (!kriterijKraja.jeKraj(roj) && !Globalno.jeZaustavljen()) {
			if (Globalno.vratiBrzinu() > 0) { Thread.sleep(Globalno.vratiBrzinu()); }
			roj.evoluiraj();
			najbolje = roj.vratiGlobalnoNajbolje();
			publish(roj.vratiCestice());
		}
		ispisiRjesenje();
	}
	
	private SusjedstvoGraditelj<double[]> vratiSusjedstvoGraditelj() {
		switch (susjedstvo) {
			case GLOBALNO_SUSJEDSTVO :
				return new SusjedstvoGraditelj<double[]>() {

					@Override
					public Susjedstvo<double[]> stvoriSusjedstvo() {
						return new GlobalnoSusjedstvo<double[]>();
					}
					
				};
			case LOKALNO_SUSJEDSTVO :
				return new SusjedstvoGraditelj<double[]>() {

					@Override
					public Susjedstvo<double[]> stvoriSusjedstvo() {
						return new LokalnoSusjedstvo<double[]>(brojOkolnihCestica);
					}
					
				};
		}
		return null;
	}
	private BrzinaKalkulator<double[]> vratiGeneratorBrzine() {
		switch (brzinaKalkulator) {
			case STANDARDNI_BRZINA_KALKULATOR :
				return new StandardniBrzinaKalkulator(c1, c2, donjaGranicaBrzine, gornjaGranicaBrzine);
			case INERCIJA_BRZINA_KALKULATOR :
				return new KonstantnaInercijaBrzinaKalkulator(c1, c2, donjaGranicaBrzine, gornjaGranicaBrzine, inercija);
			case DINAMICKA_INERCIJA_BRZINA_KALKUALTOR :
				return new DinamickaInercijaBrzinaKalkulator(c1, c2, donjaGranicaBrzine, gornjaGranicaBrzine, inercija, faktorSmanjenjaInercije);
			case DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR :
				return new DinamickaOgranicavajucaInercijaBrzinaKalkulator(c1, c2, donjaGranicaBrzine, gornjaGranicaBrzine, graniceInercije[0], graniceInercije[1], brojKorakaInercije);
			case OGRANICAVAJUCA_BRZINA_KALKULATOR :
				return new OgranicavajucaBrzinaKalkulator(c1, c2, donjaGranicaBrzine, gornjaGranicaBrzine);
		}
		return null;
	}
	@Override
    protected void process(List<Cestica<double[]>[]> populacije) {
		Cestica<double[]>[] zadnja = populacije.get(populacije.size() - 1);
		((PSOGUI) gui).iscrtajCestice(zadnja);
        setProgress((int) ((((XKorakaKriterijKraja<?>) kriterijKraja).vratiBrojProteklihGeneracija() / (double) brojGeneracija ) * 100));
    }

	@Override
	public void ispisiRjesenje() {
		gui.zapisiUZapisnik("Najbolje rjesenje: " + najbolje);
	}
	
	
}
