/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.util.List;

import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
/**
 * ElementoElencoDocumentiAllegatoFactory  factory per ElementoElencoDocumentiAllegato
 * @author Nazha Ahmad,Alessandro Marchino
 * @version 1.0.0
 * @DATE 27/07/2016
 *
 */
public final class ElementoElencoDocumentiAllegatoFactory {

	/** Costruttore privato */
	private ElementoElencoDocumentiAllegatoFactory() {
	}
	 
	/**
	 * ElementoElencoDocumentiAllegato
	 * @param subdoc il subdocumento da wrappare
	 * @param datiSoggettiAllegato i dati del soggetto allegato da wrappare
	 * @param elencoDocumentiAllegato l'elenco da wrappare
	 * @param isGestioneUeb se la gestione UEB sia attiva
	 * @return il wrapper
	 */
	public static ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> getInstance(Subdocumento<?, ?> subdoc, List<DatiSoggettoAllegato> datiSoggettiAllegato,
			ElencoDocumentiAllegato elencoDocumentiAllegato, boolean isGestioneUeb) {
		if (subdoc instanceof SubdocumentoSpesa) {
			return getInstance((SubdocumentoSpesa) subdoc, datiSoggettiAllegato,elencoDocumentiAllegato,isGestioneUeb);
		}
		
		if (subdoc instanceof SubdocumentoEntrata) {
			return getInstance((SubdocumentoEntrata) subdoc, datiSoggettiAllegato,elencoDocumentiAllegato,isGestioneUeb);
		}
		
		return null;
	}

	/**
	 * ElementoElencoDocumentiAllegatoSpesa
	 * @param subdoc il subdocumento da wrappare
	 * @param datiSoggettiAllegato i dati del soggetto allegato da wrappare
	 * @param elencoDocumentiAllegato l'elenco da wrappare
	 * @param isGestioneUeb se la gestione UEB sia attiva
	 * @return il wrapper
	 */
	public static ElementoElencoDocumentiAllegatoSpesa getInstance(SubdocumentoSpesa subdoc, List<DatiSoggettoAllegato> datiSoggettiAllegato,
			ElencoDocumentiAllegato elencoDocumentiAllegato, boolean isGestioneUeb) {
		
		Documento<?, ?> doc = subdoc.getDocumento();
		if(doc == null || doc.getSoggetto() == null) {
			// Non ho i dati del documento. che si fa ??
			return null;
		}
		
		DatiSoggettoAllegato dsa = ottieniDatiSoggettoAllegatoViaSoggetto(doc.getSoggetto(), datiSoggettiAllegato);
		return new ElementoElencoDocumentiAllegatoSpesa(elencoDocumentiAllegato, dsa, Boolean.valueOf(isGestioneUeb), subdoc);
	}

	/**
	 * ElementoElencoDocumentiAllegatoEntrata
	 * @param subdoc il subdocumento da wrappare
	 * @param datiSoggettiAllegato i dati del soggetto allegato da wrappare
	 * @param elencoDocumentiAllegato l'elenco da wrappare
	 * @param isGestioneUeb se la gestione UEB sia attiva
	 * @return il wrapper
	 */
	public static ElementoElencoDocumentiAllegatoEntrata getInstance(SubdocumentoEntrata subdoc, List<DatiSoggettoAllegato> datiSoggettiAllegato,
			ElencoDocumentiAllegato elencoDocumentiAllegato, boolean isGestioneUeb) {
		
		Documento<?, ?> doc = subdoc.getDocumento();
		if(doc == null || doc.getSoggetto() == null) {
			// Non ho i dati del documento. che si fa ??
			return null;
		}
		
		DatiSoggettoAllegato dsa = ottieniDatiSoggettoAllegatoViaSoggetto(doc.getSoggetto(),datiSoggettiAllegato);
		return new ElementoElencoDocumentiAllegatoEntrata(elencoDocumentiAllegato, dsa, Boolean.valueOf(isGestioneUeb), subdoc);
		}

	
	
	/**
	 * Ottiene i datiSoggettoAllegato a partire dal soggetto corrispondente.
	 * 
	 * @param soggetto             il soggetto da ricercare
	 * @param datiSoggettiAllegato i dati del soggetto allegato
	 * 
	 * @return i dati relativi al soggetto, se presenti
	 */
	private static DatiSoggettoAllegato ottieniDatiSoggettoAllegatoViaSoggetto(Soggetto soggetto, List<DatiSoggettoAllegato> datiSoggettiAllegato) {
		for (DatiSoggettoAllegato dsa : datiSoggettiAllegato) {
			if (soggetto.getUid() != 0 && dsa.getSoggetto() != null && soggetto.getUid() == dsa.getSoggetto().getUid()) {
				return dsa;
			}
		}
		return null;
	}
}