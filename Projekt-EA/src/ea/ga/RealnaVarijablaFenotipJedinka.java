package ea.ga;

import ea.util.RealniKrajolik;

public class RealnaVarijablaFenotipJedinka extends Jedinka<RealniKrajolik> {
	private double vrijednost;

	protected double faktorDobrote;
	
	public static final int DELTA_MUTACIJA = 1;
	
	public static final int GAUSS_MUTACIJA = 2;
	
	public static final int ARITMETICKA_SREDINA_REKOMBINACIJA = 1;
	
	public static final int TEZINSKA_SREDINA_REKOMBINACIJA = 2;
	
	public static final int ALFA_INTERVAL_REKOMBINACIJA = 3;

	
	public RealnaVarijablaFenotipJedinka(Populacija<RealniKrajolik> populacija) {
		super(populacija);
	}

	@Override
	public RealnaVarijablaFenotipJedinka kopiraj() {
		RealnaVarijablaFenotipJedinka novaJedinka = new RealnaVarijablaFenotipJedinka(populacija);
		novaJedinka.vrijednost = vrijednost;
		novaJedinka.faktorDobrote = faktorDobrote;
		return novaJedinka;
	}

	@Override
	public void mutiraj(int mutator, double vjerojatnostMutacije) { mutiraj(mutator, .1, vjerojatnostMutacije); }

	@Override
	public void mutiraj(int mutator, Object delta, double vjerojatnostMutacije) {
		if (delta == null) { return ; } 
		double staro = vrijednost;
		switch (mutator) {
			case DELTA_MUTACIJA:
				deltaMutacija((Double) delta, vjerojatnostMutacije);
				break;
			case GAUSS_MUTACIJA:
				gaussMutacija((Double) delta, vjerojatnostMutacije);
				break;
			default:
				throw new IllegalArgumentException("Mutator " + mutator + " nije valjan za realnu jedinku");
		}
		if (!populacija.vratiKrajolik().jeValjanaVrijednost(new double[] { vrijednost })) { vrijednost = staro; }
		faktorDobrote = racunajFaktorDobrote(vrijednost);
	}

	
	private void gaussMutacija(double delta, double vjerojatnostMutacije) {
		if (populacija.vratiGenerator().vratiDouble() > vjerojatnostMutacije) { return ; }
		vrijednost = vrijednost + populacija.vratiGenerator().vratiGauss() * delta;
	}

	private void deltaMutacija(double delta, double vjerojatnostMutacije) {
		if (populacija.vratiGenerator().vratiDouble() > vjerojatnostMutacije) { return ; }
		vrijednost = populacija.vratiGenerator().vratiDouble() * (2 * delta) + (vrijednost - delta);
	}

	@Override
	public String toString() {
		return "Realna jedinka sa vrijednoscu [" + vrijednost + "]";
	}

	public void inicijaliziraj() { 
		vrijednost = populacija.vratiKrajolik().vratiDonjuGranicu()[0] + 
			(populacija.vratiKrajolik().vratiGornjuGranicu()[0] - populacija.vratiKrajolik().vratiDonjuGranicu()[0]) * 
			populacija.vratiGenerator().vratiDouble();
		faktorDobrote = racunajFaktorDobrote(vrijednost);
	}

	protected double racunajFaktorDobrote(double vrijednost) { 
		return populacija.vratiKrajolik().racunajFaktorDobrote(new double[] { vrijednost });
	}

	public double racunajFaktorDobrote() { return faktorDobrote; }

	@Override
	public void rekombiniraj(int rekombinator, Jedinka<RealniKrajolik> partner) { 
		double staro = vrijednost;
		switch (rekombinator) {
			case ARITMETICKA_SREDINA_REKOMBINACIJA :
				aritmetickaSredinaRekombinacija(((RealnaVarijablaFenotipJedinka) partner).vrijednost);
				break;
			case TEZINSKA_SREDINA_REKOMBINACIJA :
				tezinskaSredinaRekombinacija(((RealnaVarijablaFenotipJedinka) partner).vrijednost);
				break;
			case ALFA_INTERVAL_REKOMBINACIJA :
				alfaIntervalRekombinacija(((RealnaVarijablaFenotipJedinka) partner).vrijednost);
				break;
			default:
				throw new IllegalArgumentException("Rekombinator " + rekombinator + " nije valjan za realnu jedinku");
		}
		//Provjera Double.isNan ide iz razloga sto se kod tezinkse zna dogodit da pogodi za rekombinaciju obje najgore jedinke pa kao rezultat dobijem NaN
		if (
			Double.isNaN(vrijednost) ||
			!populacija.vratiKrajolik().jeValjanaVrijednost(new double[] { vrijednost })
		) { vrijednost = staro; }
		faktorDobrote = racunajFaktorDobrote(vrijednost);
	}

	private void alfaIntervalRekombinacija(double partner) {
		final double alfa = .3;
		if (partner < vrijednost) {
			double temp = partner;
			partner = vrijednost;
			vrijednost = temp;
		}
		double intervalOdmak = (partner - vrijednost) * alfa;
//		vrijednost = Simulator.vratiRandomGenerator().vratiDouble() * (
//			vrijednost - partner - 2 * intervalOdmak 
//		);
		vrijednost = (vrijednost - intervalOdmak) + populacija.vratiGenerator().vratiDouble() 
			* (partner - vrijednost + 2 * intervalOdmak);
	}

	private void tezinskaSredinaRekombinacija(double partner) {
		double odmak = populacija.vratiLokalnoNajgore().racunajFaktorDobrote();
		double prvaTezina = racunajFaktorDobrote() - odmak;
		double drugaTezina = racunajFaktorDobrote(partner) - odmak;
		vrijednost = (vrijednost * prvaTezina + partner * drugaTezina) / (prvaTezina + drugaTezina);
	}

	private void aritmetickaSredinaRekombinacija(double partner) {
		vrijednost = (vrijednost + partner) / 2;
	}

	@Override
	public Double vratiVrijednost() { return vrijednost; }
	
	@Override
	public double racunajVrijednost() { return racunajFaktorDobrote(); }
	
}
