/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;

/**
 * Classe base di model per la ricerca della prima nota integrata per la validazione.
 * 
 * @author Marchino Alessandro
 */
public abstract class RisultatiRicercaValidazionePrimaNotaIntegrataBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1118917510201387933L;
	
	private Integer savedDisplayStart;
	private String riepilogoRicerca;
	
	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}
	
	/**
	 * @return the riepilogoRicerca
	 */
	public String getRiepilogoRicerca() {
		return riepilogoRicerca;
	}

	/**
	 * @param riepilogoRicerca the riepilogoRicerca to set
	 */
	public void setRiepilogoRicerca(String riepilogoRicerca) {
		this.riepilogoRicerca = riepilogoRicerca;
	}

	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link ValidazioneMassivaPrimaNotaIntegrata}.
	 * 
	 * @param ricercaSinteticaPrimaNotaIntegrataValidabile la request di ricerca sintetica per cui effettuare la validazione
	 * 
	 * @return la request creata
	 */
	public ValidazioneMassivaPrimaNotaIntegrata creaRequestValidazioneMassivaPrimaNotaIntegrata(RicercaSinteticaPrimaNotaIntegrataValidabile ricercaSinteticaPrimaNotaIntegrataValidabile) {
		ValidazioneMassivaPrimaNotaIntegrata request = creaRequest(ValidazioneMassivaPrimaNotaIntegrata.class);
		
		request.setRicercaSinteticaPrimaNotaIntegrataValidabile(ricercaSinteticaPrimaNotaIntegrataValidabile);
		
		return request;
	}
}
