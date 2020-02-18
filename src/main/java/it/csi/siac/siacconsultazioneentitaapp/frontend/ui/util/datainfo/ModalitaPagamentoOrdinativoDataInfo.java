/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero del capitolo.
 *
 */
public class ModalitaPagamentoOrdinativoDataInfo extends BaseDataInfo {

	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 */
	public ModalitaPagamentoOrdinativoDataInfo(String name) {
		super(name, "Tipo accredito {0} - {1} - IBAN: {2}", "ord_accredito_tipo_code", "ord_accredito_tipo_desc","ord_iban");
		
		this.fallbackPattern = "Ricevente: {0} - Cedente: {1} - Tipo accredito: {2} - {3} - Quietanzante: {4}";
		this.fallbackCampiKeys = new String[] {"ord_soggetto_cessione_desc", "ord_soggetto_desc",
				"ord_relaz_tipo_code", "ord_accredito_tipo_desc_cess", "ord_quietanzante"};
	}	
	
	@Override
	public boolean mayUseFallback() {
		return true;
	}
}
