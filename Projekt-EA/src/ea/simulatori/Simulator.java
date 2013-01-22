/**
 * 
 */
package ea.simulatori;

import javax.swing.SwingWorker;

import ea.gui.GUI;
import ea.util.RandomGenerator;


/**
 * Simulator evolucijskog algoritma.
 * 
 * @author Marijan Šuflaj <msufflaj32@gmail.com>
 *
 */
public abstract class Simulator<T> extends SwingWorker<Void, T> {
	
	protected RandomGenerator randomGenerator;
	
	protected GUI gui;
	
	public void postaviGUI(GUI gui) { this.gui = gui; }
	
	public abstract void ispisiRjesenje();
	
}
