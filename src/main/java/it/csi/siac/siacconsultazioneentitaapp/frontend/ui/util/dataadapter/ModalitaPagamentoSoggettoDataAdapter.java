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
public class ModalitaPagamentoSoggettoDataAdapter extends EntitaConsultabileDataAdapter {

	/**
	 * Costruttore vuoto di default
	 */
	public ModalitaPagamentoSoggettoDataAdapter() {
		super(
				asList(
						//new SimpleDataInfo("Sog. Code", "soggetto_code_princ"),
						//new SimpleDataInfo("Sog. Desc", "soggetto_desc_princ"),
						new SimpleDataInfo("Codice", "ordine"),
						new SimpleDataInfo("Descrizione", "descr_arricchita"),
						new SimpleDataInfo("Associato a", "associato_a"),
						new SimpleDataInfo("Stato", "modpag_stato_desc")
				),
				asList(
						new SimpleDataInfo("Codice sog.", "soggetto_code_princ"),
						new SimpleDataInfo("Descrizione sog.", "soggetto_desc_princ"),
						new SimpleDataInfo("Codice", "ordine"),
						new SimpleDataInfo("Descrizione", "descr_arricchita"),
						new SimpleDataInfo("Associato a", "associato_a"),
						new SimpleDataInfo("Stato", "modpag_stato_desc")
				),
				asList(
						new BaseDataInfo("testo", "Modalita pagamento soggetto {0} - {1}", "ordine", "descr_arricchita")
				)
			);
	}
	  
	
}
