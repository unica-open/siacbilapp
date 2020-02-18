/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaClassificatore;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la ricerca del classificatore gerarchico.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12/2015
 *
 */
public class RicercaClassificatoreGerarchicoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2858235974869513958L;
	
	private ClassificatoreGerarchico classificatore;
	
	/** Costruttore vuoto di default */
	public RicercaClassificatoreGerarchicoModel() {
		setTitolo("Ricerca classificatore gerarchico");
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
	 * Crea una request per il servizio {@link RicercaSinteticaClassificatore}.
	 * @param tipologiaClassificatore la tipologia di classificatore
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaClassificatore creaRequestRicercaSinteticaClassificatore(TipologiaClassificatore tipologiaClassificatore) {
		RicercaSinteticaClassificatore request = creaRequest(RicercaSinteticaClassificatore.class);
		
		request.setAnno(getAnnoEsercizioInt());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setTipologiaClassificatore(tipologiaClassificatore);
		
		request.setCodice(getClassificatore().getCodice());
		request.setDescrizione(getClassificatore().getDescrizione());
		
		return request;
	}
}
