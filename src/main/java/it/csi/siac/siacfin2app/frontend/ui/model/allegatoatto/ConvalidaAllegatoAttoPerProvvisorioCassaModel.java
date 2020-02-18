/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

/**
 * Classe di modello per la convalida dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2014
 *
 */
public class ConvalidaAllegatoAttoPerProvvisorioCassaModel extends GenericAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8558153522774388998L;
	private ProvvisorioDiCassa provvisorioDiCassa;
	private BigDecimal totaleDocumentiCollegati = BigDecimal.ZERO;
	private Integer numeroDocumentiCollegati;
	private Boolean convalidaEffettuabile = Boolean.TRUE;
	private Boolean convalidaManuale = Boolean.FALSE;
	
	/** Costruttore vuoto di default */
	public ConvalidaAllegatoAttoPerProvvisorioCassaModel() {
		super();
		setTitolo("Convalida per provvisorio");
	}

	/**
	 * @return the provvisorioDiCassa
	 */
	public ProvvisorioDiCassa getProvvisorioDiCassa() {
		return provvisorioDiCassa;
	}

	/**
	 * @param provvisorioDiCassa the provvisorioDiCassa to set
	 */
	public void setProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa) {
		this.provvisorioDiCassa = provvisorioDiCassa;
	}

	/**
	 * @return the totaleDocumentiCollegati
	 */
	public BigDecimal getTotaleDocumentiCollegati() {
		return totaleDocumentiCollegati;
	}

	/**
	 * @param totaleDocumentiCollegati the totaleDocumentiCollegati to set
	 */
	public void setTotaleDocumentiCollegati(BigDecimal totaleDocumentiCollegati) {
		this.totaleDocumentiCollegati = totaleDocumentiCollegati != null ? totaleDocumentiCollegati : BigDecimal.ZERO;
	}

	/**
	 * @return the numeroDocumentiCollegati
	 */
	public Integer getNumeroDocumentiCollegati() {
		return numeroDocumentiCollegati;
	}

	/**
	 * @param numeroDocumentiCollegati the numeroDocumentiCollegati to set
	 */
	public void setNumeroDocumentiCollegati(Integer numeroDocumentiCollegati) {
		this.numeroDocumentiCollegati = numeroDocumentiCollegati;
	}

	/**
	 * @return the convalidaEffettuabile
	 */
	public Boolean getConvalidaEffettuabile() {
		return convalidaEffettuabile;
	}

	/**
	 * @param convalidaEffettuabile the convalidaEffettuabile to set
	 */
	public void setConvalidaEffettuabile(Boolean convalidaEffettuabile) {
		this.convalidaEffettuabile = convalidaEffettuabile;
	}

	/**
	 * @return the convalidaManuale
	 */
	public Boolean getConvalidaManuale() {
		return convalidaManuale;
	}

	/**
	 * @param convalidaManuale the convalidaManuale to set
	 */
	public void setConvalidaManuale(Boolean convalidaManuale) {
		this.convalidaManuale = convalidaManuale;
	}
	
	/**
	 * @return the descrizioneCompletaProvvisorioDiCassa
	 */
	public String getDescrizioneCompletaProvvisorioDiCassa() {
		StringBuilder sb = new StringBuilder();
		sb.append(getProvvisorioDiCassa().getTipoProvvisorioDiCassa().name())
			.append("-")
			.append(getProvvisorioDiCassa().getAnno())
			.append("/")
			.append(getProvvisorioDiCassa().getNumero())
			.append(" del ")
			.append(FormatUtils.formatDate(getProvvisorioDiCassa().getDataEmissione()));
		return sb.toString();
	}
	
	/**
	 * @return the provvisorioDiCassaDiEntrata
	 */
	public Boolean getProvvisorioDiCassaDiEntrata() {
		return TipoProvvisorioDiCassa.E.equals(getProvvisorioDiCassa().getTipoProvvisorioDiCassa());
	}
	
	/**
	 * @return the totaleDocumentiCollegatiSpesa
	 */
	public BigDecimal getTotaleDocumentiCollegatiSpesa() {
		return Boolean.FALSE.equals(getProvvisorioDiCassaDiEntrata()) ? getTotaleDocumentiCollegati() : BigDecimal.ZERO;
	}
	
	/**
	 * @return the totaleDocumentiCollegatiEntrata
	 */
	public BigDecimal getTotaleDocumentiCollegatiEntrata() {
		return Boolean.TRUE.equals(getProvvisorioDiCassaDiEntrata()) ? getTotaleDocumentiCollegati() : BigDecimal.ZERO;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestRicercaProvvisorioDiCassaPerChiave() {
		RicercaProvvisorioDiCassaPerChiave request = creaRequest(RicercaProvvisorioDiCassaPerChiave.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(getProvvisorioDiCassa().getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(getProvvisorioDiCassa().getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(getProvvisorioDiCassa().getTipoProvvisorioDiCassa());
		request.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ConvalidaAllegatoAttoPerProvvisorio}.
	 * 
	 * @return la request creata
	 */
	public ConvalidaAllegatoAttoPerProvvisorio creaRequestConvalidaAllegatoAttoPerProvvisorio() {
		ConvalidaAllegatoAttoPerProvvisorio request = creaRequest(ConvalidaAllegatoAttoPerProvvisorio.class);
		
		request.setProvvisorioDiCassa(getProvvisorioDiCassa());
		request.setFlagConvalidaManuale(getConvalidaManuale());
		request.setEnte(getEnte());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuotaSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotaSpesa creaRequestRicercaQuoteSpesa() {
		RicercaQuotaSpesa request = creaRequest(RicercaQuotaSpesa.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setAnnoProvvisorio(getProvvisorioDiCassa().getAnno());
		request.setNumeroProvvisorio(getProvvisorioDiCassa().getNumero());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuotaEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotaEntrata creaRequestRicercaQuoteEntrata() {
		RicercaQuotaEntrata request = creaRequest(RicercaQuotaEntrata.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setAnnoProvvisorio(getProvvisorioDiCassa().getAnno());
		request.setNumeroProvvisorio(getProvvisorioDiCassa().getNumero());
		
		return request;
	}
	
}
