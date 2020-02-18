/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Elemento delle scritture corrispondenti alla riga rappresentante la singola registrazione e movimentoEP per la nota integrata
 * 
 * @author Elisa Chiari
 * @version 1.0.10 - 21/03/2017
 *
 */
public class ElementoQuotaPrimaNotaIntegrata implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2460090770289172367L;
	
	private Subdocumento<?,?> subdocumento;
	private RegistrazioneMovFin registrazioneMovFin;
	private MovimentoEP movimentoEP;
	
	
	/** Costruttore di wrap. */
	public ElementoQuotaPrimaNotaIntegrata() {
		// Costruttore vuoto
	}
	
	/**
	 * @return the subdocumento
	 * */
	public Subdocumento<?, ?> getSubdocumento() {
		return subdocumento;
	}

	/**
	 * @param subdocumento  the subdocumento to set
	 * */
	public void setSubdocumento(Subdocumento<?, ?> subdocumento) {
		this.subdocumento = subdocumento;
	}


	/**
	 * @return the registrazioneMovFin
	 */
	public RegistrazioneMovFin getRegistrazioneMovFin() {
		return registrazioneMovFin;
	}

	/**
	 * @param registrazioneMovFin the registrazioneMovFin to set
	 */
	public void setRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
	}

	/**
	 * @param movimentoEP the movimentoEP
	 */
	public void setMovimentoEP(MovimentoEP movimentoEP) {
		this.movimentoEP = movimentoEP;
	}
	
	
	/**
	 * @return the movimentoEP
	 */
	public MovimentoEP getMovimentoEP() {
		return movimentoEP;
	}

	/**
	 * Calcola il movimentoEP.
	 * 
	 * @return the movimentoEP
	 */
	private MovimentoEP computeAndObtainMovimentoEP() {
		if(movimentoEP == null) {
			movimentoEP = getRegistrazioneMovFin() != null && !getRegistrazioneMovFin().getListaMovimentiEP().isEmpty()
				? getRegistrazioneMovFin().getListaMovimentiEP().get(0)
				: null;
		}
		return movimentoEP;
	}


	/**
	 * @return the numeroQuotaString
	 */
	public String getNumeroQuotaString() {
		return subdocumento != null && subdocumento.getNumero() != null ? subdocumento.getNumero().toString() : "";
	}
	
	/**
	 * @return the causaleString
	 */
	public String getCausaleString() {
		StringBuilder sb = new StringBuilder();
		
		if(computeAndObtainMovimentoEP() != null && computeAndObtainMovimentoEP().getCausaleEP() != null) {
			sb.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(computeAndObtainMovimentoEP().getCausaleEP().getDescrizione()))
				.append("\" data-html=\"true\">")
				.append(computeAndObtainMovimentoEP().getCausaleEP().getCodice())
				.append("</a>");
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the statoRichiestaString
	 */
	public String getStatoRichiestaString() {
		return registrazioneMovFin != null && registrazioneMovFin.getStatoOperativoRegistrazioneMovFin() != null
			? registrazioneMovFin.getStatoOperativoRegistrazioneMovFin().getDescrizione()
			: "";
	}
	
	/**
	 * @return the dataRegistrazioneString
	 */
	public String getDataRegistrazioneString() {
		return computeAndObtainMovimentoEP() != null && computeAndObtainMovimentoEP().getDataCreazione() != null ? FormatUtils.formatDate(computeAndObtainMovimentoEP().getDataCreazioneMovimentoEP()) : "";
	}
	
	/**
	 * @return the contoFinanziarioString
	 */
	public String getContoFinanziarioString() {
		if(registrazioneMovFin == null || registrazioneMovFin.getElementoPianoDeiContiAggiornato() == null){
			return "";
		}
		if(registrazioneMovFin.getElementoPianoDeiContiIniziale() != null &&  registrazioneMovFin.getElementoPianoDeiContiIniziale().getUid() == registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid()){
			return "";
		}
		return registrazioneMovFin.getElementoPianoDeiContiAggiornato().getCodice();
	}
	
	/**
	 * @return the contoFinanziarioInizialeString
	 */
	public String getContoFinanziarioInizialeString() {
		return registrazioneMovFin != null && registrazioneMovFin.getElementoPianoDeiContiIniziale() != null ? registrazioneMovFin.getElementoPianoDeiContiIniziale().getCodice() : "";
	}
	
	/**
	 * @return the contoEconomicoPatrimonialeString
	 */
	public String getContoEconomicoPatrimonialeString() {
		return registrazioneMovFin != null && registrazioneMovFin.getConto() != null ? registrazioneMovFin.getConto().getCodice() : "";
	}
	
	@Override
	public int getUid() {
		// Uid del subdocumento
		return subdocumento != null ? subdocumento.getUid() : 0;
	}
	
}
