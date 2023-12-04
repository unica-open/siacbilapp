/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import java.math.BigDecimal;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.AnnoImpegnoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.CurrencyDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PianoDeiContiDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.TypedDataInfo;

/**
 * Column Adapter per l'Impegno.
 * 
 *
 */
public class ImpegnoDataAdapter extends EntitaConsultabileDataAdapter {
	

	
	/**
	 * Adapter per le colonne da visualizzare dell'impegno
	 */
	public ImpegnoDataAdapter() {
		super(	
				asList(
						new AnnoImpegnoDataInfo("Impegno", "right", "impegno_anno", "impegno_numero", "impegno_desc"),
						//SIAC-8188 "attoamm_oggetto", "attoal_causale"
						new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno","attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc", "attoamm_oggetto", "attoal_causale"),
						new SimpleDataInfo("Stato", "impegno_stato"),
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc"),
						new PianoDeiContiDataInfo("Piano dei Conti", "left", "pdc_code", "pdc_desc"),
//						new BaseDataInfo("Piano dei Conti", "{0} - {1}", "pdc_code", "pdc_desc"),
						//SIAC-7349 Start SR210 MR 17/04/2020 Nuovo campo per tabella riepilogo impegno
						new SimpleDataInfo("Componente", "impegno_componente"),
						new CurrencyDataInfo("Importo", "impegno_importo", true)

						),
				asList(
						/*
						   uid integer,
						  impegno_anno integer,
						  impegno_numero numeric,
						  impegno_desc varchar,
						  impegno_stato varchar,
						  impegno_importo numeric,
						  soggetto_code varchar,
						  soggetto_desc varchar,
						  attoamm_numero integer,
						  attoamm_anno varchar,
						  attoamm_oggetto varchar,
						  attoamm_tipo_code varchar,
						  attoamm_tipo_desc varchar,
						  attoamm_stato_desc varchar,
						  attoamm_sac_code varchar,
						  attoamm_sac_desc varchar,
						  pdc_code varchar,
						  pdc_desc varchar,
						  -- 29.06.2018 Sofia siac-6193
						  impegno_anno_capitolo integer,
						  impegno_nro_capitolo  integer,
						  impegno_nro_articolo  integer,
						  impegno_flag_prenotazione varchar,
						  impegno_cup varchar,
						  impegno_cig varchar,
						  impegno_tipo_debito varchar,
						  impegno_motivo_assenza_cig varchar
						 */
						
						//SIAC-3760
						new PlainNumberDataInfo("Impegno", "impegno_numero"),
						new PlainNumberDataInfo("Anno impegno", "impegno_anno"),
						new SimpleDataInfo("Descrizione","impegno_desc").forceNoEscape(),
						//SIAC-8351
						new BaseDataInfo("Struttura Competente" , "{0} - {1}", "imp_sac_code", "imp_sac_desc").forceNoEscape(),
						
						//PROVVEIDIMENTO
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new PlainNumberDataInfo("Numero Provv.", "attoamm_numero"),
						//SIAC-8350
						new BaseDataInfo("Tipo Provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new SimpleDataInfo("Descrizione provvedimento","attoamm_oggetto").forceNoEscape(),												
						//SIAC-8350
						new BaseDataInfo("Struttura Provvedimento" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
						
						//CAPITOLO
						//SIAC-6193
						new PlainNumberDataInfo("Anno capitolo", "impegno_anno_capitolo"),
						new PlainNumberDataInfo("N.Capitolo", "impegno_nro_capitolo"),
						new PlainNumberDataInfo("N.Articolo", "impegno_nro_articolo"),
						//SIAC-8351
						new BaseDataInfo("Struttura Amm. capitolo" , "{0} - {1}", "cap_sac_code", "cap_sac_desc").forceNoEscape(),
						
						new SimpleDataInfo("Componente", "impegno_componente"),
						new SimpleDataInfo("Flag prenotazione impegno","impegno_flag_prenotazione"),
						new SimpleDataInfo("Cig","impegno_cig"),
						new SimpleDataInfo("Cup","impegno_cup"),
						new SimpleDataInfo("Motivazione assenza SIOPE.","impegno_motivo_assenza_cig").forceNoEscape(),
						new SimpleDataInfo("Tipo debito SIOPE","impegno_tipo_debito").forceNoEscape(),										
						new BaseDataInfo("Piano dei Conti" , "{0} - {1}", "pdc_code", "pdc_desc").forceNoEscape(),
						new SimpleDataInfo("Stato", "impegno_stato").forceNoEscape(),
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc").forceNoEscape(),
						new TypedDataInfo<BigDecimal>("Importo", "impegno_importo"),
						//SIAC-8877
						new SimpleDataInfo("Progetto","programma"),
						new SimpleDataInfo("Cronoprogramma","cronoprogramma")
						),
				asList(
						new BaseDataInfo("testo", " Impegno {0,number,#}/{1,number,#}", "impegno_anno", "impegno_numero")
						)
				
			); 
	}  
	
}
