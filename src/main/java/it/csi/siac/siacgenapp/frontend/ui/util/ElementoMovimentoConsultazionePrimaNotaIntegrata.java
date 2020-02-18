/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.util;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * Classe di wrap per la consultazione del movimento collegato alla prima nota integrata
 * @author Alessandro Marchino
 *
 * @param <E> la tipizzazione del movimento
 */
public abstract class ElementoMovimentoConsultazionePrimaNotaIntegrata<E extends Entita> implements ModelWrapper, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1394907977294161242L;
	
	/** Il movimento */
	protected final E movimento;
	
	/**
	 * Costruttore di wrap
	 * @param movimento il movimento da wrappare
	 */
	protected ElementoMovimentoConsultazionePrimaNotaIntegrata(E movimento) {
		this.movimento = movimento;
	}
	
	@Override
	public int getUid() {
		return movimento != null ? movimento.getUid() : 0;
	}
	
	// Implementazioni di base, vuote
	/**
	 * @return the anno
	 */
	public String getAnno() {
		return "";
	}
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return "";
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return "";
	}
	
	/**
	 * @return the soggetto
	 */
	public String getSoggetto() {
		return "";
	}
	
	/**
	 * @return the pianoDeiConti
	 */
	public String getPianoDeiConti() {
		return "";
	}
	
	/**
	 * @return the importo
	 */
	public String getImporto() {
		return "";
	}
	
	/**
	 * Impegno / Accertamento
	 * @return the movimentoGestione
	 */
	public String getMovimentoGestione() {
		return "";
	}
	
	/**
	 * @return the liquidazione
	 */
	public String getLiquidazione() {
		return "";
	}
	
	/**
	 * @return the ordinativo
	 */
	public String getOrdinativo() {
		return "";
	}
	
	/**
	 * Permette di ottenere eventuali dati accessori dell'entita che devono essere visualizzati.
	 * @return the datiAccessori
	 */
	public String getDatiAccessorii(){
		//implementazione di default, ritorna una stringa vuota, da overridare nelle classi figlie
		return "";
	}
	
	/**
	 * Controlla se il piano dei conti sia valorizzato per il movimento di gestione
	 * @param mg il movimento di gestione
	 * @return true se il pdc &eacute; valorizzato
	 */
	protected boolean isValorizzatoPdc(MovimentoGestione mg) {
		return mg != null && StringUtils.isNotBlank(mg.getCodPdc()) && StringUtils.isNotBlank(mg.getDescPdc());
	}
	
	/**
	 * Estrae la stringa del piano dei conti per il movimento di gestione
	 * @param mg il movimento di gestione
	 * @return la stringa descrivente il pdc
	 */
	protected String extractPianoDeiConti(MovimentoGestione mg) {
		return new StringBuilder()
			.append(mg.getCodPdc())
			.append(" - ")
			.append(mg.getDescPdc())
			.toString();
	}
}
