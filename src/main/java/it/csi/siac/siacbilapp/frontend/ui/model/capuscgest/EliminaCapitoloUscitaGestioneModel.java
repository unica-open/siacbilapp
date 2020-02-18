/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;

/**
 * Classe di model per il Capitolo di Uscita Gestione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Eliminazione del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/07/2013
 *
 */
public class EliminaCapitoloUscitaGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3358205394247743816L;
	
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	/** Costruttore vuoto di default */
	public EliminaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Eliminazione Capitolo Spesa Previsione");
	}

	/* Getter e setter */
	
	/**
	 * @return the capitoloUscitaGestione
	 */
	public CapitoloUscitaGestione getCapitoloUscitaGestione() {
		return capitoloUscitaGestione;
	}

	/**
	 * @param capitoloUscitaGestione the capitoloUscitaGestione to set
	 */
	public void setCapitoloUscitaGestione(CapitoloUscitaGestione capitoloUscitaGestione) {
		this.capitoloUscitaGestione = capitoloUscitaGestione;
	}
	
	/**
	 * Ottiene l'uid del capitolo da eliminare.
	 * 
	 * @return l'uid del Capitolo di Uscita Gestione da eliminare
	 */
	public int getUidDaEliminare() {
		return capitoloUscitaGestione == null ? 0 : capitoloUscitaGestione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Uscita Gestione da eliminare.
	 * 
	 * @param uidDaEliminare l'uid da impostare
	 */
	public void setUidDaEliminare(int uidDaEliminare) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setUid(uidDaEliminare);
	}
	
	/* Requests */
		
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione() {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(capitoloUscitaGestione.getUid()));
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link EliminaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public EliminaCapitoloUscitaGestione creaRequestEliminaCapitoloUscitaGestione() {
		EliminaCapitoloUscitaGestione request = creaRequest(EliminaCapitoloUscitaGestione.class);
		
		request.setBilancio(getBilancio());
		request.setCapitoloUscitaGest(capitoloUscitaGestione);
		request.setEnte(getEnte());
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Uscita Gestione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return 				 l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloUGest utility = new RicercaDettaglioCapitoloUGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
}
