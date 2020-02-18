/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di model per la ricerca del dettaglio dei movimenti della prima nota integrata
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/05/2015
 *
 */
public class DettaglioMovimentiPrimaNotaIntegrataModel extends GenericBilancioModel {
	
	/** Per la serializzazione	 **/
	private static final long serialVersionUID = 7750526781111645201L;
	
	private PrimaNota primaNota;
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	/** Costruttore vuoto di default */
	public DettaglioMovimentiPrimaNotaIntegrataModel() {
		setTitolo("Risultati ricerca Prima Nota Integrata - Dettaglio movimenti");
	}

	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
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
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota() {
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		
		request.setPrimaNota(getPrimaNota());
		
		return request;
	}
}
