package abstraction.eq7Transformateur4;

import java.util.List;

import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

public class Transformation extends Transformateur4VendeurAuxEncheres{
	
	//objectifs : vérifier le stocks de fève : s'il est suffisant pour une fève, produire du chocolat avec, puis attribuer aux chocolat une marque ou nom.
	protected Journal journalTransfo ;

	public Transformation() {
		super();
		this.journalTransfo = new Journal(this.getNom()+" journal transfo", this);
			}
	
	public void next() {
		super.next();
		double stock_hg_be = this.stockFeves.get(Feve.F_HQ_BE);
		//là on va aussi demandé les autres stocks
		double peutproduireemploye = this.tauxproductionemploye*this.nbemployeCDI; //pour l'instant ça c'est 375, mais ça pourra évoluer si on change le nb d'employé
		//là faudras s'adapter, pour utiliser qu'une partie de la main d'oeuvre pour faire tel ou tel chocolat, pour l'instant on fait qu'un seul chocolat
		double qtutile1 ; //correspond à la qte de fève qu'on va effectivement transformer
		//là faudras mettre les autres qte de fève utilisées
		if (stock_hg_be > 0) {
			if (stock_hg_be > peutproduireemploye) {
				//on a assez en stock, on produit un maximum
				qtutile1 = peutproduireemploye;
			} else {


				//on retire tout notre stock de feve pour faire du chocolat
				qtutile1 = stock_hg_be;

			}
			this.stockFeves.replace(Feve.F_HQ_BE, stock_hg_be - qtutile1);//on retire qtutile1 du stock de feve haut de gamme pour faire du chocolat
			double qtechocoproduit = qtutile1*this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE); //la qte de choco produit à partir de qtutile1
			//ajouter le stocks à cocoasis
			double payermachine = qtutile1*this.coutmachine; //prix des machines car on transforme une certaine qté de fèves
			double pourcentageadjuvant = this.pourcentageTransfo.get(Feve.F_HQ_BE).get(Chocolat.C_HQ_BE)-1;
			double payeradjuvant = this.coutadjuvant*pourcentageadjuvant*qtutile1;
			
			this.journalTransfo.ajouter("Stock avant trnasfo " + this.totalStocksChocoMarque.getValeur(this.cryptogramme) );
			totalStocksChocoMarque.ajouter(this, qtutile1, cryptogramme);
			this.journalTransfo.ajouter("Stock après trnasfo " + this.totalStocksChocoMarque.getValeur(this.cryptogramme) );
			
			this.journalTransfo.ajouter("On à transformé " + qtutile1 + "T de Choco");
		} //else : on fait rien car on peut pas produire
		//là on paye les trucs : généraux (cout fixe des machines, employés qu'on doit payer dans tous les cas) et les cout qu'on vient de calculer(payermachine, payeradjuvant)
		
	
	}
	
		public List<Journal> getJournaux() {
			List<Journal> res=super.getJournaux();
			res.add(journalTransfo);
			return res;
		}
}
