/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public class WrapperComponenteImportoCapitoloAnno implements Serializable, ModelWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3390665190497705584L;
	/** Per la serializzazione  */
	
	private ComponenteImportiCapitolo componenteImportiCapitolo;
	private Integer annoCompetenza;
	private PeriodoTabellaComponentiImportiCapitolo periodoTabellaComponentiImportiCapitolo;
	
	@Override
	public int getUid() {
		return componenteImportiCapitolo != null ? componenteImportiCapitolo.getUid() : 0;
	}

	public ComponenteImportiCapitolo getComponenteImportiCapitolo() {
		return componenteImportiCapitolo;
	}

	public void setComponenteImportiCapitolo(ComponenteImportiCapitolo componenteImportiCapitolo) {
		this.componenteImportiCapitolo = componenteImportiCapitolo;
	}

	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	public PeriodoTabellaComponentiImportiCapitolo getPeriodoTabellaComponentiImportiCapitolo() {
		return periodoTabellaComponentiImportiCapitolo;
	}

	public void setPeriodoTabellaComponentiImportiCapitolo(PeriodoTabellaComponentiImportiCapitolo periodoTabellaComponentiImportiCapitolo) {
		this.periodoTabellaComponentiImportiCapitolo = periodoTabellaComponentiImportiCapitolo;
	}	
	
	
}

