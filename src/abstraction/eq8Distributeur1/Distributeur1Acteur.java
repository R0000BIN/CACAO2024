package abstraction.eq8Distributeur1;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.filiere.IFabricantChocolatDeMarque;
import abstraction.eqXRomu.filiere.IMarqueChocolat;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public class Distributeur1Acteur implements IActeur, IMarqueChocolat, IFabricantChocolatDeMarque {
	
	protected int cryptogramme;
	protected Journal journal;
	protected double coutStockage;
	protected IProduit produit;
	protected List<ChocolatDeMarque> chocolats;
	protected Variable totalStockChoco;
	protected HashMap<ChocolatDeMarque, Double> stock_Choco;
	protected HashMap<Chocolat,Integer> nombreMarquesParType;
	protected List<ChocolatDeMarque> chocoProduits;
	
	
	public Distributeur1Acteur() {
		this.journal= new Journal(this.getNom()+" journal", this);
		this.chocolats = new LinkedList<ChocolatDeMarque>();
		this.totalStockChoco = new VariablePrivee("Eq8DStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);
		this.chocoProduits = new LinkedList<ChocolatDeMarque>();
	}
	
	public void initialiser() {
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*16;
		this.stock_Choco=new HashMap<ChocolatDeMarque,Double>();
		this.nombreMarquesParType=new HashMap<Chocolat,Integer>();
		chocolats= Filiere.LA_FILIERE.getChocolatsProduits();
		this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN,"===== STOCK =====");
		for (ChocolatDeMarque cm : chocolats) {
		    Chocolat typeChoco = cm.getChocolat();
		    nombreMarquesParType.put(typeChoco, nombreMarquesParType.getOrDefault(typeChoco, 0) + 1);
	    }
		for (ChocolatDeMarque cm : chocolats) {
			double stock = 0;
			if (cm.getChocolat()==Chocolat.C_BQ) {
				stock=96000/ nombreMarquesParType.getOrDefault(Chocolat.C_BQ, 1);
			}
			if (cm.getChocolat()==Chocolat.C_MQ) {
				stock=60000/ nombreMarquesParType.getOrDefault(Chocolat.C_MQ, 1);
			}
			if (cm.getChocolat()==Chocolat.C_MQ_E) {
				stock=12000/ nombreMarquesParType.getOrDefault(Chocolat.C_MQ_E, 1);
			}
			if (cm.getChocolat()==Chocolat.C_HQ) {
				stock=48000/ nombreMarquesParType.getOrDefault(Chocolat.C_HQ, 1);
			}
			if (cm.getChocolat()==Chocolat.C_HQ_E) {
				stock=12000/ nombreMarquesParType.getOrDefault(Chocolat.C_HQ_E, 1);
			}
			if (cm.getChocolat()==Chocolat.C_HQ_BE)  {
				stock=12000/ nombreMarquesParType.getOrDefault(Chocolat.C_HQ_BE, 1);
			}
			this.stock_Choco.put(cm, stock);
			this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN,cm+"->"+this.stock_Choco.get(cm));
			this.totalStockChoco.ajouter(this, stock, cryptogramme);
		}
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ8";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	public void next() {
		this.journal.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		this.journal.ajouter("Stock == " + Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal.ajouter("=================================");
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(209, 179, 221); 
	}

	public String getDescription() {
		return "Fidéliser une clientèle avec un commerce durable et équitable dans le haut de gamme";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		return res;
	}

	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	protected double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}
		
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) {
			if (stock_Choco.containsKey(p)) {
				return stock_Choco.get(p);
			} 
		}
		return 0.0;
	}
	
	public List<ChocolatDeMarque> getChocolatsProduits() {	
		if (this.chocoProduits.size()==0) {
			Chocolat C_MQ_E = Chocolat.C_MQ_E;
			int pourcentageCacao1 =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+C_MQ_E.getGamme()).getValeur());
			this.chocoProduits.add(new ChocolatDeMarque(C_MQ_E, "Chocoflow", pourcentageCacao1));
			
			Chocolat C_HQ_E = Chocolat.C_HQ_E;
			int pourcentageCacao2 =  (int) (Filiere.LA_FILIERE.getParametre("pourcentage min cacao "+C_HQ_E.getGamme()).getValeur());
			this.chocoProduits.add(new ChocolatDeMarque(C_HQ_E, "Chocoflow", pourcentageCacao2));
		} 
		return this.chocoProduits;
	}
	
	public List<String> getMarquesChocolat() {
		LinkedList<String> choc = new LinkedList<String>();
		choc.add("Chocoflow");
		return choc;
	}  
		
}
