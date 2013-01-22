package ea.ais;

public interface Mutator<T> {

	public T mutiraj(AntiTijelo<T, ?, ?> antiTijelo);
	
	public void inicijaliziraj();
	
}
