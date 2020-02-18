/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Classe di wrap per il Subdocumento Da Associare all'allegato atto.
 */
public class ElementoSubdocumentoDaAssociareAllegatoAttoEntrata extends ElementoSubdocumentoDaAssociareAllegatoAtto<Accertamento, SubAccertamento, DocumentoEntrata, SubdocumentoEntrata, CapitoloEntrataGestione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -540465711159895974L;

	/**
	 * Costruttore a partire dal subdoc
	 * @param subdocumento il subdocumento da wrappare
	 */
	public ElementoSubdocumentoDaAssociareAllegatoAttoEntrata(SubdocumentoEntrata subdocumento) {
		super(subdocumento, subdocumento.getDocumento(), subdocumento.getAccertamento(), subdocumento.getSubAccertamento(), CODICE_TIPO_DOCUMENTO_ENTRATA);
	}

	@Override
	protected CapitoloEntrataGestione ottieniDatiCapitolo() {
		if(movimentoGestione != null) {
			return movimentoGestione.getCapitoloEntrataGestione();
		}
		return null;
	}

	@Override
	protected String[] ottieniDatiNote() {
		List<String> dettaglio = new ArrayList<String>();
		String asterisk = "";
		if(Boolean.TRUE.equals(subdocumento.getHasSubordinatiPNL())) {
			dettaglio.add("CON PNL");
			asterisk = "*";
		} else if(Boolean.TRUE.equals(subdocumento.getHasSubordinati())){
			dettaglio.add("CON SUB");
			asterisk = "*";
		}
		if(Boolean.TRUE.equals(subdocumento.getIsSubordinato())){
			dettaglio.add(ottieniDettaglioSubordinato());
			asterisk = "*";
		}
		if(subdocumento.getProvvisorioCassa() != null && subdocumento.getProvvisorioCassa().getNumero() != null && subdocumento.getProvvisorioCassa().getAnno() != null){
			dettaglio.add("PROVV. DI CASSA: " + ottieniStringaProvvisorio());
			asterisk = "*";
		}
		return new String[] {asterisk, StringUtils.join(dettaglio, " - ")};
	}
	
}
