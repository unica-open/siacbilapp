/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRateoRiscontoHelper;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RateoRisconto;



/**
 * Classe base di consultazione per ratei e risconti
 * 
 * @author Valentina
 * @version 1.0.0 - 25/07/2016
 */
public abstract class ConsultaRegistrazioneMovFinRateoRiscontoBaseModel extends ConsultaRegistrazioneMovFinBaseModel<RateoRisconto, ConsultaRegistrazioneMovFinRateoRiscontoHelper> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8982199394438590313L;
	
	private Integer uidPrimaNota;
	private Integer uidRisconto;

	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}

	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}
	
	/**
	 * @return the uidRisconto
	 */
	public Integer getUidRisconto() {
		return uidRisconto;
	}

	/**
	 * @param uidRisconto the uidRisconto to set
	 */
	public void setUidRisconto(Integer uidRisconto) {
		this.uidRisconto = uidRisconto;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "RateoRisconto";
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota(){
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		PrimaNota pn = new PrimaNota();
		pn.setUid(uidPrimaNota.intValue());
		request.setPrimaNota(pn);
		request.setRichiedente(getRichiedente());
		return request;
	}
	
	@Override
	public String getStato() {
		return "";
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		return consultazioneHelper.getDatiCreazioneModifica();
	}
	
	@Override
	public String getIntestazione() {
		StringBuilder sb = new StringBuilder();
		sb.append(consultazioneHelper.getTipoRateoRisconto())
		.append(" ")
		.append(consultazioneHelper.getRateoRisconto().getAnno())
		.append(" per ")
		.append(consultazioneHelper.getTipoMovimentoRateoRisconto())
		.append("  ")
		.append(consultazioneHelper.getAnnoMovimentoRateoRisconto())
		.append("/")
		.append(consultazioneHelper.getNumeroMovimentoRateoRisconto());
		
		if(StringUtils.isNotEmpty(consultazioneHelper.getNumeroSubMovimentoRateoRisconto())){
			sb.append('-')
			.append(consultazioneHelper.getNumeroSubMovimentoRateoRisconto());
		}
		
		
		return sb.toString();
	}
	
}
