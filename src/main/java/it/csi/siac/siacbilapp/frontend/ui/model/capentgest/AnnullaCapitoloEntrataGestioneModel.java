/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentgest;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;

/**
 * Classe di model per il Capitolo di Entrata Gestione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Annullamento del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
public class AnnullaCapitoloEntrataGestioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7478618940147302139L;
	
	private CapitoloEntrataGestione capitoloEntrataGestione;
	
	/** Costruttore vuoto di default */
	public AnnullaCapitoloEntrataGestioneModel() {
		super();
		setTitolo("Annullamento Capitolo Entrata Gestione");
	}

	/* Getter e setter */
	
	/**
	 * @return the capitoloEntrataGestione
	 */
	public CapitoloEntrataGestione getCapitoloEntrataGestione() {
		return capitoloEntrataGestione;
	}

	/**
	 * @param capitoloEntrataGestione the capitoloEntrataGestione to set
	 */
	public void setCapitoloEntrataGestione(CapitoloEntrataGestione capitoloEntrataGestione) {
		this.capitoloEntrataGestione = capitoloEntrataGestione;
	}
	
	/**
	 * Ottiene l'uid del capitolo da annullare.
	 * 
	 * @return l'uid del Capitolo di Entrata Gestione da annullare
	 */
	public int getUidDaAnnullare() {
		return capitoloEntrataGestione == null ? 0 : capitoloEntrataGestione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Entrata Gestione da annullare.
	 * 
	 * @param uidDaAnnullare l'uid da impostare
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		if(capitoloEntrataGestione == null) {
			capitoloEntrataGestione = new CapitoloEntrataGestione();
		}
		capitoloEntrataGestione.setUid(uidDaAnnullare);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public VerificaAnnullabilitaCapitoloEntrataGestione creaRequestVerificaAnnullabilitaCapitoloEntrataGestione() {
		VerificaAnnullabilitaCapitoloEntrataGestione request =creaRequest(VerificaAnnullabilitaCapitoloEntrataGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloEntrataGestione);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public AnnullaCapitoloEntrataGestione creaRequestAnnullaCapitoloEntrataGestione() {
		AnnullaCapitoloEntrataGestione request = creaRequest(AnnullaCapitoloEntrataGestione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloEntrataGest(capitoloEntrataGestione);
		request.setEnte(getEnte());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link RicercaDettaglioCapitoloEntrataGestione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public RicercaDettaglioCapitoloEntrataGestione creaRequestRicercaDettaglioCapitoloEntrataGestione() {
		RicercaDettaglioCapitoloEntrataGestione request = creaRequest(RicercaDettaglioCapitoloEntrataGestione.class);
		request.setEnte(getEnte());
		request.setRicercaDettaglioCapitoloEGest(getRicercaDettaglioCapitoloEGest(capitoloEntrataGestione.getUid()));
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Crea un'Utilit&agrave; per la Ricerca Dettaglio del Capitolo di Entrata Gestione.
	 * 
	 * @param chiaveCapitolo la chiave del Capitolo da ricercare
	 * @return 				 l'utilit&agrave; creata
	 */
	private RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest(int chiaveCapitolo) {
		RicercaDettaglioCapitoloEGest utility = new RicercaDettaglioCapitoloEGest();
		utility.setChiaveCapitolo(chiaveCapitolo);
		return utility;
	}
	
}
