/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.util.Date;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeProgetto;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siacbilser.model.TipoProgetto;

/**
 * Classe di model per l'inserimento del Progetto.
 * 
 * @author Osorio Alessandra
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/02/2014
 * @version 1.0.1 - 22/05/2015 - gestione del wizard
 *
 */
public class InserisciProgettoModel extends InserisciCronoprogrammaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2350126791003286443L;
	
	private Integer idProgetto;
	private boolean fromCronoprogramma;
//	private String informazioniProvvedimento = "";
	private boolean abilitaModificaDescrizione = false;
	
	/** Costruttore vuoto di default */
	public InserisciProgettoModel() {
		super();
		setTitolo("Crea Progetto");
	}
	
	/**
	 * @return the idProgetto
	 */
	public Integer getIdProgetto() {
		return idProgetto;
	}

	/**
	 * @param idProgetto the idProgetto to set
	 */
	public void setIdProgetto(Integer idProgetto) {
		this.idProgetto = idProgetto;
	}
	
	/**
	 * @return the fromCronoprogramma
	 */
	public boolean isFromCronoprogramma() {
		return fromCronoprogramma;
	}

	/**
	 * @param fromCronoprogramma the fromCronoprogramma to set
	 */
	public void setFromCronoprogramma(boolean fromCronoprogramma) {
		this.fromCronoprogramma = fromCronoprogramma;
	}
	
	@Override
	public String getBaseUrlCronoprogramma() {
		return "inserimentoCronoprogramma";
	}

	/**
	 * @return the abilitaModificaDescrizione
	 */
	public boolean isAbilitaModificaDescrizione() {
		return abilitaModificaDescrizione;
	}

	/**
	 * @param abilitaModificaDescrizione the abilitaModificaDescrizione to set
	 */
	public void setAbilitaModificaDescrizione(boolean abilitaModificaDescrizione) {
		this.abilitaModificaDescrizione = abilitaModificaDescrizione;
	}
	
	/* Requests */
	

	/**
	 * Crea una request per il servizio di Ricerca puntuale del Progetto.
	 *
	 * @param tipoProgetto the tipo progetto
	 * @return la request creata
	 */
	
	
	 public RicercaPuntualeProgetto creaRequestRicercaPuntualeProgetto(TipoProgetto tipoProgetto) {

		 RicercaPuntualeProgetto request = new RicercaPuntualeProgetto();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setProgetto(impostaProgettoPerLaRicercaPuntuale(tipoProgetto));
		
		return request;
	}
	
	/* Metodi di utilita' */
	
	/**
	 * Imposta il progetto per la ricerca puntuale.
	 * @param tipoProgetto 
	 * 
	 * @return il progetto creato
	 */
	private Progetto impostaProgettoPerLaRicercaPuntuale(TipoProgetto tipoProgetto) {
		Progetto progettoDaImpostare = new Progetto();
		
		progettoDaImpostare.setCodice(getProgetto().getCodice());
		progettoDaImpostare.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		progettoDaImpostare.setTipoProgetto(tipoProgetto);
		progettoDaImpostare.setBilancio(getBilancio());
		progettoDaImpostare.setEnte(getEnte());
		
		return progettoDaImpostare;
	}

	
}
