/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegratamanuale;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.RisultatiRicercaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * Classe di model per i risultati della ricerca della prima nota libera. Modulo GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public class RisultatiRicercaPrimaNotaIntegrataManualeGSAModel extends RisultatiRicercaPrimaNotaIntegrataManualeBaseModel {

	/** per la serializzazione */
	private static final long serialVersionUID = -2184713067384482017L;
	
	private ClassificatoreGSA classificatoreGSA;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataManualeGSAModel() {
		setTitolo("Risultati ricerca Prima Nota Integrata Manuale - GSA");
	}

	/**
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSA() {
		return this.classificatoreGSA;
	}

	/**
	 * @param classificatoreGSA the classificatoreGSA to set
	 */
	public void setClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_GSA;
	}
	
	@Override
	public String getAmbitoSuffix() {
		return "GSA";
	}

	@Override
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota req = super.creaRequestValidaPrimaNota();
		// SIAC-5336: aggiunto il classificatore GSA
		req.getPrimaNota().setClassificatoreGSA(impostaEntitaFacoltativa(getClassificatoreGSA()));
		return req;
	}
}
