/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.vincolo.consultazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.vincolo.ElementoCapitoloVincoloFactory;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.Vincolo;
import it.csi.siac.siacbilser.model.VincoloCapitoli;

/**
 * Factory per il wrapping dei Vincoli per l'uso nella consultazione del capitolo.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 08/01/2014
 *
 */
public final class ElementoVincoloConsultazioneFactory {
	
	/** Non permettere l'instanziazione della classe */
	private ElementoVincoloConsultazioneFactory() {
	}
	
	/**
	 * Crea un'istanza del wrapper a partire dal vincolo tra capitoli.
	 * 
	 * @param vincoloCapitoli il vincolo da wrappare
	 * @param gestioneUEB     se si gestiscono o meno le UEB
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoVincoloConsultazione getInstance(VincoloCapitoli vincoloCapitoli, boolean gestioneUEB) {
		ElementoVincoloConsultazione result = new ElementoVincoloConsultazione();
		
		// Popolo a partire dal vincoloCapitoli
		Vincolo vincolo = vincoloCapitoli;
		
		// Stringhe di utilita'
		String stato = StringUtils.capitalize(vincolo.getStatoOperativo().name().toLowerCase());
		String tipoBilancio = StringUtils.capitalize(vincolo.getTipoVincoloCapitoli().name().toLowerCase());
		
		boolean vincoloPerPrevisione = "Previsione".equals(tipoBilancio);
		
		List<? extends Capitolo<?, ?>> listaEntrata = vincoloPerPrevisione ? vincoloCapitoli.getCapitoliEntrataPrevisione() : vincoloCapitoli.getCapitoliEntrataGestione();
		List<? extends Capitolo<?, ?>> listaUscita = vincoloPerPrevisione ? vincoloCapitoli.getCapitoliUscitaPrevisione() : vincoloCapitoli.getCapitoliUscitaGestione();
		
		List<ElementoCapitoloVincolo> listaCapitoliEntrata = ElementoCapitoloVincoloFactory.getInstances(listaEntrata, gestioneUEB);
		List<ElementoCapitoloVincolo> listaCapitoliUscita = ElementoCapitoloVincoloFactory.getInstances(listaUscita, gestioneUEB);
		
		BigDecimal totaleStanziamentoEntrata0 = BigDecimal.ZERO;
		BigDecimal totaleStanziamentoEntrata1 = BigDecimal.ZERO;
		BigDecimal totaleStanziamentoEntrata2 = BigDecimal.ZERO;
		
		BigDecimal totaleStanziamentoUscita0 = BigDecimal.ZERO;
		BigDecimal totaleStanziamentoUscita1 = BigDecimal.ZERO;
		BigDecimal totaleStanziamentoUscita2 = BigDecimal.ZERO;
		
		for(ElementoCapitoloVincolo capitoloEntrata : listaCapitoliEntrata) {
			totaleStanziamentoEntrata0 = totaleStanziamentoEntrata0.add(capitoloEntrata.getCompetenzaAnno0());
			totaleStanziamentoEntrata1 = totaleStanziamentoEntrata1.add(capitoloEntrata.getCompetenzaAnno1());
			totaleStanziamentoEntrata2 = totaleStanziamentoEntrata2.add(capitoloEntrata.getCompetenzaAnno2());
		}
		
		for(ElementoCapitoloVincolo capitoloUscita : listaCapitoliUscita) {
			totaleStanziamentoUscita0 = totaleStanziamentoUscita0.add(capitoloUscita.getCompetenzaAnno0());
			totaleStanziamentoUscita1 = totaleStanziamentoUscita1.add(capitoloUscita.getCompetenzaAnno1());
			totaleStanziamentoUscita2 = totaleStanziamentoUscita2.add(capitoloUscita.getCompetenzaAnno2());
		}
		
		// Injetto i dati
		result.setVincolo(vincolo);
		result.setStato(stato);
		result.setTipoBilancio(tipoBilancio);
		result.setListaCapitoliEntrata(listaCapitoliEntrata);
		result.setListaCapitoliUscita(listaCapitoliUscita);
		result.setTotaleStanziamentoEntrata0(totaleStanziamentoEntrata0);
		result.setTotaleStanziamentoEntrata1(totaleStanziamentoEntrata1);
		result.setTotaleStanziamentoEntrata2(totaleStanziamentoEntrata2);
		result.setTotaleStanziamentoUscita0(totaleStanziamentoUscita0);
		result.setTotaleStanziamentoUscita1(totaleStanziamentoUscita1);
		result.setTotaleStanziamentoUscita2(totaleStanziamentoUscita2);
		
		return result;
	}
	
	/**
	 * Crea un'istanza parziale del wrapper a partire dal vincolo tra capitoli.
	 * 
	 * @param vincoloCapitoli il vincolo da wrappare
	 * 
	 * @return il wrapper creato
	 */
	public static ElementoVincoloConsultazione getPartialInstance(VincoloCapitoli vincoloCapitoli) {
		ElementoVincoloConsultazione result = new ElementoVincoloConsultazione();
		
		// Popolo a partire dal vincoloCapitoli
		Vincolo vincolo = vincoloCapitoli;
		
		// Stringhe di utilita'
		String stato = StringUtils.capitalize(vincolo.getStatoOperativo().name().toLowerCase());
		String tipoBilancio = StringUtils.capitalize(vincolo.getTipoVincoloCapitoli().name().toLowerCase());
		
		// Injetto i dati
		result.setVincolo(vincolo);
		result.setStato(stato);
		result.setTipoBilancio(tipoBilancio);
		
		return result;
	}
	
	/**
	 * Crea istanze del wrapper a partire da una lista di vincoli tra capitoli.
	 * 
	 * @param lista           la lista di vincoli da wrappare
	 * @param gestioneUEB     se si gestiscono o meno le UEB
	 * 
	 * @return i wrapper creati
	 */
	public static List<ElementoVincoloConsultazione> getInstances(List<VincoloCapitoli> lista, Boolean gestioneUEB) {
		List<ElementoVincoloConsultazione> result = new ArrayList<ElementoVincoloConsultazione>();
		
		for(VincoloCapitoli vincoloCapitoli : lista) {
			result.add(getInstance(vincoloCapitoli, gestioneUEB));
		}
		return result;
	}
	
	/**
	 * Crea delle istanze parziali per la consultazione dei capitoli.
	 * 
	 * @param lista la lista da wrappare
	 * 
	 * @return i wrapper creati
	 */
	public static List<ElementoVincoloConsultazione> getPartialInstances(List<VincoloCapitoli> lista) {
		List<ElementoVincoloConsultazione> result = new ArrayList<ElementoVincoloConsultazione>();
		
		for(VincoloCapitoli vincoloCapitoli : lista) {
			result.add(getPartialInstance(vincoloCapitoli));
		}
		return result;
	}
	
}
