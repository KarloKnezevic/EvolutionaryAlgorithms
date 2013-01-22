/**
 * 
 */
package ea.pso;

import ea.util.RandomGenerator;
import ea.util.RealniKrajolik;

/**
 * @author Zlikavac32
 *
 */
public class StandradniRealnaVarijablaRoj extends Roj<double[]> {

	private int brojVarijabli;

	public StandradniRealnaVarijablaRoj(
		int brojCestica, RandomGenerator generator, int brojVarijabli, RealniKrajolik krajolik, SusjedstvoGraditelj<double[]> susjedstvoGraditelj,
		BrzinaKalkulator<double[]> brzinaKalkulator 
	) {
		super(brojCestica, generator, krajolik, susjedstvoGraditelj, brzinaKalkulator);
		this.brojVarijabli = brojVarijabli;
	}

	/**
	 * @see ea.pso.Roj#inicijaliziraj()
	 */
	@Override
	public void inicijaliziraj() {
		cestice = new RealnaVarijablaCestica[brojCestica];
		for (int i = 0; i < cestice.length; i++) { 
			cestice[i] = new RealnaVarijablaCestica(brojVarijabli, (RealniKrajolik) krajolik, susjedstvoGraditelj.stvoriSusjedstvo(), brzinaKalkulator);
			cestice[i].inicijaliziraj(generator);
		}
		obnoviSusjedstvo();
	}

	private void obnoviSusjedstvo() {
		for (int i = 0; i < cestice.length; i++) {
			Susjedstvo<double[]> susjedstvo = cestice[i].vratiSusjedstvo();
			susjedstvo.stvori(i, cestice);
		}
	}

	/**
	 * @see ea.pso.Roj#evoluiraj()
	 */
	@Override
	public void evoluiraj() {
		for (int i = 0; i < cestice.length; i++) {
			cestice[i].evoluiraj(generator);
		}
		obnoviSusjedstvo();
		obnoviGlobalnoNajbolje();
		brzinaKalkulator.zavrsiKrug();
	}

}
