/**
 * 
 */
package ea.util;

import java.util.Random;

/**
 * @author Marijan Šuflaj <msufflaj32@gmail.com>
 *
 */
public class RandomGenerator extends Random {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4279674621367886979L;
	
	/**
	 * @return Slucajna vrijednost iz intervala [0, 1)
	 */
	public double vratiDouble() { return nextDouble(); }

	/**
	 * @return Gaussova vrijednost sa sredinom 0 i devijacijom 1
	 */
	public double vratiGauss() { return nextGaussian(); }
	
	public void postaviSjeme(long sjeme) { setSeed(sjeme); }
	
	public int vratiInt() { return nextInt(); }
	
	public int vratiInt(int gornjaGranica) { return nextInt(gornjaGranica); }

}
