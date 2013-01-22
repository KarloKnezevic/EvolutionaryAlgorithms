/**
 * 
 */
package ea.pso;


/**
 * @author Zlikavac32
 *
 */
public class GlobalnoSusjedstvo<T> implements Susjedstvo<T> {

	protected Cestica<T> najbolje;
	
	protected Cestica<T> najgore;

	@Override
	public void stvori(int indeksCestice, Cestica<T>[] cestice) {
		if (cestice == null || cestice.length == 0) { return; }
		Cestica<T> najgore = cestice[0];
		Cestica<T> najbolje = najgore;
		for (int i = 1; i < cestice.length; i++) {
			Cestica<T> temp = cestice[i];
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
