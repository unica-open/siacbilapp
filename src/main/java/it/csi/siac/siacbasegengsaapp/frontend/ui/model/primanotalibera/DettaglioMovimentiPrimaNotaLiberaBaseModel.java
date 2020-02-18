/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota libera
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2015
 *
 */

public abstract class DettaglioMovimentiPrimaNotaLiberaBaseModel extends GenericBilancioModel{
	

	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = -6601435221789656756L;
	
	private PrimaNota primaNotaLibera;
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;

	/**
	 * @return the primaNotaLibera
	 */
	public PrimaNota getPrimaNotaLibera() {
		return primaNotaLibera;
	}

	/**
	 * @param primaNotaLibera the primaNotaLibera to set
	 */
	public void setPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
	}

	/**
	 * @return the listaMovimentoDettaglio
	 */
	public List<MovimentoDettaglio> getListaMovimentoDettaglio() {
		return listaMovimentoDettaglio;
	}

	/**
	 * @param listaMovimentoDettaglio the listaMovimentoDettaglio to set
	 */
	public void setListaMovimentoDettaglio(List<MovimentoDettaglio> listaMovimentoDettaglio) {
		this.listaMovimentoDettaglio = listaMovimentoDettaglio != null ? listaMovimentoDettaglio : new ArrayList<MovimentoDettaglio>();
	}

	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return totaleDare;
	}

	/**
	 * @param totaleDare the totaleDare to set
	 */
	public void setTotaleDare(BigDecimal totaleDare) {
		this.totaleDare = totaleDare != null ? totaleDare : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return totaleAvere;
	}

	/**
	 * @param totaleAvere the totaleAvere to set
	 */
	public void setTotaleAvere(BigDecimal totaleAvere) {
		this.totaleAvere = totaleAvere != null ? totaleAvere : BigDecimal.ZERO;
	}
	
	
	/**
	 * Ottiene l'ambito corrispondente: pu&oacute; essere AMBITO_FIN o AMBITO_GSA.
	 * 
	 * @return l'ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * Ottiene il suffisso dell'ambito corrispondente: pu&oacute; essere "FIN" o "GSA"
	 * 
	 * @return la stringa con il suffisso dell'ambito
	 */
	public abstract String getAmbitoSuffix();
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota() {
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		
		request.setPrimaNota(getPrimaNotaLibera());
		request.getPrimaNota().setAmbito(getAmbito());
		return request;
	}

}
