/**
 * 
 */
package ea.util;

/**
 * @author Zlikavac32
 *
 */
public abstract class RealniKrajolik implements Krajolik<double[]> {

	protected double gornjaGranica[];
	
	protected double donjaGranica[];
	
	public double[] vratiGornjuGranicu() { return gornjaGranica; }
	
	public double[] vratiDonjuGranicu() { return donjaGranica; }
	
	public void postaviGornjuGranicu(double[] gornjaGranica) { this.gornjaGranica = gornjaGranica; }
	
	public void postaviDonjuGranicu(double[] donjaGranica) { this.donjaGranica = donjaGranica; }
	
	public abstract double racunajVrijednost(double[] vrijednost);
	
}
