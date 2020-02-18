/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * Elemento delle scritture per lo step 2 della PrimaNotaLibera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 *
 */
public class ElementoScritturaPrimaNotaLibera implements Serializable {
	
	/** per serializzazione **/
	private static final long serialVersionUID = 694978775745308328L;
	private MovimentoDettaglio movimentoDettaglio;
	private ContoTipoOperazione contoTipoOperazione;
	private boolean isAggiornamentoImportoManuale;
	
	/**
	 * @param movimentoDettaglio            il movimento di dettaglio
	 * @param contoTipoOperazione           il conto
	 * @param isAggiornamentoImportoManuale se l'aggiornamento dell'importo sia manuale
	 */
	public ElementoScritturaPrimaNotaLibera(MovimentoDettaglio movimentoDettaglio, ContoTipoOperazione contoTipoOperazione, boolean isAggiornamentoImportoManuale) {
		super();
		this.movimentoDettaglio = movimentoDettaglio;
		this.contoTipoOperazione = contoTipoOperazione;
		this.isAggiornamentoImportoManuale = isAggiornamentoImportoManuale;
	}
	/**
	 * @return the movimentoDettaglio
	 */
	public MovimentoDettaglio getMovimentoDettaglio() {
		return movimentoDettaglio;
	}
	/**
	 * @param movimentoDettaglio the movimentoDettaglio to set
	 */
	public void setMovimentoDettaglio(MovimentoDettaglio movimentoDettaglio) {
		this.movimentoDettaglio = movimentoDettaglio;
	}
	/**
	 * @return the contoTipoOperazione
	 */
	public ContoTipoOperazione getContoTipoOperazione() {
		return contoTipoOperazione;
	}
	/**
	 * @param contoTipoOperazione the contoTipoOperazione to set
	 */
	public void setContoTipoOperazione(ContoTipoOperazione contoTipoOperazione) {
		this.contoTipoOperazione = contoTipoOperazione;
	}
	/**
	 * @return the isAggiornamentoImportoManuale
	 */
	public boolean isAggiornamentoImportoManuale() {
		return isAggiornamentoImportoManuale;
	}
	/**
	 * @param isAggiornamentoImportoManuale the isAggiornamentoImportoManuale to set
	 */
	public void setAggiornamentoImportoManuale(boolean isAggiornamentoImportoManuale) {
		this.isAggiornamentoImportoManuale = isAggiornamentoImportoManuale;
	}
	
	// Getter di utilita'
	
	/**
	 * @return the codiceConto
	 */
	public String getCodiceConto() {
		return getMovimentoDettaglio() != null && getMovimentoDettaglio().getConto() != null ? getMovimentoDettaglio().getConto().getCodice() : "";
	}
	
	/**
	 * @return the descrizioneConto
	 */
	public String getDescrizioneConto() {
		return getMovimentoDettaglio() != null && getMovimentoDettaglio().getConto() != null ? getMovimentoDettaglio().getConto().getDescrizione() : "";
	}
	
	/**
	 * @return the dare
	 */
	public String getDare() {
		return getMovimentoDettaglio() != null
			&& getMovimentoDettaglio().getImporto() != null
			&& OperazioneSegnoConto.DARE.equals(getMovimentoDettaglio().getSegno())
				? FormatUtils.formatCurrency(getMovimentoDettaglio().getImporto())
				: "";
	}
	
	/**
	 * @return the avere
	 */
	public String getAvere() {
		return getMovimentoDettaglio() != null
			&& getMovimentoDettaglio().getImporto() != null
			&& OperazioneSegnoConto.AVERE.equals(getMovimentoDettaglio().getSegno())
				? FormatUtils.formatCurrency(getMovimentoDettaglio().getImporto())
				: "";
	}
	
	/**
	 * @return the segnoDare
	 */
	public boolean isSegnoDare() {
		return getMovimentoDettaglio() != null && OperazioneSegnoConto.DARE.equals(getMovimentoDettaglio().getSegno());
	}
	
	/**
	 * @return the segnoAvere
	 */
	public boolean isSegnoAvere() {
		return getMovimentoDettaglio() != null && OperazioneSegnoConto.AVERE.equals(getMovimentoDettaglio().getSegno());
	}
	
	/**
	 * @return the domStringMissione
	 */
	public String getDomStringMissione() {
		if(getMovimentoDettaglio() == null || getMovimentoDettaglio().getMissione() == null) {
			return "";
		}
		return getMovimentoDettaglio().getMissione().getCodice() + " - " + getMovimentoDettaglio().getMissione().getDescrizione();
	}
	
	/**
	 * @return the domStringProgramma
	 */
	public String getDomStringProgramma() {
		if(getMovimentoDettaglio() == null || getMovimentoDettaglio().getProgramma() == null) {
			return "";
		}
		return getMovimentoDettaglio().getProgramma().getCodice() + " - " + getMovimentoDettaglio().getProgramma().getDescrizione();
	}
}
