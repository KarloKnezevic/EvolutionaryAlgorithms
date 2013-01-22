/**
 * 
 */
package ea.util;


/**
 * @author Zlikavac32
 *
 */
public class XKorakaNemaPromjeneVrijednosti<T extends Comparable<T>> implements KriterijKraja<T> {

	protected int brojKoraka;
	
	protected int brojac = 0;
	
	protected T trenutni;
	
	public XKorakaNemaPromjeneVrijednosti(int brojGeneracija) {
		if (brojGeneracija < 0) { throw new IllegalArgumentException("Broj generacija mora biti veci ili jednak 0"); }
		this.brojKoraka = brojGeneracija; 
	}
	/**
	 * @see ea.util.KriterijKraja#jeKraj(java.lang.Object)
	 */
	@Override
	public boolean jeKraj(T podatak) {
		if (trenutni == null) {
			trenutni = podatak;
			return false;
		} else if (podatak.compareTo(trenutni) == 0) { brojac++; }
		else { 
			trenutni = podatak; 
			brojac = 0;
		}
				
		return brojac == brojKoraka;
	}
	
	public void resetiraj() { 
		trenutni = null;
		brojac = 0; 
	}

}
