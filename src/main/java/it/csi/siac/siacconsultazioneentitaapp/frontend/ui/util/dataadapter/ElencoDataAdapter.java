/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per l'Elenco.
 * 
 *
 */
public class ElencoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public ElencoDataAdapter() {
		super(
				asList(
						new BaseDataInfo("Elenco", "{0,number,#}/{1,number,#}", "eldoc_anno","eldoc_numero"),
						new SimpleDataInfo("stato", "eldoc_stato_desc"),
						new BaseDataInfo("Fonte", "{0,number,#}/{1}", "eldoc_sysesterno_anno","eldoc_sysesterno_numero"),
						new DateDataInfo("Data trasmissione", "eldoc_data_trasmissione"),
						new CurrencyDataInfo("Differenza", "eldoc_totale_differenza"),
						new CurrencyDataInfo("Importo entrate", "eldoc_totale_quoteentrate"),
						new CurrencyDataInfo("Importo spese", "eldoc_totale_quotespese")
						),
				asList(
						new PlainNumberDataInfo("Elenco", "eldoc_numero"),
						new PlainNumberDataInfo("Anno", "eldoc_anno"),
						new SimpleDataInfo("Stato", "eldoc_stato_desc").forceNoEscape(),
						new BaseDataInfo("Fonte", "{0,number,#}/{1}", "eldoc_sysesterno_anno","eldoc_sysesterno_numero"),
						new DateDataInfo("Data trasmissione", "eldoc_data_trasmissione"),
						new TypedDataInfo<BigDecimal>("Differenza", "eldoc_totale_differenza"),
						new TypedDataInfo<BigDecimal>("Importo entrate", "eldoc_totale_quoteentrate"),
						new TypedDataInfo<BigDecimal>("Importo spese", "eldoc_totale_quotespese")
						),
				asList(
						new BaseDataInfo("testo", "Elenco {0,number,#}/{1,number,#}", "eldoc_anno","eldoc_numero")
						)
			);
	}
	  
	
}
