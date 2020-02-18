/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;

/**
 * The Class ElementostatoOperativoVariazione.
 * @author elisa
 * @version 1.0.0 - 14-06-2018
 */
public class ElementoStatoOperativoVariazione implements Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	private static final Map<StatoOperativoVariazioneDiBilancio, String> MAPPING_STATO;
	
	static {
		Map<StatoOperativoVariazioneDiBilancio, String> tmp = new HashMap<StatoOperativoVariazioneDiBilancio, String>();
		tmp.put(StatoOperativoVariazioneDiBilancio.ANNULLATA,  null);
		tmp.put(StatoOperativoVariazioneDiBilancio.BOZZA,  null);
		tmp.put(StatoOperativoVariazioneDiBilancio.CONSIGLIO,  "%VARIAZ_ORGANO_LEG%");
		tmp.put(StatoOperativoVariazioneDiBilancio.GIUNTA,  "%VARIAZ_ORGANO_AMM%");
		tmp.put(StatoOperativoVariazioneDiBilancio.PRE_DEFINITIVA,  null);
		tmp.put(StatoOperativoVariazioneDiBilancio.DEFINITIVA,  null);
		
		MAPPING_STATO = Collections.unmodifiableMap(tmp);
	}
	
	private String etichettaOrganoAmministrativo;
	private String etichettaOrganoLegislativo;
	private StatoOperativoVariazioneDiBilancio statoOperativoVariazioneBilancio;
	
	/**
	 * Instantiates a new elemento stato operativo variazione.
	 *
	 * @param statoOperativoVariazioneBilancio the stato operativo variazione bilancio
	 * @param etichettaOrganoAmministrativo the etichetta organo amministrativo
	 * @param etichettaOrganoLegislativo the etichetta organo legislativo
	 */
	public ElementoStatoOperativoVariazione(StatoOperativoVariazioneDiBilancio statoOperativoVariazioneBilancio, String etichettaOrganoAmministrativo, String etichettaOrganoLegislativo){
		this.etichettaOrganoAmministrativo = etichettaOrganoAmministrativo;
		this.etichettaOrganoLegislativo = etichettaOrganoLegislativo;
		this.statoOperativoVariazioneBilancio = statoOperativoVariazioneBilancio;
	}
	
	/**
	 * Crea la mappa dei parametri da utilizzare
	 *
	 * @return the map
	 */
	private  Map<String,String> creaMappaParametri() {
		Map<String,String> mappaParametri = new HashMap<String, String>();
		mappaParametri.put("%VARIAZ_ORGANO_AMM%", this.etichettaOrganoAmministrativo);
		mappaParametri.put("%VARIAZ_ORGANO_LEG%", this.etichettaOrganoLegislativo);
		return mappaParametri;
	}
	
	/**
	 * Gets the stato operativo variazione.
	 *
	 * @return the stato operativo variazione
	 */
	public StatoOperativoVariazioneDiBilancio getStatoOperativoVariazioneBilancio() {
		return statoOperativoVariazioneBilancio;
	}
	
	/**
	 * Gets the descrizione composta stato operativo variazione.
	 *
	 * @return the descrizione composta stato operativo variazione
	 */
	public String getDescrizione(){
		String stringPattern = MAPPING_STATO.get(statoOperativoVariazioneBilancio);
		if(StringUtils.isBlank(stringPattern)) {
			return statoOperativoVariazioneBilancio.getDescrizione();
		}
		Map<String,String> map = creaMappaParametri(); 
		Pattern pattern = Pattern.compile("%(VARIAZ_ORGANO_AMM|VARIAZ_ORGANO_LEG)%");
		return StringUtilities.replacePlaceholders(pattern, stringPattern, map, true).toUpperCase(Locale.ITALY);
	}
	
}

