/**
 * 
 */
package ea.pso;


/**
 * @author Zlikavac32
 *
 */
public interface Susjedstvo<T> {
	
	public void stvori(int indeksCestice, Cestica<T>[] cestice);
	
	public Cestica<T> vratiNajbolju();
	
	public Cestica<T> vratiNajgoru();
	
}
