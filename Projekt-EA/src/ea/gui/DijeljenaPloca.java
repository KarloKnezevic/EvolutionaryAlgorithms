/**
 * 
 */
package ea.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSplitPane;

public class DijeljenaPloca extends JSplitPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7551358746169957904L;
	
	protected List<Component> komponente = new ArrayList<Component>();

	public DijeljenaPloca() { this(.25); }
	
	public DijeljenaPloca(double proporcije) {
		setDividerSize(0);
		setResizeWeight(proporcije);
		setBorder(null);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Component komponenta : komponente) { komponenta.setEnabled(enabled); }
	}

}
