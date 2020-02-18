/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;

/**
 * Classe di model per la ricerca del PreDocumento di Entrata per il completamento e la definizione
 * 
 * @author Marchino Alessandro
 */
public class CompletaDefinisciPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5080609141854299761L;
	
	private Date dataCompetenzaDa;
	private Date dataCompetenzaA;
	
	private Long numeroPreDocumentiIncompleti;
	private Long numeroPreDocumentiCompleti;
	private Long numeroPreDocumentiAnnullatiDefiniti;
	private Long numeroPreDocumentiTotale;
	
	private BigDecimal importoPreDocumentiIncompleti;
	private BigDecimal importoPreDocumentiCompleti;
	private BigDecimal importoPreDocumentiAnnullatiDefiniti;
	private BigDecimal importoPreDocumentiTotale;
	
	/** Costruttore vuoto di default */
	public CompletaDefinisciPreDocumentoEntrataModel() {
		setTitolo("Completa e Definisci Predisposizione di Incasso");
	}
	
	/**
	 * @return the dataCompetenzaDa
	 */
	public Date getDataCompetenzaDa() {
		return dataCompetenzaDa == null ? null : new Date(dataCompetenzaDa.getTime());
	}

	/**
	 * @param dataCompetenzaDa the dataCompetenzaDa to set
	 */
	public void setDataCompetenzaDa(Date dataCompetenzaDa) {
		this.dataCompetenzaDa = dataCompetenzaDa == null ? null : new Date(dataCompetenzaDa.getTime());
	}

	/**
	 * @return the dataCompetenzaA
	 */
	public Date getDataCompetenzaA() {
		return dataCompetenzaA == null ? null : new Date(dataCompetenzaA.getTime());
	}

	/**
	 * @param dataCompetenzaA the dataCompetenzaA to set
	 */
	public void setDataCompetenzaA(Date dataCompetenzaA) {
		this.dataCompetenzaA = dataCompetenzaA == null ? null : new Date(dataCompetenzaA.getTime());
	}
	
	/**
	 * @return the numeroPreDocumentiIncompleti
	 */
	public Long getNumeroPreDocumentiIncompleti() {
		return this.numeroPreDocumentiIncompleti;
	}

	/**
	 * @param numeroPreDocumentiIncompleti the numeroPreDocumentiIncompleti to set
	 */
	public void setNumeroPreDocumentiIncompleti(Long numeroPreDocumentiIncompleti) {
		this.numeroPreDocumentiIncompleti = numeroPreDocumentiIncompleti;
	}

	/**
	 * @return the numeroPreDocumentiCompleti
	 */
	public Long getNumeroPreDocumentiCompleti() {
		return this.numeroPreDocumentiCompleti;
	}

	/**
	 * @param numeroPreDocumentiCompleti the numeroPreDocumentiCompleti to set
	 */
	public void setNumeroPreDocumentiCompleti(Long numeroPreDocumentiCompleti) {
		this.numeroPreDocumentiCompleti = numeroPreDocumentiCompleti;
	}

	/**
	 * @return the numeroPreDocumentiAnnullatiDefiniti
	 */
	public Long getNumeroPreDocumentiAnnullatiDefiniti() {
		return this.numeroPreDocumentiAnnullatiDefiniti;
	}

	/**
	 * @param numeroPreDocumentiAnnullatiDefiniti the numeroPreDocumentiAnnullatiDefiniti to set
	 */
	public void setNumeroPreDocumentiAnnullatiDefiniti(Long numeroPreDocumentiAnnullatiDefiniti) {
		this.numeroPreDocumentiAnnullatiDefiniti = numeroPreDocumentiAnnullatiDefiniti;
	}

	/**
	 * @return the numeroPreDocumentiTotale
	 */
	public Long getNumeroPreDocumentiTotale() {
		return this.numeroPreDocumentiTotale;
	}

	/**
	 * @param numeroPreDocumentiTotale the numeroPreDocumentiTotale to set
	 */
	public void setNumeroPreDocumentiTotale(Long numeroPreDocumentiTotale) {
		this.numeroPreDocumentiTotale = numeroPreDocumentiTotale;
	}

	/**
	 * @return the importoPreDocumentiIncompleti
	 */
	public BigDecimal getImportoPreDocumentiIncompleti() {
		return this.importoPreDocumentiIncompleti;
	}

	/**
	 * @param importoPreDocumentiIncompleti the importoPreDocumentiIncompleti to set
	 */
	public void setImportoPreDocumentiIncompleti(BigDecimal importoPreDocumentiIncompleti) {
		this.importoPreDocumentiIncompleti = importoPreDocumentiIncompleti;
	}

	/**
	 * @return the importoPreDocumentiCompleti
	 */
	public BigDecimal getImportoPreDocumentiCompleti() {
		return this.importoPreDocumentiCompleti;
	}

	/**
	 * @param importoPreDocumentiCompleti the importoPreDocumentiCompleti to set
	 */
	public void setImportoPreDocumentiCompleti(BigDecimal importoPreDocumentiCompleti) {
		this.importoPreDocumentiCompleti = importoPreDocumentiCompleti;
	}

	/**
	 * @return the importoPreDocumentiAnnullatiDefiniti
	 */
	public BigDecimal getImportoPreDocumentiAnnullatiDefiniti() {
		return this.importoPreDocumentiAnnullatiDefiniti;
	}

	/**
	 * @param importoPreDocumentiAnnullatiDefiniti the importoPreDocumentiAnnullatiDefiniti to set
	 */
	public void setImportoPreDocumentiAnnullatiDefiniti(BigDecimal importoPreDocumentiAnnullatiDefiniti) {
		this.importoPreDocumentiAnnullatiDefiniti = importoPreDocumentiAnnullatiDefiniti;
	}

	/**
	 * @return the importoPreDocumentiTotale
	 */
	public BigDecimal getImportoPreDocumentiTotale() {
		return this.importoPreDocumentiTotale;
	}

	/**
	 * @param importoPreDocumentiTotale the importoPreDocumentiTotale to set
	 */
	public void setImportoPreDocumentiTotale(BigDecimal importoPreDocumentiTotale) {
		this.importoPreDocumentiTotale = importoPreDocumentiTotale;
	}
	
	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di {@link CompletaDefiniscePreDocumentoEntrata}.
	 * @return la request creata
	 */
	public CompletaDefiniscePreDocumentoEntrata creaRequestCompletaDefiniscePreDocumentoEntrata() {
		CompletaDefiniscePreDocumentoEntrata req = creaRequest(CompletaDefiniscePreDocumentoEntrata.class);
		
		// Ricerca
		req.setBilancio(getBilancio());
		req.setCausaleEntrata(impostaEntitaFacoltativa(getCausaleEntrata()));
		req.setDataCompetenzaDa(getDataCompetenzaDa());
		req.setDataCompetenzaA(getDataCompetenzaA());
		
		// Aggiornamento
		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		req.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		req.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		req.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTotaliPreDocumentoEntrataPerStato}.
	 * @return la request creata
	 */
	public RicercaTotaliPreDocumentoEntrataPerStato creaRequestRicercaTotaliPreDocumentoEntrataPerStato() {
		RicercaTotaliPreDocumentoEntrataPerStato req = creaRequest(RicercaTotaliPreDocumentoEntrataPerStato.class);
		// Parametri di ricerca
		req.setBilancio(getBilancio());
		req.setCausaleEntrata(impostaEntitaFacoltativa(getCausaleEntrata()));
		req.setDataCompetenzaDa(getDataCompetenzaDa());
		req.setDataCompetenzaA(getDataCompetenzaA());
		
		return req;
	}
	
}
