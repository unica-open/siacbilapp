/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotaintegrata;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * Classe di model per la ricerca della prima nota integrata. Modulo GSA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 */
public class RisultatiRicercaPrimaNotaIntegrataGSAModel extends RisultatiRicercaPrimaNotaIntegrataBaseModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8785495670182558416L;
	
	private ClassificatoreGSA classificatoreGSA;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataGSAModel() {
		setTitolo("Risultati ricerca prima nota integrata");
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
	public String getBaseUrl() {
		return "risultatiRicercaPrimaNotaIntegrataGSA";
	}
	
	@Override
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota req = super.creaRequestValidaPrimaNota();
		// SIAC-5336: aggiunto il classificatore GSA
		req.getPrimaNota().setClassificatoreGSA(impostaEntitaFacoltativa(getClassificatoreGSA()));
		return req;
	}
}
