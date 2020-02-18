/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * Classe di model per i risultati della ricerca della prima nota libera. Modulo GSA
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 15/10/2015
 *
 */
public class RisultatiRicercaPrimaNotaLiberaGSAModel extends RisultatiRicercaPrimaNotaLiberaBaseModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2184713067384482017L;

	private ClassificatoreGSA classificatoreGSA;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaGSAModel() {
		setTitolo("Risultati ricerca Prima Nota Libera");
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
