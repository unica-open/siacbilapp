/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.Map;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

/**
 * Classe di model per la consultazione del PreDocumento di Spesa
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 30/06/2017
 *
 */
public class DefinisciPreDocumentoSpesaPerElencoModel extends GenericPreDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1754256842637868958L;

	/** Costruttore vuoto di default */
	public DefinisciPreDocumentoSpesaPerElencoModel() {
		setTitolo("definisci predocumento di pagamento per elenco");
	}
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	//count
	private Long numeroPreDocumentiTotale = Long.valueOf(0);
	private Long numeroPreDocumentiIncompleti = Long.valueOf(0);
	private Long numeroPreDocumentiDefiniti = Long.valueOf(0);
	//importi
	private BigDecimal importoPreDocumentiTotale = BigDecimal.ZERO;
	private BigDecimal importoPreDocumentiIncompleti = BigDecimal.ZERO;
	private BigDecimal importoPreDocumentiDefiniti = BigDecimal.ZERO;
	
	/**
	 * @return the elencoDocumentiAllegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return elencoDocumentiAllegato;
	}

	/**
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato to set
	 */
	public void setElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
	}
	

	/**
	 * @return the numeroPreDocumentiTotale
	 */
	public Long getNumeroPreDocumentiTotale() {
		return numeroPreDocumentiTotale;
	}

	/**
	 * @param numeroPreDocumentiTotale the numeroPreDocumentiTotale to set
	 */
	public void setNumeroPreDocumentiTotale(Long numeroPreDocumentiTotale) {
		this.numeroPreDocumentiTotale = numeroPreDocumentiTotale;
	}

	/**
	 * @return the numeroPreDocumentiIncomplete
	 */
	public Long getNumeroPreDocumentiIncompleti() {
		return numeroPreDocumentiIncompleti;
	}

	/**
	 * Sets the numero pre documenti incompleti.
	 *
	 * @param numeroPreDocumentiIncompleti the new numero pre documenti incompleti
	 */
	public void setNumeroPreDocumentiIncompleti(Long numeroPreDocumentiIncompleti) {
		this.numeroPreDocumentiIncompleti = numeroPreDocumentiIncompleti;
	}

	/**
	 * @return the importoPreDocumentiTotale
	 */
	public BigDecimal getImportoPreDocumentiTotale() {
		return importoPreDocumentiTotale;
	}

	/**
	 * @param importoPreDocumentiTotale the importoPreDocumentiTotale to set
	 */
	public void setImportoPreDocumentiTotale(BigDecimal importoPreDocumentiTotale) {
		this.importoPreDocumentiTotale = importoPreDocumentiTotale;
	}

	/**
	 * @return the importoPreDocumentiIncomplete
	 */
	public BigDecimal getImportoPreDocumentiIncompleti() {
		return importoPreDocumentiIncompleti;
	}

	/**
	 * Sets the importo pre documenti incompleti.
	 *
	 * @param importoPreDocumentiIncompleti the new importo pre documenti incompleti
	 */
	public void setImportoPreDocumentiIncompleti(BigDecimal importoPreDocumentiIncompleti) {
		this.importoPreDocumentiIncompleti = importoPreDocumentiIncompleti;
	}

	/**
	 * @return the importoPreDocumentiDefinite
	 */
	public BigDecimal getImportoPreDocumentiDefiniti() {
		return importoPreDocumentiDefiniti;
	}

	/**
	 * Sets the importo pre documenti definit.
	 *
	 * @param importoPreDocumentiDefiniti the new importo pre documenti definit
	 */
	public void setImportoPreDocumentiDefiniti(BigDecimal importoPreDocumentiDefiniti) {
		this.importoPreDocumentiDefiniti = importoPreDocumentiDefiniti;
	}

	/**
	 * Gets the numero pre documenti definiti.
	 *
	 * @return the numeroPreDocumentiDefiniti
	 */
	public Long getNumeroPreDocumentiDefiniti() {
		return numeroPreDocumentiDefiniti;
	}

	/**
	 * Sets the numero pre documenti definiti.
	 *
	 * @param numeroPreDocumentiDefiniti the new numero pre documenti definiti
	 */
	public void setNumeroPreDocumentiDefiniti(Long numeroPreDocumentiDefiniti) {
		this.numeroPreDocumentiDefiniti = numeroPreDocumentiDefiniti;
	}

	/**
	 * Crea request ricerca totali predocumenti per elenco.
	 *
	 * @return the ricerca totali predocumenti per elenco, la request creata
	 */
	public RicercaTotaliPredocumentiPerElenco creaRequestRicercaTotaliPredocumentiPerElenco() {
		RicercaTotaliPredocumentiPerElenco req = creaRequest(RicercaTotaliPredocumentiPerElenco.class);
		req.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		req.setEnte(getEnte());
		return req;
	}

	/**
	 * Crea request definisce pre documento spesa per elenco.
	 *
	 * @return the definisce pre documento spesa per elenco
	 */
	public DefiniscePreDocumentoSpesaPerElenco creaRequestDefiniscePreDocumentoSpesaPerElenco() {
		DefiniscePreDocumentoSpesaPerElenco req = creaRequest(DefiniscePreDocumentoSpesaPerElenco.class);
		req.setBilancio(getBilancio());
		req.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		req.setImpegno(getMovimentoGestione());
		req.setSubImpegno(getSubMovimentoGestione());
		return req;
	}

	/**
	 * Imposta totali elenco.
	 *
	 * @param res la response da cui ottenere i dati
	 */
	public void impostaTotaliElenco(RicercaTotaliPredocumentiPerElencoResponse res) {
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = res.getImportiPreDocumenti();
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = res.getNumeroPreDocumenti();

		numeroPreDocumentiIncompleti = defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO));
		importoPreDocumentiIncompleti = defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO));
		numeroPreDocumentiDefiniti = defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.DEFINITO));
		importoPreDocumentiDefiniti = defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.DEFINITO));
		
		BigDecimal importoTotale = BigDecimal.ZERO;
		long numeroPredoc = 0;
		// Totale importi predoc
		for(BigDecimal importo : importiPreDocumenti.values()) {
			importoTotale = importoTotale.add(defaultToZero(importo));
		}
		// Numero importi predoc
		for(Long numero : numeroPreDocumenti.values()) {
			numeroPredoc += defaultToZero(numero).longValue();
		}
		
		importoPreDocumentiTotale = importoTotale;
		numeroPreDocumentiTotale = Long.valueOf(numeroPredoc);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	private Long defaultToZero(Long value) {
		return value != null ? value : Long.valueOf(0L);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	private BigDecimal defaultToZero(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}
}
