/**
 * 
 */
package ea.aco.gui;

import static ea.gui.GUIKonstante.ZAUSTAVI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

import ea.aco.Grad;
import ea.gui.DijeljenaPloca;
import ea.gui.GUI;
import ea.gui.RadioGumbi;
import ea.gui.TekstPodrucje;
import ea.gui.TekstualnaVrijednost;
import ea.simulatori.ACOSimulator;
import ea.util.Par;

import static ea.aco.gui.ACOGUIKonstante.*;

/**
 * @author Zlikavac32
 *
 */
public class ACOGUI extends GUI {


	private TekstPodrucje gradovi;
	
	private TekstualnaVrijednost brojGeneracija;
	
	private TekstualnaVrijednost sjeme;

	private RadioGumbi algoritam;
	
	private TekstualnaVrijednost brojMrava;
	
	private TekstualnaVrijednost konstantaIsparavanja;
	
	private TekstualnaVrijednost alfa;
	
	private TekstualnaVrijednost beta;

	private TekstualnaVrijednost brojMravaAzurira;
	
	//private TekstualnaVrijednost brojKoraka;
	
	//private TekstualnaVrijednost a;
	
	
	private Ploca gradoviPloca = new Ploca();
	
	private Ploca brojMravaAzuriraPloca = new Ploca();
	
	private Ploca brojGeneracijaPloca = new Ploca();
	
	private Ploca sjemePloca = new Ploca();

	private Ploca algoritamPloca = new Ploca();

	private Ploca brojMravaPloca = new Ploca();

	private Ploca konstantaIsparavanjaPloca = new Ploca();

	private Ploca alfaPloca = new Ploca();

	private Ploca betaPloca = new Ploca();
	
	//private Ploca brojKorakaPloca = new Ploca();
	
	//private Ploca aPloca = new Ploca();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8465945028324200176L;

	private XYSeriesCollection putanjeKolekcija;
	
	private XYSeriesCollection najboljaPutanjaKolekcija;

	protected XYSeriesCollection kolekcija;

	protected JFreeChart graf;

	protected XYPlot nacrt;

	public ACOGUI(String title) { super(title); }
	
	private class NacrtajGradove implements Runnable {
		
		Grad[] gradovi;
		
		NacrtajGradove(Grad[] gradovi) { this.gradovi = gradovi; }

		@Override
		public void run() {
			XYSeries podatci = new XYSeries("Gradovi");
			XYShapeRenderer renderer = (XYShapeRenderer) nacrt.getRenderer(0);
			renderer.removeAnnotations();
			for (Grad grad : gradovi) { 
				podatci.add(grad.x, grad.y); 
				renderer.addAnnotation(new XYTextAnnotation(grad.ime, grad.x, grad.y));
			}
			nacrt.setRenderer(0, null);
			kolekcija.removeSeries(0);
			kolekcija.addSeries(podatci);
			nacrt.setRenderer(0, renderer);
			nacrt.setDataset(kolekcija);
		}
		
		
		
	}
	
	private class NacrtajPutanju implements Runnable {

		Grad[] najbolji;
		
		Grad[] gradovi;
		
		NacrtajPutanju(Grad[] najbolji, Grad[] gradovi) {
			this.najbolji = najbolji;
			this.gradovi = gradovi;
		}
		
		@Override
		public void run() {
			XYSeries podatci = new XYSeries(PUTANJA, false, true);
			Grad prvi = gradovi[0];
			for (Grad grad : gradovi) { podatci.add(grad.x, grad.y); }
			podatci.add(prvi.x, prvi.y); 
			putanjeKolekcija.removeSeries(0);
			putanjeKolekcija.addSeries(podatci);
			nacrt.setDataset(1, putanjeKolekcija);

			podatci = new XYSeries(NAJBOLJA_PUTANJA, false, true);
			prvi = najbolji[0];
			for (Grad grad : najbolji) { podatci.add(grad.x, grad.y); }
			podatci.add(prvi.x, prvi.y); 
			najboljaPutanjaKolekcija.removeSeries(0);
			najboljaPutanjaKolekcija.addSeries(podatci);
			nacrt.setDataset(2, najboljaPutanjaKolekcija);
		}
		
	}
	
	protected JPanel stvoriKontroleKontejner() {
		
		JPanel kontroleKontejnerVanjski = new JPanel(new BorderLayout());
		JPanel kontroleKontejner = new JPanel();
	
		kontroleKontejner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		kontroleKontejner.setLayout(new BoxLayout(kontroleKontejner, BoxLayout.Y_AXIS));
		
		inicijalizirajElementeKontrola();
		
		DijeljenaPloca[] elementi = new DijeljenaPloca[] {
			gradovi, sjeme, brojMrava, 
			algoritam, konstantaIsparavanja, 
			alfa, beta, /* a ,*/ brojMravaAzurira, /* brojKoraka ,*/ brojGeneracija
		};
		
		Ploca[] ploce = new Ploca[] {
			gradoviPloca, sjemePloca, brojMravaPloca, 
			algoritamPloca, konstantaIsparavanjaPloca, 
			alfaPloca, betaPloca, /* aPloca ,*/ brojMravaAzuriraPloca, /* brojKorakaPloca ,*/ brojGeneracijaPloca
		};
		
		for (int i = 0; i < elementi.length; i++) {
			JPanel ploca = new JPanel();
			ploca.setLayout(new BoxLayout(ploca, BoxLayout.Y_AXIS));
			ploca.add(elementi[i]);
			ploca.add(Box.createRigidArea(new Dimension(0, 10)));
			ploce[i].ploca = ploca;
			kontroleKontejner.add(ploca);
		}
		
		kontroleKontejnerVanjski.add(kontroleKontejner, BorderLayout.NORTH);
		
		return kontroleKontejnerVanjski;
	}
	
	protected void inicijalizirajElementeKontrola() {
		
		ActionListener sakrijOtkrij = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean flag = !algoritam.vratiOdabrani().getActionCommand().equals(SIMPLE_ACO);
				beta.setEnabled(flag);
				flag = algoritam.vratiOdabrani().getActionCommand().equals(SIMPLE_ACO);
				brojMravaAzurira.setEnabled(flag);
				
				//flag = algoritam.vratiOdabrani().getActionCommand().equals(MAX_MIN_ANT_SYSTEM);
				//brojKoraka.setEnabled(flag);
				//a.setEnabled(flag);
			}
		};
		
		brojMravaAzurira = new TekstualnaVrijednost(BROJ_MRAVA_AZURIRA, "10");
		brojMrava = new TekstualnaVrijednost(BROJ_MRAVA, "50");	
		brojGeneracija = new TekstualnaVrijednost(BROJ_GENERACIJA, "2000");
		sjeme = new TekstualnaVrijednost(SJEME, "123456");
		algoritam = new RadioGumbi(ALGORITAM, new String[] {
			SIMPLE_ACO, ANT_SYSTEM//, MAX_MIN_ANT_SYSTEM
		}, 0, new ActionListener[] { 
			sakrijOtkrij, sakrijOtkrij//, sakrijOtkrij
		});
		konstantaIsparavanja = new TekstualnaVrijednost(KONSTANTA_ISPARAVANJA, "0.5");
		alfa = new TekstualnaVrijednost(ALFA, "1");
		beta = new TekstualnaVrijednost(BETA, "2");
		beta.setEnabled(false);
		gradovi = new TekstPodrucje(GRADOVI, 
			"1 565 575\n"
			+ "2 25 185\n"
			+ "3 345 750\n"
			+ "4 945 685\n"
			+ "5 845 655\n"
			+ "6 880 660\n"
			+ "7 25 230\n"
			+ "8 525 1000\n"
			+ "9 580 1175\n"
			+ "10 650 1130\n"
			+ "11 1605 620\n"
			+ "12 1220 580\n"
			+ "13 1465 200\n"
			+ "14 1530 5\n"
			+ "15 845 680\n"
			+ "16 725 370\n"
			+ "17 145 665\n"
			+ "18 415 635\n"
			+ "19 510 875\n"
			+ "20 560 365\n"
			+ "21 300 465\n"
			+ "22 520 585\n"
			+ "23 480 415\n"
			+ "24 835 625\n"
			+ "25 975 580\n"
			+ "26 1215 245\n"
			+ "27 1320 315\n"
			+ "28 1250 400\n"
			+ "29 660 180\n"
			+ "30 410 250\n"
			+ "31 420 555\n"
			+ "32 575 665\n"
			+ "33 1150 1160\n"
			+ "34 700 580\n"
			+ "35 685 595\n"
			+ "36 685 610\n"
			+ "37 770 610\n"
			+ "38 795 645\n"
			+ "39 720 635\n"
			+ "40 760 650\n"
			+ "41 475 960\n"
			+ "42 95 260\n"
			+ "43 875 920\n"
			+ "44 700 500\n"
			+ "45 555 815\n"
			+ "46 830 485\n"
			+ "47 1170 65\n"
			+ "48 830 610\n"
			+ "49 605 625\n"
			+ "50 595 360\n"
			+ "51 1340 725\n"
			+ "52 1740 245\n",
		10);
		//brojKoraka = new TekstualnaVrijednost(BROJ_KORAKA, "20");
		//brojKoraka.setEnabled(false);
		//a = new TekstualnaVrijednost("A", "5");
		//a.setEnabled(false);
	}

	protected ChartPanel stvoriGraf() {
		XYSeries podatci = new XYSeries("Gradovi");
		kolekcija = new XYSeriesCollection();
		kolekcija.addSeries(podatci);
		graf = ChartFactory.createXYLineChart("Gradovi", "", "", null, PlotOrientation.VERTICAL, true, false, false);
		nacrt = graf.getXYPlot();
		nacrt.setRenderer(0, new XYShapeRenderer());
		
		java.awt.geom.Ellipse2D.Double kruzici = new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setSeriesShape(0, kruzici);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesFillPaint(0, Color.YELLOW);
        renderer.setSeriesOutlinePaint(0, Color.GRAY);
        

		podatci = new XYSeries(PUTANJA);
		putanjeKolekcija = new XYSeriesCollection();
		putanjeKolekcija.addSeries(podatci);
        
        nacrt.setRenderer(1, renderer);

		kruzici = new java.awt.geom.Ellipse2D.Double(-4D, -4D, 8D, 8D);
        renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setSeriesShape(0, kruzici);
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesFillPaint(0, Color.YELLOW);
        renderer.setSeriesOutlinePaint(0, Color.GRAY);
        

		podatci = new XYSeries(NAJBOLJA_PUTANJA);
		najboljaPutanjaKolekcija = new XYSeriesCollection();
		najboljaPutanjaKolekcija.addSeries(podatci);
        
        nacrt.setRenderer(2, renderer);
		
		ChartPanel grafPloca = new ChartPanel(graf);
		
		return grafPloca;
	}
	
	protected void pokreniSimulaciju(JButton gumb) 
		throws UnknownFunctionException, UnparsableExpressionException {
		
		ACOSimulator simulator = new ACOSimulator();

		try {
			simulator.koristeciSjeme(Long.parseLong(sjeme.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Sjeme mora biti cijeli broj");
			return ;
		} 
		
		try {
			simulator.koristeciBrojMrava(Integer.parseInt(brojMrava.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj mrava mora biti cijeli broj");
			return ;
		} 
		
		
		List<Par<String, Par<Double, Double>>> gradoviLista = new ArrayList<Par<String, Par<Double, Double>>>();
		String[] linije = gradovi.vratiVrijednost().split("\n");
		for (int i = 0; i < linije.length; i++) {
//			String dijelovi[] = linije[i].split(",");
//			int x = Integer.parseInt(dijelovi[0].trim());
//			int y = Integer.parseInt(dijelovi[1].trim());
//			Par<String, Par<Integer, Integer>> grad = new Par<String, Par<Integer, Integer>>(
//				dijelovi.length > 2 ? dijelovi[2].trim() : Integer.toString(i + 1), 
//				new Par<Integer, Integer>(x, y)
//			);
			String dijelovi[] = linije[i].split(" ");
			double x = Double.parseDouble(dijelovi[1].trim());
			double y = Double.parseDouble(dijelovi[2].trim());
			Par<String, Par<Double, Double>> grad = new Par<String, Par<Double, Double>>(
				dijelovi[0].trim(), 
				new Par<Double, Double>(x, y)
			);
			gradoviLista.add(grad);
		}
		
		simulator.koristeciGradove(gradoviLista);
		
		
		int odabraniAlgoritam; 
		if (algoritam.vratiOdabrani().getActionCommand().equals(SIMPLE_ACO)) {
			odabraniAlgoritam = ACOSimulator.SIMPLE_ACO_ALGORITAM;
		} else if (algoritam.vratiOdabrani().getActionCommand().equals(ANT_SYSTEM)) {
			odabraniAlgoritam = ACOSimulator.ANT_SYSTEM_ALGORITAM;
		} else {
			odabraniAlgoritam = ACOSimulator.MAX_MIN_ANT_SYSTEM_ALGORITM;
		}
		simulator.koristeciAlgoritam(odabraniAlgoritam);

		try {
			simulator.koristeciAlfa(Double.parseDouble(alfa.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Alfa mora biti broj");
			return ;
		} 
		
		if (odabraniAlgoritam == ACOSimulator.SIMPLE_ACO_ALGORITAM) {
			try {
				simulator.koristeciBrojMravaZaAzuriranje(Integer.parseInt(brojMravaAzurira.vratiVrijednost()));
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Broj mrava za azuriranje mora biti cijeli broj");
				return ;
			} 
		} else {

			try {
				simulator.koristeciBeta(Double.parseDouble(beta.vratiVrijednost()));
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Beta mora biti broj");
				return ;
			} 
			/*if (odabraniAlgoritam == ACOSimulator.MAX_MIN_ANT_SYSTEM_ALGORITM) {
				try {
					simulator.koristecKonstantuA(Double.parseDouble(a.vratiVrijednost()));
				} catch (NumberFormatException e) { 
					zapisiUZapisnik("Konstanta A mora biti broj");
					return ;
				} 
				try {
					simulator.uzBrojKoraka(Integer.parseInt(brojKoraka.vratiVrijednost()));
				} catch (NumberFormatException e) { 
					zapisiUZapisnik("Broj koraka mora biti cijeli broj");
					return ;
				} 
			}*/
		}
		
		try {
			simulator.koristeciKonstantuIsparavanja(Double.parseDouble(konstantaIsparavanja.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Konstanta isparavanja mora biti broj");
			return ;
		} 
		
		try {
			simulator.uzBrojGeneracija(Integer.parseInt(brojGeneracija.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj generacija mora biti cijeli broj");
			return ;
		}
		
		simulator.postaviGUI(this);
		simulator.addPropertyChangeListener(new ZaustaviSimulaciju(gumb));
		simulator.execute();
		
		this.simulator = simulator;
		
		
		gumb.setText(ZAUSTAVI);
				
	}
	
	public void iscrtajPutanju(Grad[] najbolji, Grad[] gradovi) { SwingUtilities.invokeLater(new NacrtajPutanju(najbolji, gradovi)); }


	public void iscrtajGradove(Grad[] gradovi) { SwingUtilities.invokeLater(new NacrtajGradove(gradovi));	}
}
