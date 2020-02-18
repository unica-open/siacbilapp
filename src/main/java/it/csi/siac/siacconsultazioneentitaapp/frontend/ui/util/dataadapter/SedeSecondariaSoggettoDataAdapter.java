/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per l'Indirizzo
 */
public class SedeSecondariaSoggettoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public SedeSecondariaSoggettoDataAdapter() {
		super(
				asList(
						//new SimpleDataInfo("Codice sog.", "soggetto_code_princ"),
						//new SimpleDataInfo("Denominazione sog.", "soggetto_desc_princ"),
						new BaseDataInfo("Indirizzo", "{0} {1}, {2} {3} - {4}", "via_tipo_desc", "toponimo", "zip_code", "comune_desc", "sigla_automobilistica"),
						new SimpleDataInfo("Stato", "soggetto_stato_desc")
				),
				asList(
						new SimpleDataInfo("Codice sog.", "soggetto_code_princ"),
						new SimpleDataInfo("Descrizione sog.", "soggetto_desc_princ"),
						new BaseDataInfo("Indirizzo", "{0} {1}, {2} {3} - {4}", "via_tipo_desc", "toponimo", "zip_code", "comune_desc", "sigla_automobilistica"),
						new SimpleDataInfo("Stato", "soggetto_stato_desc")
				),
				asList(
						new BaseDataInfo("testo", "Sede secondaria soggetto {0}", "soggetto_desc")
				)
			);
	}
	  
	
}
