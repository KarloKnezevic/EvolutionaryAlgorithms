/**
 * 
 */
package ea.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

public class KvacicaGumbi extends DijeljenaPloca {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7235788694288270945L;

	public KvacicaGumbi(String labela, Object[] opcije) { this(labela, opcije, new int[0], new ItemListener[0], .25); }

	public KvacicaGumbi(String labela, Object[] opcije, int[] selected) { 
		this(labela, opcije, selected, new ItemListener[0], .25); 
	}
	
	public KvacicaGumbi(String labela, Object[] opcije, int[] selected, ItemListener[] promjena, double proporcije) {
		super(proporcije);
		
		JPanel labelaKontejner = new JPanel(new BorderLayout());
		labelaKontejner.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		labelaKontejner.add(new JLabel(labela), BorderLayout.NORTH);
		
		setLeftComponent(labelaKontejner);
		
		JPanel gumbiciPloca = new JPanel(new GridLayout(0, 1, 0, 3));
		
		if (promjena == null) { promjena = new ItemListener[0]; }
		
		for (int i = 0; i < opcije.length; i++) {
			JCheckBox gumbic = new JCheckBox(opcije[i].toString());
			komponente.add(gumbic);
			gumbiciPloca.add(gumbic);
			if (promjena.length > i) { gumbic.addItemListener(promjena[i]); }
			for (int indeks : selected) {
				if (indeks == i) {
					gumbic.setSelected(true);
					break;
				}
			}
		}
		
		setRightComponent(gumbiciPloca);
		
	}
	
	public JCheckBox[] vratiOdabrane() {
		ArrayList<JCheckBox> odabrani = new ArrayList<JCheckBox>();
		for (Component gumbic : komponente) {
			if (((JCheckBox) gumbic).isSelected()) { odabrani.add((JCheckBox) gumbic); }
		}
		return (JCheckBox[]) odabrani.toArray();
	}
	
}
