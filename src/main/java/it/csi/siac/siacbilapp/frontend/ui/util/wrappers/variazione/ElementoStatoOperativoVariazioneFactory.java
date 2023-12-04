/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siaccommonapp.util.exception.FrontEndUncheckedException;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * Factory per il wrapping delle Variazioni
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 12/11/2013
 *
 */
public final class ElementoStatoOperativoVariazioneFactory {
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	private static final String ETICHETTA_ORGANO_AMM_DEFAULT = "GIUNTA";
	private static final String ETICHETTA_ORGANO_LEG_DEFAULT = "CONSIGLIO";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoStatoOperativoVariazioneFactory() {
	}
	
	/**
	 * Ottiene l'instanza of ElementoStatoOperativoVariazione.
	 *
	 * @param gestioneLivelli the gestione livelli
	 * @param statoOperativoVariazioneBilancio the stato operativo variazione di bilancio
	 * @return single instance of ElementoStatoOperativoVariazioneFactory
	 */
	public static ElementoStatoOperativoVariazione getInstance(Map<TipologiaGestioneLivelli, String> gestioneLivelli, StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio){
		if(statoOperativoVariazioneBilancio == null || gestioneLivelli == null) {
			//non gestisco il caso il cui lo stato e' null: deve PER forza essere valorizzato
			throw new FrontEndUncheckedException("stato operativo variazione di bilancio e configurazione dell'ente devono essere entrambi presenti.");
		}
		String organoAmministrativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_AMM, ETICHETTA_ORGANO_AMM_DEFAULT);
		String organoLegislativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_LEG, ETICHETTA_ORGANO_LEG_DEFAULT);
		ElementoStatoOperativoVariazione wrapper = new ElementoStatoOperativoVariazione(statoOperativoVariazioneBilancio,organoAmministrativo, organoLegislativo);
		return wrapper;
	}

	/**
	 * Gets the single instance of ElementoStatoOperativoVariazioneFactory.
	 *
	 * @param gestioneLivelli the gestione livelli
	 * @param statiOperativi the stati operativi
	 * @return single instance of ElementoStatoOperativoVariazioneFactory
	 */
	public static List<ElementoStatoOperativoVariazione> getInstances(Map<TipologiaGestioneLivelli, String> gestioneLivelli, List<StatoOperativoVariazioneBilancio> statiOperativi){
		List<ElementoStatoOperativoVariazione> listaWrapper = new ArrayList<ElementoStatoOperativoVariazione>();
		String organoAmministrativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_AMM, ETICHETTA_ORGANO_AMM_DEFAULT);
		String organoLegislativo = ottieniEtichettaByConfigurazioneEnte(gestioneLivelli, TipologiaGestioneLivelli.VARIAZ_ORGANO_LEG, ETICHETTA_ORGANO_LEG_DEFAULT);
		for (StatoOperativoVariazioneBilancio st : statiOperativi) {
			ElementoStatoOperativoVariazione wrapper = new ElementoStatoOperativoVariazione(st, organoAmministrativo, organoLegislativo);
			listaWrapper.add(wrapper);
		}
		return listaWrapper;
	}
	
	/**
	 * @param gestioneLivelli
	 * @return
	 */
	private static String ottieniEtichettaByConfigurazioneEnte(Map<TipologiaGestioneLivelli, String> gestioneLivelli, TipologiaGestioneLivelli tipologiaGestioneLivelli, String etichettaDefault) {
		String parametroEnteOrganoAmm = gestioneLivelli.get(tipologiaGestioneLivelli);
		String organoAmministrativo = StringUtils.isNotBlank(parametroEnteOrganoAmm)? parametroEnteOrganoAmm : etichettaDefault;
		return organoAmministrativo;
	}

}
