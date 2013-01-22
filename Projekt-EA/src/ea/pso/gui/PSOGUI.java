/**
 * 
 */
package ea.pso.gui;

import static ea.gui.GUIKonstante.ZAUSTAVI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

import ea.gui.DijeljenaPloca;
import ea.gui.GUI;
import ea.gui.RadioGumbi;
import ea.gui.TekstualnaVrijednost;
import ea.pso.Cestica;
import ea.simulatori.PSOSimulator;
import ea.util.RealniKrajolik;

import static ea.pso.gui.PSOGUIKonstante.*;

/**
 * @author Zlikavac32
 *
 */
public class PSOGUI extends GUI {

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
	
	private TekstualnaVrijednost brojCestica;
	
	private RadioGumbi brzinaKalkualtor;
	
	private TekstualnaVrijednost brojGeneracija;
	
	private RadioGumbi susjedstvo;
	
	private TekstualnaVrijednost c1;
	
	private TekstualnaVrijednost c2;
	
	private TekstualnaVrijednost donjaGranicaBrzinaX;
	
	private TekstualnaVrijednost gornjaGranicaBrzinaX;
	
	private TekstualnaVrijednost donjaGranicaBrzinaY;
	
	private TekstualnaVrijednost gornjaGranicaBrzinaY;
	
	private TekstualnaVrijednost inercija;
	
	private TekstualnaVrijednost faktorSmanjeInercije;
	
	private TekstualnaVrijednost maksInercija;
	
	private TekstualnaVrijednost brojKorakaInercija;
	
	private TekstualnaVrijednost minInercija;

	private TekstualnaVrijednost brojOkolnihJedinki;

	private TekstualnaVrijednost brojTocaka;

	private RadioGumbi trazi;

	
	//Ploce
	
	private Ploca funkcijaPloca = new Ploca();
	
	private Ploca xOdPloca = new Ploca();
	
	private Ploca xDoPloca = new Ploca();
	
	private Ploca yDoPloca = new Ploca();
	
	private Ploca yOdPloca = new Ploca();
	
	private Ploca sjemePloca = new Ploca();
	
	private Ploca brojCesticaPloca = new Ploca();
	
	private Ploca brzinaKalkualtorPloca = new Ploca();
	
	private Ploca brojGeneracijaPloca = new Ploca();
	
	private Ploca susjedstvoPloca = new Ploca();
	
	private Ploca c1Ploca = new Ploca();
	
	private Ploca c2Ploca = new Ploca();
	
	private Ploca donjaGranicaBrzinaXPloca = new Ploca();
	
	private Ploca gornjaGranicaBrzinaXPloca = new Ploca();
	
	private Ploca donjaGranicaBrzinaYPloca = new Ploca();
	
	private Ploca gornjaGranicaBrzinaYPloca = new Ploca();
	
	private Ploca inercijaPloca = new Ploca();
	
	private Ploca faktorSmanjeInercijePloca = new Ploca();
	
	private Ploca maksInercijaPloca = new Ploca();
	
	private Ploca brojKorakaInercijaPloca = new Ploca();
	
	private Ploca minInercijaPloca = new Ploca();

	private Ploca brojOkolnihJedinkiPloca = new Ploca();

	private Ploca brojTocakaPloca = new Ploca();

	private Ploca traziPloca = new Ploca();
	
	

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
	private class NacrtajCestice extends SwingWorker<Void, Void> {

		Cestica<double[]>[] cestice;
		
		private XYSeries podatci;

		private VectorSeries vektor;
		
		NacrtajCestice(Cestica<double[]>[] cestice) {
			this.cestice = cestice;
		}
		
		/**
		 * @see javax.swing.SwingWorker#doInBackground()
		 */
		@Override
		protected Void doInBackground() throws Exception {
			podatci = new XYSeries("Podatci");
			vektor = new VectorSeries("Vektori");
			for (int i = 0; i < cestice.length; i++) {
				double[] temp = cestice[i].vratiVrijednost();
				double[] staro = cestice[i].vratiStaruVrijednost();
				podatci.add(temp[0], temp[1]);
				if (staro != null) {
					vektor.add(staro[0], staro[1], temp[0] - staro[0], temp[1] - staro[1]);
				}
			}
			return null;
		}

		@Override
		protected void done() {
			XYSeriesCollection kolekcijaJedinki = new XYSeriesCollection(podatci);
			nacrt.setDataset(1, kolekcijaJedinki);
			VectorSeriesCollection vektori = (VectorSeriesCollection) nacrt.getDataset(2);
			vektori.removeAllSeries();
			vektori.addSeries(vektor);
		}
		
	}
		
	public PSOGUI(String title) { super(title); }
	
	protected JPanel stvoriKontroleKontejner() {
		
		JPanel kontroleKontejnerVanjski = new JPanel(new BorderLayout());
		JPanel kontroleKontejner = new JPanel();
	
		kontroleKontejner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		kontroleKontejner.setLayout(new BoxLayout(kontroleKontejner, BoxLayout.Y_AXIS));
		
		inicijalizirajElementeKontrola();
		
		DijeljenaPloca[] elementi = new DijeljenaPloca[] {
			funkcija, sjeme, xOd, xDo, yOd, yDo, brojTocaka, brojCestica, 
			trazi, brzinaKalkualtor, c1, c2, 
			inercija, faktorSmanjeInercije, minInercija, maksInercija, brojKorakaInercija, 
			susjedstvo, brojOkolnihJedinki,
			donjaGranicaBrzinaX, gornjaGranicaBrzinaX, donjaGranicaBrzinaY, gornjaGranicaBrzinaY, 
			brojGeneracija
		};
		
		Ploca[] ploce = new Ploca[] {
			funkcijaPloca, sjemePloca, xOdPloca, xDoPloca, yOdPloca, yDoPloca, brojTocakaPloca, brojCesticaPloca, 
			traziPloca, brzinaKalkualtorPloca, c1Ploca, c2Ploca, 
			inercijaPloca, faktorSmanjeInercijePloca, minInercijaPloca, maksInercijaPloca, brojKorakaInercijaPloca,
			susjedstvoPloca, brojOkolnihJedinkiPloca,
			donjaGranicaBrzinaXPloca, gornjaGranicaBrzinaXPloca, donjaGranicaBrzinaYPloca, gornjaGranicaBrzinaYPloca, 
			brojGeneracijaPloca
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
				String akcija = brzinaKalkualtor.vratiOdabrani().getActionCommand();
				boolean flag = akcija.equals(INERCIJA_BRZINA_KALKULATOR) || 
					akcija.equals(DINAMICKA_INERCIJA_BRZINA_KALKUALTOR);
				inercija.setEnabled(flag);
				flag = akcija.equals(DINAMICKA_INERCIJA_BRZINA_KALKUALTOR);
				faktorSmanjeInercije.setEnabled(flag);
				flag = akcija.equals(DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR);
				maksInercija.setEnabled(flag);
				minInercija.setEnabled(flag);
				brojKorakaInercija.setEnabled(flag);
				flag = susjedstvo.vratiOdabrani().getActionCommand().equals(LOKALNO_SUSJEDSTVO);
				brojOkolnihJedinki.setEnabled(flag);
			}
		};
		
		funkcija = new TekstualnaVrijednost("Funkcija", FUNKCIJA);
		xOd = new TekstualnaVrijednost("X Od", "-10");
		xDo = new TekstualnaVrijednost("X Do", "10");
		yOd = new TekstualnaVrijednost("Y Od", "-10");
		yDo = new TekstualnaVrijednost("Y Do", "10");
		brojTocaka  = new TekstualnaVrijednost("Broj točaka", "50");
		sjeme = new TekstualnaVrijednost("Sjeme", "123456");
		brojCestica = new TekstualnaVrijednost("Broj čestica", "15");
		brzinaKalkualtor = new RadioGumbi("Brzina kalkulator", new String[] {
			STANDARDNI_BRZINA_KALKULATOR, INERCIJA_BRZINA_KALKULATOR, DINAMICKA_INERCIJA_BRZINA_KALKUALTOR,
			DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR, OGRANICAVAJUCA_BRZINA_KALKULATOR
		}, 0, new ActionListener[] {
			sakrijOtkrij, sakrijOtkrij, sakrijOtkrij, sakrijOtkrij, sakrijOtkrij
		});
		brojGeneracija = new TekstualnaVrijednost("Broj generacija", "250");
		susjedstvo = new RadioGumbi("Susjedstvo", new String[] {
			GLOBALNO_SUSJEDSTVO, LOKALNO_SUSJEDSTVO
		}, 0, new ActionListener[] {
			sakrijOtkrij, sakrijOtkrij
		});
		c1 = new TekstualnaVrijednost("c1", "2");
		c2 = new TekstualnaVrijednost("c2", "2.5");
		donjaGranicaBrzinaX = new TekstualnaVrijednost("Donja granica brzina X", "-8");
		gornjaGranicaBrzinaX = new TekstualnaVrijednost("Gornja granica brzina X", "8");
		donjaGranicaBrzinaY = new TekstualnaVrijednost("Donja granica brzina Y", "-8");
		gornjaGranicaBrzinaY = new TekstualnaVrijednost("Gornja granica brzina Y", "8");
		inercija = new TekstualnaVrijednost("Inercija", "0.9");
		inercija.setEnabled(false);
		faktorSmanjeInercije = new TekstualnaVrijednost("Faktor smanjenja inercije", "0.9");
		faktorSmanjeInercije.setEnabled(false);
		maksInercija = new TekstualnaVrijednost("Maks inercija", "0.9");
		maksInercija.setEnabled(false);
		minInercija = new TekstualnaVrijednost("Min inercija", "0.2");
		minInercija.setEnabled(false);
		brojKorakaInercija = new TekstualnaVrijednost("Broj koraka inercija", "150");
		brojKorakaInercija.setEnabled(false);
		brojOkolnihJedinki = new TekstualnaVrijednost("Broj okolnih čestica", "5");
		brojOkolnihJedinki.setEnabled(false);
		trazi = new RadioGumbi("Traži", new Object[] {
			MINIMUM, MAKSIMUM	
		}, 0);
		
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
		
		nacrt.setDataset(2, new VectorSeriesCollection());
		nacrt.setRenderer(2, new VectorRenderer());
		
		nacrt.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		ChartPanel grafPloca = new ChartPanel(graf);
		
		return grafPloca;
	}
	
	protected void pokreniSimulaciju(JButton gumb) 
		throws UnknownFunctionException, UnparsableExpressionException {
		
		PSOSimulator simulator = new PSOSimulator();
		
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
			simulator.saBrojemCestica(Integer.parseInt(brojCestica.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj cestica mora biti cijeli broj");
			return ;
		}
		
		String brzinaKalkulatorVrijednost = brzinaKalkualtor.vratiOdabrani().getActionCommand();
		int brzinaKalkulatorOpcija;
		
		if (brzinaKalkulatorVrijednost.equals(STANDARDNI_BRZINA_KALKULATOR)) {
			brzinaKalkulatorOpcija = PSOSimulator.STANDARDNI_BRZINA_KALKULATOR;
		} else if (brzinaKalkulatorVrijednost.equals(INERCIJA_BRZINA_KALKULATOR)) { 
			brzinaKalkulatorOpcija = PSOSimulator.INERCIJA_BRZINA_KALKULATOR;
		} else if (brzinaKalkulatorVrijednost.equals(DINAMICKA_INERCIJA_BRZINA_KALKUALTOR)) { 
			brzinaKalkulatorOpcija = PSOSimulator.DINAMICKA_INERCIJA_BRZINA_KALKUALTOR;
		} else if (brzinaKalkulatorVrijednost.equals(DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR)) { 
			brzinaKalkulatorOpcija = PSOSimulator.DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR;
		} else {
			brzinaKalkulatorOpcija = PSOSimulator.OGRANICAVAJUCA_BRZINA_KALKULATOR;
		}
		
		if (brzinaKalkulatorOpcija == PSOSimulator.INERCIJA_BRZINA_KALKULATOR || brzinaKalkulatorOpcija == PSOSimulator.DINAMICKA_INERCIJA_BRZINA_KALKUALTOR) {

			try {
				simulator.koristeciInerciju(Double.parseDouble(inercija.vratiVrijednost()));
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Inercija mora biti broj");
				return ;
			}
			
			if (brzinaKalkulatorOpcija == PSOSimulator.DINAMICKA_INERCIJA_BRZINA_KALKUALTOR) {

				try {
					simulator.koristeciFaktorSmanjenjaInercije(Double.parseDouble(faktorSmanjeInercije.vratiVrijednost()));
				} catch (NumberFormatException e) { 
					zapisiUZapisnik("Faktor smanjenja inercije mora biti broj");
					return ;
				}
			}
		} else if (brzinaKalkulatorOpcija == PSOSimulator.DINAMICKA_OGRANICAVAJUCA_INERCIJA_KALKUALTOR) {

			try {
				simulator.koristeciGraniceInercije(new double[] {
					Double.parseDouble(minInercija.vratiVrijednost()),
					Double.parseDouble(maksInercija.vratiVrijednost())
				});
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Minimum i maksimum inercije moraju biti brojevi");
				return ;
			}
			
			try {
				simulator.koristeciBrojKorakaInercije(Integer.parseInt(brojKorakaInercija.vratiVrijednost()));
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Broj koraka inercije mora biti cijeli broj");
				return ;
			}
		}
		
		simulator.koristeciKalkulatorBrzine(brzinaKalkulatorOpcija);
		
		try {
			simulator.koristeciC1IC2(
				Double.parseDouble(c1.vratiVrijednost()),
				Double.parseDouble(c2.vratiVrijednost())
			);
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Konstante c1 i c2 biti brojevi");
			return ;
		}
		
		String susjedstvoVrijednost = susjedstvo.vratiOdabrani().getActionCommand();
		
		int susjedstvoOpcija;
		
		if (susjedstvoVrijednost.equals(GLOBALNO_SUSJEDSTVO)) {
			susjedstvoOpcija = PSOSimulator.GLOBALNO_SUSJEDSTVO;
		} else {
			susjedstvoOpcija = PSOSimulator.LOKALNO_SUSJEDSTVO;

			
			try {
				simulator.koristeciBrojOkolnihCestica(Integer.parseInt(brojOkolnihJedinki.vratiVrijednost()));
			} catch (NumberFormatException e) { 
				zapisiUZapisnik("Broj okolnih jedinki mora biti cijeli broj");
				return ;
			}
			
		}
			
		simulator.koristeciSusjedstvo(susjedstvoOpcija);
		
		try {
			simulator.uzGraniceBrzine(
				new double[] {
					Double.parseDouble(donjaGranicaBrzinaX.vratiVrijednost()),
					Double.parseDouble(donjaGranicaBrzinaY.vratiVrijednost())
				},new double[] {
					Double.parseDouble(gornjaGranicaBrzinaX.vratiVrijednost()),
					Double.parseDouble(gornjaGranicaBrzinaY.vratiVrijednost())
				}
			);
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Granice brzina moraju biti brojevi");
			return ;
		}
		
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

	public void iscrtajCestice(Cestica<double[]>[] cestice) {
		new NacrtajCestice(cestice).execute();
	}
}
