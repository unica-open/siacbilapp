/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Helper per la consultazione dei dati della generica entit&agrave;
 * @author Marchino Alessandro
 * @param <E> la tipizzazione dell'entit&agrave;
 *
 */
public abstract class ConsultaRegistrazioneMovFinBaseHelper<E extends Entita> implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 184208977107737834L;
	
	private final boolean isGestioneUEB;
	
	/**
	 * Costruttore di wrap
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ConsultaRegistrazioneMovFinBaseHelper(boolean isGestioneUEB) {
		this.isGestioneUEB = isGestioneUEB;
	}
	
	/**
	 * @return the datiCreazioneModifica
	 */
	public abstract String getDatiCreazioneModifica();
	
	/**
	 * Calcolo dei dati di creazione e di modifica.
	 * 
	 * @param dataCreazione  la data di creazione dell'entita
	 * @param loginCreazione il login di creazione dell'entita
	 * @param dataModifica   la data di ultima modifica dell'entita
	 * @param loginModifica  il login di ultima modifica dell'entita
	 * 
	 * @return una stringa corrispondente ai dati di creazione e modifica
	 */
	protected String calcolaDatiCreazioneModifica(Date dataCreazione, String loginCreazione, Date dataModifica, String loginModifica) {
		return new StringBuilder()
			.append("Inserimento: ")
			.append(FormatUtils.formatDate(dataCreazione))
			.append(" (")
			.append(loginCreazione != null ? loginCreazione : "")
			.append(") - Ultima modifica: ")
			.append(FormatUtils.formatDate(dataModifica))
			.append(" (")
			.append(loginModifica != null ? loginModifica : "")
			.append(")")
			.toString();
	}
	
	/**
	 * Calcolo dei dati del capitolo
	 * @param capitolo il capitolo
	 * @return i dati del capitolo
	 */
	protected String calcolaDatiCapitolo(Capitolo<?, ?> capitolo) {
		if(capitolo == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(capitolo.getNumeroCapitolo())
			.append("/")
			.append(capitolo.getNumeroArticolo());
		if(isGestioneUEB) {
			sb.append("/")
				.append(capitolo.getNumeroUEB());
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo dei dati del soggetto.
	 * 
	 * @param soggetto il soggetto da cui ottenere i dati
	 * 
	 * @return i dati del soggetto
	 */
	protected String calcolaDatiSoggetto(Soggetto soggetto) {
		List<String> chunks = new ArrayList<String>();
		
		if(StringUtils.isNotBlank(soggetto.getCodiceSoggetto())) {
			chunks.add(soggetto.getCodiceSoggetto());
		}
		if(StringUtils.isNotBlank(soggetto.getDenominazione())) {
			chunks.add(soggetto.getDenominazione());
		}
		if(StringUtils.isNotBlank(soggetto.getCodiceFiscale())) {
			chunks.add("CF: " + soggetto.getCodiceFiscale());
		}
		if(StringUtils.isNotBlank(soggetto.getPartitaIva())) {
			chunks.add("P.IVA: " + soggetto.getPartitaIva());
		}
		
		return StringUtils.join(chunks, " - ");
	}
	
}
