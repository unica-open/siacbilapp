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
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

/**
 * Wrapper per la modifica al movimento di gestione nella registrazioneMovFin.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione della modifica
 *
 */
public abstract class ElementoModificaMovimentoGestioneRegistrazioneMovFin<M extends ModificaMovimentoGestione> implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8860557695968726631L;
	
	/** La modifica al movimento di gestione */
	protected M modificaMovimentoGestione;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param modificaMovimentoGestione il modificaMovimentoGestione da wrappare
	 */
	public ElementoModificaMovimentoGestioneRegistrazioneMovFin(M modificaMovimentoGestione) {
		this.modificaMovimentoGestione = modificaMovimentoGestione;
	}
	
	/**
	 * @return the domStringModifiche
	 */
	public String getDomStringModifiche() {
		if(modificaMovimentoGestione == null) {
			return "";
		}
		return modificaMovimentoGestione.getNumeroModificaMovimentoGestione() + "";
	}
	
	/**
	 * @return the domStringTipo
	 */
	public String getDomStringTipo() {
		if(modificaMovimentoGestione == null) {
			return "";
		}
		return modificaMovimentoGestione.getTipoModificaMovimentoGestione();
	}
	
	/**
	 * @return the domStringStato
	 */
	public String getDomStringStato() {
		if(modificaMovimentoGestione == null || modificaMovimentoGestione.getStatoOperativoModificaMovimentoGestione() == null) {
			return null;
		}
		return modificaMovimentoGestione.getStatoOperativoModificaMovimentoGestione().toString();
	}
	
	/**
	 * @return the domStringDescrizione
	 */
	public abstract String getDomStringDescrizione();
	
	/**
	 * @return the domStringProvvedimento
	 */
	public String getDomStringProvvedimento() {
		if(modificaMovimentoGestione == null || modificaMovimentoGestione.getAttoAmministrativo() == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		chunks.add(modificaMovimentoGestione.getAttoAmministrativo().getAnno() + "/" + modificaMovimentoGestione.getAttoAmministrativo().getNumero());
		if(modificaMovimentoGestione.getAttoAmministrativo().getTipoAtto() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getAttoAmministrativo().getTipoAtto().getDescrizione())) {
			chunks.add("<a rel=\"popover\" href=\"#\" data-original-title=\"Oggetto\" data-trigger=\"hover\" data-content=\"" +
					FormatUtils.formatHtmlAttributeString(modificaMovimentoGestione.getAttoAmministrativo().getOggetto()) + "\">"
					+ modificaMovimentoGestione.getAttoAmministrativo().getTipoAtto().getDescrizione() + "</a>");
		}
		if(modificaMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile() != null
				&& StringUtils.isNotBlank(modificaMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione())) {
			chunks.add(modificaMovimentoGestione.getAttoAmministrativo().getStrutturaAmmContabile().getDescrizione());
		}
		if(StringUtils.isNotBlank(modificaMovimentoGestione.getAttoAmministrativo().getStatoOperativo())) {
			chunks.add(modificaMovimentoGestione.getAttoAmministrativo().getStatoOperativo());
		}
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the domStringModificaDiSoggetto
	 */
	public String getDomStringModificaDiSoggetto() {
		if(modificaMovimentoGestione == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		if(modificaMovimentoGestione.getSoggettoNewMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getCodiceSoggetto())) {
			chunks.add(modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getCodiceSoggetto());
		}
		if(modificaMovimentoGestione.getSoggettoOldMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getSoggettoOldMovimentoGestione().getCodiceSoggetto())) {
			chunks.add(modificaMovimentoGestione.getSoggettoOldMovimentoGestione().getCodiceSoggetto());
		}
		if(modificaMovimentoGestione.getClasseSoggettoNewMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getClasseSoggettoNewMovimentoGestione().getCodice())) {
			chunks.add(modificaMovimentoGestione.getClasseSoggettoNewMovimentoGestione().getCodice());
		}
		if(modificaMovimentoGestione.getClasseSoggettoOldMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getClasseSoggettoOldMovimentoGestione().getCodice())) {
			chunks.add(modificaMovimentoGestione.getClasseSoggettoOldMovimentoGestione().getCodice());
		}
		if(modificaMovimentoGestione.getSoggettoNewMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getCodiceFiscale())) {
			chunks.add("CF: " + modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getCodiceFiscale());
		}
		if(modificaMovimentoGestione.getSoggettoNewMovimentoGestione() != null && StringUtils.isNotBlank(modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getPartitaIva())) {
			chunks.add("P.IVA: <a rel=\"popover\" href=\"#\" data-original-title=\"Denominazione\" data-trigger=\"hover\" data-content=\""
					+ FormatUtils.formatHtmlAttributeString(modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getDenominazione()) + "\">"
					+ modificaMovimentoGestione.getSoggettoNewMovimentoGestione().getPartitaIva() + "</a>");
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * @return the domStringModificaDiImporto
	 */
	public String getDomStringModificaDiImporto() {
		if(modificaMovimentoGestione == null) {
			return "";
		}
		// importoNew
		return FormatUtils.formatCurrency(modificaMovimentoGestione.getImportoOld());
	}
	
	@Override
	public int getUid() {
		return modificaMovimentoGestione != null ? modificaMovimentoGestione.getUid() : 0;
	}

}
