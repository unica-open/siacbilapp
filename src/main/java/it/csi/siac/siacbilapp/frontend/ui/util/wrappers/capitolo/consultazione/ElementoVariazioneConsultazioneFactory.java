/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.DatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;

/**
 * Factory per l'elemento delle variazioni per la consultazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/11/2013
 *
 */
public final class ElementoVariazioneConsultazioneFactory {
	
	/** Pattern per la conversione del nome dell'enum */
	private static final Pattern SNAKE_TO_CAMEL_PATTERN = Pattern.compile("_");
	
	/** Non permettere l'instanziazione della classe */
	private ElementoVariazioneConsultazioneFactory() {
	}
	
	/**
	 * Crea un'istanza di {@link ElementoVariazioneConsultazione} a partire dai dati della variazione.
	 * 
	 * @param datiVariazioneImportiCapitoloPerAnnoNonNegative i dati delle variazioni non negative
	 * @param datiVariazioneImportiCapitoloPerAnnoNegative i dati delle variazioni negative
	 * @param annoEsercizio l'anno di esercizio
	 * @return l'istanza del wrapper dei dati di consultazione relativa ai dati forniti
	 */
	public static ElementoVariazioneConsultazione getInstance(Map<Integer, DatiVariazioneImportoCapitoloAnno> datiVariazioneImportiCapitoloPerAnnoNonNegative,
			Map<Integer, DatiVariazioneImportoCapitoloAnno> datiVariazioneImportiCapitoloPerAnnoNegative, Integer annoEsercizio) {
		ElementoVariazioneConsultazione res = new ElementoVariazioneConsultazione();
		
		populate(res, datiVariazioneImportiCapitoloPerAnnoNonNegative, annoEsercizio.intValue(), "Aumento");
		populate(res, datiVariazioneImportiCapitoloPerAnnoNegative, annoEsercizio.intValue(), "Diminuzione");
		
		return res;
	}

	/**
	 * Popolamento dell'istanza.
	 * 
	 * @param res l'istanza da popolare
	 * @param datiVariazioneAnno i dati della variazione per cui effettuare il popolamento
	 * @param annoEsercizio l'anno di esercizio
	 * @param direzione la direzione di interesse del wrapper (aumento/diminuzione)
	 */
	private static void populate(ElementoVariazioneConsultazione res, Map<Integer, DatiVariazioneImportoCapitoloAnno> datiVariazioneAnno, int annoEsercizio, String direzione) {
		
		// Mi interessano solo gli anni esercizio, +1 e +2
		int[] deltasAnno = new int[] {0, 1, 2};
		for(int delta : deltasAnno) {
			populate(res, datiVariazioneAnno, annoEsercizio, direzione, delta);
		}
	}
	
	/**
	 * Popolamento per l'anno di interesse
	 * @param res l'istanza da popolare
	 * @param datiVariazioneAnno i dati della variazione per cui effettuare il popolamento
	 * @param annoEsercizio l'anno di esercizio
	 * @param direzione la direzione di interesse del wrapper (aumento/diminuzione)
	 * @param delta il delta dell'anno rispetto all'anno di esercizio
	 */
	private static void populate(ElementoVariazioneConsultazione res, Map<Integer, DatiVariazioneImportoCapitoloAnno> datiVariazioneAnno, int annoEsercizio, String direzione, int delta) {
		Integer anno = Integer.valueOf(annoEsercizio + delta);
		DatiVariazioneImportoCapitoloAnno datiVariazioneImportoCapitoloAnno = datiVariazioneAnno.get(anno);
		if(datiVariazioneImportoCapitoloAnno == null) {
			return;
		}
		
		for(Entry<StatoOperativoVariazioneDiBilancio, DatiVariazioneImportoCapitolo> entry : datiVariazioneImportoCapitoloAnno.getDatiVariazioneCapitolo().entrySet()) {
			populate(res, direzione, delta, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Popolamento dei dati
	 * @param res l'istanza da popolare
	 * @param direzione la direzione di interesse del wrapper (aumento/diminuzione)
	 * @param delta il delta dell'anno rispetto all'anno di esercizio
	 * @param sovdb lo stato della variazione
	 * @param dvic i dati della variazione
	 */
	private static void populate(ElementoVariazioneConsultazione res, String direzione,
			int delta, StatoOperativoVariazioneDiBilancio sovdb, DatiVariazioneImportoCapitolo dvic) {
		String metodo = "variazioniIn";
		String stato = computeStringForStato(sovdb);
		String anno = Integer.toString(delta);
		String stringaNumero = metodo + direzione + stato + "Num" + anno;
		String stringaCompetenza = metodo + direzione + stato + "Competenza" + anno;
		String stringaResiduo = metodo + direzione + stato + "Residuo" + anno;
		String stringaCassa = metodo + direzione + stato + "Cassa" + anno;
		
		ReflectionUtil.addToLong(res, stringaNumero, dvic.getNumero());
		addImporto(res, stringaCompetenza, dvic.getStanziamento());
		addImporto(res, stringaResiduo, dvic.getStanziamentoResiduo());
		addImporto(res, stringaCassa, dvic.getStanziamentoCassa());
	}
	
	/**
	 * Aggiunge l'importo nel wrapper
	 * @param res l'istanza da popolare
	 * @param fieldName il nome del campo da popolare
	 * @param importo l'importo da aggiungere
	 */
	private static void addImporto(ElementoVariazioneConsultazione res, String fieldName, BigDecimal importo) {
		if(importo == null) {
			return;
		}
		ReflectionUtil.addToBigDecimal(res, fieldName, importo.abs());
	}
	
	/**
	 * Computa la stringa corrispondente allo stato.
	 * 
	 * @param statoOperativoVariazioneDiBilancio lo stato operativo della variazione
	 * @return la stringa corrispondente allo stato
	 */
	private static String computeStringForStato(StatoOperativoVariazioneDiBilancio statoOperativoVariazioneDiBilancio) {
		if(statoOperativoVariazioneDiBilancio == null) {
			return "";
		}
		// Giunta e consiglio vanno insieme
		if(StatoOperativoVariazioneDiBilancio.CONSIGLIO.equals(statoOperativoVariazioneDiBilancio) || StatoOperativoVariazioneDiBilancio.GIUNTA.equals(statoOperativoVariazioneDiBilancio)) {
			return "GiuntaConsiglio";
		}
		String tmp = WordUtils.capitalizeFully(statoOperativoVariazioneDiBilancio.toString(), new char[]{'_'});
		Matcher matcher = SNAKE_TO_CAMEL_PATTERN.matcher(tmp);
		return matcher.replaceAll("");
	}
	
}
