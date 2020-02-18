/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di wrap per il Subdocumento di entrata.
 * @author Alessandro Marchino
 *
 */
public class ElementoSubdocumentoEntrata extends ElementoSubdocumento<SubdocumentoEntrata, Accertamento, SubAccertamento, CapitoloEntrataGestione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8868369333580105549L;

	/**
	 * Costruttore di wrap
	 * @param subdocumento il subdocumento da wrappare
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	public ElementoSubdocumentoEntrata(SubdocumentoEntrata subdocumento, boolean isGestioneUEB) {
		super(subdocumento, subdocumento != null ? subdocumento.getAccertamento() : null, subdocumento != null ? subdocumento.getSubAccertamento() : null, isGestioneUEB);
	}
	
	@Override
	protected CapitoloEntrataGestione ottieniCapitolo() {
		if(movimentoGestione == null){
			return null;
		}
		return movimentoGestione.getCapitoloEntrataGestione();
	}

}
