/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Factory per Elemento delle scritture per lo step 2 della PrimaNotaLibera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public final class ElementoScritturaPrimaNotaLiberaFactory {
	/** Non instanziare la classe */
	private ElementoScritturaPrimaNotaLiberaFactory() {
	}
	
	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da causaleEP
	 * 
	 * @param causaleEP CausaleEp da cui ricavare i conti
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaLibera> creaListaScrittureDaCausaleEP(CausaleEP causaleEP) {
		List<ElementoScritturaPrimaNotaLibera> listaScritture = new ArrayList<ElementoScritturaPrimaNotaLibera>();
		List<ContoTipoOperazione> listaCTOp = causaleEP.getContiTipoOperazione();
		int numeroRiga =  0;
		for (ContoTipoOperazione cTOp : listaCTOp) {
			MovimentoDettaglio movDettaglio = new MovimentoDettaglio();
			
			if (cTOp.getOperazioneSegnoConto()!=null )  {
				movDettaglio.setNumeroRiga(Integer.valueOf(numeroRiga));
				movDettaglio.setConto(cTOp.getConto());
				movDettaglio.setSegno(cTOp.getOperazioneSegnoConto());
			}
			// nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
			listaScritture.add (new ElementoScritturaPrimaNotaLibera(movDettaglio, cTOp, false));
			numeroRiga++;
		}
		return listaScritture;
	}
	
	/**
	 * Creazione dell'elemento di scruitttura manuale
	 * 
	 * @param cTOp ContoTipoOperazione per la scrittura
	 * @param movDettaglio MovimentoDettaglio per la scrittura
	 * 
	 * 
	 * @return la scrittura
	 */
	public static ElementoScritturaPrimaNotaLibera creaElementoScritturaManuale(ContoTipoOperazione cTOp, MovimentoDettaglio movDettaglio) {
		return new ElementoScritturaPrimaNotaLibera(movDettaglio, cTOp, true);
	}

	/**
	 * Creazione della lista di scritture necessarie alla prima nota ricavate da primanotalibera se aggiornamento
	 * 
	 * @param primaNotaLibera primaNota da cui ricavare i conti
	 * @param contiCausale    boolean che indica se i conti sono da causale
	 * 
	 * @return la lista delle scritture
	 */
	public static List<ElementoScritturaPrimaNotaLibera> creaListaScrittureDaPrimaNota(PrimaNota primaNotaLibera, boolean contiCausale) {
		List<ElementoScritturaPrimaNotaLibera> listaScritture = new ArrayList<ElementoScritturaPrimaNotaLibera>();
		List<MovimentoEP> listaMovEp = primaNotaLibera.getListaMovimentiEP();
		if (listaMovEp != null) {
			for (MovimentoEP movEP : listaMovEp) {
				for (MovimentoDettaglio movDettaglio : movEP.getListaMovimentoDettaglio()) {
					if (movDettaglio != null) {
						ContoTipoOperazione cTOp = new ContoTipoOperazione();
						cTOp.setOperazioneSegnoConto(movDettaglio.getSegno());
						cTOp.setOperazioneUtilizzoImporto(OperazioneUtilizzoImporto.PROPOSTO);
						
						// Nella modalita aggiornamento metto false perche' al momento il valore e' preso da causale e non dal campo di comodo
						listaScritture.add (new ElementoScritturaPrimaNotaLibera(movDettaglio, cTOp, !contiCausale));
					
					}
				}
			}
		}
		return listaScritture;
	}
	

}
