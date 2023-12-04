/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;

public class BaseCompletaDefinisciPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3490983130366137495L;
	
	private Date dataCompetenzaDa;
	private Date dataCompetenzaA;

	private Accertamento accertamento;
	
	private Long numeroPreDocumentiIncompleti;
	private Long numeroPreDocumentiCompleti;
	private Long numeroPreDocumentiAnnullatiDefiniti;
	private Long numeroPreDocumentiTotale;
	
	private BigDecimal importoPreDocumentiIncompleti;
	private BigDecimal importoPreDocumentiCompleti;
	private BigDecimal importoPreDocumentiAnnullatiDefiniti;
	private BigDecimal importoPreDocumentiTotale;

	//NO CASSA
	private Long numeroPreDocumentiNoCassaIncompleti;
	private Long numeroPreDocumentiNoCassaCompleti;
	private Long numeroPreDocumentiNoCassaAnnullatiDefiniti;
	private Long numeroPreDocumentiNoCassaTotale;
	
	private BigDecimal importoPreDocumentiNoCassaIncompleti;
	private BigDecimal importoPreDocumentiNoCassaCompleti;
	private BigDecimal importoPreDocumentiNoCassaAnnullatiDefiniti;
	private BigDecimal importoPreDocumentiNoCassaTotale;
	
	
	private boolean completaDefinisciAbilitato = true; 
	
	
	
	/**
	 * @return the dataCompetenzaDa
	 */
	public Date getDataCompetenzaDa() {
		return dataCompetenzaDa;
	}
	/**
	 * @param dataCompetenzaDa the dataCompetenzaDa to set
	 */
	public void setDataCompetenzaDa(Date dataCompetenzaDa) {
		this.dataCompetenzaDa = dataCompetenzaDa;
	}
	/**
	 * @return the dataCompetenzaA
	 */
	public Date getDataCompetenzaA() {
		return dataCompetenzaA;
	}
	/**
	 * @param dataCompetenzaA the dataCompetenzaA to set
	 */
	public void setDataCompetenzaA(Date dataCompetenzaA) {
		this.dataCompetenzaA = dataCompetenzaA;
	}
	/**
	 * @return the numeroPreDocumentiIncompleti
	 */
	public Long getNumeroPreDocumentiIncompleti() {
		return numeroPreDocumentiIncompleti;
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
		return numeroPreDocumentiCompleti;
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
		return numeroPreDocumentiAnnullatiDefiniti;
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
		return numeroPreDocumentiTotale;
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
		return importoPreDocumentiIncompleti;
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
		return importoPreDocumentiCompleti;
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
		return importoPreDocumentiAnnullatiDefiniti;
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
		return importoPreDocumentiTotale;
	}
	/**
	 * @param importoPreDocumentiTotale the importoPreDocumentiTotale to set
	 */
	public void setImportoPreDocumentiTotale(BigDecimal importoPreDocumentiTotale) {
		this.importoPreDocumentiTotale = importoPreDocumentiTotale;
	}
	/**
	 * @return the numeroPreDocumentiNoCassaIncompleti
	 */
	public Long getNumeroPreDocumentiNoCassaIncompleti() {
		return numeroPreDocumentiNoCassaIncompleti;
	}
	/**
	 * @param numeroPreDocumentiNoCassaIncompleti the numeroPreDocumentiNoCassaIncompleti to set
	 */
	public void setNumeroPreDocumentiNoCassaIncompleti(Long numeroPreDocumentiNoCassaIncompleti) {
		this.numeroPreDocumentiNoCassaIncompleti = numeroPreDocumentiNoCassaIncompleti;
	}
	/**
	 * @return the numeroPreDocumentiNoCassaCompleti
	 */
	public Long getNumeroPreDocumentiNoCassaCompleti() {
		return numeroPreDocumentiNoCassaCompleti;
	}
	/**
	 * @param numeroPreDocumentiNoCassaCompleti the numeroPreDocumentiNoCassaCompleti to set
	 */
	public void setNumeroPreDocumentiNoCassaCompleti(Long numeroPreDocumentiNoCassaCompleti) {
		this.numeroPreDocumentiNoCassaCompleti = numeroPreDocumentiNoCassaCompleti;
	}
	/**
	 * @return the numeroPreDocumentiNoCassaAnnullatiDefiniti
	 */
	public Long getNumeroPreDocumentiNoCassaAnnullatiDefiniti() {
		return numeroPreDocumentiNoCassaAnnullatiDefiniti;
	}
	/**
	 * @param numeroPreDocumentiNoCassaAnnullatiDefiniti the numeroPreDocumentiNoCassaAnnullatiDefiniti to set
	 */
	public void setNumeroPreDocumentiNoCassaAnnullatiDefiniti(Long numeroPreDocumentiNoCassaAnnullatiDefiniti) {
		this.numeroPreDocumentiNoCassaAnnullatiDefiniti = numeroPreDocumentiNoCassaAnnullatiDefiniti;
	}
	/**
	 * @return the numeroPreDocumentiNoCassaTotale
	 */
	public Long getNumeroPreDocumentiNoCassaTotale() {
		return numeroPreDocumentiNoCassaTotale;
	}
	/**
	 * @param numeroPreDocumentiNoCassaTotale the numeroPreDocumentiNoCassaTotale to set
	 */
	public void setNumeroPreDocumentiNoCassaTotale(Long numeroPreDocumentiNoCassaTotale) {
		this.numeroPreDocumentiNoCassaTotale = numeroPreDocumentiNoCassaTotale;
	}
	/**
	 * @return the importoPreDocumentiNoCassaIncompleti
	 */
	public BigDecimal getImportoPreDocumentiNoCassaIncompleti() {
		return importoPreDocumentiNoCassaIncompleti;
	}
	/**
	 * @param importoPreDocumentiNoCassaIncompleti the importoPreDocumentiNoCassaIncompleti to set
	 */
	public void setImportoPreDocumentiNoCassaIncompleti(BigDecimal importoPreDocumentiNoCassaIncompleti) {
		this.importoPreDocumentiNoCassaIncompleti = importoPreDocumentiNoCassaIncompleti;
	}
	/**
	 * @return the importoPreDocumentiNoCassaCompleti
	 */
	public BigDecimal getImportoPreDocumentiNoCassaCompleti() {
		return importoPreDocumentiNoCassaCompleti;
	}
	/**
	 * @param importoPreDocumentiNoCassaCompleti the importoPreDocumentiNoCassaCompleti to set
	 */
	public void setImportoPreDocumentiNoCassaCompleti(BigDecimal importoPreDocumentiNoCassaCompleti) {
		this.importoPreDocumentiNoCassaCompleti = importoPreDocumentiNoCassaCompleti;
	}
	/**
	 * @return the importoPreDocumentiNoCassaAnnullatiDefiniti
	 */
	public BigDecimal getImportoPreDocumentiNoCassaAnnullatiDefiniti() {
		return importoPreDocumentiNoCassaAnnullatiDefiniti;
	}
	/**
	 * @param importoPreDocumentiNoCassaAnnullatiDefiniti the importoPreDocumentiNoCassaAnnullatiDefiniti to set
	 */
	public void setImportoPreDocumentiNoCassaAnnullatiDefiniti(BigDecimal importoPreDocumentiNoCassaAnnullatiDefiniti) {
		this.importoPreDocumentiNoCassaAnnullatiDefiniti = importoPreDocumentiNoCassaAnnullatiDefiniti;
	}
	/**
	 * @return the importoPreDocumentiNoCassaTotale
	 */
	public BigDecimal getImportoPreDocumentiNoCassaTotale() {
		return importoPreDocumentiNoCassaTotale;
	}
	/**
	 * @param importoPreDocumentiNoCassaTotale the importoPreDocumentiNoCassaTotale to set
	 */
	public void setImportoPreDocumentiNoCassaTotale(BigDecimal importoPreDocumentiNoCassaTotale) {
		this.importoPreDocumentiNoCassaTotale = importoPreDocumentiNoCassaTotale;
	}
	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}
	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	
	/**
	 * @return the completaDefinisciAbilitato
	 */
	public boolean isCompletaDefinisciAbilitato() {
		return completaDefinisciAbilitato;
	}


	/**
	 * @param completaDefinisciAbilitato the completaDefinisciAbilitato to set
	 */
	public void setCompletaDefinisciAbilitato(boolean completaDefinisciAbilitato) {
		this.completaDefinisciAbilitato = completaDefinisciAbilitato;
	}
	
	/**
	 * Crea una request per il servizio di {@link CompletaDefiniscePreDocumentoEntrata}.
	 * @param reqRicerca 
	 * @return la request creata
	 */
	public CompletaDefiniscePreDocumentoEntrata creaRequestCompletaDefiniscePreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata reqRicerca) {
		CompletaDefiniscePreDocumentoEntrata req = creaRequest(CompletaDefiniscePreDocumentoEntrata.class);
		
		// Ricerca
		req.setBilancio(getBilancio());
		
		req.setRicercaSinteticaPredocumentoEntrata(reqRicerca);
		
		req.setUidPredocumentiDaFiltrare(reqRicerca.getUidPredocumentiDaFiltrare());
		
		// Aggiornamento
		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		req.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		req.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		req.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		
		//SIAC-6780
		req.setProvvisorioCassa(getProvvisorioCassa());
		
		
		return req;
	}
	
}
