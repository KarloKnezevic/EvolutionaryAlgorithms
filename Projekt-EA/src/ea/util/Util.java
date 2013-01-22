/**
 * 
 */
package ea.util;

/**
 * @author Zlikavac32
 *
 */
public class Util {

	public static void izmjesaj(int[] polje, RandomGenerator generator) {
		for (int i = 0; i < polje.length; i++) {
			int prvi = generator.vratiInt(polje.length);
			int drugi = generator.vratiInt(polje.length);
			int temp = polje[prvi];
			polje[prvi] = polje[drugi];
			polje[drugi] = temp;
		}
	}
	
}
