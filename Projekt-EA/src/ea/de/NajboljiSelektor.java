package ea.de;

import java.util.List;

import ea.util.Krajolik;
import ea.util.Par;
import ea.util.RandomGenerator;

public class NajboljiSelektor<T extends Krajolik<double[][]>> implements Selektor<double[][], T> {
	
	protected double faktorTezine;
	
	protected int brojParova;
	
	public NajboljiSelektor(double faktorTezine, int brojParova) {
		this.faktorTezine = faktorTezine;
		this.brojParova = brojParova;
	}

	@Override
	public Par<Vektor<double[][], T>, double[][]> selektiraj(
		int indeks, Populacija<double[][], T> populacija, RandomGenerator generator
	) {
		List<Vektor<double[][], T>> vektori = populacija.vratiPopulaciju();
		
		int kraj = vektori.size();
		int[] listaIndeksa = new int[kraj];
		
		for (int i = 0; i < kraj; i++) {
			listaIndeksa[i] = i;
		}
		
		kraj--;
		
		double[][] tempVektor = populacija.vratiPopulaciju().get(0).vratiVrijednost();

		int tempKraj = brojParova * 2;
		
		int[] indeksi = new int[tempKraj];
		
		for (int i = 0; i < tempKraj; i++) {
			int r = generator.vratiInt(kraj);
			indeksi[i] = listaIndeksa[r];
			int temp = listaIndeksa[kraj - 1];
			listaIndeksa[kraj - 1] = listaIndeksa[r];
			listaIndeksa[r] = temp;
			kraj--;
		}
		
		double[][] donor = new double[tempVektor.length][tempVektor[0].length];
		
		for (int k = 0; k < brojParova; k++) {
			double[][] prvi = populacija.vratiPopulaciju().get(indeksi[2 * k]).vratiVrijednost();
			double[][] drugi = populacija.vratiPopulaciju().get(indeksi[2 * k + 1]).vratiVrijednost();
			
			for (int i = 0; i < donor.length; i++) {
				for (int j = 0; j < donor[0].length; j++) {
					donor[i][j] = faktorTezine * (prvi[i][j] - drugi[i][j]);
				}
			}
		}
		
		return new Par<Vektor<double[][], T>, double[][]>(
			populacija.vratiLokalnoNajbolje().kopiraj(),
			donor
		);
		
	}

}
