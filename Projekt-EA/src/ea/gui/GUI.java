package ea.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;

import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;


import ea.Globalno;
import ea.simulatori.Simulator;

import static ea.gui.GUIKonstante.*;

/**
 * @author Marijan Šuflaj <msufflaj32@gmail.com>
 *
 */
public abstract class GUI extends JFrame {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -3200550997106801733L;

	protected JProgressBar mjeracNapretka;

	protected static JTextArea zapisnikPodrucje;
	
	protected Simulator<?> simulator;

	protected class Ploca {
		
		public JPanel ploca;
		
		public Ploca() { 
			//
		}
	}
	
	protected class ZaustaviSimulaciju implements PropertyChangeListener {
		
		protected JButton gumb;
		
		public ZaustaviSimulaciju(JButton gumb) { this.gumb = gumb; }
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress".equals(evt.getPropertyName())) { 
				int vrijednost = (Integer) evt.getNewValue();
				mjeracNapretka.setValue(vrijednost);
				if (vrijednost == 100) { gumb.setText(POKRENI); }
			}
		}
	}
	
	private static class ZapisiUZapisnik implements Runnable {

		private String zapis;
		
		private ZapisiUZapisnik(String zapis) { this.zapis = zapis; }
		
		@Override
		public void run() { zapisnikPodrucje.append(zapis + "\n"); }
		
	}
	
	/**
	 * Stvara graficko sucelje i postavlja naslov.
	 * 
	 * @param title 
	 */
	
	public GUI(String title) {  super(title); }
	
	/**
	 * Dodaje komponente.
	 */
	public void inicijaliziraj() { 
		setLayout(new BorderLayout());
		setSize(new Dimension(800, 600));
		
		JSplitPane grafZapisnikDijeljeniProzorcic = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		grafZapisnikDijeljeniProzorcic.setResizeWeight(.15);
		zapisnikPodrucje = new JTextArea();
		zapisnikPodrucje.setWrapStyleWord(true);
		zapisnikPodrucje.setLineWrap(true);
		zapisnikPodrucje.setFont(new Font("Verdana", Font.PLAIN, 11));
		JScrollPane zapisnik = new JScrollPane(zapisnikPodrucje);
		
		zapisnik.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		grafZapisnikDijeljeniProzorcic.setTopComponent(stvoriGraf());
		grafZapisnikDijeljeniProzorcic.setBottomComponent(zapisnik);
		add(grafZapisnikDijeljeniProzorcic, BorderLayout.CENTER);
		
		
		JPanel kontroleDesnaStranaGlavniPanel = new JPanel(new BorderLayout());
		kontroleDesnaStranaGlavniPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		
		JButton pokreniGumb = new JButton(POKRENI);
		pokreniGumb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton gumb = (JButton) event.getSource();
				if (gumb.getText().equals(POKRENI)) { 
					zapisnikPodrucje.setText("");
					mjeracNapretka.setValue(0);
					try { 
						Globalno.postaviZaustavljeno(false);
						pokreniSimulaciju(gumb); 
					} 
					catch (Throwable e) { 
						zapisiUZapisnikGresku(e.getMessage()); 
						e.printStackTrace();
					}
				} else {
					zaustaviSimulaciju(gumb);
				}
			}
		});
		
		JPanel akcijeKontrolePloca = new JPanel(new BorderLayout());
		akcijeKontrolePloca.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
		
		JSlider brzina = new JSlider(JSlider.HORIZONTAL, 0, 1000, 850);
		Dictionary<Integer, JLabel> labele = new Hashtable<Integer, JLabel>(2);
		labele.put(0, new JLabel("Min"));
		labele.put(1000, new JLabel("Max"));
		brzina.setSnapToTicks(true);
		brzina.setPaintLabels(true);
		brzina.setLabelTable(labele);
		brzina.setToolTipText("Brzina");
		brzina.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider brzina = (JSlider) e.getSource();
				if (!brzina.getValueIsAdjusting()) {
					Globalno.postaviBrzinu(1000 - brzina.getValue());
				}
			}
		});
		
		Globalno.postaviBrzinu(150);
		
		JPanel brzinaLabelaPloca = new JPanel(new BorderLayout());
		brzinaLabelaPloca.add(new JLabel("Brzina"), BorderLayout.WEST);
		
		mjeracNapretka = new JProgressBar(0, 100);
		mjeracNapretka.setValue(0);
		mjeracNapretka.setStringPainted(true);
		
		JPanel brzinaPloca = new JPanel();
		brzinaPloca.setLayout(new BoxLayout(brzinaPloca, BoxLayout.Y_AXIS));
		brzinaPloca.add(brzinaLabelaPloca);
		brzinaPloca.add(Box.createRigidArea(new Dimension(0, 10)));
		brzinaPloca.add(brzina);
		brzinaPloca.add(Box.createRigidArea(new Dimension(0, 10)));
		brzinaPloca.add(mjeracNapretka);
		brzinaPloca.add(Box.createRigidArea(new Dimension(0, 10)));
		
		JPanel akcijeKontrolePlocaUnutarnja = new JPanel(new GridLayout(1, 0, 5, 0));
		akcijeKontrolePlocaUnutarnja.add(pokreniGumb);
		
		akcijeKontrolePloca.add(brzinaPloca, BorderLayout.NORTH);
		akcijeKontrolePloca.add(akcijeKontrolePlocaUnutarnja, BorderLayout.EAST);

		JScrollPane kontroleScrollPane = new JScrollPane(stvoriKontroleKontejner());
		kontroleScrollPane.getVerticalScrollBar().setUnitIncrement(24);
		kontroleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		kontroleScrollPane.setBorder(null);
		
		kontroleDesnaStranaGlavniPanel.add(kontroleScrollPane, BorderLayout.CENTER);
		kontroleDesnaStranaGlavniPanel.add(akcijeKontrolePloca, BorderLayout.SOUTH);
		
		add(kontroleDesnaStranaGlavniPanel, BorderLayout.EAST);	
	}
	
	protected abstract ChartPanel stvoriGraf();

	protected abstract void pokreniSimulaciju(JButton gumb) 
		throws UnknownFunctionException, UnparsableExpressionException;

	protected void zaustaviSimulaciju(JButton gumb) {
		//simulator.ispisiRjesenje();
		Globalno.postaviZaustavljeno(true);
		simulator.cancel(true);
		gumb.setText(POKRENI);
	}
	
	protected abstract JPanel stvoriKontroleKontejner();

	public void zapisiUZapisnik(String zapis) { SwingUtilities.invokeLater(new ZapisiUZapisnik(zapis)); }

	public void zapisiUZapisnikGresku(String zapis) { zapisiUZapisnik("GRESKA: " + zapis); }
	
}
