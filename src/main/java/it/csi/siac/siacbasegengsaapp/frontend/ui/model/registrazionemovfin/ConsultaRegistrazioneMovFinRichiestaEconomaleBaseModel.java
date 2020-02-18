/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRichiestaEconomaleHelper;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * Classe base di consultazione per la richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
public abstract class ConsultaRegistrazioneMovFinRichiestaEconomaleBaseModel extends ConsultaRegistrazioneMovFinBaseModel<RichiestaEconomale, ConsultaRegistrazioneMovFinRichiestaEconomaleHelper> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8982199394438590313L;
	
	private Integer uidRichiestaEconomale;
	
	/**
	 * @return the uidRichiestaEconomale
	 */
	public Integer getUidRichiestaEconomale() {
		return uidRichiestaEconomale;
	}

	/**
	 * @param uidRichiestaEconomale the uidRichiestaEconomale to set
	 */
	public void setUidRichiestaEconomale(Integer uidRichiestaEconomale) {
		this.uidRichiestaEconomale = uidRichiestaEconomale;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "RichiestaEconomale";
	}

	@Override
	public String getIntestazione() {
		if(consultazioneHelper.getRichiestaEconomale() == null || consultazioneHelper.getRichiestaEconomale().getMovimento() == null) {
			return "";
		}
		
		return new StringBuilder()
			.append("Richiesta n. ")
			.append(consultazioneHelper.getRichiestaEconomale().getNumeroRichiesta())
			.append(" - Movimento n. ")
			.append(consultazioneHelper.getRichiestaEconomale().getMovimento().getNumeroMovimento())
			.toString();
	}

	@Override
	public String getStato() {
		return "";
	}

	@Override
	public String getDatiCreazioneModifica() {
		return "";
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRichiestaEconomale creaRequestRicercaDettaglioRichiestaEconomale(){
		RicercaDettaglioRichiestaEconomale req = creaRequest(RicercaDettaglioRichiestaEconomale.class);

		RichiestaEconomale re = new RichiestaEconomale();
		re.setUid(getUidRichiestaEconomale().intValue());
		req.setRichiestaEconomale(re);
		
		return req;
	
	}
}
