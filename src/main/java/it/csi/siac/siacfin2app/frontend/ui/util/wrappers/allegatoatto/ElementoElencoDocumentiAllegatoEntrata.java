/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Wrapper per l'Elenco Documenti Allegato
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2014
 * 
 */
public class ElementoElencoDocumentiAllegatoEntrata extends ElementoElencoDocumentiAllegato<Accertamento, SubAccertamento, CapitoloEntrataGestione, DocumentoEntrata, SubdocumentoEntrata> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6147387531935468187L;
	
	/**
	 * Costruttore a partire dall'elenco, dai dati del soggetto e dal subdocumento.
	 * 
	 * @param elencoDocumentiAllegato l'elenco da wrappare
	 * @param datiSoggettoAllegato    i dati del soggetto
	 * @param isGestioneUEB           se la gestione delle UEB sia attiva
	 * @param subdocumentoEntrata     il subdocumento
	 */
	public ElementoElencoDocumentiAllegatoEntrata(ElencoDocumentiAllegato elencoDocumentiAllegato, DatiSoggettoAllegato datiSoggettoAllegato, Boolean isGestioneUEB,
			SubdocumentoEntrata subdocumentoEntrata) {
		super(elencoDocumentiAllegato,
				subdocumentoEntrata,
				subdocumentoEntrata.getDocumento(),
				subdocumentoEntrata.getAccertamento(),
				subdocumentoEntrata.getSubAccertamento(),
				datiSoggettoAllegato,
				subdocumentoEntrata.getAttoAmministrativo(),
				"",
				BigDecimal.ZERO,
				CODICE_TIPO_DOCUMENTO_ENTRATA,
				CODICE_TIPO_MOVIMENTO_GESTIONE_ENTRATA,
				isGestioneUEB);
	}
	
	@Override
	protected CapitoloEntrataGestione ottieniDatiCapitolo() {
		if(subdocumento.getAccertamento() != null) {
			return subdocumento.getAccertamento().getCapitoloEntrataGestione();
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

	@Override
	public String getImportoEntrata() {
		if(subdocumento == null) {
			return "-";
		}
		return FormatUtils.formatCurrency(subdocumento.getImportoDaIncassare());
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder()
			.append("ENTRATA")
			.appendSuper(super.hashCode());
		return hcb.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ElementoElencoDocumentiAllegatoEntrata)) {
			return false;
		}
		return super.equals(obj);
	}
	
	@Override
	public int compareTo(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> other) {
		int comparison = super.compareTo(other);
		if(comparison != 0){
			return comparison;
		}
		if(other.documento instanceof DocumentoSpesa) {
			return 1;
			
		}
		return this.getUid() - other.getUid();
	}
	
	@Override
	protected boolean isMovimentoGestioneDurc() {
		return false;
	}

}
