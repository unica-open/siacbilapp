/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;

/**
 * The Class InserisciCespiteModel.
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/08/2018
 */
public abstract class BaseInserisciCespiteModel extends GenericCespiteModel {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 246692498940591399L;
	
	private String copiaNumeroInventario;

	/**
	 * @return the copiaNumeroInventario
	 */
	public String getCopiaNumeroInventario() {
		return this.copiaNumeroInventario;
	}

	/**
	 * @param copiaNumeroInventario the copiaNumeroInventario to set
	 */
	public void setCopiaNumeroInventario(String copiaNumeroInventario) {
		this.copiaNumeroInventario = copiaNumeroInventario;
	}
	
	/**
	 * Gets the uid tipo bene cespite inserito.
	 *
	 * @return the uid tipo bene cespite inserito
	 */
	public int getUidCespiteInserito() {
		return getCespite() != null ? getCespite().getUid() : 0; 
	}
	
	/**
	 * @return the flagDonazioneRinvenimento
	 */
	public abstract Boolean getFlagDonazioneRinvenimento();
	
	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the formTitle
	 */
	public abstract String getFormTitle();

	/**
	 * Crea request inserisci tipo bene cespite.
	 *
	 * @return the inserisci tipo bene cespite
	 */
	public InserisciCespite creaRequestInserisciCespite() {
		InserisciCespite req = creaRequest(InserisciCespite.class);
		getCespite().setFlagStatoBene(Boolean.TRUE);
		getCespite().setFlgDonazioneRinvenimento(getFlagDonazioneRinvenimento());
		req.setCespite(getCespite());
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCespitePerChiave}.
	 * @return la request creata
	 */
	public RicercaCespitePerChiave creaRequestRicercaCespitePerChiave() {
		RicercaCespitePerChiave req = creaRequest(RicercaCespitePerChiave.class);
		
		req.setCespite(new Cespite());
		req.getCespite().setNumeroInventario(getCopiaNumeroInventario());
		
		req.setModelDetails(CespiteModelDetail.ClassificazioneGiuridicaCespite, CespiteModelDetail.TipoBeneCespite);
		return req;
	}

}
