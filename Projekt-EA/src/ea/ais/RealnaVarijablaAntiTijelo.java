package ea.ais;

import java.text.DecimalFormat;
import java.util.Arrays;

import ea.util.BinarniDekoder;
import ea.util.RandomGenerator;
import ea.util.RealniKrajolik;

public class RealnaVarijablaAntiTijelo extends AntiTijelo<byte[], double[], RealniKrajolik>{

	protected int brojBitova;
	
	protected double faktorDobrote;
	
	protected double[] vrijednost;
	
	protected int brojVarijabli;
	
	protected byte[] bitovi;
	
	protected BinarniDekoder dekoder;
	
	protected static DecimalFormat format = new DecimalFormat("0.000000");
	
	public RealnaVarijablaAntiTijelo(int brojVarijabli, int brojBitova, RealniKrajolik krajolik) {
		super(krajolik);
		this.brojBitova = brojBitova;
		this.brojVarijabli = brojVarijabli;
		this.bitovi = new byte[brojVarijabli * brojBitova];
		dekoder = new BinarniDekoder(brojBitova, krajolik);
	}
	
	protected void azuriraj() {
		vrijednost = dekodiraj(bitovi);
		faktorDobrote = krajolik.racunajFaktorDobrote(vrijednost);
	}
	
	protected double[] dekodiraj(byte[] bitovi) {
		return dekoder.dekodiraj(bitovi);
	}

	@Override
	public double racunajFaktorDobrote() {
		return faktorDobrote;
	}

	@Override
	public double[] vratiVrijednost() {
		return Arrays.copyOf(vrijednost, vrijednost.length);
	}

	@Override
	public RealnaVarijablaAntiTijelo kopiraj() {
		RealnaVarijablaAntiTijelo vrati = new RealnaVarijablaAntiTijelo(brojVarijabli, brojBitova, krajolik);
		vrati.faktorDobrote = faktorDobrote;
		vrati.dekoder = dekoder;
		vrati.bitovi = Arrays.copyOf(bitovi, bitovi.length);
		vrati.vrijednost = vratiVrijednost();
		vrati.starost = starost;
		vrati.pocetnaStarost = pocetnaStarost;
		
		return vrati;
	}

	@Override
	public void inicijaliziraj(RandomGenerator generator) {
		for (int i = 0; i < brojVarijabli; i++) {
			for (int j = 0; j < brojBitova; j++) {
				bitovi[i * brojBitova + j] = (byte) ((generator.vratiDouble() < 0.5) ? 0 : 1);
			}
		}
		azuriraj();
	}

	@Override
	public void mutiraj(Mutator<byte[]> mutator) {
		if (mutator == null) { return; }
		byte[] mutirano = mutator.mutiraj(this);
		if (mutirano.length != bitovi.length) {
			return ;
		}
		bitovi = mutirano;
		azuriraj();
	}

	@Override
	public String toString() {
		StringBuilder graditelj = new StringBuilder(1000);
		
		graditelj.append("RealnaVarijablaAntiTijelo [\n");
		
		for (int i = 0; i < brojVarijabli; i++) {
			graditelj.append("\t")
				.append(i).append(" = ");
			for (int j = 0; j < brojBitova; j++) {
				graditelj.append(bitovi[i * brojBitova + j]);
			}
			graditelj.append("\n");
		}
		
		graditelj.append("]\nf(");
		
		for (int i = 0; i < brojVarijabli; i++) {
			graditelj.append(format.format(vrijednost[i]));
			if (i +1 < brojVarijabli) {
				graditelj.append(", ");
			}
		}
		
		graditelj.append(") = ");
		graditelj.append(format.format(krajolik.racunajVrijednost(vrijednost)));
		
		return graditelj.toString();
	}

	@Override
	public byte[] vratiReprezentaciju() {
		return Arrays.copyOf(bitovi, bitovi.length);
	}
}
