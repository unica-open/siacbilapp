/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BooleanStringDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per l'Indirizzo
 */
public class IndirizzoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public IndirizzoDataAdapter() {
		super(
				asList(
						new SimpleDataInfo("Tipo", "indirizzo_tipo_desc"),
						new BaseDataInfo("Indirizzo", "{0} {1}, {2} {3} - {4}", "via_tipo_desc", "toponimo", "zip_code", "comune_desc", "sigla_automobilistica"),
						new BooleanStringDataInfo("Principale", "<div class=\"pull-centered\">{0}</div>", "principale", "S", "SI", "N", "NO"),
						new BooleanStringDataInfo("Avviso", "<div class=\"pull-centered\">{0}</div>", "avviso", "S", "SI", "N", "NO")
				),
				asList(
						new SimpleDataInfo("Tipo", "indirizzo_tipo_desc"),
						new BaseDataInfo("Indirizzo", "{0} {1}, {2} {3} - {4}", "via_tipo_desc", "toponimo", "zip_code", "comune_desc", "sigla_automobilistica"),
						new BooleanStringDataInfo("Principale", "{0}", "principale", "S", "SI", "N", "NO"),
						new BooleanStringDataInfo("Avviso", "{0}", "avviso", "S", "SI", "N", "NO")
				),
				asList(
						new BaseDataInfo("testo", "Indirizzo {0} {1}, {2} {3} - {4}", "via_tipo_desc", "toponimo", "zip_code", "comune_desc", "sigla_automobilistica")
				)
			);
	}
	  
	
}
