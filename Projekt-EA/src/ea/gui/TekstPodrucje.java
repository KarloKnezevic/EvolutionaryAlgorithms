/**
 * 
 */
package ea.gui;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TekstPodrucje extends DijeljenaPloca {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2567398625523494805L;
	
	private JTextArea poljeZaUnos = new JTextArea();
	
	public TekstPodrucje(String labela) { this(labela, "", 10, .25); }
	
	public TekstPodrucje(String labela, String pocetnaVrijednost, int brojRedova) { 
		this(labela, pocetnaVrijednost, brojRedova, .25); 
	}
	
	public TekstPodrucje(String labela, String pocetnaVrijednost, int brojRedova, double proporcije) {
		super(proporcije);

		JScrollPane gradoviScrollPane = new JScrollPane(poljeZaUnos);
		//gradoviScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel labelaKontejner = new JPanel(new BorderLayout());
		labelaKontejner.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		labelaKontejner.add(new JLabel(labela), BorderLayout.NORTH);
		
		setLeftComponent(labelaKontejner);
		
		poljeZaUnos.setRows(brojRedova);
		
//		poljeZaUnos.setWrapStyleWord(true);
//		poljeZaUnos.setLineWrap(true);
		
		poljeZaUnos.setText(pocetnaVrijednost);
		setRightComponent(gradoviScrollPane);
		komponente.add(poljeZaUnos);
	}
	
	public String vratiVrijednost() { return poljeZaUnos.getText(); }
	
}
