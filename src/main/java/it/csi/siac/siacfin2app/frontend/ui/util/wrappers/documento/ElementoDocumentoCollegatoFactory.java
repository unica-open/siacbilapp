/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

/**
 * Classe di factory per il wrapper del Documento collegato.
 * 
 * @author Ahmad Nazha, Valentina Triolo
 *
 */
public class ElementoDocumentoCollegatoFactory extends BaseFactory{
	
	/**
	 * Ottiene un'istanza del wrapper a partire dal Documento.
	 * 
	 * @param documento il documento da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoDocumentoCollegato getInstance(Documento<?, ?> documento) {
		
		ElementoDocumentoCollegato result = new ElementoDocumentoCollegato();
		
		Integer uid = documento.getUid();
		String tipo="";
		
		if (documento instanceof DocumentoEntrata){
			tipo = "E";
		} else if (documento instanceof DocumentoSpesa){
			tipo = "S";
		}
		
		String doc = Integer.toString(documento.getAnno()).concat("/")
				.concat(documento.getTipoDocumento().getCodice()).concat("/")
				.concat(documento.getNumero());

		String data = documento.getDataEmissione() != null?FormatUtils.formatDate(documento.getDataEmissione()):"";
		
		String statoOperativoDocumentoCode = documento.getStatoOperativoDocumento().getCodice();
		String statoOperativoDocumentoDesc = documento.getStatoOperativoDocumento().getDescrizione();
		
		String soggetto = "";
		if(documento.getSoggetto() != null) {
			soggetto = documento.getSoggetto().getCodiceSoggetto().concat("-")
			   .concat(documento.getSoggetto().getDenominazione());
		}
		
		String loginModifica = documento.getLoginModifica();
		String tipoDocumento = documento.getTipoDocumento().getCodiceGruppo();
		BigDecimal importo = documento.getImporto();
		BigDecimal importoDaDedurreSuFattura = documento.getImportoDaDedurreSuFattura();
				
		result.setUid(uid);
		result.setTipo(tipo);
		result.setDocumento(doc);
		result.setData(data);
		result.setStatoOperativoDocumentoCode(statoOperativoDocumentoCode);
		result.setStatoOperativoDocumentoDesc(statoOperativoDocumentoDesc);
		result.setSoggetto(soggetto);
		result.setLoginModifica(loginModifica);
		result.setTipoDocumento(tipoDocumento);
		result.setImporto(importo);
		result.setImportoDaDedurreSuFattura(importoDaDedurreSuFattura);
		
		return result;
	}
	
	/**
	 * Ottiene le istanze del wrapper a partire dai Documenti.
	 * @param <D> la tipizzazione del documento
	 * @param documenti i documenti da wrappare
	 * 
	 * @return i wrapper creati
	 */
	public static <D extends Documento<?, ?>> List<ElementoDocumentoCollegato> getInstances(List<D> documenti) {
		
		List<ElementoDocumentoCollegato> result = new ArrayList<ElementoDocumentoCollegato>();
		
		for(D documento : documenti) {
			result.add(getInstance(documento));
		}
		
		return result;
	}

}
