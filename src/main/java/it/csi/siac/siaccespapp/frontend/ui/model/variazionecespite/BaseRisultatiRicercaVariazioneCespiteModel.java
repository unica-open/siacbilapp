/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.variazionecespite;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;

/**
 * Classe di base per i risultati di ricerca della variazione del cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/08/2018
 *
 */
public abstract class BaseRisultatiRicercaVariazioneCespiteModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 7150320890446875447L;
	
	private VariazioneCespite variazioneCespite;
	
	private Cespite cespite;
	
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
	 * @return the cespite
	 */
	public Cespite getCespite() {
		return cespite;
	}

	/**
	 * @param cespite the cespite to set
	 */
	public void setCespite(Cespite cespite) {
		this.cespite = cespite;
	}

	/**
	 * @return the formTitle
	 */
	public abstract String getFormTitle();
	
	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the testoSelectTipoVariazione
	 */
	public abstract String getTestoSelectTipoVariazione();
	
	/**
	 * Ottiene il flag che indica se il tipo di variazione &eacute; di incremento
	 * @return il flag
	 */
	protected abstract Boolean getFlagTipoVariazioneIncremento();

	/**
	 * Crea request ricerca dettaglio variazione cespite.
	 *
	 * @return the ricerca dettaglio variazione cespite
	 */
	public RicercaDettaglioVariazioneCespite creaRequestRicercaDettaglioVariazioneCespite() {
		RicercaDettaglioVariazioneCespite req = creaRequest(RicercaDettaglioVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite, VariazioneCespiteModelDetail.CespiteModelDetail);
		return req;
	}

	/**
	 * Crea request aggiorna variazione cespite.
	 *
	 * @return the aggiorna variazione cespite
	 */
	public AggiornaVariazioneCespite creaRequestAggiornaVariazioneCespite() {
		AggiornaVariazioneCespite req = creaRequest(AggiornaVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		req.getVariazioneCespite().setFlagTipoVariazioneIncremento(getFlagTipoVariazioneIncremento());
		req.getVariazioneCespite().setCespite(getCespite());
		
		return req;
	}

	/**
	 * Crea request elimina variazione cespite.
	 *
	 * @return the elimina variazione cespite
	 */
	public EliminaVariazioneCespite creaRequestEliminaVariazioneCespite() {
		EliminaVariazioneCespite req = creaRequest(EliminaVariazioneCespite.class);
		
		req.setVariazioneCespite(getVariazioneCespite());
		return req;
	}

}
