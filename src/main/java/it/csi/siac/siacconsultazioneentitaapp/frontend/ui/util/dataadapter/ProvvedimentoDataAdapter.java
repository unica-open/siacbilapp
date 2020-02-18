/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per il Provvedimento
 * 
 *
 */
public class ProvvedimentoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public ProvvedimentoDataAdapter() {
		super(
				asList(
						new SimpleDataInfo("Anno", "attoamm_anno"),
						new PlainNumberDataInfo("Numero", "attoamm_numero"),
						new BaseDataInfo("Tipo", "{0}-{1}", "attoamm_tipo_code", "attoamm_tipo_desc"),
						new SimpleDataInfo("Oggetto", "attoamm_oggetto"),
						new BaseDataInfo("Strutt. Amm.","{0}-{1}", "attoamm_sac_code", "attoamm_sac_desc"),
						new SimpleDataInfo("Stato", "attoamm_stato_desc"),
						//SIAC 6929
						new SimpleDataInfo("Avanzamento", "attoamm_avanzamento")
						//
						),
				asList(
						new SimpleDataInfo("Anno", "attoamm_anno"),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new BaseDataInfo("Tipo", "{0}-{1}", "attoamm_tipo_code", "attoamm_tipo_desc").forceNoEscape(),
						new SimpleDataInfo("Oggetto", "attoamm_oggetto").forceNoEscape(),
						new BaseDataInfo("Strutt. Amm.","{0}-{1}", "attoamm_sac_code", "attoamm_sac_desc").forceNoEscape(),
						new SimpleDataInfo("Stato", "attoamm_stato_desc").forceNoEscape(),
						//SIAC 6929
						new SimpleDataInfo("Avanzamento", "attoamm_avanzamento")
						//
						),
				asList(
						new BaseDataInfo("testo", " Provvedimento {0}/{1,number,#}/{2}", "attoamm_anno","attoamm_numero","attoamm_tipo_code" )
						)
			);
	}
	  
	
}
