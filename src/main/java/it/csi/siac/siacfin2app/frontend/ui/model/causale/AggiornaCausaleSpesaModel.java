/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Classe di model per l'aggiornamento del Causale di Spesa
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 29/04/2014
 *
 */
public class AggiornaCausaleSpesaModel extends GenericCausaleSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5481575444709997686L;
	
	private Integer uidCausaleDaAggiornare;
	private String causaleStatoDataCalcolato;
	
	/** Costruttore vuoto di default */
	public AggiornaCausaleSpesaModel() {
		setTitolo("Aggiornamento Causale di Pagamento");
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
	 * Crea una request per il servizio di {@link AggiornaCausaleSpesa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaCausaleSpesa creaRequestAggiornaCausaleSpesa() {
		AggiornaCausaleSpesa request = new AggiornaCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleSpesa causale = getCausale();
		
		causale.setEnte(getEnte());
		causale.setTipoCausale(getTipoCausale());
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		causale.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		causale.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
	
		request.setCausaleSpesa(causale);
		
		return request;
	}
	
	@Override
	public RicercaDettaglioCausaleSpesa creaRequestRicercaDettaglioCausaleSpesa() {
		RicercaDettaglioCausaleSpesa request = new RicercaDettaglioCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		CausaleSpesa causale = new CausaleSpesa();
		causale.setUid(getUidCausaleDaAggiornare());
		request.setCausaleSpesa(causale);
		
		return request;
	}


	/**
	 * Imposta la stringa da visualizzare in fase di aggiornamento: 
	 * metter&agrave; la data creazione se la causale &eacute; valida;
	 * quella di annullamento se la causale &eacute; annullata
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
