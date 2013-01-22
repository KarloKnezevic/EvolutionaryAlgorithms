/**
 * 
 */
package ea.ais.gui;

import static ea.gui.GUIKonstante.ZAUSTAVI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

import ea.ais.AntiTijelo;
import ea.gui.DijeljenaPloca;
import ea.gui.GUI;
import ea.gui.RadioGumbi;
import ea.gui.TekstualnaVrijednost;
import ea.simulatori.AISSimulator;
import ea.simulatori.PSOSimulator;
import ea.util.RealniKrajolik;

import static ea.ais.gui.AISGUIKonstante.*;

/**
 * @author Zlikavac32
 *
 */
public class AISGUI extends GUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5336525365429544799L;
	
	private TekstualnaVrijednost funkcija;
	
	private TekstualnaVrijednost xOd;
	
	private TekstualnaVrijednost xDo;
	
	private TekstualnaVrijednost yDo;
	
	private TekstualnaVrijednost yOd;
	
	private TekstualnaVrijednost sjeme;
	
	private TekstualnaVrijednost brojAntiTijela;
	
	private TekstualnaVrijednost brojGeneracija;

	private TekstualnaVrijednost brojTocaka;

	private RadioGumbi trazi;
	
	private RadioGumbi vrstaMutacije;
	
	private RadioGumbi algoritam;
	
	private TekstualnaVrijednost ro;
	
	private TekstualnaVrijednost beta;
	
	private TekstualnaVrijednost brojKlonova;
	
	private TekstualnaVrijednost brojNovih;
	
	private TekstualnaVrijednost brojBitova;

	
	//Ploce
	
	private Ploca funkcijaPloca = new Ploca();
	
	private Ploca xOdPloca = new Ploca();
	
	private Ploca xDoPloca = new Ploca();
	
	private Ploca yDoPloca = new Ploca();
	
	private Ploca yOdPloca = new Ploca();
	
	private Ploca sjemePloca = new Ploca();
	
	private Ploca brojAntiTijelaPloca = new Ploca();
	
	private Ploca brojGeneracijaPloca = new Ploca();

	private Ploca brojTocakaPloca = new Ploca();

	private Ploca traziPloca = new Ploca();
	
	private Ploca vrstaMutacijePloca = new Ploca();
	
	private Ploca algoritamPloca = new Ploca();
	
	private Ploca roPloca = new Ploca();
	
	private Ploca betaPloca = new Ploca();
	
	private Ploca brojKlonovaPloca = new Ploca();
	
	private Ploca brojNovihPloca = new Ploca();
	
	private Ploca brojBitovaPloca = new Ploca();
	
	

	private DefaultXYZDataset kolekcija;

	private JFreeChart graf;

	private XYPlot nacrt;

	private XYBlockRenderer funkcijaRenderer;
	

	
	private class NacrtajFunkciju extends SwingWorker<Void, Void> {
		
		RealniKrajolik krajolik;
		
		double[][] podatci;

		double min = Double.MAX_VALUE;
		
		double max = Double.MIN_NORMAL;

		private double doljeX;

		private double doljeY;

		private double goreX;

		private double goreY;
		
		NacrtajFunkciju(RealniKrajolik krajolik) {
			this.krajolik = krajolik;
		}

		@Override
		public Void doInBackground()
			throws Exception {

			int brojElemenata = Integer.parseInt(brojTocaka.vratiVrijednost());
			if (brojElemenata < 1) { throw new IllegalArgumentException("Broj tocaka mora biti veci od 0"); }
			podatci = new double[3][(brojElemenata + 1) * (brojElemenata + 1)];
			doljeX = krajolik.vratiDonjuGranicu()[0];
			goreX = krajolik.vratiGornjuGranicu()[0];
			doljeY = krajolik.vratiDonjuGranicu()[1];
			goreY = krajolik.vratiGornjuGranicu()[1];
			double korakX = (goreX - doljeX) / brojElemenata;
			double korakY = (goreY - doljeY) / brojElemenata;
			double xPomak = doljeX;
			int indeksElementa = 0;
			for (int i = 0; i <= brojElemenata; i++) {
				double yPomak = doljeY;
				for (int j = 0; j <= brojElemenata; j++) {
					double vrijednost = krajolik.racunajVrijednost(new double[] {
						xPomak, yPomak
					});
					podatci[0][indeksElementa] = xPomak;
					podatci[1][indeksElementa] = yPomak;
					podatci[2][indeksElementa++] = vrijednost;
					if (vrijednost < min) { min = vrijednost; }
					if (vrijednost > max) { max = vrijednost; }
					yPomak += korakY;
				}
				xPomak += korakX;
			}
			
			return null;
		}
		
		@Override
		protected void done() {
			kolekcija.addSeries("", podatci);			
			funkcijaRenderer.setPaintScale(new GrayPaintScale(min, max));
		}
	}
	

	/**
	 * @author Zlikavac32
	 *
	 */
	private class NacrtajPopulaciju extends SwingWorker<Void, Void> {

		List<AntiTijelo<byte[], double[], RealniKrajolik>> populacija;
		
		private XYSeries podatci;

		NacrtajPopulaciju(List<AntiTijelo<byte[], double[], RealniKrajolik>> populacija) {
			this.populacija = populacija;
		}
		
		/**
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected Void doInBackground() throws Exception {
			podatci = new XYSeries("Podatci");
			for (AntiTijelo<byte[], double[], RealniKrajolik> antiTijelo : populacija) {
				double[] vrijednost = antiTijelo.vratiVrijednost();
				podatci.add(vrijednost[0], vrijednost[1]);
			}
			return null;
		}

		@Override
		protected void done() {
			XYSeriesCollection kolekcijaJedinki = new XYSeriesCollection(podatci);
			nacrt.setDataset(1, kolekcijaJedinki);
		}
		
	}


	public AISGUI(String title) { super(title); }
	
	protected JPanel stvoriKontroleKontejner() {
		
		JPanel kontroleKontejnerVanjski = new JPanel(new BorderLayout());
		JPanel kontroleKontejner = new JPanel();
	
		kontroleKontejner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		kontroleKontejner.setLayout(new BoxLayout(kontroleKontejner, BoxLayout.Y_AXIS));
		
		inicijalizirajElementeKontrola();
		
		DijeljenaPloca[] elementi = new DijeljenaPloca[] {
			funkcija, sjeme, xOd, xDo, yOd, yDo, brojTocaka, brojAntiTijela, 
			trazi, brojBitova, vrstaMutacije, ro, algoritam, 
			brojKlonova, beta, brojNovih, brojGeneracija
		};
		
		Ploca[] ploce = new Ploca[] {
			funkcijaPloca, sjemePloca, xOdPloca, xDoPloca, yOdPloca, yDoPloca, brojTocakaPloca, brojAntiTijelaPloca, 
			traziPloca, brojBitovaPloca, vrstaMutacijePloca, roPloca, algoritamPloca, 
			brojKlonovaPloca, betaPloca, brojNovihPloca, brojGeneracijaPloca
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
				boolean flag = vrstaMutacije.vratiOdabrani().getActionCommand().equals(DISTRUBUIRANI_BIT_MUTATOR);
				ro.setEnabled(flag);
				
				flag = algoritam.vratiOdabrani().getActionCommand().equals(CLONALG_ALGORITAM);
				brojKlonova.setEnabled(!flag);
				beta.setEnabled(flag);
				brojNovih.setEnabled(flag);
				
			}
		};
		
		funkcija = new TekstualnaVrijednost("Funkcija", FUNKCIJA);
		xOd = new TekstualnaVrijednost("X Od", "-10");
		xDo = new TekstualnaVrijednost("X Do", "10");
		yOd = new TekstualnaVrijednost("Y Od", "-10");
		yDo = new TekstualnaVrijednost("Y Do", "10");
		brojTocaka  = new TekstualnaVrijednost("Broj točaka", "50");
		sjeme = new TekstualnaVrijednost("Sjeme", "123456");
		brojAntiTijela = new TekstualnaVrijednost("Broj antitijela", "15");
		brojGeneracija = new TekstualnaVrijednost("Broj generacija", "50");
		trazi = new RadioGumbi("Traži", new Object[] {
			MINIMUM, MAKSIMUM	
		}, 0);
		brojBitova = new TekstualnaVrijednost("Broj bitova", "8"); 
		vrstaMutacije = new RadioGumbi("Vrsta mutacije", new String[] {
			NEMA, JEDAN_BIT_MUTATOR, JEDAN_BIT_VARIJABLA_MUTATOR, DISTRUBUIRANI_BIT_MUTATOR
		}, 0, new ActionListener[] {
			sakrijOtkrij, sakrijOtkrij, sakrijOtkrij, sakrijOtkrij
		}); 
		ro = new TekstualnaVrijednost("Ro", "2"); 
		ro.setEnabled(false);
		algoritam = new RadioGumbi("Algoritam", new String[] {
			SAIS_ALGORITAM, CLONALG_ALGORITAM
		}, 0, new ActionListener[] {
			sakrijOtkrij, sakrijOtkrij
		}); 
		brojKlonova = new TekstualnaVrijednost("Broj klonova", "10"); 
		beta = new TekstualnaVrijednost("Beta", "20"); 
		beta.setEnabled(false);
		brojNovih = new TekstualnaVrijednost("Broj novih", "10");
		brojNovih.setEnabled(false);
		
	}

	protected ChartPanel stvoriGraf() {
		kolekcija = new DefaultXYZDataset();
		graf = ChartFactory.createXYLineChart(FUNKCIJA, "", "", kolekcija, PlotOrientation.VERTICAL, true, false, false);
		graf.removeLegend();
		
		nacrt = graf.getXYPlot();
		nacrt.getDomainAxis().setLowerMargin(0);
		nacrt.getDomainAxis().setUpperMargin(0);
		nacrt.getRangeAxis().setLowerMargin(0);
		nacrt.getRangeAxis().setUpperMargin(0);
		
		funkcijaRenderer = new XYBlockRenderer();
		
		nacrt.setRenderer(0, funkcijaRenderer);
		
		nacrt.setRenderer(1, new XYShapeRenderer());
		
		nacrt.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		ChartPanel grafPloca = new ChartPanel(graf);
		
		return grafPloca;
	}
	
	protected void pokreniSimulaciju(JButton gumb) 
		throws UnknownFunctionException, UnparsableExpressionException {
		
		AISSimulator simulator = new AISSimulator();
		
		super.simulator = simulator;
		
		simulator.postaviGUI(this);
		
		try {
			simulator.koristeciSjeme(Long.parseLong(sjeme.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Sjeme mora biti cijeli broj");
			return ;
		} 
		
		simulator.traziEkstrem(
			trazi.vratiOdabrani().getActionCommand().equals(MINIMUM) ? PSOSimulator.MINIMUM
				: PSOSimulator.MAKSIMUM
		);
		
		try {
			simulator.unutarGranica(
				new double[] {
					Double.parseDouble(xOd.vratiVrijednost()),
					Double.parseDouble(yOd.vratiVrijednost())
				},new double[] {
					Double.parseDouble(xDo.vratiVrijednost()),
					Double.parseDouble(yDo.vratiVrijednost())
				}
			);
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Granice moraju biti brojevi");
			return ;
		}
		
		try {
			simulator.saBrojemAntiTijela(Integer.parseInt(brojAntiTijela.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj antitijela mora biti cijeli broj");
			return ;
		}
		
		try {
			simulator.koristeciBrojBitova(Integer.parseInt(brojBitova.vratiVrijednost()));
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Broj bitova mora biti cijeli broj");
			return ;
		}
		
		int mutacija = -1;
		
		if (vrstaMutacije.vratiOdabrani().getActionCommand().equals(JEDAN_BIT_MUTATOR)) {
			mutacija = AISSimulator.JEDAN_BIT_MUTATOR;
		} else if (vrstaMutacije.vratiOdabrani().getActionCommand().equals(JEDAN_BIT_VARIJABLA_MUTATOR)) {
			mutacija = AISSimulator.JEDAN_BIT_VARIJABLA_MUTATOR;
		} else if (vrstaMutacije.vratiOdabrani().getActionCommand().equals(DISTRUBUIRANI_BIT_MUTATOR)) {
			mutacija = AISSimulator.DISTRIBUIRANI_BIT_MUTATOR;
		}
		
		if (mutacija == AISSimulator.DISTRIBUIRANI_BIT_MUTATOR) {
			try {
				simulator.koristeciRo(Double.parseDouble(ro.vratiVrijednost()));
			} catch (NumberFormatException e) {
				zapisiUZapisnik("Ro mora biti broj");
				return ;
			}
		}
		
		int algoritam = this.algoritam.vratiOdabrani().getActionCommand().equals(SAIS_ALGORITAM)
				? AISSimulator.SAIS_ALGORITAM : AISSimulator.CLONALG_ALGORITAM;

		if (algoritam == AISSimulator.SAIS_ALGORITAM) {
			try {
				simulator.uzBrojKlonova(Integer.parseInt(brojKlonova.vratiVrijednost()));
			} catch (NumberFormatException e) {
				zapisiUZapisnik("Broj klonova mora biti cijeli broj");
				return ;
			}
		} else {
			try {
				simulator.uzBetu(Integer.parseInt(beta.vratiVrijednost()));
			} catch (NumberFormatException e) {
				zapisiUZapisnik("Beta mora biti cijeli broj");
				return ;
			}
			
			try {
				simulator.uzBrojNovih(Integer.parseInt(brojNovih.vratiVrijednost()));
			} catch (NumberFormatException e) {
				zapisiUZapisnik("Broj novih mora biti cijeli broj");
				return ;
			}
		}
		
		simulator.koristeciAlgoritam(algoritam);
		simulator.koristeciMuaciju(mutacija);
		
		String funkcijaString = funkcija.vratiVrijednost().toLowerCase();

		graf.setTitle(funkcijaString);
		
		simulator.koristeciFunkciju(
			new ExpressionBuilder(funkcijaString).withVariableNames("x", "y")
			.withVariable("pi", Math.PI).withVariable("e", Math.E).build()
		);
		
		try {
			simulator.uzBrojGeneracija(Integer.parseInt(brojGeneracija.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj generacija mora biti cijeli broj");
			return ;
		}
		
		simulator.addPropertyChangeListener(new ZaustaviSimulaciju(gumb));
		simulator.execute();
		
		gumb.setText(ZAUSTAVI);
				
	}

	public void iscrtajFunkciju(RealniKrajolik krajolik) {
		new NacrtajFunkciju(krajolik).execute();
	}

	public void iscrtajPopulaciju(List<AntiTijelo<byte[], double[], RealniKrajolik>> populacija) {
		new NacrtajPopulaciju(populacija).execute();
	}

}
