/**
 * 
 */
package ea.aco;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Razred koji predstavlja mrava kao jedno rješenje problema
 * TSPa.
 * @author Zlikavac32
 *
 */
public class TSPMrav extends Mrav implements Iterable<Integer> {

	protected double[][] udaljenosti;
	
	protected Grad[] gradovi;
	
	protected int[] put;
	
	protected int indeks = 0;
	
	protected double duljinaPuta = 0;
	
	/**
	 * Stvara novog mrava. ulazni parametri su dvodimenzionalna matrica
	 * sa svim udaljenostima gradova gdje vrijednost na indeksu
	 * (i, j) odgovara udaljenosti između gradova i, j. Svaki indeks
	 * odgovara jednom gradu u polju gradovi.
	 * @param udaljenosti dvodimenzionalno polje udaljenosti
	 * @param gradovi polje gradova
	 */
	public TSPMrav(double[][] udaljenosti, Grad[] gradovi) {
		this.put = new int[udaljenosti.length];
		this.udaljenosti = udaljenosti;
		this.gradovi = gradovi;
	}

	/**
	 * Dodaje grad u trenutno izgrađeni put. Indeks odgovara gradu
	 * u polju gradovi
	 * @param grad
	 */
	public void dodajGradUPut(int grad) {
		if (indeks == put.length) { throw new IndexOutOfBoundsException("Dodano previse gradova"); }
		int preth = indeks - 1;
		if (indeks > 0) { 
			duljinaPuta += udaljenosti[put[preth]][grad];
			if (indeks == put.length - 1) {
				duljinaPuta += udaljenosti[put[0]][grad];
			}
		}
		put[indeks++] = grad;
	}
	
	/**
	 * Vraća indeks zadnje dodanog grada u rješenje (put)
	 * @return Indeks zadnje dodanog grada
	 */
	public int vratiPrethodni() {
		if (indeks == 0) { return -1; }
		return put[indeks - 1];
	}

	/**
	 * Vraća iterator koji prolazi kroz sve gradove u putu
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private int tren = 0;
			
			@Override
			public boolean hasNext() { return tren < indeks; }

			@Override
			public Integer next() { return put[tren++]; }

			@Override
			public void remove() {
				//Nije implementirano
			}
		};
	}

	/**
	 * Resetira rješenje trenutnog mrava kako bi mogao raditi na novom rješenju
	 */
	public void resetiraj() {
		indeks = 0;
		duljinaPuta = 0;
	}
	
	/**
	 * Vraća pronađeni put kao polje gradova. Prolaskom redom
	 * kroz polje, dobiva se obavljeni put
	 * @return polje gradova u poretku kako smo ih prolazili
	 * @throws IllegalStateException Ako put nije obavljen do kraja
	 */
	public Grad[] vratiPutanju() {
		if (indeks != gradovi.length) { throw new IllegalStateException("Indeks putanje i broj gradova nisu isti"); }
		Grad[] putanja = new Grad[gradovi.length];
		for (int i = 0; i < indeks; i++) {
			putanja[i] = gradovi[put[i]];
		}
		return putanja;		
	}

	/**
	 * Stvara kopiju trenutnog mrava
	 * @see Mrav#kopiraj()
	 */
	@Override
	public Mrav kopiraj() {
		TSPMrav novi = new TSPMrav(udaljenosti, gradovi);
		novi.put = Arrays.copyOf(put, put.length);
		novi.indeks = indeks;
		novi.duljinaPuta = duljinaPuta;
		return novi;
	}
	
	/**
	 * Vraća tekstulnu reprezentaciju rješenja mrava
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder graditelj = new StringBuilder("TSPMrav [");
		for (Grad grad : vratiPutanju()) {
			graditelj.append("\n\t");
			graditelj.append(grad);
		}
		graditelj.append("\n]");
		return graditelj.toString();
	}

	/**
	 * Vraća ukupnu udaljenost koju smo prošli do sada
	 * @return Ukupna udaljenost koju smo prošli
	 */
	public double vratiDuljinuPuta() { return duljinaPuta; }

	/**
	 * Računa faktor dobrote trenutnog rješenja. Manji
	 * put daje bolji faktor dobrote
	 * @see Mrav#racunajFaktorDobrote()
	 */
	@Override
	public double racunajFaktorDobrote() {
		if (duljinaPuta == 0) { return Double.MAX_VALUE; }
		return 1 / duljinaPuta; 
	}

}
