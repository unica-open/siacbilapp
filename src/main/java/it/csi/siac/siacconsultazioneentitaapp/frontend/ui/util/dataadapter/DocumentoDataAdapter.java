/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BooleanStringDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DocumentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ImportoDocumentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per il Documento
 * 
 *
 */
public class DocumentoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public DocumentoDataAdapter() {
		super(	asList(
						new DocumentoDataInfo("Documento", "left", "doc_desc","doc_anno","doc_tipo_code","doc_numero"),
						new PlainNumberDataInfo("Quota", "subdoc_numero"),
						new DateDataInfo("Data", "doc_data_emissione"),
						new SimpleDataInfo("Stato", "doc_stato_desc"),
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc"),
						new BooleanStringDataInfo("Pagato CEC", "<div class=\"pull-centered\">{0}</div>", "subdoc_pagato_cec", "true", "S", "false", "N"),
						new ImportoDocumentoDataInfo("Importo", "left", "subdoc_importo", "doc_importo")
						),
				asList(
						new SimpleDataInfo("Documento", "doc_numero").forceNoEscape(),
						new PlainNumberDataInfo("Anno", "doc_anno"),
						new SimpleDataInfo("Tipo", "doc_tipo_code").forceNoEscape(),
						new PlainNumberDataInfo("Quota", "subdoc_numero"),
						new SimpleDataInfo("Descrizione", "doc_desc").forceNoEscape(),
						new DateDataInfo("Data", "doc_data_emissione"),
						new SimpleDataInfo("Stato", "doc_stato_desc").forceNoEscape(),
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc").forceNoEscape(),
						new BooleanStringDataInfo("Pagato CEC", "{0}", "subdoc_pagato_cec", "true", "S", "false", "N"),
						new TypedDataInfo<BigDecimal>("Importo", "subdoc_importo")
						),
				asList(
						new BaseDataInfo("testo", "Documento {0,number,#}/{1}/{2}", "doc_anno","doc_tipo_code","doc_numero")
						)
			);
	}
	  
}
