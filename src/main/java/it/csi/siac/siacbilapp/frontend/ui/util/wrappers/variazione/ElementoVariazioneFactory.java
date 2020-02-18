/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;

/**
 * Factory per il wrapping delle Variazioni
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 12/11/2013
 *
 */
public final class ElementoVariazioneFactory {
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	private static final String UNDEFINED = "";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoVariazioneFactory() {
	}
	
	/**
	 * Wrapper per le Variazioni
	 * 
	 * @param variazione la variazione da Wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoVariazione getInstance(VariazioneDiBilancio variazione) {
		if(variazione == null) {
			return null;
		}
		ElementoVariazione wrapper = new ElementoVariazione();
		popolaDatiBase(variazione, wrapper);
		if(variazione instanceof VariazioneCodificaCapitolo) {
			popolaDatiCodifica((VariazioneCodificaCapitolo) variazione, wrapper);
		} else if(variazione instanceof VariazioneImportoCapitolo) {
			popolaDatiImporto((VariazioneImportoCapitolo) variazione, wrapper);
		} else if(variazione instanceof VariazioneImportoSingoloCapitolo) {
			popolaDatiImportoSingoloCapitolo((VariazioneImportoSingoloCapitolo) variazione, wrapper);
		}
		return wrapper;
	}
	
	/**
	 * Gets the single instance of ElementoVariazioneFactory.
	 *
	 * @param variazione the variazione
	 * @param gestioneLivelli the gestione livelli
	 * @return single instance of ElementoVariazioneFactory
	 */
	public static ElementoVariazione getInstance(VariazioneDiBilancio variazione, Map<TipologiaGestioneLivelli, String> gestioneLivelli) {
		ElementoVariazione wrapper = getInstance(variazione);
		if(wrapper == null) {
			return null;
		}
		
		ElementoStatoOperativoVariazione elementoStatoOperativoVariazione = ElementoStatoOperativoVariazioneFactory.getInstance(gestioneLivelli, wrapper.getStatoVariazione());
		wrapper.setElementoStatoOperativoVariazione(elementoStatoOperativoVariazione);
		return wrapper;
	}
	
	/**
	 * Popolamento dei dati di base
	 * @param variazione la variazione
	 * @param wrapper il wrapper da popolare
	 */
	private static void popolaDatiBase(VariazioneDiBilancio variazione, ElementoVariazione wrapper) {
		wrapper.setUid(variazione.getUid());
		wrapper.setNumero(variazione.getNumero());
		wrapper.setDescrizione(variazione.getDescrizione() == null ? UNDEFINED : variazione.getDescrizione());
		wrapper.setTipoVariazione(variazione.getTipoVariazione());
		wrapper.setStatoVariazione(variazione.getStatoOperativoVariazioneDiBilancio());
		wrapper.setAnno(variazione.getBilancio().getAnno());
		//SIAC-6884
		wrapper.setDataAperturaProposta(variazione.getDataAperturaProposta());
		wrapper.setDataChiusuraProposta(variazione.getDataChiusuraProposta());
		wrapper.setStrutturaAmministrativoContabileDirezioneProponente(variazione.getDirezioneProponente());
	}
	
	/**
	 * Popolamento dei dati della variazione codifica
	 * @param variazione la variazione
	 * @param wrapper il wrapper da popolare
	 */
	private static void popolaDatiCodifica(VariazioneCodificaCapitolo variazione, ElementoVariazione wrapper) {
		wrapper.setProvvedimento(componiStringaAttoAmministrativo(variazione.getAttoAmministrativo()));
		wrapper.setApplicazione(ottieniApplicazione(variazione.getCapitoli()));
	}
	
	/**
	 * Popolamento dei dati della variazione importi
	 * @param variazione la variazione
	 * @param wrapper il wrapper da popolare
	 */
	private static void popolaDatiImporto(VariazioneImportoCapitolo variazione, ElementoVariazione wrapper) {
		wrapper.setProvvedimento(componiStringaAttoAmministrativo(variazione.getAttoAmministrativo()));
		wrapper.setProvvedimentoBilancio(componiStringaAttoAmministrativo(variazione.getAttoAmministrativoVariazioneBilancio()));
		wrapper.setApplicazione(ottieniApplicazione(variazione.getCapitoli()));
	}
	
	/**
	 * Popolamento dei dati della variazione importi
	 * @param variazione la variazione
	 * @param wrapper il wrapper da popolare
	 */
	private static void popolaDatiImportoSingoloCapitolo(VariazioneImportoSingoloCapitolo variazione, ElementoVariazione wrapper) {
		// Da deprecare?
		wrapper.setProvvedimento(componiStringaAttoAmministrativo(variazione.getAttoAmministrativo()));
		wrapper.setProvvedimentoBilancio(componiStringaAttoAmministrativo(variazione.getAttoAmministrativoVariazioneDiBilancio()));
		
		List<Capitolo<?, ?>> list = new ArrayList<Capitolo<?,?>>();
		list.add(variazione.getCapitolo());
		wrapper.setApplicazione(ottieniApplicazione(list));
	}
	
	/**
	 * Composizione della stringa dell'atto amministrativo
	 * @param attoAmministrativo l'atto
	 * @return la stringa dell'atto
	 */
	private static String componiStringaAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		if(attoAmministrativo == null) {
			return UNDEFINED;
		}
		return new StringBuilder()
				.append(attoAmministrativo.getAnno())
				.append("/")
				.append(attoAmministrativo.getNumero())
				.append(attoAmministrativo.getTipoAtto() != null ? "/" + attoAmministrativo.getTipoAtto().getCodice() : "")
				.toString();
	}

	/**
	 * Ottiene l'applicazione a partire dalla variazione.
	 * <br>
	 * <strong>ATTENZIONE!</strong>
	 * <br>
	 * Ad oggi ottiene solo le applicazioni <code>Previsione</code> e <code>Gestione</code>
	 * 
	 * @param listaCapitoli la lista dei capitoli
	 * @return l'applicazione
	 * 
	 * @throws IllegalArgumentException nel caso in cui non sia stato possibile estrarre l'applicazione
	 */
	private static String ottieniApplicazione(List<Capitolo<?, ?>> listaCapitoli) {
		String result = "";
		if(!listaCapitoli.isEmpty()){
			TipoCapitolo tipoCapitolo = listaCapitoli.get(0).getTipoCapitolo();
			if(tipoCapitolo == TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE || tipoCapitolo == TipoCapitolo.CAPITOLO_USCITA_PREVISIONE) {
				result = "Previsione";
			} else {
				result = "Gestione";
			}
		}
		return result;
	}
}
