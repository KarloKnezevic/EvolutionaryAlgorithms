package ea.aco;

/**
 * Apstraktni razred koji predstavlja jednog mrava
 * koji predstavlja jedno rješenje
 * @author Zlikavac32
 *
 */
public abstract class Mrav implements Comparable<Mrav> {

	
	/**
	 * Računa faktor dobrote za trenutnu jedinku.
	 * 
	 * @return Faktor dobrote
	 */
	public abstract double racunajFaktorDobrote();
	
	/**
	 * Stvara kopiju trenutnog mrava
	 * @return Koprija trenutnog mrava
	 */
	public abstract Mrav kopiraj();

	/**
	 * Obavlja usporedbu mrava na temelju faktora dobrote. 
	 * Veći faktor dobrote treba biti ispred manjeg.
	 * Ako je trenutni mrav sa boljim faktorom dobrote,
	 * jednakim ili manjim od mrava koji je poslan, vraća se
	 * -1, 0 ili 1 u tom redosljedu
	 * @see Comparable#compareTo(Object) 
	 */
	@Override
	public int compareTo(Mrav strani) {
		if (strani == null) { return -1; }
		double mojFaktorDobrote = racunajFaktorDobrote();
		double straniFaktorDobrote = strani.racunajFaktorDobrote();
		if (mojFaktorDobrote > straniFaktorDobrote) { return -1; }
		else if (mojFaktorDobrote < straniFaktorDobrote) { return 1; }
		return 0;
	}
	
}
