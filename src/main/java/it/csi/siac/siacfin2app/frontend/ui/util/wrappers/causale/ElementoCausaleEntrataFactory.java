/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.causale;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Factory per il wrapping delle Causali di Entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 17/04/2014
 */
public final class ElementoCausaleEntrataFactory extends ElementoCausaleFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCausaleEntrataFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dalla Causale.
	 * @param <C> la tipizzazione della causale
	 * @param causale                               la causale da wrappare
	 * @param listaTipoAtto                         la lista dei tipi atto per recuperare la descrizione
	 * @param listaStrutturaAmministrativoContabile la lista delle struture amministrative per recuperare la descrizione
	 * @param gestioneUEB                           se l'ente gestisce le UEB
	 * 
	 * @return il wrapper creato
	 */
	public static <C extends CausaleEntrata> ElementoCausale getInstance(C causale, List<TipoAtto> listaTipoAtto, 
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, Boolean gestioneUEB) {
		
		ElementoCausale result = new ElementoCausale();
		
		CapitoloEntrataGestione capitolo = causale.getCapitoloEntrataGestione();
		Accertamento accertamento = causale.getAccertamento();
		SubAccertamento subAccertamento = causale.getSubAccertamento();
		AttoAmministrativo attoAmministrativo = causale.getAttoAmministrativo();
		
		TipoAtto tipoAtto = null;
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = null;
		
		if(attoAmministrativo != null) {
			tipoAtto =ComparatorUtils.searchByUid(listaTipoAtto, attoAmministrativo.getTipoAtto());
			strutturaAmministrativoContabile = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, 
					attoAmministrativo.getStrutturaAmmContabile());
		}
		
		populateAttributes(causale, result, capitolo, accertamento, subAccertamento, tipoAtto, strutturaAmministrativoContabile, gestioneUEB);
		
		return result;
	}
	
  
	/**
	 * Ottiene istanze del wrapper a partire dalle causale
	 * 
	 * @param causali                               le causali da wrappare
	 * @param listaTipoAtto                         la lista dei tipi atto per recuperare la descrizione
	 * @param listaStrutturaAmministrativoContabile la lista delle struture amministrative per recuperare la descrizione
	 * @param gestioneUEB                           se l'ente gestisce le UEB
	 * 
	 * @return i wrapper creati
	 */
	public static List<ElementoCausale> getInstances (List<CausaleEntrata> causali,  List<TipoAtto> listaTipoAtto, 
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile, Boolean gestioneUEB) {
		List<ElementoCausale> result = new ArrayList<ElementoCausale>();

		for(CausaleEntrata causale : causali) {
			result.add(getInstance(causale, listaTipoAtto, listaStrutturaAmministrativoContabile, gestioneUEB));
		}
		
		return result;
	}

}
