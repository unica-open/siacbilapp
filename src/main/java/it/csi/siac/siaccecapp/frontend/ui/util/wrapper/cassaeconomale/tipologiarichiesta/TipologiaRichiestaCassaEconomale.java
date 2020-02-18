/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccorser.model.AzioneConsentita;

/**
 * Tipologia per la richiesta della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/12/2014
 *
 */
public class TipologiaRichiestaCassaEconomale implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5033644585901356484L;
	
	private TipoRichiestaEconomale tipoRichiestaEconomale;
	private List<AbilitazioneRichiestaCassaEconomale> listaAbilitazioneRichiestaCassaEconomale = new ArrayList<AbilitazioneRichiestaCassaEconomale>();
	/**
	 * @return the tipoRichiestaEconomale
	 */
	public TipoRichiestaEconomale getTipoRichiestaEconomale() {
		return tipoRichiestaEconomale;
	}
	/**
	 * @param tipoRichiestaEconomale the tipoRichiestaEconomale to set
	 */
	public void setTipoRichiestaEconomale(TipoRichiestaEconomale tipoRichiestaEconomale) {
		this.tipoRichiestaEconomale = tipoRichiestaEconomale;
	}
	/**
	 * @return the listaAbilitazioneRichiestaCassaEconomale
	 */
	public List<AbilitazioneRichiestaCassaEconomale> getListaAbilitazioneRichiestaCassaEconomale() {
		return listaAbilitazioneRichiestaCassaEconomale;
	}
	/**
	 * @param listaAbilitazioneRichiestaCassaEconomale the listaAbilitazioneRichiestaCassaEconomale to set
	 */
	public void setListaAbilitazioneRichiestaCassaEconomale(List<AbilitazioneRichiestaCassaEconomale> listaAbilitazioneRichiestaCassaEconomale) {
		this.listaAbilitazioneRichiestaCassaEconomale = listaAbilitazioneRichiestaCassaEconomale != null ? listaAbilitazioneRichiestaCassaEconomale : new ArrayList<AbilitazioneRichiestaCassaEconomale>();
	}
	
	/**
	 * Aggiunge un'abilitazione alla lista.
	 * 
	 * @param nome             il nome dell'abilitazione
	 * @param url              l'URL
	 * @param azioniRichieste  le azioni richieste
	 * @param azioniConsentite le azioni consentite
	 */
	protected void addAbilitazioneRichiestaCassaEconomale(String nome, String url, List<AzioneConsentita> azioniConsentite, AzioniConsentite... azioniRichieste) {
		AbilitazioneRichiestaCassaEconomale abilitazione = new AbilitazioneRichiestaCassaEconomale();
		abilitazione.setAbilitato(AzioniConsentiteFactory.isConsentitoAll(azioniConsentite, azioniRichieste));
		abilitazione.setNome(nome);
		abilitazione.setUrl(url);
		getListaAbilitazioneRichiestaCassaEconomale().add(abilitazione);
	}
	
}
