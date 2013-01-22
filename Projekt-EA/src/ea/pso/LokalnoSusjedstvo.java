/**
 * 
 */
package ea.pso;


/**
 * @author Zlikavac32
 *
 */
public class LokalnoSusjedstvo<T> implements Susjedstvo<T> {

	protected Cestica<T> najbolje;
	
	protected Cestica<T> najgore;

	protected int udaljenost;
	
	public LokalnoSusjedstvo(int udaljenost) {
		if (udaljenost < 0) {
			throw new IllegalArgumentException("Udaljenost lokalnog susjedstva mora biti veca ili jednaka 0");
		}
		this.udaljenost = udaljenost;
	}
	
	@Override
	public void stvori(int indeksCestice, Cestica<T>[] cestice) {
		int velicina = 1 + udaljenost * 2;
		if (velicina > cestice.length) {
			throw new IllegalArgumentException("Raspon susjedstva je veci od populacije");
		}

		Cestica<T> najgore = cestice[indeksCestice];
		Cestica<T> najbolje = najgore;
		for (int i = 0; i < udaljenost; i++) {
			Cestica<T> temp = cestice[(indeksCestice - i - 1 + cestice.length) % cestice.length];
			if (temp.compareTo(najbolje) < 0) {
				najbolje = temp;
			}
			if (temp.compareTo(najgore) > 0) {
				najgore = temp;
			}
		}
		for (int i = 0; i < udaljenost; i++) {
			Cestica<T> temp = cestice[(indeksCestice + i + 1 + cestice.length) % cestice.length];
			if (temp.compareTo(najbolje) < 0) {
				najbolje = temp;
			}
			if (temp.compareTo(najgore) > 0) {
				najgore = temp;
			}
		}
		this.najbolje = najbolje.kopiraj();
		this.najgore = najgore.kopiraj();
	}

	/**
	 * @see ea.pso.Susjedstvo#vratiNajbolju()
	 */
	@Override
	public Cestica<T> vratiNajbolju() {
		return najbolje.kopiraj();
	}

	/**
	 * @see ea.pso.Susjedstvo#vratiNajgoru()
	 */
	@Override
	public Cestica<T> vratiNajgoru() {
		return najgore.kopiraj();
	}

}
