package ea.util;

public class BinarniDekoder {

	private int brojBitovaPoVarijabli;
	
	private RealniKrajolik krajolik;

	public BinarniDekoder(int brojBitovaPoVarijabli, RealniKrajolik krajolik) {
		if (brojBitovaPoVarijabli < 1 || brojBitovaPoVarijabli > 63) {
			throw new IllegalArgumentException("Broj bitova po varijabli mora biti u rasponu [1, 63]");
		}
		this.brojBitovaPoVarijabli = brojBitovaPoVarijabli;
		this.krajolik = krajolik;
	}
	
	public double[] dekodiraj(byte[] bitovi) {
		
		if (bitovi.length % brojBitovaPoVarijabli != 0) {
			throw new IllegalArgumentException("Broj bitova mora biti djeljiv sa brojem bitova po varijabli");
		}
		
		double[] rjesenja = new double[bitovi.length / brojBitovaPoVarijabli];
		
		for (int i = 0; i < rjesenja.length; i++) {
			double vrijednost = 0.;
			long potencija = 1;
			for (int j = 0; j < brojBitovaPoVarijabli; j++) {
				if (bitovi[j + brojBitovaPoVarijabli * i] == 1) { vrijednost += potencija; }
				potencija <<= 1;
			}
			rjesenja[i] = vrijednost / ((1L << brojBitovaPoVarijabli) - 1) * (krajolik.vratiGornjuGranicu()[i] - krajolik.vratiDonjuGranicu()[i]) 
					+ krajolik.vratiDonjuGranicu()[i];
		}
		
		return rjesenja;
	}
	
}
