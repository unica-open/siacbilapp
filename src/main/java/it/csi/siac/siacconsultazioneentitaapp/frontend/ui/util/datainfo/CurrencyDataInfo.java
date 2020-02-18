/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Descrizione informazioni di una Colonna con un solo dato di tipo Importo.
 * 
 * @author Domenico
 */
public class CurrencyDataInfo extends BaseDataInfo {
	
	private final boolean summableImporto;
	
	/**
	 * Costruttore per l'importo
	 * @param name il nome del campo
	 * @param campoKey la chiave del campo
	 */
	public CurrencyDataInfo(String name, String campoKey) {
		this(name, campoKey, false);
	}
	
	/**
	 * Costruttore per l'importo
	 * @param name il nome del campo
	 * @param campoKey la chiave del campo
	 * @param summableImporto se l'importo sia sommabile
	 */
	public CurrencyDataInfo(String name, String campoKey, boolean summableImporto) {
		super(name, "<span class=\"pull-right\">{0,number,###,##0.00}</span>", campoKey);
		this.summableImporto = summableImporto;
	}
	
	@Override
	public boolean isSummableImporto() {
		return summableImporto;
	}
}