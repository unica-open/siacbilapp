/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.MovimentoGestioneHelper;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Classe base di model per le azioni che necessitano del riepilogo della richiesta economale (eg. inserimento, aggiornamento, consultazione).
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 *
 */
public abstract class BaseRiepilogoRichiestaEconomaleModel extends BaseRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5115569597042433337L;
	
	private Impegno movimentoGestione;
	private SubImpegno subMovimentoGestione;
	private Impegno movimentoGestioneCopia;
	private SubImpegno subMovimentoGestioneCopia;
	//true se restituzione altro ufficio per ASM
	private boolean altroUfficio = false;
	
	/**
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Impegno movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubImpegno getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubImpegno subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}
	
	/**
	 * @return the movimentoGestioneCopia
	 */
	public Impegno getMovimentoGestioneCopia() {
		return movimentoGestioneCopia;
	}

	/**
	 * @param movimentoGestioneCopia the movimentoGestioneCopia to set
	 */
	public void setMovimentoGestioneCopia(Impegno movimentoGestioneCopia) {
		this.movimentoGestioneCopia = movimentoGestioneCopia;
	}

	/**
	 * @return the subMovimentoGestioneCopia
	 */
	public SubImpegno getSubMovimentoGestioneCopia() {
		return subMovimentoGestioneCopia;
	}

	/**
	 * @param subMovimentoGestioneCopia the subMovimentoGestioneCopia to set
	 */
	public void setSubMovimentoGestioneCopia(SubImpegno subMovimentoGestioneCopia) {
		this.subMovimentoGestioneCopia = subMovimentoGestioneCopia;
	}
	
	/**
	 * @param altroUfficio the altroUfficio to set
	 */
	public void setAltroUfficio(boolean altroUfficio) {
		this.altroUfficio = altroUfficio;
	}
	/**
	 * @return the cassaMista
	 */
	public boolean isAltroUfficio() {
		return this.altroUfficio;
	}

	/**
	 * @return the cassaMista
	 */
	public boolean isCassaMista() {
		return getCassaEconomale() != null && TipoDiCassa.MISTA.equals(getCassaEconomale().getTipoDiCassa());
	}


	
	/**
	 * @return the disponibileMovimentoGestione
	 */
	public BigDecimal getDisponibileMovimentoGestione() {
		Impegno mg = getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null ? getSubMovimentoGestione() : getMovimentoGestione();
		return mg != null ? mg.getDisponibilitaLiquidare() : null;
	}
	
	/**
	 * @return the flagCassaEconomaleMovimentoGestione
	 */
	public String getFlagCassaEconomaleMovimentoGestione() {
		// SIAC-5623
		Impegno mg = getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null ? getSubMovimentoGestione() : getMovimentoGestione();
		return mg == null
			? null
			: mg.isFlagCassaEconomale() ? "SI" : "NO";
	}
	
	/**
	 * @return the capitoloMovimento
	 */
	public CapitoloUscitaGestione getCapitoloMovimento() {
		return getMovimentoGestione() != null ? getMovimentoGestione().getCapitoloUscitaGestione() : null;
	}
	
	/**
	 * @return the descrizioneCapitoloMovimento
	 */
	public String getDescrizioneCapitoloMovimento() {
		CapitoloUscitaGestione cug = getCapitoloMovimento();
		return computeDescrizioneCapitolo(cug);
	}
	
	/**
	 * Calcola la descrizione del capitolo fornito in input.
	 * @param <C> la tipizzazione del capitolo
	 * 
	 * @param capitolo il capitolo
	 * @return la descrizione del capitolo
	 */
	protected <C extends Capitolo<?, ?>> String computeDescrizioneCapitolo(C capitolo) {
		StringBuilder sb = new StringBuilder();
		if(capitolo != null) {
			sb.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo());
			if(isGestioneUEB()) {
				sb.append("/")
					.append(capitolo.getNumeroUEB());
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return the attoAmministrativoMovimento
	 */
	public AttoAmministrativo getAttoAmministrativoMovimento() {
		return getMovimentoGestione() != null ? getMovimentoGestione().getAttoAmministrativo() : null;
	}
	
	/**
	 * @return the stringaRiepilogoRichiestaEconomale
	 */
	public String getStringaRiepilogoRichiestaEconomale() {
		StringBuilder sb = new StringBuilder();
		
		RichiestaEconomale re = ottieniRichiestaEconomalePerRiepilogo();
		
		if(re != null && re.getUid() != 0) {
			// Richiesta numero
			sb.append(computaStringaNumeroRichiesta(re))
				// Richiesta del
				.append(computaStringaDataRichiesta(re))
				// Stato richiesta
				.append(computaStringaStatoRichiesta(re))
				// Sospeso
				.append(computaStringaSospeso(re))
				// Movimento
				.append(computaStringaMovimento(re));
		}
		
		return sb.toString();
	}
	
	/**
	 * Ottiene la richiesta per la composizione del riepilogo.
	 * 
	 * @return la richiesta di cui ottenere il riepilogo
	 */
	protected RichiestaEconomale ottieniRichiestaEconomalePerRiepilogo() {
		return getRichiestaEconomale();
	}

	/**
	 * Calcolo della stringa del numero richiesta.
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return la stringa del numero richiesta
	 */
	protected String computaStringaNumeroRichiesta(RichiestaEconomale richiestaEconomale) {
		StringBuilder sb = new StringBuilder();
		if(richiestaEconomale != null) {
			sb.append("Richiesta N. ")
				.append(richiestaEconomale.getNumeroRichiesta());
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo della stringa della data richiesta.
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaDataRichiesta(RichiestaEconomale richiestaEconomale) {
		StringBuilder sb = new StringBuilder();
		if(richiestaEconomale != null && richiestaEconomale.getDataCreazione() != null) {
			sb.append(" del ")
				.append(FormatUtils.formatDate(richiestaEconomale.getDataCreazione()));
		} else {
			
			sb.append(" del ")
			.append(FormatUtils.formatDate(new Date()));
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo della stringa dello stato richiesta.
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return la stringa dello stato richiesta
	 */
	protected String computaStringaStatoRichiesta(RichiestaEconomale richiestaEconomale) {
		StringBuilder sb = new StringBuilder();
		if(richiestaEconomale != null && richiestaEconomale.getStatoOperativoRichiestaEconomale() != null) {
			sb.append(" - ")
				.append(richiestaEconomale.getStatoOperativoRichiestaEconomale().getDescrizione());
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo della stringa del sospeso.
	 * <br/>
	 * Tale stringa &eacute; presente solo per le richieste:
	 * <ul>
	 *     <li>ANTICIPO_SPESE</li>
	 *     <li>ANTICIPO_TRASFERTA_DIPENDENTI</li>
	 *     <li>ANTICIPO_SPESE_MISSIONE</li>
	 * </ul>
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return la stringa del sospeso
	 */
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		// Implementazione di default: non restituisco nulla
		return "";
	}
	
	/**
	 * Calcolo della stringa del movimento.
	 * @param richiestaEconomale la richiesta economale contentente il movimento
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaMovimento(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale == null || richiestaEconomale.getMovimento() == null || richiestaEconomale.getBilancio() == null
				|| richiestaEconomale.getMovimento().getNumeroMovimento() == null) {
			return "";
		}
		return computaStringaMovimento(richiestaEconomale.getBilancio(),richiestaEconomale.getMovimento());
	}
	
	/**
	 * Calcolo della stringa del movimento.
	 * @param rendicontoRichiesta il rendiconto richiesta contentente il movimento
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaMovimento(RendicontoRichiesta rendicontoRichiesta) {
		if(rendicontoRichiesta == null || rendicontoRichiesta.getMovimento() == null || rendicontoRichiesta.getRichiestaEconomale() == null 
				|| rendicontoRichiesta.getRichiestaEconomale().getBilancio() == null
				||rendicontoRichiesta.getMovimento().getNumeroMovimento() == null) {
			return "";
		}
		return computaStringaMovimento(rendicontoRichiesta.getRichiestaEconomale().getBilancio(),rendicontoRichiesta.getMovimento());
	}
	
	/**
	 * Calcolo della stringa del movimento.
	 * @param bilancio il bilancio
	 * @param movimento il movimento per cui calcolare la stringa
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaMovimento(Bilancio bilancio, Movimento movimento) {
		return new StringBuilder()
			.append(" - Movimento N. ")
			.append(bilancio.getAnno())
			.append("/")
			.append(movimento.getNumeroMovimento())
			.toString();
	}
	
	/**
	 * Calcolo della stringa del movimento.
	 * @param movimento il movimento per cui calcolare la stringa
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaStringaMovimento(Movimento movimento) {
		return new StringBuilder()
			.append(" - Movimento N. ")
			.append(FormatUtils.formatDateYear(movimento.getDataMovimento()))
			.append("/")
			.append(movimento.getNumeroMovimento())
			.toString();
	}
	
	/**
	 * @return the stringaRiepilogoModalitaDiPagamento
	 */
	public String getStringaRiepilogoModalitaDiPagamento() {
		Movimento movimento = ottieniMovimento();
		if(movimento == null) {
			return "";
		}
		
		final StringBuilder sb = new StringBuilder();
		final String separator = " - ";
		
		if(isCassaMista()) {
			sb.append("Cassa ")
			.append(movimento.getModalitaPagamentoCassa().getDescrizione());
			addModalitaPagamentoDipendenteToStringBuilder(sb, movimento, separator);
		} else {
			addModalitaPagamentoDipendenteToStringBuilder(sb, movimento, separator);
		}
		
		if(movimento.getDettaglioPagamento() != null && !movimento.getDettaglioPagamento().isEmpty()) {
			sb.append(separator)
			.append(movimento.getDettaglioPagamento());
		}
		return sb.toString();
	}
	
	private void addModalitaPagamentoDipendenteToStringBuilder(StringBuilder sb, Movimento movimento, String separator) {
		if(movimento.getModalitaPagamentoDipendente() != null 
				&& movimento.getModalitaPagamentoDipendente().getDescrizione() != null) {
			sb.append(separator)
			.append("Dipendente ")
			.append(movimento.getModalitaPagamentoDipendente().getDescrizione());
		}
	}
	
	/**
	 * Ottiene il movimento da considerare.
	 * 
	 * @return il movimento
	 */
	protected Movimento ottieniMovimento() {
		// To override in subclasses for Rendiconto
		return getRichiestaEconomale().getMovimento();
	}
	
	/**
	 * @return the numeroRendicontoStampa
	 */
	public String getNumeroRendicontoStampa() {
		// TODO
		return "";
	}
	
	/**
	 * @return the numeroRendicontoStampa
	 */
	public String getDataRendicontoStampa() {
		return "";
	}
	
	/**
	 * @return the datiMatricola
	 */
	public String getDatiMatricola() {
		if(getRichiestaEconomale() == null) {
			return "";
		}
		if(getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getMatricola());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getMatricola());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getNome());
		if (!isGestioneHR()) {
			//hr mette tutti i dati in un solo campo
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCognome());
		}
		return StringUtils.join(chunks, " - ");
	}

/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		return creaRequestRicercaImpegnoPerChiaveOttimizzato(getMovimentoGestione());
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @param impegno l'impegno per cui creare la request
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno impegno) {
		
		if(getSubMovimentoGestione() != null){
			return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), impegno, getSubMovimentoGestione());
		}
		
		return MovimentoGestioneHelper.creaRequestRicercaImpegnoPerChiaveOttimizzato(getAnnoEsercizioInt(), getEnte(), getRichiedente(), impegno);

	}
	
}
