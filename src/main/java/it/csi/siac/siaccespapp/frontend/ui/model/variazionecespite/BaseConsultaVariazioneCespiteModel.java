/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;

/**
 * Classe base per la consultazione della varaizione del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/08/2018
 *
 */
public abstract class BaseConsultaVariazioneCespiteModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1601344853397503097L;
	
	private VariazioneCespite variazioneCespite;

	/**
	 * @return the variazioneCespite
	 */
	public VariazioneCespite getVariazioneCespite() {
		return this.variazioneCespite;
	}

	/**
	 * @param variazioneCespite the variazioneCespite to set
	 */
	public void setVariazioneCespite(VariazioneCespite variazioneCespite) {
		this.variazioneCespite = variazioneCespite;
	}
	
	/**
	 * @return the statoVariazioneCespite
	 */
	public String getStatoVariazioneCespite() {
		if(variazioneCespite == null || variazioneCespite.getStatoVariazioneCespite() == null) {
			return "";
		}
		return variazioneCespite.getStatoVariazioneCespite().getCodice() + " - " + variazioneCespite.getStatoVariazioneCespite().getDescrizione();
	}
	
	/**
	 * @return the tipoBeneCespite
	 */
	public String getTipoBeneCespite() {
		if(variazioneCespite == null || variazioneCespite.getCespite() == null || variazioneCespite.getCespite().getTipoBeneCespite() == null) {
			return "";
		}
		return variazioneCespite.getCespite().getTipoBeneCespite().getCodice() + " - " + variazioneCespite.getCespite().getTipoBeneCespite().getDescrizione();
	}

	/**
	 * @return the classificazioneGiudiziariaCespite
	 */
	public String getClassificazioneGiudiziariaCespite() {
		if(variazioneCespite == null || variazioneCespite.getCespite() == null || variazioneCespite.getCespite().getClassificazioneGiuridicaCespite() == null) {
			return "";
		}
		ClassificazioneGiuridicaCespite cl = variazioneCespite.getCespite().getClassificazioneGiuridicaCespite();
		 return StringUtils.defaultString(cl.getCodice(), "") + "-" + StringUtils.defaultString(cl.getDescrizione(), "");
	}
	
	/**
	 * @return the statoVariazioneCespite
	 */
	public String getStatoCespite() {
		if(variazioneCespite == null || variazioneCespite.getCespite() == null) {
			return "";
		}
		return FormatUtils.formatBoolean(variazioneCespite.getCespite().getFlagStatoBene(), "Attivo", "Non attivo", "");
	}
	
	/**
	 * @return the tipoVariazione
	 */
	public abstract String getTipoVariazione();
	/**
	 * @return the formTitle
	 */
	public abstract String getFormTitle();
	/**
	 * @return the boxTitle
	 */
	public abstract String getBoxTitle();
	
	// Requests
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioVariazioneCespite}.
	 * @return la request creata
	 */
	public RicercaDettaglioVariazioneCespite creaRequestRicercaDettaglioVariazioneCespite() {
		RicercaDettaglioVariazioneCespite req = creaRequest(RicercaDettaglioVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite,
				VariazioneCespiteModelDetail.CespiteModelDetail,
				CespiteModelDetail.ClassificazioneGiuridicaCespite,
				CespiteModelDetail.TipoBeneCespiteModelDetail);
		
		return req;
	}

}
