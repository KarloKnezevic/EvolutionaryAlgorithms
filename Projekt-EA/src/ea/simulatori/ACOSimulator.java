/**
 * 
 */
package ea.simulatori;

import java.util.List;

import ea.Globalno;
import ea.aco.AntSystemTSPKolonija;
//import ea.aco.MaxMinAntSystemTSPKolonija;
import ea.aco.SimpleACOTSPKolonija;
import ea.aco.TSPKolonija;
import ea.aco.gui.ACOGUI;
import ea.util.Par;
import ea.util.RandomGenerator;
import ea.util.XKorakaKriterijKraja;
import ea.aco.TSPMrav;

/**
 * @author Zlikavac32
 *
 */
public class ACOSimulator extends Simulator<Par<TSPMrav, TSPMrav>> {

	public static final int SIMPLE_ACO_ALGORITAM = 0;
	
	public static final int ANT_SYSTEM_ALGORITAM = 1;

	public static final int MAX_MIN_ANT_SYSTEM_ALGORITM = 2;
	
	private XKorakaKriterijKraja<TSPKolonija> kriterijKraja;
	
	private int brojGeneracija = 1;

	private List<Par<String, Par<Double, Double>>> gradoviLista;

	private int brojMrava;

	private double konstantaIsparavanja;

	private double beta;

	private double alfa;

	private int algoritam;

	private int brojMravaAzurira;

	//private int brojKoraka;

	private TSPMrav najbolje = null;

	//private double a;
	
	public ACOSimulator() { 
		randomGenerator = new RandomGenerator();
	}
	
	public void koristeciSjeme(long sjeme) { randomGenerator.postaviSjeme(sjeme); }

	public void koristeciBrojMrava(int brojMrava) {
		if (brojMrava < 1) { throw new IllegalArgumentException("Broj mrava mora biti veci od 0"); }
		this.brojMrava = brojMrava;
	}

	public void koristeciBrojMravaZaAzuriranje(int brojMravaAzurira) {
		if (brojMravaAzurira < 1) { 
			throw new IllegalArgumentException("Broj mrava za azuriranje mora biti veci od 0"); 
		}
		this.brojMravaAzurira = brojMravaAzurira;
	}

	public void uzBrojGeneracija(int brojGeneracija) { 
		this.brojGeneracija = brojGeneracija;
		kriterijKraja = new XKorakaKriterijKraja<TSPKolonija>(brojGeneracija); 
	}

	public void koristeciGradove(
			List<Par<String, Par<Double, Double>>> gradoviLista) {
		this.gradoviLista = gradoviLista;
	}

	public void koristeciKonstantuIsparavanja(double konstantaIsparavanja) {
		this.konstantaIsparavanja = konstantaIsparavanja;
	}

	public void koristeciBeta(double beta) {
		this.beta = beta;
	}

	public void koristeciAlfa(double alfa) {
		this.alfa = alfa;
	}
	
	public void koristeciAlgoritam(int algoritam) {
		if (algoritam != SIMPLE_ACO_ALGORITAM && algoritam != ANT_SYSTEM_ALGORITAM && algoritam != MAX_MIN_ANT_SYSTEM_ALGORITM) {
			throw new IllegalArgumentException("Podrzani algoritmi su SIMPLE_ACO_ALGORITAM, MAX_MIN_ANT_SYSTEM_ALGORITM i ANT_SYSTEM_ALGORITAM");
		}
		this.algoritam = algoritam;
	}
	
	//public void koristecKonstantuA(double a) {
	//	this.a = a;
	//}

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
		
		TSPKolonija kolonija = null;
		if (algoritam == ANT_SYSTEM_ALGORITAM) {
			kolonija = new AntSystemTSPKolonija(gradoviLista, brojMrava, konstantaIsparavanja, randomGenerator, alfa, beta);
		} else if (algoritam == SIMPLE_ACO_ALGORITAM) {
			kolonija = new SimpleACOTSPKolonija(gradoviLista, brojMrava, konstantaIsparavanja, randomGenerator, alfa);
		}// else {
		//	kolonija = new MaxMinAntSystemTSPKolonija(gradoviLista, brojMrava, konstantaIsparavanja, randomGenerator, alfa, brojKoraka, a);
		//}
		
		kolonija.inicijaliziraj();
		((ACOGUI) gui).iscrtajGradove(kolonija.vratiGradove());
		kriterijKraja = new XKorakaKriterijKraja<TSPKolonija>(brojGeneracija);
		while (!kriterijKraja.jeKraj(kolonija) && !Globalno.jeZaustavljen()) {
			if (Globalno.vratiBrzinu() > 0) { Thread.sleep(Globalno.vratiBrzinu()); }
			kolonija.evoluiraj(brojMravaAzurira);
			TSPMrav moguceNajbolje = (TSPMrav) kolonija.vratiLokalnoNajbolje();
			najbolje = (TSPMrav) kolonija.vratiGlobalnoNajbolje();
			publish(new Par<TSPMrav, TSPMrav>(najbolje, moguceNajbolje));	
		}
		ispisiRjesenje();
	}
	
	@Override
    protected void process(List<Par<TSPMrav, TSPMrav>> populacije) {
		Par<TSPMrav, TSPMrav> zadnji = populacije.get(populacije.size() - 1);
		((ACOGUI) gui).iscrtajPutanju(zadnji.prvi.vratiPutanju(), zadnji.drugi.vratiPutanju());
        setProgress((int) ((((XKorakaKriterijKraja<TSPKolonija>) kriterijKraja).vratiBrojProteklihGeneracija() / (double) brojGeneracija) * 100));
    }

	//public void uzBrojKoraka(int brojKoraka) {
	//	this.brojKoraka = brojKoraka;
	//}

	@Override
	public void ispisiRjesenje() {
		gui.zapisiUZapisnik("Najbolje rjesenje: " + najbolje);
		if (najbolje != null) { gui.zapisiUZapisnik("Ukupne udaljenosti: " + najbolje.vratiDuljinuPuta()); }
	}
	
}
