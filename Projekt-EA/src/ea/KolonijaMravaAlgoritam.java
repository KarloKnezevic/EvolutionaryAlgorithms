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
import ea.aco.gui.ACOGUI;

/**
 * Ulaz u naš applet za koloniju mrava.
 * 
 * @author Marijan Šuflaj <msufflaj32@gmail.com>
 *
 */
public class KolonijaMravaAlgoritam extends JApplet {

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
						GUI gui = new ACOGUI("Optimizacija kolonijom mrava");
						gui.inicijaliziraj();
						gui.setVisible(true);
					}
				});
			}
		});
		add(gumb);
	}

}