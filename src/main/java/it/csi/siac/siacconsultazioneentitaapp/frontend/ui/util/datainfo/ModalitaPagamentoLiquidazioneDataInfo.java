/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con il numero del capitolo.
 *
 */
public class ModalitaPagamentoLiquidazioneDataInfo extends BaseDataInfo {

	/**
	 * Costruttore.
	 * 
	 * @param name il nome del campo
	 */
    
	public ModalitaPagamentoLiquidazioneDataInfo(String name) {
		super(name, "Tipo accredito {0} - {1} - IBAN: {2}", "accredito_tipo_code", "accredito_tipo_desc","iban");
		
		this.fallbackPattern = "{0} - {1} - {2} - {3} - {4}";
		this.fallbackCampiKeys = new String[] {"soggetto_cessione_desc", "soggetto_desc",
				"relaz_tipo_code", "accredito_tipo_desc_cess", "quietanzante"};
	}	
	
	@Override
	public boolean mayUseFallback() {
		return true;
	}
}
