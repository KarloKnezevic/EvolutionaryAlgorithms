package ea;

/**
 * Razred koji nam služi za glonalno postavljaje postavki.
 * Jedan problem je što to postavlja svim appletima koji su 
 * pokrenuti na isti gumb Pokreni
 * 
 * @author Zlikavac32
 *
 */
public class Globalno {

	private static volatile int brzina;
	
	private static volatile boolean zaustavljeno = true;
	
	/**
	 * Postavlja brzinu izvođenja programa u milisekundama
	 * @param brzina 
	 */
	public static void postaviBrzinu(int brzina) { Globalno.brzina = brzina; }
	
	/**
	 * Vraća trenutno postavljenu brzinu izvođenja u milisekundama
	 * @return Trenutna brzina u milisekundama
	 */
	public static int vratiBrzinu() { return brzina; }
	
	/**
	 * Postavlja zastavicu ovisno o tome da li je simulacija
	 * pokrenuta ili zaustavljenja
	 * @param zaustavljeno
	 */
	public static void postaviZaustavljeno(boolean zaustavljeno) { Globalno.zaustavljeno = zaustavljeno; }
	
	/**
	 * Vraća true ako je simulacija zaustavljena, false inače
	 * @return true ako je simulacija zaustavljena, false inače
	 */
	public static boolean jeZaustavljen() { return zaustavljeno; }
	
}
