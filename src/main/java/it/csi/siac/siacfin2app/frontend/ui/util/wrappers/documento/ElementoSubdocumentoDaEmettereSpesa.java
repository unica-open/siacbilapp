/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di wrap per il Subdocumento da emettere di spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/11/2014
 *
 */
public class ElementoSubdocumentoDaEmettereSpesa extends ElementoSubdocumentoDaEmettere<SubdocumentoSpesa, DocumentoSpesa, CapitoloUscitaGestione, Impegno, SubImpegno> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7715350360717454929L;

	/**
	 * Costruttore a partire dal subdocumento wrappato e dall'eventuale gestione della UEB.
	 * 
	 * @param subdocumento il subdocumento da wrappare
	 * @param gestioneUEB  la gestione dell'UEB dell'ente
	 */
	public ElementoSubdocumentoDaEmettereSpesa(SubdocumentoSpesa subdocumento, boolean gestioneUEB) {
		super(subdocumento,
			gestioneUEB,
			subdocumento != null && subdocumento.getImpegno() != null ? subdocumento.getImpegno().getCapitoloUscitaGestione() : null,
			subdocumento != null ? subdocumento.getImpegno() : null,
			subdocumento != null ? subdocumento.getSubImpegno() : null);
	}
	
	@Override
	public String getDomStringModalitaPagamentoSoggetto() {
		StringBuilder sb = new StringBuilder();
		if(subdocumento != null 
				&& subdocumento.getModalitaPagamentoSoggetto() != null
				&& subdocumento.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto() != null) {
			// Si riferisce alla ModalitaPagamentoSoggetto collegata alla liquidazione
			// Codice della ModalitaAccredito, nel tooltip esporre la  Denominazione
			// Nel tooltip concatenare anche la denomianzione della SedeSecondariaSoggetto nel caso si presente
			
			sb.append("<a data-original-title='Denominazione' data-trigger='hover' rel='popover' data-content='");
			String denominazioneModalitaPagamentoSoggetto = calcolaDenominazione(subdocumento.getModalitaPagamentoSoggetto());
			sb.append(FormatUtils.formatHtmlAttributeString(denominazioneModalitaPagamentoSoggetto));
			if(StringUtils.isNotBlank(subdocumento.getModalitaPagamentoSoggetto().getAssociatoA()) &&
				!BilConstants.ASSOCIATO_A_SOGGETTO.getConstant().equalsIgnoreCase(subdocumento.getModalitaPagamentoSoggetto().getAssociatoA())) {
				
				sb.append(" (Sede: ")
					.append(subdocumento.getModalitaPagamentoSoggetto().getAssociatoA())
					.append(")");
			}
			sb.append("'>")
				.append(subdocumento.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto().getCodice());
			sb.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * Calcolo della denominazione della modalit&agrave; di pagamento del soggetto.
	 * 
	 * @param modalitaPagamentoSoggetto la modalit&agrave; da cui ottenere la denominazione
	 * 
	 * @return la denominazione
	 */
	private String calcolaDenominazione(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		StringBuilder sbModalitaAccreditoSoggetto = new StringBuilder();
		List<String> listModalitaPagamentoSoggettoPartials = new ArrayList<String>();
		StringBuilder sbModalitaPagamentoSoggetto = new StringBuilder();
		
		if(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto() != null) {
			sbModalitaAccreditoSoggetto.append("(")
				.append(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto().getCodice())
				.append(" - ")
				.append(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto().getDescrizione())
				.append(" ) ");
			
			sbModalitaPagamentoSoggetto.append(sbModalitaAccreditoSoggetto.toString());
		}
		
		computePartialsForModalitaPagamentoSoggetto(modalitaPagamentoSoggetto, listModalitaPagamentoSoggettoPartials);
		
		if(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null && modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid() != 0) {
			computePartialsForModalitaPagamentoSoggetto(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2(), listModalitaPagamentoSoggettoPartials);
		}
		
		sbModalitaPagamentoSoggetto.append(StringUtils.join(listModalitaPagamentoSoggettoPartials, " - "));
		return sbModalitaPagamentoSoggetto.toString();
	}

	/**
	 * Calcola i parziali per la modalita di pagamento soggetto.
	 * 
	 * @param modalitaPagamentoSoggetto             la modalita per cui calcolare i parziali
	 * @param listModalitaPagamentoSoggettoPartials i parziali
	 */
	private void computePartialsForModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto, List<String> listModalitaPagamentoSoggettoPartials) {
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getIban())) {
			listModalitaPagamentoSoggettoPartials.add("iban: " + modalitaPagamentoSoggetto.getIban());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getBic())) {
			listModalitaPagamentoSoggettoPartials.add("bic: " + modalitaPagamentoSoggetto.getBic());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getContoCorrente())) {
			listModalitaPagamentoSoggettoPartials.add("conto: " + modalitaPagamentoSoggetto.getContoCorrente());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getIntestazioneConto())) {
			listModalitaPagamentoSoggettoPartials.add("intestato a " + modalitaPagamentoSoggetto.getIntestazioneConto());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getSoggettoQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("quietanzante: " + modalitaPagamentoSoggetto.getSoggettoQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getCodiceFiscaleQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("CF: " + modalitaPagamentoSoggetto.getCodiceFiscaleQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getDataNascitaQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("nato il " + modalitaPagamentoSoggetto.getDataNascitaQuietanzante());
		}
		if(StringUtils.isNotBlank(modalitaPagamentoSoggetto.getLuogoNascitaQuietanzante()) && StringUtils.isNotBlank(modalitaPagamentoSoggetto.getStatoNascitaQuietanzante())) {
			listModalitaPagamentoSoggettoPartials.add("a: " + modalitaPagamentoSoggetto.getLuogoNascitaQuietanzante() + ", " + modalitaPagamentoSoggetto.getStatoNascitaQuietanzante());
		}
	}

	@Override
	public String getDomStringLiquidazione() {
		StringBuilder sb = new StringBuilder();
		if(subdocumento != null && subdocumento.getLiquidazione() != null) {
			sb.append(subdocumento.getLiquidazione().getAnnoLiquidazione())
				.append("/")
				.append(subdocumento.getLiquidazione().getNumeroLiquidazione().toPlainString());
		}
		return sb.toString();
	}
	
	@Override
	public String getDomStringDistinta() {
		StringBuilder sb = new StringBuilder();
		if(subdocumento != null && subdocumento.getLiquidazione() != null && subdocumento.getLiquidazione().getDistinta() != null) {
			sb.append(subdocumento.getLiquidazione().getDistinta().getCodice());
		}
		return sb.toString();
	}
	
	@Override
	public String getDomStringContoTesoreria() {
		StringBuilder sb = new StringBuilder();
		if(subdocumento != null && subdocumento.getLiquidazione() != null && subdocumento.getLiquidazione().getContoTesoreria() != null) {
			sb.append(subdocumento.getLiquidazione().getContoTesoreria().getCodice());
		}
		return sb.toString();
	}
	
	/**
	 * Gets the richiesta durc.
	 *
	 * @return the richiesta durc
	 */
	public String getRichiestoDurc() {		
		return FormatUtils.formatBoolean(isImpegnoRichiestaConfermaDurc(), "S&igrave;", "No");
	}
	
	private boolean isImpegnoRichiestaConfermaDurc() {
		if(subdocumento == null || subdocumento.getImpegno() == null) {
			return false ;
		}
		Impegno impegno = subdocumento.getImpegno();
		return impegno.isFlagSoggettoDurc();
	}
	/**
	 * Gets the data fine validita durc.
	 *
	 * @return the data fine validita durc
	 */
	private Date getDataFineValiditaDurc() {
		if(subdocumento == null){
			return null;
		}
		// Dai requisiti: In caso di cessione si deve considerare il DURC del ricevente (beneficiario amministrativo)
		if(subdocumento.getModalitaPagamentoSoggetto() != null && subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione()!= null && StringUtils.isNotBlank(subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione().getCodiceSoggetto())) {
			return subdocumento.getModalitaPagamentoSoggetto().getSoggettoCessione().getDataFineValiditaDurc();
		}
		return subdocumento.getDocumento() != null && subdocumento.getDocumento().getSoggetto() != null? 
				subdocumento.getDocumento().getSoggetto().getDataFineValiditaDurc() : null;
	}
	
	/**
	 * Gets the dom string info selezione.
	 *
	 * @return the dom string info selezione
	 */
	public String getDomStringInfoSelezione() {
		StringBuilder sb = new StringBuilder();
		if(!isImpegnoRichiestaConfermaDurc() || !isDurcScadutoONull()) {
			return "";
		}
		Date dataFineValiditaDurc = getDataFineValiditaDurc();
		sb.append("<a data-original-title='Dati Durc' data-html='true' data-trigger='hover' rel='popover' data-content='")
		.append("Rich. DURC:" )
		.append(FormatUtils.formatBoolean(isImpegnoRichiestaConfermaDurc(), "S&igrave;", "No"))
		.append("<br/>")
		.append("Data fine validita DURC: ")
		.append(dataFineValiditaDurc != null? FormatUtils.formatDate(dataFineValiditaDurc) : "Durc da richiedere");
		sb.append("'>")
			.append("*");
		sb.append("</a>");
		
		return sb.toString();
	}
	
	/**
	 * Gets the inibisci selezione.
	 *
	 * @return the inibisci selezione
	 */
	public boolean getInibisciSelezione() {
		return isImpegnoRichiestaConfermaDurc() && isDurcScadutoONull();
	}
	
	private boolean isDurcScadutoONull() {
		Date dataFineValiditaDurc = getDataFineValiditaDurc();
		if(dataFineValiditaDurc == null) {
			return true;
		}
		Date now = new Date();
		
		//SIAC-7687
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc);
		
		return dateNow.compareTo(dateFineDurc)>0;
	}
	
	/**
	 * Gets the data piu prossima.
	 *
	 * @param dates the dates
	 * @return the data piu prossima
	 */
	public static final Date getDataPiuProssima(Date... dates) {
		Date now = new Date();
		Date dataScadenzaPiuProssimaAScadenza = null;
		Date dataScadenzaPiuVecchia = null;
		if(dates == null || dates.length == 0) {
			return null;
		}
		
		for (Date dsf : dates) {
			if(dsf == null) {
				continue;
			}
			
			if(now.before(dsf) && (dataScadenzaPiuProssimaAScadenza == null || dataScadenzaPiuProssimaAScadenza.after(dsf))) {
				// Se la data e' successiva ad ora ed e' prima della data a scadenza piu' prossima gia' registrata...
				dataScadenzaPiuProssimaAScadenza = dsf;
				continue;
			} 
			if(dataScadenzaPiuVecchia == null || dsf.before(dataScadenzaPiuVecchia)) {
				// Se la data di scadenza e' dopo la piu' vecchia gia' registrata
				dataScadenzaPiuVecchia = dsf;
			}
		}
		return dataScadenzaPiuProssimaAScadenza != null ? dataScadenzaPiuProssimaAScadenza : dataScadenzaPiuVecchia;
	}
	
}
