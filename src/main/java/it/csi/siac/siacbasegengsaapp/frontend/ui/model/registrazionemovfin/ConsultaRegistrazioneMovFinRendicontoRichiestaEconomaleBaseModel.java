/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinRendicontoRichiestaHelper;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * Classe base di consultazione per la richiesta economale.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 12/10/2015
 */
public abstract class ConsultaRegistrazioneMovFinRendicontoRichiestaEconomaleBaseModel extends ConsultaRegistrazioneMovFinBaseModel<RendicontoRichiesta, ConsultaRegistrazioneMovFinRendicontoRichiestaHelper> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8982199394438590313L;
	
	// e' la richiesta economale del rendiconto viene popolata quando ricerco il dettaglio rendiconto
	private Integer uidRendicontoRichiestaEconomale;

	/**
	 * @return the uidRendicontoRichiestaEconomale
	 */
	public Integer getUidRendicontoRichiestaEconomale() {
		return uidRendicontoRichiestaEconomale;
	}

	/**
	 * @param uidRendicontoRichiestaEconomale the uidRendicontoRichiestaEconomale to set
	 */
	public void setUidRendicontoRichiestaEconomale(Integer uidRendicontoRichiestaEconomale) {
		this.uidRendicontoRichiestaEconomale = uidRendicontoRichiestaEconomale;
	}

	@Override
	public String getConsultazioneSubpath() {
		return "RendicontoRichiestaEconomale";
	}

	@Override
	public String getIntestazione() {
		if(consultazioneHelper.getRichiestaEconomale() == null || consultazioneHelper.getRendicontoRichiesta().getMovimento() == null) {
			return "";
		}
		
		return new StringBuilder()
			.append("Rendiconto Richiesta n. ")
			.append(consultazioneHelper.getRichiestaEconomale().getNumeroRichiesta())
			.append(" - Movimento n. ")
			.append(consultazioneHelper.getRendicontoRichiesta().getMovimento().getNumeroMovimento())
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
	 * Crea una request per il servizio di {@link RicercaDettaglioRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRendicontoRichiesta creaRequestRicercaDettaglioRendiconcontoRichiestaEconomale(){
		RicercaDettaglioRendicontoRichiesta req = creaRequest(RicercaDettaglioRendicontoRichiesta.class);
		RendicontoRichiesta rr = new RendicontoRichiesta();
		rr.setUid(getUidRendicontoRichiestaEconomale().intValue());
		req.setRendicontoRichiesta(rr);
		return req;
	
	}
}
