/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * @author Elisa Chiari
 *
 */
public class ConsultaEntitaCollegateModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4585760313011940506L;
	
	
	private List<TipoEntitaConsultabile> entitaConsultabili = new ArrayList<TipoEntitaConsultabile>();
	
	/** Costruttore */
	public ConsultaEntitaCollegateModel() {
		super();
		setTitolo("consultazione generica entita collegate");
	}
	
	/**
	 * @return the request per la ricerca degli oggetti con cui popolare la selezione del div-selezione
	 */
	public OttieniNavigazioneTipoEntitaConsultabile creaRequestOttieniFigliEntitaConsultabile() {
		// TODO Auto-generated method stub
		return creaRequest(OttieniNavigazioneTipoEntitaConsultabile.class);
	}

	/**
	 * @return the entitaConsultabili
	 */
	public List<TipoEntitaConsultabile> getEntitaConsultabili() {
		return entitaConsultabili;
	}


	/**
	 * @param entitaConsultabili the entitaConsultabili to set
	 */
	public void setEntitaConsultabili(List<TipoEntitaConsultabile> entitaConsultabili) {
		this.entitaConsultabili = entitaConsultabili;
	}

}
