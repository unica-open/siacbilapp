/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.BaseFactory;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siaccommon.util.number.NumberUtil;

/**
 * Factory per il wrapping dei Progetti.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 05/02/2014
 */
public final class ElementoProgettoFactory extends BaseFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoProgettoFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal progetto.
	 * 
	 * @param progetto il progetto da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoProgetto getInstance(Progetto progetto) {
		ElementoProgetto result = new ElementoProgetto();
		
		int uid = progetto.getUid();
		String codice = progetto.getCodice();
		String descrizione = progetto.getDescrizione();
		String statoOperativoProgetto = progetto.getStatoOperativoProgetto() != null
				? capitaliseString(progetto.getStatoOperativoProgetto().name())
				: "";
		
		String provvedimento = progetto.getAttoAmministrativo()!= null ? progetto.getAttoAmministrativo().getAnnoNumeroTipo() : "";

		String ambito = progetto.getTipoAmbito() != null
				? capitaliseString(progetto.getTipoAmbito().getDescrizione())
				: "";
				
		String codiceTipoProgetto = progetto.getTipoProgetto() != null? progetto.getTipoProgetto().getCodice() : "";
		
		result.setUid(uid);
		result.setCodice(codice);
		result.setDescrizione(descrizione);
		result.setStatoOperativoProgetto(statoOperativoProgetto);
		result.setProvvedimento(provvedimento);
		result.setAmbito(ambito);
		result.setCodiceTipoProgetto(codiceTipoProgetto);
		result.setValoreComplessivo(NumberUtil.toImporto(progetto.getValoreComplessivo()));
		return result;
	}
	
	
   /**
	 * Ottiene un'istanza del wrapper a partire da un progetto.
	 * 
	 * @param progetti    i progetti da wrappare
 
	 * @return il wrapper creato
	 */
	public static List<ElementoProgetto> getInstances(List<Progetto> progetti) {
		List<ElementoProgetto> result = new ArrayList<ElementoProgetto>();
		
		for(Progetto progetto : progetti) {
			result.add(getInstance(progetto));
		}
		return result;
	}
	
}
