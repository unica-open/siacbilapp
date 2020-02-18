/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.dataadapter;

import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.BaseDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.PopoverDataInfo;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo.SimpleDataInfo;

/**
 * Column Adapter per il Soggetto
 * 
 *
 */
public class SoggettoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default.
	 */
	public SoggettoDataAdapter() {
		super(
				asList(
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc"),						
						new PopoverDataInfo("Stato", "{0}", "left", "nota stato", "{1}", "soggetto_nota_operazione", "soggetto_stato_desc"),						  
						new SimpleDataInfo("Codice Fiscale", "soggetto_codice_fiscale"),
						new SimpleDataInfo("Partita IVA", "soggetto_partita_iva"),
						new SimpleDataInfo("Matricola", "soggetto_matricola"),						
						new BaseDataInfo("Indirizzo Soggetto", "{0}-{1} {2}", "soggetto_comune_desc", "soggetto_via_tipo_desc","soggetto_toponimo")						
						),
				asList(
						new BaseDataInfo("Soggetto", "{0}-{1}", "soggetto_code", "soggetto_desc").forceNoEscape(),						
						new BaseDataInfo("Stato", "{0}  {1}", "soggetto_stato_desc", "soggetto_nota_operazione").forceNoEscape(),						
						new SimpleDataInfo("Codice Fiscale", "soggetto_codice_fiscale"),
						new SimpleDataInfo("Partita IVA", "soggetto_partita_iva"),
						new SimpleDataInfo("Matricola", "soggetto_matricola").forceNoEscape(),
						new BaseDataInfo("Indirizzo Soggetto", "{0}-{1} {2}", "soggetto_comune_desc", "soggetto_via_tipo_desc","soggetto_toponimo").forceNoEscape(),						
						new SimpleDataInfo("Carte contabili", "soggetto_extcarta")	

						),
				asList(new BaseDataInfo("testo", " Soggetto {0}-{1}", "soggetto_code", "soggetto_desc"))
			);
	}
	
}
