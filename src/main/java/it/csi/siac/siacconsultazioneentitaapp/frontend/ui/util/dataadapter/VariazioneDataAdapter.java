/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per la Variazione
 * 
 *
 */
public class VariazioneDataAdapter extends EntitaConsultabileDataAdapter {
	

	
	/**
	 * Costruttore vuoto di default.
	 */
	public VariazioneDataAdapter() {
		super(
				asList(
						new PlainNumberDataInfo("Variazione", "variazione_num"),
						new SimpleDataInfo("Applicazione", "variazione_applicazione"),
						new BaseDataInfo("Tipo","{0}-{1}" ,"variazione_tipo_code", "variazione_tipo_desc"),
						new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno", "attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc"),
						new SimpleDataInfo("Stato", "variazione_stato_tipo_desc")
						),
				asList(
						new PlainNumberDataInfo("Variazione", "variazione_num"),
						new SimpleDataInfo("Applicazione", "variazione_applicazione").forceNoEscape(),
						new BaseDataInfo("Tipo","{0}-{1}" ,"variazione_tipo_code", "variazione_tipo_desc").forceNoEscape(),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
						new BaseDataInfo("Tipo Provv." , "{0} - {1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new BaseDataInfo("Sruttura Amm. Cont" , "{0} - {1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						new SimpleDataInfo("Stato", "variazione_stato_tipo_desc").forceNoEscape()
						),
				asList(
						new BaseDataInfo("testo", "Variazione N.{0,number,#}", "variazione_num")
						)
				
			);
	}
	  
	
}
