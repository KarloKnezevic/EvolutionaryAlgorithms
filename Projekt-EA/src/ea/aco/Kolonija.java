package ea.aco;

/**
 * Apstraktni razred koji predstavlja jednu koloniju
 * @author Zlikavac32
 *
 */
public abstract class Kolonija {
	
	protected Mrav[] mravi;
	
	protected Mrav najbolje;
	
	/**
	 * Inicijalizira koloniju
	 */
	public abstract void inicijaliziraj();
	
	/**
	 * Obavlja šetnje mrava u koloniji ovisno o impelementaciji
	 */
	public abstract void obaviSetnje();
	
	/**
	 * Ažurira tragove mrava ovisno o implemntaciji
	 */
	public abstract void azurirajTragove();
	
	/**
	 * Ažurira tragove broja najboljih mrava ovisno o implementaciji
	 * @param brojNajboljihMrava
	 */
	public abstract void azurirajTragove(int brojNajboljihMrava);
	
	/**
	 * Obavlja ispravanje tragova ovisno o implementaciji
	 */
	public abstract void obaviIsparavnje();
	
	/**
	 * Vraća najgoreg mrava (mrav sa najgorim rješenjem) u trenutnom ciklusu evolucije
	 * @return Najgori mrav
	 */
	public Mrav vratiLokalnoNajgore() {
		if (mravi == null || mravi.length < 1) { return null; }
		Mrav najgore = mravi[0];
		for (int i = 1; i < mravi.length; i++) {
			if (mravi[i].compareTo(najgore) > 0) { najgore = mravi[i]; }
		}
		return najgore.kopiraj();
	}

	/**
	 * Vraća mrava sa najboljim rješenjem u trenutnom ciklusu evolucije
	 * @return Najbolji mrav
	 */
	public Mrav vratiLokalnoNajbolje() {
		if (mravi == null || mravi.length < 1) { return null; }
		Mrav najbolje = mravi[0];
		for (int i = 1; i < mravi.length; i++) {
			if (mravi[i].compareTo(najbolje) < 0) { najbolje = mravi[i]; }
		}
		return najbolje.kopiraj();
	}
	
	/**
	 * Obnavlja najbolje globlano rješenje
	 */
	protected void obnoviGlobalnoNajbolje() {
		Mrav moguceNajbolje = vratiLokalnoNajbolje();
		if (najbolje == null || moguceNajbolje.compareTo(najbolje) < 0) { najbolje = moguceNajbolje; }
	}
	
	/**
	 * Vraća mrava sa najboljim rješenjem od početka evolucije
	 * pa do trentnog ciklusa pod uvjetom
	 * da je nakon svakog ciklusa pozvana metoda {@link Kolonija#obnoviGlobalnoNajbolje()}
	 * @return Najbolji mrav na globlanoj razini
	 */
	public Mrav vratiGlobalnoNajbolje() { return najbolje.kopiraj(); }

	/**
	 * Evoluira trenutnu populaciju ovisno o implementaciji
	 * @param brojMravaAzurira
	 */
	public abstract void evoluiraj(int brojMravaAzurira);

	/**
	 * Evoluira trenutnu populaciju ovisno o implementaciji
	 */
	public abstract void evoluiraj();
}
