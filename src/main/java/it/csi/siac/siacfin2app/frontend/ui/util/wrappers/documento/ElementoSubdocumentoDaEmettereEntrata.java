/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di wrap per il Subdocumento da emettere di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/11/2014
 *
 */
public class ElementoSubdocumentoDaEmettereEntrata
	extends ElementoSubdocumentoDaEmettere<SubdocumentoEntrata, DocumentoEntrata, CapitoloEntrataGestione, Accertamento, SubAccertamento> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7715350360717454929L;

	/**
	 * Costruttore a partire dal subdocumento wrappato e dall'eventuale gestione della UEB.
	 * 
	 * @param subdocumento il subdocumento da wrappare
	 * @param gestioneUEB  la gestione dell'UEB dell'ente
	 */
	public ElementoSubdocumentoDaEmettereEntrata(SubdocumentoEntrata subdocumento, boolean gestioneUEB) {
		super(subdocumento,
			gestioneUEB,
			subdocumento != null && subdocumento.getAccertamento() != null ? subdocumento.getAccertamento().getCapitoloEntrataGestione() : null,
			subdocumento != null ? subdocumento.getAccertamento() : null,
			subdocumento != null ? subdocumento.getSubAccertamento() : null);
	}
	
}
