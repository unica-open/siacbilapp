/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.stornoueb;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siacbilser.model.TipoCapitolo;

/**
 * Factory per il wrapping degli storni UEB.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 8/09/2013
 *
 */
public final class ElementoStornoUEBFactory {
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	// Un tempo era "undefined"
	private static final String UNDEFINED = "";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoStornoUEBFactory() {
	}
	
	/**
	 * Wrapper per lo Storno UEB.
	 * 
	 * @param storno lo storno da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoStornoUEB getInstance(StornoUEB storno) {
		if(storno == null) {
			return null;
		}
		ElementoStornoUEB wrapper = new ElementoStornoUEB();
		
		// Campi da injettare
		int uid;
		String numeroStorno;
		String capitoloSorgente;
		String capitoloDestinazione;
		String provvedimento;

		// Valorizzazione dei campi
		uid = storno.getUid();
		numeroStorno = (storno.getNumero() == null ? UNDEFINED : storno.getNumero().toString());
		
		Capitolo<?, ?> sorgente = storno.getCapitoloSorgente();
		Capitolo<?, ?> destinazione = storno.getCapitoloDestinazione();
		AttoAmministrativo attoAmministrativo = storno.getAttoAmministrativo();
		
		// Gestione del capitolo sorgente
		capitoloSorgente = ottieniCampiDelCapitolo(sorgente);
		
		// Gestione del capitolo di destinazione
		capitoloDestinazione = ottieniCampiDelCapitolo(destinazione);
		
		// Gestione del provvedimento
		if(attoAmministrativo != null) {
			provvedimento = attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero();
		} else {
			provvedimento = UNDEFINED;
		}
		
		// Injezione dei campi
		wrapper.setUid(uid);
		wrapper.setNumeroStorno(numeroStorno);
		wrapper.setCapitoloSorgente(capitoloSorgente);
		wrapper.setCapitoloDestinazione(capitoloDestinazione);
		wrapper.setProvvedimento(provvedimento);
		
		return wrapper;
	}
	
	/**
	 * Metodo di utilit&agrave; per ottenere i campi del capitolo.
	 * 
	 * @param capitolo il capitolo da cui ottenere i dati
	 * 
	 * @return la stringa rappresentante il capitolo
	 */
	private static String ottieniCampiDelCapitolo(Capitolo<?, ?> capitolo) {
		String result = "";
		if(capitolo == null) {
			result = UNDEFINED;
		} else {
			// Costruisco il prefisso del wrapper a partire dal tipo di capitolo, notando che il tipo può essere solo di gestione
			String prefisso = capitolo.getTipoCapitolo() == TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE ? "E" : "S";
			result = prefisso + "-" + capitolo.getAnnoCapitolo() + "/" + capitolo.getNumeroCapitolo() + "/" +
					capitolo.getNumeroArticolo() + "-" + capitolo.getNumeroUEB();
		}
		return result;
	}
	
}
