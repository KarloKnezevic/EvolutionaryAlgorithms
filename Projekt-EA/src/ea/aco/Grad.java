/**
 * 
 */
package ea.aco;

/**
 * @author Zlikavac32
 *
 */
public class Grad {
	
	public final double x;
	
	public final double y;
	
	public final String ime;
	
	public Grad(double x, double y, String ime) {
		this.x = x;
		this.y = y;
		this.ime = ime;
	}
	
	public double udaljenostDo(Grad grad) { return udaljenost(this, grad); }
	
	public static double udaljenost(Grad g1, Grad g2) {
		double xD = g2.x - g1.x;
		double yD = g2.y - g1.y;
		return Math.sqrt(xD * xD + yD * yD);
	}
	
	@Override
	public String toString() {
		return "Grad " + ime + " (" + x + ", " + y + ")";
	}

}
