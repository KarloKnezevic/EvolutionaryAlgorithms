/**
 * 
 */
package ea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ea.gui.GUI;
import ea.de.gui.DEGUI;

/**
 * Ulaz u naš applet za diferencijsku evoluciju.
 * 
 * @author Marijan Šuflaj <msufflaj32@gmail.com>
 *
 */
public class DiferencijskaEvolucijaAlgoritam extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4803571029460862673L;
	
	/**
	 * Stvara grafičko sučelje.
	 * 
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		super.init();
		try { UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName()); } 
		catch (UnsupportedLookAndFeelException e) {
			//Ignoriraj
		}
		catch (ClassNotFoundException e) {
			//Ignoriraj
		}
		catch (InstantiationException e) {
			//Ignoriraj
		}
		catch (IllegalAccessException e) {
			//Ignoriraj
		}
		
		JButton gumb = new JButton("Pokreni");
		gumb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						GUI gui = new DEGUI("Diferencijska evolucija");
						gui.inicijaliziraj();
						gui.setVisible(true);
					}
				});
			}
		});
		add(gumb);
	}

}