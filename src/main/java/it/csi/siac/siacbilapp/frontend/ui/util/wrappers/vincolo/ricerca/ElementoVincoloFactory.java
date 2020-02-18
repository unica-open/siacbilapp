/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.ricerca;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;

/**
 * Factory per il wrapping dei Vincoli.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 07/01/2014
 *
 */
public final class ElementoVincoloFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoVincoloFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal vincolo tra capitoli.
	 * 
	 * @param vincoloCapitoli il vincolo da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoVincolo getInstance(VincoloCapitoli vincoloCapitoli) {
		ElementoVincolo result = new ElementoVincolo();
		
		Integer uid = vincoloCapitoli.getUid();
		String codice = vincoloCapitoli.getCodice();
		String descrizione = vincoloCapitoli.getDescrizione();
		String trasferimentiVincolati = Boolean.TRUE.equals(vincoloCapitoli.getFlagTrasferimentiVincolati()) ? "Sì" : "No";
		String bilancio = StringUtils.capitalize(vincoloCapitoli.getTipoVincoloCapitoli().name().toLowerCase());
		Integer numeroCapitoliEntrata = Math.max(vincoloCapitoli.getCapitoliEntrataGestione().size(), vincoloCapitoli.getCapitoliEntrataPrevisione().size());
		Integer numeroCapitoliUscita = Math.max(vincoloCapitoli.getCapitoliUscitaGestione().size(), vincoloCapitoli.getCapitoliUscitaPrevisione().size());
		StatoOperativo statoOperativo = vincoloCapitoli.getStatoOperativo();
		
		result.setUid(uid);
		result.setCodice(codice);
		result.setDescrizione(descrizione);
		result.setBilancio(bilancio);
		result.setTrasferimentiVincolati(trasferimentiVincolati);
		result.setNumeroCapitoliEntrata(numeroCapitoliEntrata);
		result.setNumeroCapitoliUscita(numeroCapitoliUscita);
		result.setStatoOperativo(statoOperativo);
		
		return result;
	}
	
}
