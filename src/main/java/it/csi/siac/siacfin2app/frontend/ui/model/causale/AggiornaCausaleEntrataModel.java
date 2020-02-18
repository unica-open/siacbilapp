/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Classe di model per l'aggiornamento del Causale di Entrata
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 29/04/2014
 *
 */
public class AggiornaCausaleEntrataModel extends GenericCausaleEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5481575444709997686L;
	
	private Integer uidCausaleDaAggiornare;
	
	private String causaleStatoDataCalcolato;
	
	/** Costruttore vuoto di default */
	public AggiornaCausaleEntrataModel() {
		setTitolo("Aggiornamento Causale di Incasso");
	}

	/**
	 * @return the uidCausaleDaAggiornare
	 */
	public Integer getUidCausaleDaAggiornare() {
		return uidCausaleDaAggiornare;
	}

	/**
	 * @param uidCausaleDaAggiornare the uidCausaleDaAggiornare to set
	 */
	public void setUidCausaleDaAggiornare(Integer uidCausaleDaAggiornare) {
		this.uidCausaleDaAggiornare = uidCausaleDaAggiornare;
	}
	
	/**
	 * @return the causaleStatoDataCalcolato
	 */
	public String getCausaleStatoDataCalcolato() {
		return causaleStatoDataCalcolato;
	}

	/**
	 * @param causaleStatoDataCalcolato the causaleStatoDataCalcolato to set
	 */
	public void setCausaleStatoDataCalcolato(String causaleStatoDataCalcolato) {
		this.causaleStatoDataCalcolato = causaleStatoDataCalcolato;
	}

	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di {@link AggiornaCausaleEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaCausaleEntrata creaRequestAggiornaCausaleEntrata() {
		AggiornaCausaleEntrata request = new AggiornaCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleEntrata causale = getCausale();
		
		causale.setEnte(getEnte());
		causale.setTipoCausale(getTipoCausale());
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		causale.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
	
		request.setCausaleEntrata(causale);
		
		return request;
	}
	
	@Override
	public RicercaDettaglioCausaleEntrata creaRequestRicercaDettaglioCausaleEntrata() {
		RicercaDettaglioCausaleEntrata request = new RicercaDettaglioCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleEntrata causale = new CausaleEntrata();
		causale.setUid(getUidCausaleDaAggiornare());
		request.setCausaleEntrata(causale);
		
		return request;
	}


	/**
	 * Imposta la stringa da visualizzare in fase di aggiornamento: 
	 * metter&agrave; la data creazione se la causale &eacute; valida; quella di annullamento se la causale &eacute; annullata
	 * 
	 * @return la stringa con il valore calcolato
	 */
	public String calcolaCausaleStatoDataCalcolato() {
		return "Stato: " + getCausale().getStatoOperativoCausale() + " - dal: " +
				(StatoOperativoCausale.ANNULLATA.equals(getCausale().getStatoOperativoCausale()) ?
					FormatUtils.formatDate(getCausale().getDataScadenza()) :
					FormatUtils.formatDate(getCausale().getDataCreazioneCausale()));
	}
	
}
