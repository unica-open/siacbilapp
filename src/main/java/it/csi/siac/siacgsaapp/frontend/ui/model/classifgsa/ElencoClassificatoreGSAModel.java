/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacgsaapp.frontend.ui.model.classifgsa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValido;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;

/**
 * Classe di model per l'elenco dei classificatori GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
public class ElencoClassificatoreGSAModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4160293014212386902L;
	
	private List<ClassificatoreGSA> listaClassificatoreGSA = new ArrayList<ClassificatoreGSA>();

	/** Costruttore vuoto di default */
	public ElencoClassificatoreGSAModel() {
		setTitolo("Elenco Classificatore GSA");
	}

	/**
	 * @return the listaClassificatoreGSA
	 */
	public List<ClassificatoreGSA> getListaClassificatoreGSA() {
		return this.listaClassificatoreGSA;
	}

	/**
	 * @param listaClassificatoreGSA the listaClassificatoreGSA to set
	 */
	public void setListaClassificatoreGSA(List<ClassificatoreGSA> listaClassificatoreGSA) {
		this.listaClassificatoreGSA = listaClassificatoreGSA;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaClassificatoreGSAValido}.
	 * @return la request creata
	 */
	public RicercaClassificatoreGSAValido creaRequestRicercaClassificatoreGSAValido() {
		RicercaClassificatoreGSAValido req = creaRequest(RicercaClassificatoreGSAValido.class);
		
		req.setAmbito(Ambito.AMBITO_GSA);
		req.setBilancio(getBilancio());
		
		return req;
	}
}
