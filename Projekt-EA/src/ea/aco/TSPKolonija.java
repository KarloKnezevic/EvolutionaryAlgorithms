package ea.aco;

//import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ea.util.Par;
import ea.util.RandomGenerator;

/**
 * Apstraktni razred koji predstavlja jednu koloniju
 * za rješavanje TSPa
 * @author Zlikavac32
 *
 */
public abstract class TSPKolonija extends Kolonija {

	protected RandomGenerator generator;

	protected Grad[] gradovi;
	
	protected int[] indeksi;
	
	protected double[][] udaljenosti;
	
	protected double[][] tragovi;
	
	protected double[][] tragoviCache;
	
	protected double alfa;
	
	protected double konstantaIsparavanja;

	/**
	 * Stvara novu koloniju. Prvi parametar je lista gradova gdje je
	 * element liste Par u sa prvim elementom imenom grada, a drugim
	 * elementom novi razred Par sa koordinama X i Y. Drugi parametar 
	 * je broj mrava koji će tražiti rješenje. Nakon toga slijede
	 * konstanta isparavanja, generator pseudosličajih brojeva i 
	 * konstanta alfa
	 * @param gradovi
	 * @param brojMrava
	 * @param konstantaIspravanja
	 * @param generator
	 * @param alfa
	 */
	public TSPKolonija(
		List<Par<String, Par<Double, Double>>> gradovi, int brojMrava, 
		double konstantaIspravanja, RandomGenerator generator, double alfa
	) {
		this.generator = generator;
		this.gradovi = new Grad[gradovi.size()];
		this.alfa = alfa;
		Iterator<Par<String, Par<Double, Double>>> iterator = gradovi.iterator();
		for (int i = 0; iterator.hasNext(); i++) { 
			Par<String, Par<Double, Double>> gradOpis = iterator.next();
			this.gradovi[i] = new Grad(gradOpis.drugi.prvi, gradOpis.drugi.drugi, gradOpis.prvi);
		}
		mravi = new TSPMrav[brojMrava];
		this.konstantaIsparavanja = 1 - konstantaIspravanja;
	}
	
	/**
	 * Inicijalizira trenutnu populaciju
	 * @see Kolonija#inicijaliziraj()
	 */
	@Override
	public void inicijaliziraj() {
		indeksi = new int[gradovi.length];
		tragoviCache = new double[gradovi.length][gradovi.length];
		for (int i = 0; i < indeksi.length; i++) { indeksi[i] = i; }
		udaljenosti = new double[gradovi.length][gradovi.length];
		tragovi = new double[gradovi.length][gradovi.length];
		double pocetniTrag = 1e-2;
		for (int i = 0; i < udaljenosti.length; i++) {
			udaljenosti[i][i] = 0;
			tragovi[i][i] = 0;
			for (int j = i + 1; j < udaljenosti.length; j++) {
				udaljenosti[i][j] = gradovi[i].udaljenostDo(gradovi[j]);
				udaljenosti[j][i] = udaljenosti[i][j];
				//System.out.println("Udaljenost od " + gradovi[i] + " do " + gradovi[j] + " je " + udaljenosti[i][j]);
				tragovi[j][i] = pocetniTrag;
				tragovi[i][j] = tragovi[j][i];
			}
		}
		for (int i = 0; i < mravi.length; i++) { mravi[i] = new TSPMrav(udaljenosti, gradovi); }
	}
	
	/**
	 * Vraća sve gradove koje možemo proći
	 * @return Polje svih gradova
	 */
	public Grad[] vratiGradove() { return gradovi; }
	
	/**
	 * Obavlja šetnje svih mrava u našoj populaciji
	 * @see Kolonija#obaviSetnje()
	 */
	@Override
	public void obaviSetnje() {
		for (int i = 0; i < mravi.length; i++) { obaviSetnju((TSPMrav) mravi[i]); }
	}
	
	/**
	 * Apstraktna metoda koja će oviso u implementaciji
	 * obaviti šetnju samo jednog mrava
	 * @param mrav 
	 */
	protected abstract void obaviSetnju(TSPMrav mrav);
	
	/**
	 * Računa sve tragove i sprema ih u privremenu memoriju
	 * budući da se tokom šetnji ne mjenjaju, već se mjenjaju
	 * prije novog ciklusa šetnji
	 */
	protected void azurirajTragoviCache() {
		for (int i = 0; i < udaljenosti.length; i++) {
			tragoviCache[i][i] = 0;
			for (int j = i + 1; j < udaljenosti.length; j++) {
				tragoviCache[j][i] = Math.pow(tragovi[i][j], alfa);
				tragoviCache[i][j] = tragoviCache[j][i];
			}
		}
	}
	
	/**
	 * Obavlja isparavnje tragova
	 * @see Kolonija#obaviIsparavnje()
	 */
	@Override
	public void obaviIsparavnje() {
		for (int i = 0; i < tragovi.length; i++) {
			for (int j = i; j < tragovi.length; j++) {
				tragovi[j][i] *= konstantaIsparavanja;
				tragovi[i][j] = tragovi[j][i];
			}
		}
//		System.out.println("-----------------------------------");
//		for (int i = 0; i < udaljenosti.length; i++) {
//			for (int j = 0; j < udaljenosti.length; j++) {
//				System.out.print((new DecimalFormat("0.00000").format(tragovi[i][j])) + " ");
//			}
//			System.out.println();
//		}
	}

	/**
	 * Ažurira tragove u ovisnosti o tome koliko je najboljih mrava
	 * odabrano za ažuriranje. Tragovi se ažuriraju na putevima
	 * kojima je mrav prošao
	 */
	@Override
	public void azurirajTragove(int brojNajboljihMrava) {
		Arrays.sort(mravi);
		brojNajboljihMrava = Math.min(mravi.length, brojNajboljihMrava);
		for (int i = 0; i < brojNajboljihMrava; i++) {
			TSPMrav mrav = (TSPMrav) mravi[i];
			Iterator<Integer> putanja = mrav.iterator();
			if (!putanja.hasNext()) { continue; }
			Integer pocetak = putanja.next();
			double nazivnik = mrav.duljinaPuta;
			if (Double.compare(mrav.duljinaPuta, 0) == 0) {
				nazivnik = 1e-20;
			}
			double delta = 1 / nazivnik;
			while (putanja.hasNext()) {
				Integer kraj = putanja.next();
				tragovi[kraj][pocetak] += delta;
				tragovi[pocetak][kraj] = tragovi[kraj][pocetak];
				pocetak = kraj;
			}
		}
	}

}
