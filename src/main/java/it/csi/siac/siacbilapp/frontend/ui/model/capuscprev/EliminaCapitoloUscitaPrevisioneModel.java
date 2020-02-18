/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscprev;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;

/**
 * Classe di model per il Capitolo di Uscita Previsione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Eliminazione del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/07/2013
 *
 */
public class EliminaCapitoloUscitaPrevisioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3358205394247743816L;
	
	private CapitoloUscitaPrevisione capitoloUscitaPrevisione;
	
	/** Costruttore vuoto di default */
	public EliminaCapitoloUscitaPrevisioneModel() {
		super();
		setTitolo("Eliminazione Capitolo Spesa Previsione");
	}

	/* Getter e setter */
	
	/**
	 * @return the capitoloUscitaPrevisione
	 */
	public CapitoloUscitaPrevisione getCapitoloUscitaPrevisione() {
		return capitoloUscitaPrevisione;
	}

	/**
	 * @param capitoloUscitaPrevisione the capitoloUscitaPrevisione to set
	 */
	public void setCapitoloUscitaPrevisione(CapitoloUscitaPrevisione capitoloUscitaPrevisione) {
		this.capitoloUscitaPrevisione = capitoloUscitaPrevisione;
	}
	
	/**
	 * Ottiene l'uid del capitolo da eliminare.
	 * 
	 * @return l'uid del Capitolo di Uscita Previsione da eliminare
	 */
	public int getUidDaEliminare() {
		return capitoloUscitaPrevisione == null ? 0 : capitoloUscitaPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Uscita Previsione da eliminare.
	 * 
	 * @param uidDaEliminare l'uid da impostare
	 */
	public void setUidDaEliminare(int uidDaEliminare) {
		if(capitoloUscitaPrevisione == null) {
			capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		}
		capitoloUscitaPrevisione.setUid(uidDaEliminare);
	}
	
	/* Requests */
		
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUPrev(getRicercaDettaglioCapitoloUPrev(capitoloUscitaPrevisione.getUid()));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloUscitaPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public EliminaCapitoloUscitaPrevisione creaRequestEliminaCapitoloUscitaPrevisione() {
		EliminaCapitoloUscitaPrevisione request = creaRequest(EliminaCapitoloUscitaPrevisione.class);
		
		request.setBilancio(getBilancio());
		request.setCapitoloUscitaPrev(capitoloUscitaPrevisione);
		request.setEnte(getEnte());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Previsione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return 				 l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUPrev getRicercaDettaglioCapitoloUPrev(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUPrev utility = new RicercaDettaglioCapitoloUPrev();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
}
