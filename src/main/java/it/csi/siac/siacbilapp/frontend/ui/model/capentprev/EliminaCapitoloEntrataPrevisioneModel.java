/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;

/**
 * Classe di model per il Capitolo di Entrata Previsione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Eliminazione del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/07/2013
 *
 */
public class EliminaCapitoloEntrataPrevisioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3358205394247743816L;
	
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	
	/** Costruttore vuoto di default */
	public EliminaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Eliminazione Capitolo Entrata Previsione");
	}

	/* Getter e setter */
	
	/**
	 * @return the capitoloEntrataPrevisione
	 */
	public CapitoloEntrataPrevisione getCapitoloEntrataPrevisione() {
		return capitoloEntrataPrevisione;
	}

	/**
	 * @param capitoloEntrataPrevisione the capitoloEntrataPrevisione to set
	 */
	public void setCapitoloEntrataPrevisione(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		this.capitoloEntrataPrevisione = capitoloEntrataPrevisione;
	}
	
	/**
	 * Ottiene l'uid del capitolo da eliminare.
	 * 
	 * @return l'uid del Capitolo di Entrata Previsione da eliminare
	 */
	public int getUidDaEliminare() {
		return capitoloEntrataPrevisione == null ? 0 : capitoloEntrataPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Entrata Previsione da eliminare.
	 * 
	 * @param uidDaEliminare l'uid da impostare
	 */
	public void setUidDaEliminare(int uidDaEliminare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setUid(uidDaEliminare);
	}
	
	/* Requests */
		
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataPrevisione creaRequestRicercaDettaglioCapitoloEntrataPrevisione() {
		RicercaDettaglioCapitoloEntrataPrevisione request = creaRequest(RicercaDettaglioCapitoloEntrataPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEPrev(getRicercaDettaglioCapitoloEPrev(capitoloEntrataPrevisione.getUid()));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public EliminaCapitoloEntrataPrevisione creaRequestEliminaCapitoloEntrataPrevisione() {
		EliminaCapitoloEntrataPrevisione request = creaRequest(EliminaCapitoloEntrataPrevisione.class);
		
		request.setBilancio(getBilancio());
		request.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		request.setEnte(getEnte());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Previsione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return 				 l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEPrev getRicercaDettaglioCapitoloEPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEPrev utility = new RicercaDettaglioCapitoloEPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
}
