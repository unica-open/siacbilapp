/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.DateDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.NumeroProvvedimentoDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PlainNumberDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per l'Allegato
 */
public class AllegatoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public AllegatoDataAdapter() {
		super(
				asList(
						new SimpleDataInfo("Causale", "attoal_causale"),
						new DateDataInfo("Data scadenza", "attoal_data_scadenza"),
						new SimpleDataInfo("Stato", "attoal_stato"),
						new SimpleDataInfo("Versione invio firma", "attoal_versione_invio_firma"),
						new NumeroProvvedimentoDataInfo("N. Provv.", "top", "attoamm_anno","attoamm_numero", "attoamm_tipo_code", "attoamm_tipo_desc" , "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc")
				),
				asList(
						//SIAC-3760
						new SimpleDataInfo("Causale", "attoal_causale").forceNoEscape(),
						new DateDataInfo("Data scadenza", "attoal_data_scadenza"),
						new SimpleDataInfo("Stato", "attoal_stato").forceNoEscape(),
						new SimpleDataInfo("Versione invio firma", "attoal_versione_invio_firma"),
						new PlainNumberDataInfo("Provvedimento", "attoamm_numero"),
						new SimpleDataInfo("Anno provv.", "attoamm_anno"),
						new SimpleDataInfo("Stato provv.", "attoamm_stato_desc").forceNoEscape(),
						new BaseDataInfo("Struttura Amm. Cont.", "{0} {1} {2}", "attoamm_sac_code", "attoamm_sac_desc", "attoamm_stato_desc").forceNoEscape()
				),
				asList(new BaseDataInfo("testo", "Allegato {0}/{1,number,#}", "attoamm_anno","attoamm_numero"))
			);
	}
	  
	
}
