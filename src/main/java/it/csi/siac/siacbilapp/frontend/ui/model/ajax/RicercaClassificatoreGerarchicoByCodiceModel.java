/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del classificatore gerarachico a partire dal codice.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12c/2015
 *
 */
public class RicercaClassificatoreGerarchicoByCodiceModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7684259806742706924L;
	
	private ClassificatoreGerarchico classificatore;
	
	/** Costruttore vuoto di default */
	public RicercaClassificatoreGerarchicoByCodiceModel() {
		setTitolo("Ricerca classificatore by codice");
	}

	/**
	 * @return the classificatore
	 */
	public ClassificatoreGerarchico getClassificatore() {
		return classificatore;
	}

	/**
	 * @param classificatore the classificatore to set
	 */
	public void setClassificatore(ClassificatoreGerarchico classificatore) {
		this.classificatore = classificatore;
	}
	
	/* **** Request **** */
	
	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno}.
	 * 
	 * @param tipologiaClassificatore la tipologia
	 * @return la request creata
	 */
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(TipologiaClassificatore tipologiaClassificatore) {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno request = creaRequest(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setClassificatore(getClassificatore());
		request.setTipologiaClassificatore(tipologiaClassificatore);
		
		return request;
	}
	
}
