/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinLiquidazioneHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;

/**
 * Classe base di model per la consultazione della liquidazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 *
 */
public abstract class ConsultaRegistrazioneMovFinLiquidazioneBaseModel extends ConsultaRegistrazioneMovFinTransazioneElementareBaseModel<Liquidazione, ConsultaRegistrazioneMovFinLiquidazioneHelper>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1815861948767673812L;
	
	private Liquidazione liquidazione;
	private BigDecimal numero;
	private Integer anno;
	
	/**
	 * @return the liquidazione
	 */
	public Liquidazione getLiquidazione() {
		return liquidazione;
	}

	/**
	 * @param liquidazione the liquidazione to set
	 */
	public void setLiquidazione(Liquidazione liquidazione) {
		this.liquidazione = liquidazione;
	}

	/**
	 * @return the numero
	 */
	public BigDecimal getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	
	@Override
	public String getConsultazioneSubpath() {
		return "Liquidazione";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaLiquidazionePerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaLiquidazionePerChiave creaRequestRicercaLiquidazionePerChiave() {
		RicercaLiquidazionePerChiave req = new RicercaLiquidazionePerChiave();
		RicercaLiquidazioneK pRicercaLiquidazioneK = new RicercaLiquidazioneK();
		pRicercaLiquidazioneK.setAnnoLiquidazione(getAnno());
		pRicercaLiquidazioneK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaLiquidazioneK.setNumeroLiquidazione(getNumero());
		pRicercaLiquidazioneK.setBilancio(getBilancio());
		Liquidazione liquidazioneRequest = new Liquidazione();
		liquidazioneRequest.setNumeroLiquidazione(getNumero());
		liquidazioneRequest.setAnnoLiquidazione(getAnno());
		pRicercaLiquidazioneK.setLiquidazione(liquidazioneRequest );
		req.setpRicercaLiquidazioneK(pRicercaLiquidazioneK );
		req.setRichiedente(getRichiedente());
		req.setDataOra(new Date());
		req.setEnte(getEnte());
		return req;
	}

	@Override
	public String getIntestazione() {
		if(getLiquidazione() == null || getLiquidazione().getNumeroLiquidazione() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Liquidazione ")
			.append(getLiquidazione().getAnnoLiquidazione())
			.append(" / ")
			.append(getLiquidazione().getNumeroLiquidazione().toPlainString())
			.toString();
	}
	
	@Override
	public String getStato() {
		if(getLiquidazione() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato: ")
			.append(getLiquidazione().getStatoOperativoLiquidazione())
			.append(" dal ")
			.append(FormatUtils.formatDate(getLiquidazione().getDataStatoOperativoLiquidazione()))
			.toString();
	}
	
}
