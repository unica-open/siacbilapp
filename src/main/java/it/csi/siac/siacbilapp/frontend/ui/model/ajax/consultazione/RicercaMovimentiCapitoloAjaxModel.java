/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoVariazioneConsultazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;

/**
 * @author Marchino Alessandro
 * @version 1.0.0 - 17/nov/2014
 *
 */
public class RicercaMovimentiCapitoloAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2103574074671738548L;
	
	private Integer uidCapitolo;
	private ElementoVariazioneConsultazione elementoVariazioneConsultazione = new ElementoVariazioneConsultazione();
	
	// SIAC-3871
	private List<VincoloCapitoli> listaVincoloCapitoli = new ArrayList<VincoloCapitoli>();
	
	//SIAC-6177
	private String etichettaGiunta = "Giunta";
	private String etichettaConsiglio = "Consiglio";
	
	//SIAC-7735
	private boolean capitoloUscitaSaldoZero;
	
	/** Costruttore vuoto di default */
	public RicercaMovimentiCapitoloAjaxModel() {
		setTitolo("Ricerca movimenti capitolo");
	}

	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return uidCapitolo;
	}

	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(Integer uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}

	/**
	 * @return the elementoVariazioneConsultazione
	 */
	public ElementoVariazioneConsultazione getElementoVariazioneConsultazione() {
		return elementoVariazioneConsultazione;
	}

	/**
	 * @param elementoVariazioneConsultazione the elementoVariazioneConsultazione to set
	 */
	public void setElementoVariazioneConsultazione(ElementoVariazioneConsultazione elementoVariazioneConsultazione) {
		this.elementoVariazioneConsultazione = elementoVariazioneConsultazione;
	}
	
	/**
	 * @return the listaVincoloCapitoli
	 */
	public List<VincoloCapitoli> getListaVincoloCapitoli() {
		return listaVincoloCapitoli;
	}

	/**
	 * @param listaVincoloCapitoli the listaVincoloCapitoli to set
	 */
	public void setListaVincoloCapitoli(List<VincoloCapitoli> listaVincoloCapitoli) {
		this.listaVincoloCapitoli = listaVincoloCapitoli != null ? listaVincoloCapitoli : new ArrayList<VincoloCapitoli>();
	}
	
	/**
	 * @return the etichettaGiunta
	 */
	public String getEtichettaGiunta() {
		return etichettaGiunta;
	}

	/**
	 * @param etichettaGiunta the etichettaGiunta to set
	 */
	public void setEtichettaGiunta(String etichettaGiunta) {
		this.etichettaGiunta = etichettaGiunta;
	}

	/**
	 * @return the etichettaConsiglio
	 */
	public String getEtichettaConsiglio() {
		return etichettaConsiglio;
	}

	/**
	 * @param etichettaConsiglio the etichettaConsiglio to set
	 */
	public void setEtichettaConsiglio(String etichettaConsiglio) {
		this.etichettaConsiglio = etichettaConsiglio;
	}
	
	/* **** Utilities **** */
	
	/**
	 * Gets the descrizione giunta consiglio.
	 *
	 * @return the descrizione giunta consiglio
	 */
	public String getDescrizioneGiuntaConsiglio() {
		return new StringBuilder()
					.append(getEtichettaGiunta())
					.append(" o ")
					.append(getEtichettaConsiglio()).toString();
	}
	

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaMovimentiCapitoloUscitaPrevisione}.
	 * 
	 * @return la request creata
	 */
	public RicercaMovimentiCapitoloUscitaPrevisione creaRequestRicercaMovimentiCapitoloUscitaPrevisione() {
		RicercaMovimentiCapitoloUscitaPrevisione request = creaRequest(RicercaMovimentiCapitoloUscitaPrevisione.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		CapitoloUscitaPrevisione capitoloUscitaPrevisione = new CapitoloUscitaPrevisione();
		capitoloUscitaPrevisione.setUid(getUidCapitolo().intValue());
		request.setCapitoloUPrev(capitoloUscitaPrevisione);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaMovimentiCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaMovimentiCapitoloUscitaGestione creaRequestRicercaMovimentiCapitoloUscitaGestione() {
		RicercaMovimentiCapitoloUscitaGestione request = creaRequest(RicercaMovimentiCapitoloUscitaGestione.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(getUidCapitolo().intValue());
		request.setCapitoloUscitaGestione(capitoloUscitaGestione);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaMovimentiCapitoloEntrataPrevisione}.
	 * 
	 * @return la request creata
	 */
	public RicercaMovimentiCapitoloEntrataPrevisione creaRequestRicercaMovimentiCapitoloEntrataPrevisione() {
		RicercaMovimentiCapitoloEntrataPrevisione request = creaRequest(RicercaMovimentiCapitoloEntrataPrevisione.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		CapitoloEntrataPrevisione capitoloEntrataPrevisione = new CapitoloEntrataPrevisione();
		capitoloEntrataPrevisione.setUid(getUidCapitolo().intValue());
		request.setCapitoloEPrev(capitoloEntrataPrevisione);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaMovimentiCapitoloEntrataGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaMovimentiCapitoloEntrataGestione creaRequestRicercaMovimentiCapitoloEntrataGestione() {
		RicercaMovimentiCapitoloEntrataGestione request = creaRequest(RicercaMovimentiCapitoloEntrataGestione.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(getUidCapitolo().intValue());
		request.setCapitoloEntrataGestione(capitoloEntrataGestione);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaVincolo}.
	 * @param tipoCapitolo il tipo di capitolo
	 * @return la request creata
	 */
	public RicercaVincolo creaRequestRicercaVincolo(TipoCapitolo tipoCapitolo) {
		RicercaVincolo request = creaRequest(RicercaVincolo.class);
		
		Vincolo vincolo = new Vincolo();
		vincolo.setEnte(getEnte());
		
		request.setVincolo(vincolo);
		request.setBilancio(getBilancio());
		
		Capitolo<?, ?> capitolo = tipoCapitolo.newCapitoloInstance();
		capitolo.setUid(getUidCapitolo().intValue());
		request.setCapitolo(capitolo);
		
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaVariazioniCapitolo}.
	 * @return la request creata
	 */
	public RicercaVariazioniCapitolo creaRequestRicercaVariazioniCapitolo() {
		RicercaVariazioniCapitolo request = creaRequest(RicercaVariazioniCapitolo.class);
		
		// Inizializzo un capitolo qualsiasi
		CapitoloEntrataPrevisione capitolo = new CapitoloEntrataPrevisione();
		capitolo.setUid(getUidCapitolo().intValue());
		
		request.setBilancio(getBilancio());
		capitolo.setEnte(getEnte());
		
		request.setCapitolo(capitolo);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaStoricoVariazioniCodificheCapitolo}
	 * 
	 * @return la request creata
	 */
	public RicercaStoricoVariazioniCodificheCapitolo creaRequestRicercaStoricoVariazioniCodificheCapitolo() {
		RicercaStoricoVariazioniCodificheCapitolo request = creaRequest(RicercaStoricoVariazioniCodificheCapitolo.class);
		
		// Inizializzo un capitolo qualsiasi
		CapitoloEntrataPrevisione capitolo = new CapitoloEntrataPrevisione();
		capitolo.setUid(getUidCapitolo().intValue());
		capitolo.setEnte(getEnte());
		request.setCapitolo(capitolo);

		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}

	public boolean isCapitoloUscitaSaldoZero() {
		return capitoloUscitaSaldoZero;
	}

	public void setCapitoloUscitaSaldoZero(boolean capitoloUscitaSaldoZero) {
		this.capitoloUscitaSaldoZero = capitoloUscitaSaldoZero;
	}

}
