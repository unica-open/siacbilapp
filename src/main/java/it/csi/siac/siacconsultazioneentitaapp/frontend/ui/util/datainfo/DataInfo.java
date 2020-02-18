/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

import java.util.Map;

/**
 * Descrizione informazioni di un Dato da mostrare a front-end.
 * Tale dato pu&ograve; essere il valore di una colonna oppure una generica etichetta da mostrare.
 * 
 * @author Domenico Lisi
 * @author Alessandro Marchino
 * @version 1.0.0
 * @version 1.1.0 - 28/06/2016 - gestione della tipizzazione della risposta
 * @param <D> la tipizzazione del dato in risposta
 *
 */
public interface DataInfo<D> {
	
	
	/**
	 * Restituisce il dato formattato da mostrare a front-end a partire dalla mappa dei campi.
	 * 
	 * @param campi campi che arrivano dal servizio.
	 * @return dato ad esempio il dato contenuto nella colonna con il relativo HTML.
	 */
	D getData(Map<String, Object> campi);

	
	/**
	 * Ottiene il nome identificativo del dato.
	 * 
	 * @return nome nel caso sia una colonna ad esempio conterr&agrave; il nome della colonna.
	 */
	String getName();
	
	/**
	 * Imposto la possibilit&agrave; di usare il Fallback ovvero l'utilizzo di un'eventuale
	 * ricalcolo della stringa a fronte di condizione nulla sulla prossima elaborazione
	 * @return se il fallback possa essere usato
	 */
	boolean mayUseFallback();
	
	/**
	 * Mappa dei dati utilizzati nel caso di fallback
	 * @param campi campi che arrivano dal servizio.
	 * @return dato ad esempio il dato contenuto nella colonna con il relativo HTML per il fallback.
	 */
	D getFallbackData(Map<String, Object> campi);
	
	/**
	 * Se &eacute; l'informazione contenente l'importo sommabile
	 * @return se il dato contiente un importo sommabile l'importo sia sommabile
	 */
	boolean isSummableImporto();
}