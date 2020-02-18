/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ModalitaPagamentoLiquidazioneDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.ModalitaPagamentoOrdinativoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroLiquidazioneDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroOrdinativoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoConOggettoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per la Liquidazione
 * 
 *
 */
public class LiquidazioneDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public LiquidazioneDataAdapter() {
		super(
				asList(
						new NumeroLiquidazioneDataInfo("Liquidazione", "right", "liq_numero", "liq_desc"),
						// SIAC-3805
						new PlainNumberDataInfo("Anno", "liq_anno"),
						new BaseDataInfo("Stato", "{0}", "liq_stato"),
						new BaseDataInfo("Creditore","{0} - {1}" ,"soggetto_code", "soggetto_desc"),
						new BaseDataInfo("Capitolo", "{0}/{1}/{2}", "capitolo_anno","capitolo_numero", "capitolo_articolo"),
						new BaseDataInfo("Impegno", "{0,number,#}/{1, number,#}", "movgest_anno","movgest_numero"),
						// SIAC-5899
						new NumeroProvvedimentoConOggettoDataInfo("N. Provv.", "top", 
							"attoamm_anno",
							"attoamm_numero", 
							"attoamm_tipo_code", 
							"attoamm_tipo_desc", 
							"attoamm_sac_code", 
							"attoamm_sac_desc", 
							"attoamm_stato_desc",
							"attoamm_oggetto"
							),
						// SIAC-5164
						new NumeroOrdinativoDataInfo("Ordinativo", "top", "ord_anno", "ord_numero", "ord_stato_desc"),
						new CurrencyDataInfo("Importo", "liq_importo", true)
						),
				asList(
						//SIAC-3760
						new PlainNumberDataInfo("Liquidazione", "liq_numero"),
						new SimpleDataInfo("Descrizione", "liq_desc").forceNoEscape(),
						// SIAC-3805
						new PlainNumberDataInfo("Anno", "liq_anno"),
						new SimpleDataInfo("Stato", "liq_stato").forceNoEscape(),
						new SimpleDataInfo("Split/Ritenute ", "liq_esiste_split").forceNoEscape(),
						new TypedDataInfo<BigDecimal>("Importo", "liq_importo"),

						new ModalitaPagamentoLiquidazioneDataInfo("Mod pag liquidazione"),
						
						
						new SimpleDataInfo("Codice Fiscale", "sog_codice_fiscale"),
						new SimpleDataInfo("Partita Iva", "sog_partita_iva"),
						new SimpleDataInfo("Carte Contabili", "carte_contabili"),						
						
						new BaseDataInfo("Creditore","{0} - {1}" ,"soggetto_code", "soggetto_desc").forceNoEscape(),
						new SimpleDataInfo("N.Capitolo", "capitolo_numero"),
						new SimpleDataInfo("N.Articolo", "capitolo_articolo"),
						new SimpleDataInfo("Anno capitolo", "capitolo_anno"),
						new PlainNumberDataInfo("Impegno", "movgest_numero"),
						new PlainNumberDataInfo("Anno impegno", "movgest_anno"),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),						
						new BaseDataInfo("Tipo Provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new BaseDataInfo("Sruttura Amm. Cont" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						new SimpleDataInfo("Oggetto provv.", "attoamm_oggetto").forceNoEscape(),	
					
						new DateDataInfo("Inserimento atto contabile","attoal_data_creazione"),
						new DateDataInfo("Scadenza atto contabile","attoal_data_scadenza"),
						new SimpleDataInfo("Stato atto contabile","attoal_stato_desc"),
	
						// SIAC-5164
						new PlainNumberDataInfo("Anno ordinativo", "ord_anno"),
						new PlainNumberDataInfo("N.ordinativo", "ord_numero"),
				        new DateDataInfo("Data emissione ordinativo","ord_emissione_data"),
				        new DateDataInfo("Data quitanza ordinativo","ord_quietanza_data"),
						new SimpleDataInfo("Stato ordinativo", "ord_stato_desc"),
						new ModalitaPagamentoOrdinativoDataInfo("Mod pag ordinativo")

						),
				asList(new BaseDataInfo("testo", "Liquidazione {0,number,#}", "liq_numero"))
		);
	}
	  
	
}
