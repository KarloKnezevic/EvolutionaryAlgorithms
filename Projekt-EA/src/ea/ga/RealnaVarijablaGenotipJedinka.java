package ea.ga;

import java.util.Arrays;

import ea.util.BinarniDekoder;
import ea.util.RealniKrajolik;

public class RealnaVarijablaGenotipJedinka extends Jedinka<RealniKrajolik> {
	

	/**
	 * Oznaka mutacije okretom bita.
	 */
	public static final int OKRET_BITA_MUTACIJA = 1;
	
	/**
	 * Oznaka rekombinacije sa jednim cvorom.
	 */
	public static final int JEDAN_CVOR_REKOMBINACIJA = 1;
	
	/**
	 * Oznaka rekombinacije sa dva cvora.
	 */
	public static final int DVA_CVORA_REKOMBINACIJA = 2;
	
	protected byte[] bitovi;
	
	protected int brojBitova;
	
	protected double faktorDobrote;
	
	protected BinarniDekoder dekoder;
	
	public RealnaVarijablaGenotipJedinka(Populacija<RealniKrajolik> populacija, int brojBitova) {
		super(populacija); 
		if (brojBitova < 1 || brojBitova > 63) { 
			throw new IllegalArgumentException("Broj bitova mora biti u rasponu [0,63]");
		}
		this.brojBitova = brojBitova; 
		dekoder = new BinarniDekoder(brojBitova, populacija.vratiKrajolik());
	}
	

	protected double racunajFaktorDobrote(double vrijednost) { 
		return populacija.vratiKrajolik().racunajFaktorDobrote(new double[] { vrijednost });
	}
	
	@Override
	public double racunajFaktorDobrote() { return faktorDobrote; }

	@Override
	public RealnaVarijablaGenotipJedinka kopiraj() {
		RealnaVarijablaGenotipJedinka novaJedinka = new RealnaVarijablaGenotipJedinka(populacija, brojBitova);
		if (bitovi != null) {
			novaJedinka.bitovi = Arrays.copyOf(bitovi, bitovi.length);
		}
		novaJedinka.faktorDobrote = faktorDobrote;
		return novaJedinka;
	}

	@Override
	public void mutiraj(int mutator, double vjerojatnostMutacije) { 
		switch (mutator) {
			case OKRET_BITA_MUTACIJA:
				okretBitaMutacija(vjerojatnostMutacije);
				break;
			default:
				throw new IllegalArgumentException("Mutator " + mutator + " nije valjan za binarnu jedinku");
		}
		faktorDobrote = racunajFaktorDobrote(dekodiraj(bitovi));
	}

	@Override
	public void mutiraj(int mutator, Object delta, double vjerojatnostMutacije) {
		mutiraj(mutator, vjerojatnostMutacije);
	}
	
	private void okretBitaMutacija(double vjerojatnostMutacije) {
		if (brojBitova < 0 || brojBitova > 63) { 
			throw new IllegalArgumentException("Broj bitova mora biti u rasponu [0,63]");
		}
		for (int i = 0; i < bitovi.length; i++) {
			if (populacija.vratiGenerator().vratiDouble() > vjerojatnostMutacije) { continue; }
			bitovi[i] = (byte) (1 - bitovi[i]);
		}
	}

	@Override
	public String toString() {
		return "Binarna jedinka sa vrijednoscu [" + vratiBitove(bitovi) + "; stvarna vrijednost " + dekodiraj(bitovi) + "]";
	}

	protected String vratiBitove(byte[] bitovi) {
		StringBuilder graditelj = new StringBuilder(bitovi.length);
		for (int i = bitovi.length - 1; i >= 0; i--) { graditelj.append(bitovi[i]); }
		return graditelj.toString();
	}

	public void inicijaliziraj() { 
		bitovi = new byte[brojBitova];
		for (int i = 0; i < brojBitova; i++) {
			bitovi[i] = (byte) (populacija.vratiGenerator().vratiDouble() < .5 ? 1 : 0);
		}
		faktorDobrote = racunajFaktorDobrote(dekodiraj(bitovi));
 	}
	
	protected double dekodiraj(byte[] bitovi) {
		return dekoder.dekodiraj(bitovi)[0];
	}

	@Override
	public void rekombiniraj(int rekombinator, Jedinka<RealniKrajolik> partner) { 
		switch (rekombinator) {
			case JEDAN_CVOR_REKOMBINACIJA :
				jedanCvorRekombinacija(((RealnaVarijablaGenotipJedinka) partner).bitovi);
				break;
			case DVA_CVORA_REKOMBINACIJA :
				dvaCvoraRekombinacija(((RealnaVarijablaGenotipJedinka) partner).bitovi);
				break;
			default:
				throw new IllegalArgumentException("Rekombinator " + rekombinator + " nije valjan za binarnu jedinku");
		}
		faktorDobrote = racunajFaktorDobrote(dekodiraj(bitovi));
	}

	private void dvaCvoraRekombinacija(byte[] partner) {
		if (partner.length != brojBitova) { 
			throw new IllegalArgumentException("Broj bitova oba partnera mora biti isti");
		}
		int prva = populacija.vratiGenerator().vratiInt(brojBitova);
		int druga = populacija.vratiGenerator().vratiInt(brojBitova);
		byte tempBitovi[] = Arrays.copyOf(partner, partner.length);
		if (prva > druga) {
			int temp = prva;
			prva = druga;
			druga = temp;
		}
		for (int i = 0; i < prva; i++) { tempBitovi[i] = bitovi[i]; }
		for (int i = prva; i < druga; i++) { bitovi[i] = tempBitovi[i]; }
		for (int i = druga; i < bitovi.length; i++) { tempBitovi[i] = bitovi[i]; }
		if (racunajFaktorDobrote(dekodiraj(tempBitovi)) > racunajFaktorDobrote(dekodiraj(bitovi))) {
			bitovi = tempBitovi;
		}
	}

	private void jedanCvorRekombinacija(byte[] partner) {
		if (partner.length != brojBitova) { 
			throw new IllegalArgumentException("Broj bitova oba partnera mora biti isti");
		}
		int lokacija = populacija.vratiGenerator().vratiInt(brojBitova);
		byte tempBitovi[] = Arrays.copyOf(partner, partner.length);
		for (int i = 0; i < lokacija; i++) { tempBitovi[i] = bitovi[i]; }
		for (int i = lokacija; i < partner.length; i++) { bitovi[i] = tempBitovi[i]; }
		if (racunajFaktorDobrote(dekodiraj(tempBitovi)) > racunajFaktorDobrote(dekodiraj(bitovi))) {
			bitovi = tempBitovi;
		}
	}

	@Override
	public Double vratiVrijednost() { return dekodiraj(bitovi); }
	
	@Override
	public double racunajVrijednost() { return populacija.vratiKrajolik().racunajVrijednost(new double[] { dekodiraj(bitovi) }); }
	
}
