/**
 * 
 */
package ea.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RadioGumbi extends DijeljenaPloca {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1645576708734909006L;
	
	private ButtonGroup grupaGumbica = new ButtonGroup();

	public RadioGumbi(String labela, Object[] opcije) { this(labela, opcije, -1, new ActionListener[0], .25); }

	public RadioGumbi(String labela, Object[] opcije, int selected) { 
		this(labela, opcije, selected, new ActionListener[0], .25); 
	}

	public RadioGumbi(String labela, Object[] opcije, int selected, ActionListener[] promjena) { 
		this(labela, opcije, selected, promjena, .25); 
	}
	
	public RadioGumbi(String labela, Object[] opcije, int selected, ActionListener[] promjena, double proporcije) {
		super(proporcije);
		
		JPanel labelaKontejner = new JPanel(new BorderLayout());
		labelaKontejner.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		labelaKontejner.add(new JLabel(labela), BorderLayout.NORTH);
		
		setLeftComponent(labelaKontejner);
		
		JPanel gumbiciPloca = new JPanel(new GridLayout(0, 1, 0, 3));
		
		if (promjena == null) { promjena = new ActionListener[0]; }
		
		for (int i = 0; i < opcije.length; i++) {
			JRadioButton gumbic = new JRadioButton(opcije[i].toString());
			gumbic.setActionCommand(opcije[i].toString());
			grupaGumbica.add(gumbic);
			komponente.add(gumbic);
			if (promjena.length > i) { gumbic.addActionListener(promjena[i]); }
			gumbiciPloca.add(gumbic);
			if (i == selected) { grupaGumbica.setSelected(gumbic.getModel(), true); }
		}
		
		setRightComponent(gumbiciPloca);
		
	}
	
	public ButtonModel vratiOdabrani() { return grupaGumbica.getSelection(); }	
	
}
