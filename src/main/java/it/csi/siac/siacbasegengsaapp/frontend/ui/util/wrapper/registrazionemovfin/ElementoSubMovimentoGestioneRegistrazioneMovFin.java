/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * Wrapper per il submovimento di gestione nella registrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <S> la tipizzazione del submovimento
 *
 */
public abstract class ElementoSubMovimentoGestioneRegistrazioneMovFin<S extends MovimentoGestione> implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -83863903775939910L;
	/** Il submovimento di gestione */
	protected S subMovimentoGestione;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param subMovimentoGestione il modificaMovimentoGestione da wrappare
	 */
	public ElementoSubMovimentoGestioneRegistrazioneMovFin(S subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}
	
	/**
	 * @return the domStringSubMovimento
	 */
	public String getDomStringSubMovimento() {
		if(subMovimentoGestione == null || subMovimentoGestione.getNumeroBigDecimal() == null) {
			// Se il submovimento non e' presente esco
			return "";
		}
		// Numero e descrizione in popover
		return new StringBuilder()
			.append("<a href=\"#\" rel=\"popover\" data-original-title=\"Descrizione\" data-trigger=\"hover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(subMovimentoGestione.getDescrizione()))
			.append("\">")
			.append(subMovimentoGestione.getNumeroBigDecimal().toPlainString())
			.append("</a>")
			.toString();
	}
	
	/**
	 * @return the domStringStato
	 */
	public final String getDomStringStato() {
		if(subMovimentoGestione == null || StringUtils.isBlank(ottieniCodiceStato()) || StringUtils.isBlank(ottieniDescrizioneStato())) {
			// Se non ho codice e descrizione esco
			return "";
		}
		// Codice e descrizione in popover
		return new StringBuilder()
			.append("<a href=\"#\" rel=\"popover\" data-original-title=\"Descrizione\" data-trigger=\"hover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(ottieniDescrizioneStato()))
			.append("\">")
			.append(ottieniCodiceStato())
			.append("</a>")
			.toString();
	}
	
	/**
	 * Ottiene il codice dello stato del movimento di gestione.
	 * 
	 * @return the codiceStato
	 */
	protected abstract String ottieniCodiceStato();
	
	/**
	 * Ottiene la descrizione dello stato del movimento di gestione.
	 * 
	 * @return the descrizioneStato
	 */
	protected abstract String ottieniDescrizioneStato();
	
	/**
	 * @return the domStringSoggetto
	 */
	public String getDomStringSoggetto() {
		if(subMovimentoGestione == null || subMovimentoGestione.getSoggetto() == null) {
			// Se non ho il soggetto esco
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		if(StringUtils.isNotBlank(subMovimentoGestione.getSoggetto().getCodiceSoggetto())) {
			// Codice con denominazione nel popover
			chunks.add("<a href=\"#\" rel=\"popover\" data-original-title=\"Denominazione\" data-trigger=\"hover\" data-content=\""
					+ FormatUtils.formatHtmlAttributeString(subMovimentoGestione.getSoggetto().getDenominazione()) + "\">"
					+ subMovimentoGestione.getSoggetto().getCodiceSoggetto() + "</a>");
		}
		if(StringUtils.isNotBlank(subMovimentoGestione.getSoggetto().getCodiceFiscale())) {
			// Coice fiscale
			chunks.add("CF: " + subMovimentoGestione.getSoggetto().getCodiceFiscale());
		}
		if(StringUtils.isNotBlank(subMovimentoGestione.getSoggetto().getPartitaIva())) {
			// Partita iva
			chunks.add("P.IVA: " + subMovimentoGestione.getSoggetto().getPartitaIva());
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the domStringProvvedimento
	 */
	public String getDomStringProvvedimento() {
		if(subMovimentoGestione == null || subMovimentoGestione.getAttoAmministrativo() == null) {
			// Se non ho il provvedimento esco
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		// Anno + numero
		chunks.add(subMovimentoGestione.getAttoAmministrativo().getAnno() + " / " + subMovimentoGestione.getAttoAmministrativo().getNumero());
		if(subMovimentoGestione.getAttoAmministrativo().getTipoAtto() != null && StringUtils.isNotBlank(subMovimentoGestione.getAttoAmministrativo().getTipoAtto().getDescrizione())) {
			// Tipo atto con oggetto dell'atto amministrativo come popover
			chunks.add("<a href=\"#\" rel=\"popover\" data-original-title=\"Oggetto\" data-trigger=\"hover\" data-content=\""
					+ FormatUtils.formatHtmlAttributeString(subMovimentoGestione.getAttoAmministrativo().getOggetto()) + "\">"
					+ subMovimentoGestione.getAttoAmministrativo().getTipoAtto().getDescrizione() + "</a>");
		}
		if(subMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile() != null && StringUtils.isNotBlank(subMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione())) {
			// Descrizione della SAC
			chunks.add(subMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione());
		}
		if(StringUtils.isNotBlank(subMovimentoGestione.getAttoAmministrativo().getStatoOperativo())) {
			// Stato
			chunks.add("Stato: " + subMovimentoGestione.getAttoAmministrativo().getStatoOperativo());
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the domStringScadenza
	 */
	public String getDomStringScadenza() {
		if(subMovimentoGestione == null) {
			// Se non ho la data di scadenza, esco
			return "";
		}
		// Data formattata secondo lo standard italiano
		return FormatUtils.formatDate(subMovimentoGestione.getDataScadenza());
	}
	
	/**
	 * @return the domStringImporto
	 */
	public String getDomStringImporto() {
		if(subMovimentoGestione == null) {
			// Se non ho l'importo esco
			return "";
		}
		// L'importo formattato secondo lo standard italiano
		return FormatUtils.formatCurrency(subMovimentoGestione.getImportoAttuale());
	}
	
	@Override
	public int getUid() {
		return subMovimentoGestione != null ? subMovimentoGestione.getUid() : 0;
	}

}
