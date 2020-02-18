/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.datainfo;

/**
 * Enumera le colonne
 * 
 * @author Domenico
 *
 */
public interface ColumnEntitaConsultabile {
	
	/**
	 * Ottiene il nome della colonna
	 * @return il nome
	 */
	String name();
	
	/**
	 * Ottiene le informazioni della colonna
	 * @return le info della colonna
	 */
	DataInfo<?> getColumInfo();

}
