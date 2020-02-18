/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.model.TipologiaAttributo;

/**
 * Superclasse per il model del Capitolo di Uscita Previsione.
 * <br>
 * Si occupa di definire i campi e i metodi comuni per le actions del Capitolo di Uscita Previsione, con attenzione a:
 * <ul>
 *   <li>aggiornamento</li>
 *   <li>inserimento</li>
 *   <li>ricerca</li>
 * </ul>
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 17/01/2014
 *
 */
public class CapitoloUscitaPrevisioneModel extends CapitoloUscitaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5626877997514058309L;
	
	private boolean flagPerMemoriaEditabile;

	/**
	 * @return the flagPerMemoriaEditabile
	 */
	public boolean isFlagPerMemoriaEditabile() {
		return flagPerMemoriaEditabile;
	}

	/**
	 * @param flagPerMemoriaEditabile the flagPerMemoriaEditabile to set
	 */
	public void setFlagPerMemoriaEditabile(boolean flagPerMemoriaEditabile) {
		this.flagPerMemoriaEditabile = flagPerMemoriaEditabile;
	}

	@Override
	public void valutaModificabilitaAttributi(ControllaAttributiModificabiliCapitoloResponse response, boolean isMassivo) {
		super.valutaModificabilitaAttributi(response, isMassivo);
		
		boolean unicoArticolo = response.getStessoNumCapArt().longValue() <= 1L;
		
		// Corsivo per memoria
		flagPerMemoriaEditabile = isEditabile(unicoArticolo, isMassivo, response, TipologiaAttributo.FLAG_PER_MEMORIA);
	}
	
}
