/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.progetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaProgetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;

/**
 * Classe di model per la ricerca del Progetto. Contiene una mappatura del form di ricerca.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2013
 */
public class RicercaProgettoModel extends GenericProgettoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5594106911780880133L;
		
	// Per la ricerca di dettaglio
	private Integer uidProgetto;
	
	//Stato Operativo
	private List<StatoOperativoProgetto> listaStatoProgetto = new ArrayList<StatoOperativoProgetto>();
	
	private String flagFondoPluriennaleVincolato;
    
	private String flagInvestimentoInCorsoDiDefinizione;
	private ElementoProgetto elementoProgetto;
	
	/** Costruttore vuoto di default */
	public RicercaProgettoModel() {
		super();
		setTitolo("Ricerca Progetto");
	}

	/**
	 * @return the uidProgetto
	 */
	public Integer getUidProgetto() {
		return uidProgetto;
	}

	/**
	 * @param uidProgetto the uidProgetto to set
	 */
	public void setUidProgetto(Integer uidProgetto) {
		this.uidProgetto = uidProgetto;
	}

	/**
	 * @return the listaStatoProgetto
	 */
	public List<StatoOperativoProgetto> getListaStatoProgetto() {
		return listaStatoProgetto;
	}

	/**
	 * @param listaStato the listaStato to set
	 */
	public void setListaStatoProgetto(List<StatoOperativoProgetto> listaStato) {
		this.listaStatoProgetto = listaStato;
	}

	/**
	 * @return the flagFondoPluriennaleVincolato
	 */
	public String getFlagFondoPluriennaleVincolato() {
		return flagFondoPluriennaleVincolato;
	}

	/**
	 * @param flagFondoPluriennaleVincolato the flagFondoPluriennaleVincolato to set
	 */
	public void setFlagFondoPluriennaleVincolato(
			String flagFondoPluriennaleVincolato) {
		this.flagFondoPluriennaleVincolato = flagFondoPluriennaleVincolato;
	}

	/**
	 * @return the elementoProgetto
	 */
	public ElementoProgetto getElementoProgetto() {
		return elementoProgetto;
	}

	
	/**
	 * @return the flagInvestimentoInCorsoDiDefinizione
	 */
	public String getFlagInvestimentoInCorsoDiDefinizione() {
		return flagInvestimentoInCorsoDiDefinizione;
	}

	/**
	 * @param flagInvestimentoInCorsoDiDefinizione the flagInvestimentoInCorsoDiDefinizione to set
	 */
	public void setFlagInvestimentoInCorsoDiDefinizione(
			String flagInvestimentoInCorsoDiDefinizione) {
		this.flagInvestimentoInCorsoDiDefinizione = flagInvestimentoInCorsoDiDefinizione;
	}

	/**
	 * @param elementoProgetto the elementoProgetto to set
	 */
	public void setElementoProgetto(
			ElementoProgetto elementoProgetto) {
		this.elementoProgetto = elementoProgetto;
	}
	
	@Override
	public String getStringaProvvedimento(){
		return "provvedimento";
	}
	
	/* Requests */
	/**
	 * Crea una request per il servizio di Ricerca Sintetica Progetto.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaProgetto creaRequestRicercaSinteticaProgetto() {
		RicercaSinteticaProgetto request = creaRequest(RicercaSinteticaProgetto.class);
		
		getProgetto().setEnte(getEnte());
		injettaFondoPluriennaleVincolatoNelProgetto();
		injettaFlagInvestimentoInCorsoDiDefinizione();

		injettaAttoAmministrativoNelProgetto();
		request.setProgetto(getProgetto());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	/**
	 * inietta il flag Investimento InCorso Di DEFINIZIONE 
	 */
	private void injettaFlagInvestimentoInCorsoDiDefinizione() {
		if(flagInvestimentoInCorsoDiDefinizione == null || flagInvestimentoInCorsoDiDefinizione.isEmpty()) {
			return;
		}
		
		Boolean investimentoInCorsoDiDefinizione = "S".equalsIgnoreCase(flagInvestimentoInCorsoDiDefinizione);
		getProgetto().setInvestimentoInCorsoDiDefinizione(investimentoInCorsoDiDefinizione);
	}

	/**
	 * Injetta Il fondo pluriennale vincolato.
	 */
	private void injettaFondoPluriennaleVincolatoNelProgetto() {
		if(flagFondoPluriennaleVincolato == null || flagFondoPluriennaleVincolato.isEmpty()) {
			return;
		}
		
		Boolean rilevanteFondoPluriennaleVincolato = "S".equalsIgnoreCase(flagFondoPluriennaleVincolato);
		getProgetto().setRilevanteFPV(rilevanteFondoPluriennaleVincolato);
	}
	
	/**
	 * Injetta l'atto amministrativo all'interno del progetto, nel caso non sia gi&agrave; presente, per la ricerca.
	 */
	private void injettaAttoAmministrativoNelProgetto() {
		AttoAmministrativo aa = getProgetto().getAttoAmministrativo();
		if((aa == null || aa.getUid() == 0) && (getUidProvvedimento() != null && getUidProvvedimento().intValue() != 0)) {
			// Non ho l'atto. Lo creo ex novo
			aa = new AttoAmministrativo();
			aa.setUid(getUidProvvedimento().intValue());
			getProgetto().setAttoAmministrativo(aa);
		}
	}

	/**
	 * Crea una request per il servizio di Ricerca di Dettaglio Progetto.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioProgetto creaRequestRicercaDettaglioProgetto() {
		RicercaDettaglioProgetto request = creaRequest(RicercaDettaglioProgetto.class);
		
		getProgetto().setEnte(getEnte());
		request.setChiaveProgetto(uidProgetto);
		
		return request;
	}
	
	
}
