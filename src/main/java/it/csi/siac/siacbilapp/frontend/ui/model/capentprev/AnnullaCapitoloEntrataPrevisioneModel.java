/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.capentprev;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;

/**
 * Classe di model per il Capitolo di Entrata Previsione. Contiene una mappatura dei campi necessar&icirc; all'utilizzo
 * del servizio di Annullamento del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 15/07/2013
 *
 */
public class AnnullaCapitoloEntrataPrevisioneModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7269031157125465123L;
	
	private CapitoloEntrataPrevisione capitoloEntrataPrevisione;
	
	/** Costruttore vuoto di default */
	public AnnullaCapitoloEntrataPrevisioneModel() {
		super();
		setTitolo("Annullamento Capitolo Entrata Previsione");
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
	 * Ottiene l'uid del capitolo da annullare.
	 * 
	 * @return l'uid del Capitolo di Entrata Previsione da annullare
	 */
	public int getUidDaAnnullare() {
		return capitoloEntrataPrevisione == null ? 0 : capitoloEntrataPrevisione.getUid();
	}
	
	/**
	 * Imposta l'uid nel Capitolo di Entrata Previsione da annullare.
	 * 
	 * @param uidDaAnnullare l'uid da impostare
	 */
	public void setUidDaAnnullare(int uidDaAnnullare) {
		if(capitoloEntrataPrevisione == null) {
			capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		}
		capitoloEntrataPrevisione.setUid(uidDaAnnullare);
	}

	/* Requests */
	
	/**
	 * Restituisce una Request di tipo {@link VerificaAnnullabilitaCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public VerificaAnnullabilitaCapitoloEntrataPrevisione creaRequestVerificaAnnullabilitaCapitoloEntrataPrevisione() {
		VerificaAnnullabilitaCapitoloEntrataPrevisione request = creaRequest(VerificaAnnullabilitaCapitoloEntrataPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitolo(capitoloEntrataPrevisione);
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	/**
	 * Restituisce una Request di tipo {@link AnnullaCapitoloEntrataPrevisione} a partire dal Model.
	 * 
	 * @return la Request creata
	 */
	public AnnullaCapitoloEntrataPrevisione creaRequestAnnullaCapitoloEntrataPrevisione() {
		AnnullaCapitoloEntrataPrevisione request = creaRequest(AnnullaCapitoloEntrataPrevisione.class);
		request.setBilancio(getBilancio());
		request.setCapitoloEntrataPrev(capitoloEntrataPrevisione);
		request.setEnte(getEnte());
		return request;
	}
	
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
