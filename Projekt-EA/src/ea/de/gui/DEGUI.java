/**
 * 
 */
package ea.de.gui;

import static ea.gui.GUIKonstante.*;
import static ea.de.gui.DEGUIKonstante.*;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

import ea.de.Vektor;
import ea.gui.DijeljenaPloca;
import ea.gui.GUI;
import ea.gui.RadioGumbi;
import ea.gui.TekstualnaVrijednost;
import ea.simulatori.DESimulator;
import ea.util.PolinomKrajolik;
import ea.util.RealniKrajolik;

/**
 * @author Zlikavac32
 *
 */
public class DEGUI extends GUI {

	private TekstualnaVrijednost funkcija;

	private TekstualnaVrijednost donjaGranica;

	private TekstualnaVrijednost brojTocaka;
	
	private TekstualnaVrijednost gornjaGranica;

	private TekstualnaVrijednost velicinaPopulacije;

	private TekstualnaVrijednost brojGeneracija;

	private TekstualnaVrijednost sjeme;
	
	private TekstualnaVrijednost brojUzoraka;
	
	private TekstualnaVrijednost redPolinoma;
	
	private TekstualnaVrijednost gornjaGranicaKoeficijenta;
	
	private TekstualnaVrijednost donjaGranicaKoeficijenta;
	
	private TekstualnaVrijednost brojParova;
	
	private RadioGumbi mutator;
	
	private RadioGumbi selektor;
	
	private TekstualnaVrijednost vjerojatnostMutacije;
	
	private TekstualnaVrijednost faktorTezine;
	

	//Ploce

	private Ploca funkcijaPloca = new Ploca();

	private Ploca donjaGranicaPloca = new Ploca();

	private Ploca brojTocakaPloca = new Ploca();

	private Ploca gornjaGranicaPloca = new Ploca();

	private Ploca velicinaPopulacijePloca = new Ploca();

	private Ploca brojGeneracijaPloca = new Ploca();

	private Ploca sjemePloca = new Ploca();

	private Ploca brojUzorakaPloca = new Ploca();
	
	private Ploca redPolinomaPloca = new Ploca();
	
	private Ploca gornjaGranicaKoeficijentaPloca = new Ploca();
	
	private Ploca donjaGranicaKoeficijentaPloca = new Ploca();
	
	private Ploca brojParovaPloca = new Ploca();
	
	private Ploca mutatorPloca = new Ploca();
	
	private Ploca selektorPloca = new Ploca();
	
	private Ploca vjerojatnostMutacijePloca = new Ploca();
	
	private Ploca faktorTezinePloca = new Ploca();

	
	private	int brojElemenata;
	
	protected XYSeriesCollection kolekcija;

	protected JFreeChart graf;

	protected XYPlot nacrt;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8465945028324200176L;
	
	private class NacrtajFunkciju extends SwingWorker<Void, Void> {
		
		private RealniKrajolik krajolik;
		
		private XYSeries podatci;
		
		NacrtajFunkciju(RealniKrajolik krajolik) {
			this.krajolik = krajolik;
		}

		@Override
		protected Void doInBackground() 
			throws Exception {
			podatci = new XYSeries("Funckija");

			double dolje = krajolik.vratiDonjuGranicu()[0];
			double gore = krajolik.vratiGornjuGranicu()[0];
			double korak = (gore - dolje) / brojElemenata;
			for (int i = 0; i <= brojElemenata; i++) {
				podatci.add(dolje, krajolik.racunajVrijednost(new double[] { dolje }));
				dolje += korak;			
			}
			return null;
		}
		
		@Override
		protected void done() {
			kolekcija.removeSeries(0);
			kolekcija.addSeries(podatci);
		}
	}

	
	private class NacrtajFunkcijuAproksimacija extends SwingWorker<Void, Void> {
		
		private XYSeries podatci;
		
		private Vektor<double[][], PolinomKrajolik> vektor;
		
		NacrtajFunkcijuAproksimacija(Vektor<double[][], PolinomKrajolik> vektor) {
			this.vektor = vektor;
		}

		@Override
		protected Void doInBackground() 
			throws Exception {
			podatci = new XYSeries("Aproksimacija");
			PolinomKrajolik krajolik = vektor.vratiKrajolik();
			double[][] koeficijenti = vektor.vratiVrijednost();
			double dolje = krajolik.vratiKrajolikFunkcije().vratiDonjuGranicu()[0];
			double gore = krajolik.vratiKrajolikFunkcije().vratiGornjuGranicu()[0];
			double korak = (gore - dolje) / brojElemenata;
			for (int i = 0; i <= brojElemenata; i++) {
				podatci.add(dolje, krajolik.racunajVrijednost(new double[] { dolje }, koeficijenti));
				dolje += korak;			
			}
			return null;
		}
		
		@Override
		protected void done() {
			((XYSeriesCollection) nacrt.getDataset(2)).removeAllSeries();
			((XYSeriesCollection) nacrt.getDataset(2)).addSeries(podatci);
		}
	}
	
	private class NacrtajTocke extends SwingWorker<Void, Void> {
		
		private double[][] tocke;
		
		private XYSeries podatci;

		private RealniKrajolik krajolik;
		
		NacrtajTocke(double[][] tocke, RealniKrajolik krajolik) {
			this.tocke = tocke;
			this.krajolik = krajolik;
		}

		@Override
		public Void doInBackground()
			throws Exception {
			podatci = new XYSeries("podatci");
			for (int i = 0; i < tocke.length; i++) {
				podatci.add(tocke[i][0], krajolik.racunajVrijednost(tocke[i]));
			}
			return null;
		}
		
		@Override
		protected void done() {
			XYSeriesCollection kolekcijaJedinki = new XYSeriesCollection(podatci);
			nacrt.setDataset(1, kolekcijaJedinki);
		}
		
	}

	public DEGUI(String title) {
		super(title);
	}
	
	
	protected JPanel stvoriKontroleKontejner() {
		
		JPanel kontroleKontejner = new JPanel();
	
		kontroleKontejner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		kontroleKontejner.setLayout(new BoxLayout(kontroleKontejner, BoxLayout.Y_AXIS));
		
		inicijalizirajElementeKontrola();
		
		DijeljenaPloca[] elementi = new DijeljenaPloca[] {
			funkcija, sjeme, donjaGranica, gornjaGranica, brojTocaka, brojUzoraka, gornjaGranicaKoeficijenta,
			donjaGranicaKoeficijenta, redPolinoma,
			velicinaPopulacije, mutator, vjerojatnostMutacije, selektor, brojParova, faktorTezine,
			brojGeneracija
		};
		
		Ploca[] ploce = new Ploca[] {
			funkcijaPloca, sjemePloca, donjaGranicaPloca, gornjaGranicaPloca, brojTocakaPloca, 
			brojUzorakaPloca, gornjaGranicaKoeficijentaPloca,
			donjaGranicaKoeficijentaPloca, redPolinomaPloca,
			velicinaPopulacijePloca, mutatorPloca, vjerojatnostMutacijePloca, selektorPloca, 
			faktorTezinePloca, brojParovaPloca,
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
		return kontroleKontejner;
	}
	
	protected void inicijalizirajElementeKontrola() {		
		brojGeneracija = new TekstualnaVrijednost("Broj generacija", "350");
		velicinaPopulacije = new TekstualnaVrijednost("Broj vektora", "100");
		brojTocaka = new TekstualnaVrijednost("Broj točaka", "1000");
		gornjaGranica = new TekstualnaVrijednost("Do", "5");
		donjaGranica = new TekstualnaVrijednost("Od", "-5");
		sjeme = new TekstualnaVrijednost("Sjeme", "123456");
		funkcija = new TekstualnaVrijednost("Funkcija", DEGUIKonstante.FUNKCIJA);
		brojUzoraka = new TekstualnaVrijednost("Broj uzoraka", "100");
		redPolinoma = new TekstualnaVrijednost("Red polinoma", "5");
		gornjaGranicaKoeficijenta = new TekstualnaVrijednost("Gornja granica koeficijenta", "5");
		donjaGranicaKoeficijenta = new TekstualnaVrijednost("Donja granica koeficijenta", "-5");
		brojParova = new TekstualnaVrijednost("Broj parova", "1");
		mutator = new RadioGumbi("Vrsta mutacije", new Object[] {
			BINOMNA_MUTACIJA, UNIFORMNA_MUTACIJA
		}, 0);
		selektor = new RadioGumbi("Selekcija", new Object[] {
			NAJBOLJI_SELEKTOR, RANDOM_SELEKTOR
		}, 0);
		vjerojatnostMutacije = new TekstualnaVrijednost("Vjerojatnost mutacije", "0.95");
		faktorTezine = new TekstualnaVrijednost("Faktor težine", "0.9");
	}

	protected ChartPanel stvoriGraf() {
		XYSeries podatci = new XYSeries("Funkcija");
		kolekcija = new XYSeriesCollection();
		kolekcija.addSeries(podatci);
		graf = ChartFactory.createXYLineChart(DEGUIKonstante.FUNKCIJA, "X", "Y", kolekcija, PlotOrientation.VERTICAL, false, false, false);
		nacrt = graf.getXYPlot();
		nacrt.setRenderer(1, new XYShapeRenderer());
		nacrt.setRenderer(2, new StandardXYItemRenderer());
		nacrt.setDataset(2, new XYSeriesCollection());
		
		ChartPanel grafPloca = new ChartPanel(graf);
		
		return grafPloca;
	}

	public void nacrtajFunkciju(RealniKrajolik krajolik) {
		new NacrtajFunkciju(krajolik).execute();
	}
	
	protected void pokreniSimulaciju(JButton gumb) 
		throws UnknownFunctionException, UnparsableExpressionException {
		
		
		DESimulator simulator = new DESimulator();
		
		brojElemenata = Integer.parseInt(brojTocaka.vratiVrijednost());
		if (brojElemenata < 1) { throw new IllegalArgumentException("Broj tocaka mora biti veci od 0"); }
		
		try {
			simulator.koristeciSjeme(Long.parseLong(sjeme.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Sjeme mora biti cijeli broj");
			return ;
		} 
		
		try {
			simulator.unutarGranica(
				Double.parseDouble(donjaGranica.vratiVrijednost()),
				Double.parseDouble(gornjaGranica.vratiVrijednost())
			);
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Granice moraju biti realan broj");
			return ;
		}
		
		
		try {
			simulator.saVelicinomPopulacije(Integer.parseInt(velicinaPopulacije.vratiVrijednost()));
		} catch (NumberFormatException e) { 
			zapisiUZapisnik("Broj voektora mora biti cijeli broj");
			return ;
		}
		
		try {
			simulator.saBrojemUzoraka(Integer.parseInt(brojUzoraka.vratiVrijednost()));
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Broj uzoraka mora biti cijeli broj");
			return ;
		}
		
		try {
			simulator.uzRedPolinoma(Integer.parseInt(redPolinoma.vratiVrijednost()));
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Red polinoma mora biti cijeli broj");
			return ;
		}
		
		try {
			simulator.koristeciGornjuGranicuKoeficijenata(
				Double.parseDouble(gornjaGranicaKoeficijenta.vratiVrijednost())
			);
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Gornja granica koeficijenata mora biti broj");
			return ;
		}
		
		try {
			simulator.koristeciDonjuGranicuKoeficijenata(
				Double.parseDouble(donjaGranicaKoeficijenta.vratiVrijednost())
			);
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Donja granica koeficijenata mora biti broj");
			return ;
		}
		
		try {
			simulator.saBrojemParova(Integer.parseInt(brojParova.vratiVrijednost()));
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Broj parova mora biti cijeli broj");
			return ;
		}
		
		try {
			simulator.uzVjerojatnostMutacije(
				Double.parseDouble(vjerojatnostMutacije.vratiVrijednost())
			);
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Vjerojatnost mutacije mora biti broj");
			return ;
		}
		
		try {
			simulator.uzFaktorTezine(
				Double.parseDouble(faktorTezine.vratiVrijednost())
			);
		} catch (NumberFormatException e) {
			zapisiUZapisnik("Faktor tezine mora biti broj");
			return ;
		}
		
		int mutator = -1;
		String vrijednost = this.mutator.vratiOdabrani().getActionCommand();
		
		if (vrijednost.equals(UNIFORMNA_MUTACIJA)) {
			mutator = DESimulator.UNOFIRMNA_MUTACIJA;
		} else if (vrijednost.equals(BINOMNA_MUTACIJA)) {
			mutator = DESimulator.BINOMNA_MUTACIJA;
		}
		
		simulator.koristeciMutaciju(mutator);
		
		int selektor = -1;
		vrijednost = this.selektor.vratiOdabrani().getActionCommand();
		
		if (vrijednost.equals(NAJBOLJI_SELEKTOR)) {
			selektor = DESimulator.NAJBOLJI_SELEKTOR;
		} else if (vrijednost.equals(RANDOM_SELEKTOR)) {
			selektor = DESimulator.RANDOM_SELEKTOR;
		}
		
		simulator.koristeciSelektor(selektor);
		
		String funkcijaString = funkcija.vratiVrijednost().toLowerCase();
		graf.setTitle(funkcijaString);
		
		simulator.koristeciFunkciju(
			new ExpressionBuilder(funkcijaString).withVariableNames("x")
			.withVariable("pi", Math.PI).withVariable("e", Math.E).build()
		);
		
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

	public void iscrtajTocke(double[][] tocke, RealniKrajolik krajolik) {
		new NacrtajTocke(tocke, krajolik).execute();
	}

	public void iscrtajAproksimaciju(Vektor<double[][], PolinomKrajolik> vektor) {
		new NacrtajFunkcijuAproksimacija(vektor).execute();
	}
}
