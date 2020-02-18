/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe di wrap per il Subdocumento Da Associare all'allegato atto.
 */
public class ElementoSubdocumentoDaAssociareAllegatoAttoSpesa extends ElementoSubdocumentoDaAssociareAllegatoAtto<Impegno, SubImpegno, DocumentoSpesa, SubdocumentoSpesa, CapitoloUscitaGestione> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6439219637744274294L;

	/**
	 * Costruttore a partire dal subdoc
	 * @param subdocumento il subdocumento da wrappare
	 */
	public ElementoSubdocumentoDaAssociareAllegatoAttoSpesa(SubdocumentoSpesa subdocumento) {
		super(subdocumento, subdocumento.getDocumento(), subdocumento.getImpegno(), subdocumento.getSubImpegno(), CODICE_TIPO_DOCUMENTO_SPESA);
	}

	@Override
	protected CapitoloUscitaGestione ottieniDatiCapitolo() {
		if(movimentoGestione != null) {
			return movimentoGestione.getCapitoloUscitaGestione();
		}
		return null;
	}

	@Override
	protected String[] ottieniDatiNote() {
		List<String> dettaglio = new ArrayList<String>();
		String asterisk = "";
		
		if(Boolean.TRUE.equals(subdocumento.getHasRitenuteDiverseSplit())){
			dettaglio.add("RIT");
			asterisk = "*";
		}
		// In esclusione
		if(Boolean.TRUE.equals(subdocumento.getHasSubordinatiPNL())) {
			dettaglio.add("CON PNL");
			asterisk = "*";
		} else if(Boolean.TRUE.equals(subdocumento.getHasSubordinati())){
			dettaglio.add("CON SUB");
			asterisk = "*";
		}
		
		if(Boolean.TRUE.equals(subdocumento.getIsSubordinato())){
			dettaglio.add("DOC PADRE: " + ottieniStringaDocCollegatoPadre());
			asterisk = "*";
		}
		if(subdocumento.getProvvisorioCassa() != null && subdocumento.getProvvisorioCassa().getNumero() != null && subdocumento.getProvvisorioCassa().getAnno() != null){
			dettaglio.add("PROVV. DI CASSA: " + ottieniStringaProvvisorio());
			asterisk = "*";
		}
		return new String[] {asterisk, StringUtils.join(dettaglio, " - ")};
	}
	
	@Override
	public String getDomStringIva() {
		if(subdocumento.getTipoIvaSplitReverse() == null || subdocumento.getTipoIvaSplitReverse() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a href=\"#\" rel=\"popover\" data-original-title=\"Dettagli\" data-content=\"Importo IVA: ")
				.append(FormatUtils.formatCurrency(subdocumento.getImportoSplitReverse()))
				.append("\" data-trigger=\"hover\">")
				.append(subdocumento.getTipoIvaSplitReverse().getDescrizione())
				.append("</a>")
				.toString();
	}
	
}
