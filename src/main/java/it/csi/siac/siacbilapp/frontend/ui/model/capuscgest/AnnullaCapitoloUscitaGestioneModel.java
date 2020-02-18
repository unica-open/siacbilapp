/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capuscgest;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;

/**
 * Classe di model per il Capitolo di Uscita Gestione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Annullamento del Capitolo di Uscita Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
public class AnnullaCapitoloUscitaGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8876347062227588097L;
	
	private CapitoloUscitaGestione capitoloUscitaGestione;
	
	/** Costruttore vuoto di default */
	public AnnullaCapitoloUscitaGestioneModel() {
		super();
		setTitolo("Annullamento Capitolo Spesa Gestione");
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
	 * Ottiene l'uid del capitolo da annullare.
	 * 
	 * @return l'uid del Capitolo di Uscita Gestione da annullare
	 */
	public int getUidDaAnnullare() {
		return capitoloUscitaGestione == null ? 0 : capitoloUscitaGestione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Uscita Gestione da annullare.
	 * 
	 * @param uidDaAnnullare l'uid da impostare
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		if(capitoloUscitaGestione == null) {
			capitoloUscitaGestione = new CapitoloUscitaGestione();
		}
		capitoloUscitaGestione.setUid(uidDaAnnullare);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public VerificaAnnullabilitaCapitoloUscitaGestione creaRequestVerificaAnnullabilitaCapitoloUscitaGestione() {
		VerificaAnnullabilitaCapitoloUscitaGestione request = creaRequest(VerificaAnnullabilitaCapitoloUscitaGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloUscitaGestione);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public AnnullaCapitoloUscitaGestione creaRequestAnnullaCapitoloUscitaGestione() {
		AnnullaCapitoloUscitaGestione request = creaRequest(AnnullaCapitoloUscitaGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloUscitaGest(capitoloUscitaGestione);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloUscitaGestione} a partire dal Model.
	 * 
	 * @return 					la Request creata
	 */
	public RicercaDettaglioCapitoloUscitaGestione creaRequestRicercaDettaglioCapitoloUscitaGestione() {
		RicercaDettaglioCapitoloUscitaGestione request = creaRequest(RicercaDettaglioCapitoloUscitaGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloUGest(getRicercaDettaglioCapitoloUGest(capitoloUscitaGestione.getUid()));
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
