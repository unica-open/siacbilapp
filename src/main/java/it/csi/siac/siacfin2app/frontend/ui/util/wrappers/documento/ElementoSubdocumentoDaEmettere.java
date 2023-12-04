/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * Classe di wrap per il Subdocumento da emettere.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 20/11/2014
 * 
 * @param <S>  la tipizzazione del subdocumento
 * @param <D>  la tipizzazione del documento
 * @param <C>  la tipizzazione del capitolo
 * @param <M>  la tipizzazione del movimento di gestione
 * @param <SM> la tipizzazione del submovimento di gestione
 *
 */
public class ElementoSubdocumentoDaEmettere<S extends Subdocumento<D, ?>, D extends Documento<S, ?>, C extends Capitolo<?, ?>,
	M extends MovimentoGestione, SM extends M> implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6081852567432530746L;
	/** La gestione UEB */
	protected final boolean gestioneUEB;
	/** Subdocumento wrappato */
	protected final S subdocumento;
	/** Capitolo wrappato */
	protected final C capitolo;
	/** Movimento di gestione wrappato */
	protected final M movimento;
	/** Submovimento di gestione wrappato */
	protected final SM submovimento;
	
	// Wrappers
	
	/**
	 * Costruttore a partire dal subdocumento wrappato e dall'eventuale gestione della UEB.
	 * 
	 * @param subdocumento il subdocumento da wrappare
	 * @param gestioneUEB  la gestione dell'UEB dell'ente
	 * @param capitolo il capitolo da wrappare
	 * @param movimento il movimento da wrappare
	 * @param submovimento il submovimento da wrappare
	 */
	public ElementoSubdocumentoDaEmettere(S subdocumento, boolean gestioneUEB, C capitolo, M movimento, SM submovimento) {
		super();
		this.subdocumento = subdocumento;
		this.gestioneUEB = gestioneUEB;
		this.capitolo = capitolo;
		this.movimento = movimento;
		this.submovimento = submovimento;
	}

	@Override
	public int getUid() {
		return subdocumento == null ? 0 : subdocumento.getUid();
	}
	
	/**
	 * @return the domStringAttoAmministrativo
	 */
	public String getDomStringAttoAmministrativo() {
		StringBuilder sb = new StringBuilder();
		
		if(subdocumento != null && subdocumento.getAttoAmministrativo() != null) {
			// Esporre Anno/Numero/codice Tipo/codice Struttura
			// TooltipText: Oggetto
			
			sb.append("<a data-original-title='Oggetto' data-trigger='hover' rel='popover' data-content='")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getAttoAmministrativo().getOggetto()))
				.append("'>")
				.append(subdocumento.getAttoAmministrativo().getAnno())
				.append("/")
				.append(subdocumento.getAttoAmministrativo().getNumero());
			if(subdocumento.getAttoAmministrativo().getTipoAtto() != null) {
				sb.append("/")
					.append(subdocumento.getAttoAmministrativo().getTipoAtto().getCodice());
			}
			if(subdocumento.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
				sb.append("/")
					.append(subdocumento.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
			}
			sb.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringElenco
	 */
	public String getDomStringElenco() {
		StringBuilder sb = new StringBuilder();
		
		if(subdocumento != null && subdocumento.getElencoDocumenti() != null) {
			// Anno/numero elenco
			sb.append(subdocumento.getElencoDocumenti().getAnno())
				.append("/")
				.append(subdocumento.getElencoDocumenti().getNumero());
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringSoggetto
	 */
	public String getDomStringSoggetto() {
		StringBuilder sb = new StringBuilder();
		
		if(subdocumento != null && subdocumento.getDocumento() != null && subdocumento.getDocumento().getSoggetto() != null) {
			// Codice, nel tooltip esporre la Denominazione
			
			sb.append("<a data-original-title='Denominazione' data-trigger='hover' rel='popover' data-content='")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getDocumento().getSoggetto().getDenominazione()))
				.append("'>")
				.append(subdocumento.getDocumento().getSoggetto().getCodiceSoggetto())
				.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringModalitaPagamentoSoggetto
	 */
	public String getDomStringModalitaPagamentoSoggetto() {
		return "";
	}
	
	/**
	 * @return the domStringDocumento
	 */
	public String getDomStringDocumento() {
		StringBuilder sb = new StringBuilder();
		
		if(subdocumento != null && subdocumento.getDocumento() != null) {
			// Anno/tipo/numero
			// Descrizione della quota documento
			
			sb.append("<a data-original-title='Descrizione' data-trigger='hover' rel='popover' data-content='")
				.append(FormatUtils.formatHtmlAttributeString(subdocumento.getDescrizione()))
				.append("'>")
				.append(subdocumento.getDocumento().getAnno());
			if(subdocumento.getDocumento().getTipoDocumento() != null) {
				sb.append("/")
					.append(subdocumento.getDocumento().getTipoDocumento().getCodice());
			}
			sb.append("/")
				.append(subdocumento.getDocumento().getNumero())
				.append("-")
				.append(subdocumento.getNumero())
				.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringLiquidazione
	 */
	public String getDomStringLiquidazione() {
		return "";
	}
	
	/**
	 * @return the domStringCapitolo
	 */
	public String getDomStringCapitolo() {
		StringBuilder sb = new StringBuilder();
		
		if(capitolo != null) {
			// Capitolo/articolo-ueb
			
			sb.append(capitolo.getAnnoCapitolo())
				.append("/")
				.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo());
			if(gestioneUEB) {
				sb.append("-")
					.append(capitolo.getNumeroUEB());
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringMovimento
	 */
	public String getDomStringMovimento() {
		StringBuilder sb = new StringBuilder();
		
		if(movimento != null) {
			// Anno/numero – eventuale sub
			
			sb.append(movimento.getAnnoMovimento())
				.append("/")
				.append(movimento.getNumeroBigDecimal().toPlainString());
			if(submovimento != null) {
				sb.append("-")
					.append(submovimento.getNumeroBigDecimal().toPlainString());
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return the domStringDistinta
	 */
	public String getDomStringDistinta() {
		return "";
	}
	
	/**
	 * @return the domStringContoTesoreria
	 */
	public String getDomStringContoTesoreria() {
		return "";
	}
	
	/**
	 * @return the domStringImporto
	 */
	public BigDecimal getDomStringImporto() {
		return subdocumento != null ? subdocumento.getImportoNotNull().subtract(subdocumento.getImportoDaDedurreNotNull()) : BigDecimal.ZERO;
	}
	
	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza(){
		return subdocumento.getDataScadenza();
	}
	
	/**
	 * Gets the dom stringa provvisorio.
	 *
	 * @return the dom stringa provvisorio
	 */
	public String getDomStringProvvisorio() {
		StringBuilder sbHTML = new StringBuilder();
		String popoverPrefix = "<a data-original-title=\"Provvisorio di cassa\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"";
		
		if(subdocumento.getProvvisorioCassa() == null || subdocumento.getProvvisorioCassa().getNumero() == null) {
			return sbHTML.toString();				
		}
		
		sbHTML.append(popoverPrefix)
				.append(subdocumento.getProvvisorioCassa().getAnno())
				.append("/")
				.append(subdocumento.getProvvisorioCassa().getNumero())
				.append("\" data-trigger=\"hover\">")
				.append("*")
				.append( "</a>");
		return sbHTML.toString();
	}
}
