/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroMandatoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per la Reversale
 * 
 *
 */
public class ReversaleDataAdapter extends EntitaConsultabileDataAdapter {

	/*
	 * -- 12.07.2018 Sofia jira siac-6193
  conto_tesoreria varchar,
  distinta_code varchar,
  distinta_desc varchar,
  ord_split     varchar,
  ord_ritenute  varchar 
	 *

		Indicazione se è una reversale per le ritenute o per lo split:
		colonna “split” con valori Si/No per indicare se la reversale è relativa a iva split
		colonna “ritenute” con valori Si/No per indicare se la reversale è relativa a ritenute (inps, irpef,…)
	 */
	
	
	/**
	 * Costruttore vuoto di default.
	 */
	public ReversaleDataAdapter() {
		super(
				asList(
						new NumeroMandatoDataInfo("Reversale", "ord_numero", "ord_ts_code", "ord_desc"),
						new BaseDataInfo("Debitore", "{0} - {1}","soggetto_code", "soggetto_desc"),
						new SimpleDataInfo("Stato", "ord_stato_desc"),
						new DateDataInfo("Data quietanza", "ord_quietanza_data"),
						new CurrencyDataInfo("Importo", "importo", true),
						new BaseDataInfo("Capitolo", "{0}/{1}/{2}", "capitolo_anno","capitolo_numero", "capitolo_articolo"),
						//SIAC-8188 "attoamm_oggetto", "attoal_causale"
						new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno","attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc", "attoamm_oggetto", "attoal_causale")
						),
	
				asList(
						new PlainNumberDataInfo("Reversale", "ord_numero"),
						new SimpleDataInfo("SubReversale", "ord_ts_code").forceNoEscape(),
						new SimpleDataInfo("Descrizione", "ord_desc").forceNoEscape(),
						new BaseDataInfo("Debitore", "{0} - {1}","soggetto_code", "soggetto_desc").forceNoEscape(),
						new SimpleDataInfo("Stato", "ord_stato_desc").forceNoEscape(),
						new DateDataInfo("Data quietanza", "ord_quietanza_data"),						
						new TypedDataInfo<BigDecimal>("Importo", "importo"),
						new SimpleDataInfo("N.Capitolo", "capitolo_numero"),
						new SimpleDataInfo("N.Articolo", "capitolo_articolo"),
						new SimpleDataInfo("Anno capitolo", "capitolo_anno"),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
						new BaseDataInfo("Tipo Provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new BaseDataInfo("Sruttura Amm. Cont" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						
						new DateDataInfo("Data emissione ord.", "ord_emissione_data"),
						
						new DateDataInfo("Data quietanza ord.", "ord_quietanza_data"),
						new SimpleDataInfo("Conto Tesoreria", "conto_tesoreria").forceNoEscape(),
						new BaseDataInfo("Distinta", "{0} - {1}" , "distinta_code" , "distinta_desc"),
						new SimpleDataInfo("Spilt", "ord_split").forceNoEscape(),
						new SimpleDataInfo("Ritenuta", "ord_ritenute").forceNoEscape()

						),
				asList(new BaseDataInfo("testo", "Reversale {0,number,#}", "ord_numero"))
			);
	}
	  
	
}
