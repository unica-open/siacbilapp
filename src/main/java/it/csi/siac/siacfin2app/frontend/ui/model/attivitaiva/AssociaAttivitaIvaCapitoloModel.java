/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * Classe di model per l'associazione tra Attivita Iva e Capitolo
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 28/05/2014
 *
 */
public class AssociaAttivitaIvaCapitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6650237586906898693L;
	
	private Capitolo<?, ?> capitolo;
	private AttivitaIva attivitaIva;
	private String tipoCapitolo;
	private StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio;
	
	private List<StatoOperativoElementoDiBilancio> listaStatoOperativoElementoDiBilancio = new ArrayList<StatoOperativoElementoDiBilancio>();
	private List<AttivitaIva> listaAttivitaIva = new ArrayList<AttivitaIva>();
	
	/** Costruttore vuoto di default */
	public AssociaAttivitaIvaCapitoloModel() {
		setTitolo("Associa attività iva al capitolo");
	}

	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the attivitaIva
	 */
	public AttivitaIva getAttivitaIva() {
		return attivitaIva;
	}

	/**
	 * @param attivitaIva the attivitaIva to set
	 */
	public void setAttivitaIva(AttivitaIva attivitaIva) {
		this.attivitaIva = attivitaIva;
	}

	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the statoOperativoElementoDiBilancio
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoElementoDiBilancio() {
		return statoOperativoElementoDiBilancio;
	}

	/**
	 * @param statoOperativoElementoDiBilancio the statoOperativoElementoDiBilancio to set
	 */
	public void setStatoOperativoElementoDiBilancio(
			StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio) {
		this.statoOperativoElementoDiBilancio = statoOperativoElementoDiBilancio;
	}

	/**
	 * @return the listaStatoOperativoElementoDiBilancio
	 */
	public List<StatoOperativoElementoDiBilancio> getListaStatoOperativoElementoDiBilancio() {
		return listaStatoOperativoElementoDiBilancio;
	}

	/**
	 * @param listaStatoOperativoElementoDiBilancio the listaStatoOperativoElementoDiBilancio to set
	 */
	public void setListaStatoOperativoElementoDiBilancio(List<StatoOperativoElementoDiBilancio> listaStatoOperativoElementoDiBilancio) {
		this.listaStatoOperativoElementoDiBilancio = listaStatoOperativoElementoDiBilancio != null ? listaStatoOperativoElementoDiBilancio : new ArrayList<StatoOperativoElementoDiBilancio>();
	}

	/**
	 * @return the listaAttivitaIva
	 */
	public List<AttivitaIva> getListaAttivitaIva() {
		return listaAttivitaIva;
	}

	/**
	 * @param listaAttivitaIva the listaAttivitaIva to set
	 */
	public void setListaAttivitaIva(List<AttivitaIva> listaAttivitaIva) {
		this.listaAttivitaIva = listaAttivitaIva != null ? listaAttivitaIva : new ArrayList<AttivitaIva>();
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaRelazioneAttivitaIvaCapitolo}.
	 * 
	 * @return la request creata
	 */
	public RicercaRelazioneAttivitaIvaCapitolo creaRequestRicercaRelazioneAttivitaIvaCapitolo() {
		RicercaRelazioneAttivitaIvaCapitolo request = new RicercaRelazioneAttivitaIvaCapitolo();
		
		request.setBilancio(getBilancio());
		request.setCapitolo(getCapitolo());
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceRelazioneAttivitaIvaCapitolo}.
	 * 
	 * @return la request creata
	 */
	public InserisceRelazioneAttivitaIvaCapitolo creaRequestInserisceRelazioneAttivitaIvaCapitolo() {
		InserisceRelazioneAttivitaIvaCapitolo request = new InserisceRelazioneAttivitaIvaCapitolo();
		
		request.setAttivitaIva(getAttivitaIva());
		request.setCapitolo(getCapitolo());
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		// Imposto l'ente
		request.getAttivitaIva().setEnte(getEnte());
		request.getCapitolo().setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaRelazioneAttivitaIvaCapitolo}.
	 * 
	 * @return la request creata
	 */
	public EliminaRelazioneAttivitaIvaCapitolo creaRequestEliminaRelazioneAttivitaIvaCapitolo() {
		EliminaRelazioneAttivitaIvaCapitolo request = new EliminaRelazioneAttivitaIvaCapitolo();
		
		request.setAttivitaIva(getAttivitaIva());
		request.setCapitolo(getCapitolo());
		request.setDataOra(now());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
}
