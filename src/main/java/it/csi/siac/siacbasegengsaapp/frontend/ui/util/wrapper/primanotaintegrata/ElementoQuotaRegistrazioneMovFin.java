/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Elemento delle scritture corrispondenti alla riga rappresentante la singola registrazione e movimentoEP per la nota integrata
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 28/04/2015
 * @author Marchino Alessandro
 * @version 1.0.1 - 15/05/2015
 * 
 * @param <D> la tipizzazione del documento wrappato
 * @param <S> la tipizzazione del subdocumento wrappato
 *
 */
public class ElementoQuotaRegistrazioneMovFin<D extends Documento<S, ?>, S extends Subdocumento<D, ?>> implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2460090770289172367L;
	
	private final S subdocumento;
	private final D documento;
	
	private RegistrazioneMovFin registrazioneMovFin;
	private MovimentoEP movimentoEP;
	
	private BigDecimal imponibileDaImpostare = BigDecimal.ZERO;
	private BigDecimal impostaDaImpostare = BigDecimal.ZERO;
	
	private boolean datiIvaSuDocumento;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param subdocumento        il subdocumento da impostare
	 * @param registrazioneMovFin la registrazione da impostare
	 */
	public ElementoQuotaRegistrazioneMovFin(S subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
		this.subdocumento = subdocumento;
		this.documento = subdocumento.getDocumento() != null ? subdocumento.getDocumento() : null;
		this.imponibileDaImpostare = subdocumento.getImportoNotNull();
		this.impostaDaImpostare = subdocumento.getImportoNotNull();
	}
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param notaCredito         la nota di credito
	 * @param subdocumento        il subdocumento da impostare
	 * @param registrazioneMovFin la registrazione da impostare
	 */
	public ElementoQuotaRegistrazioneMovFin(D notaCredito, S subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		this.registrazioneMovFin = registrazioneMovFin;
		this.subdocumento = subdocumento;
		this.documento = subdocumento.getDocumento() != null ? subdocumento.getDocumento() : null;
		this.imponibileDaImpostare = subdocumento.getImportoDaDedurreNotNull();
		this.impostaDaImpostare = subdocumento.getImportoDaDedurreNotNull();
	}
	
	/**
	 * @return the subdocumento
	 */
	public S getSubdocumento() {
		return subdocumento;
	}
	
	/**
	 * @return the documento
	 */
	public D getDocumento() {
		return documento;
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
	 * @return the imponibileDaImpostare
	 */
	public BigDecimal getImponibileDaImpostare() {
		return imponibileDaImpostare;
	}

	/**
	 * @param imponibileDaImpostare the imponibileDaImpostare to set
	 */
	public void setImponibileDaImpostare(BigDecimal imponibileDaImpostare) {
		this.imponibileDaImpostare = imponibileDaImpostare != null ? imponibileDaImpostare : BigDecimal.ZERO;
	}

	/**
	 * @return the impostaDaImpostare
	 */
	public BigDecimal getImpostaDaImpostare() {
		return impostaDaImpostare;
	}

	/**
	 * @param impostaDaImpostare the impostaDaImpostare to set
	 */
	public void setImpostaDaImpostare(BigDecimal impostaDaImpostare) {
		this.impostaDaImpostare = impostaDaImpostare != null ? impostaDaImpostare : BigDecimal.ZERO;
	}

	/**
	 * @return the datiIvaSuDocumento
	 */
	public boolean isDatiIvaSuDocumento() {
		return datiIvaSuDocumento;
	}

	/**
	 * @param datiIvaSuDocumento the datiIvaSuDocumento to set
	 */
	public void setDatiIvaSuDocumento(boolean datiIvaSuDocumento) {
		this.datiIvaSuDocumento = datiIvaSuDocumento;
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
	
	/**
	 * @return the rilevanteIva
	 */
	public boolean isRilevanteIva() {
		return subdocumento != null && Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA());
	}
	
	@Override
	public int getUid() {
		// Uid del subdocumento
		return subdocumento != null ? subdocumento.getUid() : 0;
	}
	
	/**
	 * @return <code>true</code> se i dati del movimento sono popolati; <code>false</code> altrimenti
	 */
	public boolean hasDatiMovimento() {
		return computeAndObtainMovimentoEP() != null && computeAndObtainMovimentoEP().getCausaleEP() != null && computeAndObtainMovimentoEP().getCausaleEP().getUid() != 0;
	}
}
