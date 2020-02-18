/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ModalitaPagamentoOrdinativoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroMandatoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoConOggettoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per il mandato
 * @author Elisa Chiari
 * @version 1.0.0 - 31/03/2016
 *
 */
public class MandatoDataAdapter extends EntitaConsultabileDataAdapter {
	
	/**
	 * Costruttore vuoto di default.
	 */
	public MandatoDataAdapter() {
		super(asList(
						new NumeroMandatoDataInfo("Mandato", "ord_numero", "ord_ts_code", "ord_desc"),				
						new DateDataInfo("Data emissione", "ord_emissione_data"),
						new BaseDataInfo("Creditore", "{0}-{1}","ord_soggetto_code", "ord_soggetto_desc"),
						new BaseDataInfo("Cod. Mod. Pag.","{0} - {1}" ,"accredito_tipo_code", "accredito_tipo_desc"),
						new SimpleDataInfo("Stato", "ord_stato_desc"),						
						//SIAC-4646 - si richiede di eliminare le colonne Quietanza e Data quietanza.
						//new BaseDataInfo("Quietanza","{0,number,#}/{1,number,#}","provc_anno", "provc_numero"),
						new DateDataInfo("Data quietanza", "ord_quietanza_data"),						
						//new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno","attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc"),
						new NumeroProvvedimentoConOggettoDataInfo("N. Provv.", "top", 
								"attoamm_anno",
								"attoamm_numero", 
								"attoamm_tipo_code", 
								"attoamm_tipo_desc", 
								"attoamm_sac_code", 
								"attoamm_sac_desc", 
								"attoamm_stato_desc",
								"liq_attoamm_desc"
								),
						new CurrencyDataInfo("Importo", "importo", true)
				),
				asList(
						//new BaseDataInfo("Mandato", "{0,number,#}/{1}-{2}", "ord_numero", "ord_ts_code", "ord_desc").forceNoEscape(),
						new BaseDataInfo("Mandato", "{0,number,#}", "ord_numero").forceNoEscape(),
						new SimpleDataInfo("Sub.",  "ord_ts_code").forceNoEscape(),
						new SimpleDataInfo("Causale",  "ord_desc").forceNoEscape(),

						new SimpleDataInfo("Causale atto contabile",  "liq_attoamm_desc").forceNoEscape(),

						new DateDataInfo("Data emissione", "ord_emissione_data"),
						new BaseDataInfo("Creditore", "{0} - {1}","ord_soggetto_code", "ord_soggetto_desc").forceNoEscape(),
						
						new SimpleDataInfo("Cod fisc sogg. ", "sog_codice_fiscale").forceNoEscape(),
						new SimpleDataInfo("Partita Iva sogg. ", "sog_partita_iva").forceNoEscape(),
						new SimpleDataInfo("Carte contabili sogg ","carte_contabili"),
						
						
						new BaseDataInfo("Cod. Mod. Pag.","{0} - {1}" ,"accredito_tipo_code", "accredito_tipo_desc").forceNoEscape(),
						new SimpleDataInfo("Stato", "ord_stato_desc").forceNoEscape(),
						//new BaseDataInfo("Quietanza","{0,number,#}/{1,number,#}","provc_anno", "provc_numero"),
						new DateDataInfo("Data quietanza", "ord_quietanza_data"),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
						new BaseDataInfo("Tipo Provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new BaseDataInfo("Sruttura Amm. Cont" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						new TypedDataInfo<BigDecimal>("Importo", "importo"),
						
						new DateDataInfo("Inserimento atto contabile","liq_attoalg_data_inserimento"),
						new DateDataInfo("Scadenza atto contabile","liq_attoalg_data_scad"),
						new SimpleDataInfo("Stato atto contabile","attoamm_stato_desc"),
						new SimpleDataInfo("Split ord.", "ord_split").forceNoEscape(),
						new TypedDataInfo<BigDecimal>("Importo split", "ord_split_importo"),
						new SimpleDataInfo("Ritenute ord.", "ord_ritenute").forceNoEscape(),
						new TypedDataInfo<BigDecimal>("Importo ritenute", "ord_ritenute_importo"),
						new SimpleDataInfo("Copertura ord.", "ord_copertura"),						
						new SimpleDataInfo("Conto di tesoreria", "ord_conto_tesoreria"),
						new BaseDataInfo("Distinta","{0} - {1}","ord_distinta_codice", "ord_distinta_desc").forceNoEscape(),	
						new ModalitaPagamentoOrdinativoDataInfo("Mod pag ordinativo")
						),
				asList(
						new BaseDataInfo("testo", "Mandato {0,number,#}", "ord_numero")
						)
			);
	}
	
}

