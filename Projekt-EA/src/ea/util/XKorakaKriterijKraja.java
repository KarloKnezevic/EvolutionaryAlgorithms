/**
 * 
 */
package ea.util;


/**
 * @author Zlikavac32
 *
 */
public class XKorakaKriterijKraja<T> implements KriterijKraja<T> {

	protected int brojKoraka;
	
	protected int brojac = 0;
	
	public XKorakaKriterijKraja(int brojGeneracija) {
		if (brojGeneracija < 0) { throw new IllegalArgumentException("Broj generacija mora biti veci ili jednak 0"); }
		this.brojKoraka = brojGeneracija; 
	}

	public synchronized boolean jeKraj(T populacija) { return (brojac++) == brojKoraka; }
	
	public synchronized int vratiBrojProteklihGeneracija() { return brojac == 0 ? 0 : brojac - 1; }

	@Override
	public void resetiraj() {
		brojac = 0;
	}

}
