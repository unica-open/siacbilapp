/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata per il documento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @verison 1.1.0 - 14/10/2015 - gestione GEN/GSA
 * 
 * @param <D>  la tipizzazione del documento
 * @param <S>  la tipizzazione del subdocumento
 * @param <SI> la tipizzazione del subdocumento iva
 * @param <E>  la tipizzazione del wrapper del subdocumento
 * @param <H>  la tipizzazione dell'helper
 * @param <M>  la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataDocumentoBaseAction<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>,
	E extends ElementoQuotaRegistrazioneMovFin<D, S>, H extends ConsultaRegistrazioneMovFinDocumentoHelper<D, S, SI>, M extends InserisciPrimaNotaIntegrataDocumentoBaseModel<D, S, SI, E, D, S, SI, H>>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<D, H, M>  {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6174136171472344600L;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		// Per evitare problemi
		sessionHandler.setParametro(BilSessionParameter.NON_PULIRE_MODEL, null);
		
		checkCasoDUsoApplicabile();
		
		// In inserimento arrivo da registrazione
		try {
			ricercaDettaglioRegistrazioneMovFin();
			log.debug(methodName, "registrazione ottenuta");
			
			ricercaDettaglioPrimaNota();
			
			// Calcolo della prima nota
			ricercaDettaglioDocumento();
			
			// Ricerco le quote con i movimenti
			ricercaMovimentiEP();
			if(inEsecuzioneRegistrazioneMovFin()) {
				return INPUT;
			}
			
			// Ricerco la causale EP
			caricaListaCausaleEP(false);
			caricaListaClassi();
			caricaListaTitoli();
			
			log.debug(methodName, "documento ottenuto");
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// SIAC-5333
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(wsife.getMessage()));
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		// Calcolo delle liste di utilita'
		computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		
		impostaDatiPerVisualizzazioneDettaglio();
		
		// Wrap quote
		wrapQuote();
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca di dettaglio del documento per il popolamento dei dati.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void ricercaDettaglioDocumento() throws WebServiceInvocationFailureException {
		// To implement
	}
	
	/**
	 * Ricerca dei movimenti EP.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void ricercaMovimentiEP() throws WebServiceInvocationFailureException {
		RicercaSinteticaRegistrazioneMovFin req = model.creaRequestRicercaSinteticaRegistrazioneMovFin();
		logServiceRequest(req);
		RicercaSinteticaRegistrazioneMovFinResponse res = registrazioneMovFinService.ricercaSinteticaRegistrazioneMovFin(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSinteticaRegistrazioneMovFin.class, res));
		}
		
		// Di per se', mi andrebbe bene anche avere una sola registrazione
		model.setListaRegistrazioneMovFin(res.getRegistrazioniMovFin());
	}
	
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin) {
		final String methodName = "computaStringheDescrizioneDaRegistrazione";
		D documento = model.getDocumento();
		if(documento == null) {
			log.debug(methodName, "Non ho sufficienti dati per calcolare le stringhe");
			return;
		}
		
		String descrizionePrimaNota = calcolaStringaDescrizionePrimaNota(documento);
		String descrizioneMovimentoFinanziario = calcolaStringaDescrizioneMovimentoFinanziario(documento);
		String descrizioneRichiesta = calcolaDescrizioneRichiesta();
	
		model.setDescrMovimentoFinanziario(descrizioneMovimentoFinanziario);
		model.setDescrRichiesta(descrizioneRichiesta);
		if(StringUtils.isBlank(model.getPrimaNota().getDescrizione())) {
			// Imposto solo una volta la descrizione
			model.getPrimaNota().setDescrizione(descrizionePrimaNota);
		}
	}

	/**
	 * Calcola la stringa della descrizione della richiesta.
	 * 
	 * @return la stringa popolata
	 */
	private String calcolaDescrizioneRichiesta() {
		RegistrazioneMovFin sb = model.getRegistrazioneMovFin();
		StringBuilder sbDescrizioneMovFin = new StringBuilder();
		sbDescrizioneMovFin.append(FormatUtils.formatDate(sb.getDataRegistrazione()));
		if(sb.getEvento() != null) {
			if(sb.getEvento().getTipoCollegamento() != null) {
				sbDescrizioneMovFin.append(" - ")
					.append(sb.getEvento().getTipoCollegamento().getDescrizione());
			}
			sbDescrizioneMovFin.append(" - ")
				.append(sb.getEvento().getDescrizione());
		}
		return sbDescrizioneMovFin.toString();
	}

	/**
	 * Calcola la stringa della descrizione del movimento finanziario.
	 * 
	 * @param documento il documento
	 * 
	 * @return la stringa popolata
	 */
	private String calcolaStringaDescrizioneMovimentoFinanziario(D documento) {
		StringBuilder sb = new StringBuilder();
		sb.append(documento.getAnno())
			.append("/")
			.append(documento.getNumero())
			.append(computeSubdocumento());
		if(documento.getTipoDocumento() != null) {
			sb.append("/")
				.append(documento.getTipoDocumento().getCodice());
		}
		if(documento.getSoggetto() != null) {
			sb.append("/")
				.append(documento.getSoggetto().getCodiceSoggetto());
		}
		return sb.append(computeSuffix())
				.toString();
	}

	/**
	 * Calcola la stringa della descrizione della prima nota.
	 * 
	 * @param documento il documento
	 * @return la stringa popolata
	 */
	private String calcolaStringaDescrizionePrimaNota(D documento) {
		StringBuilder sb = new StringBuilder();
		if(documento.getTipoDocumento() != null) {
			sb.append(documento.getTipoDocumento().getDescrizione())
				.append(" ");
		}
		return sb.append(documento.getAnno())
			.append("/")
			.append(documento.getNumero())
			.append(computeSubdocumento())
			.append(" ")
			.append(documento.getDescrizione())
			.toString();
	}
	
	/**
	 * Calcolo del subdocumento.
	 * 
	 * @return la porzione di descrizione corrispondente al subdocumento
	 */
	protected String computeSubdocumento() {
		// Implementazione di default: non calcolo alcunche'
		return "";
	}

	/**
	 * Calcolo del suffisso.
	 * 
	 * @return il suffisso
	 */
	protected String computeSuffix() {
		// Implementazione di default: non calcolo alcunche'
		return "";
	}
	
	/**
	 * Impostazione dei dati per la visualizzazione del dettaglio del documento.
	 */
	private void impostaDatiPerVisualizzazioneDettaglio() {
		// Impostazione dati documenti collegati
		D documento = model.getDocumento();
		
		calcolaStringaDocumentiCollegati(documento);
		calcolaStringaNoteCredito(documento);
	}
	
	/**
	 * Creazione della stringa dei documenti collegati.
	 * 
	 * @param documento il documento da cui ottenere i collegati
	 */
	private void calcolaStringaDocumentiCollegati(D documento) {
		List<Documento<?, ?>> listaDocumentiCollegati = new ArrayList<Documento<?,?>>();
		listaDocumentiCollegati.addAll(documento.getListaDocumentiEntrataFiglio());
		listaDocumentiCollegati.addAll(documento.getListaDocumentiSpesaFiglio());
		
		if(documento.getTipoDocumento() != null && BilConstants.CODICE_NOTE_ACCREDITO.getConstant().equals(documento.getTipoDocumento().getCodice())){
			listaDocumentiCollegati.addAll(documento.getListaDocumentiSpesaPadre());
			listaDocumentiCollegati.addAll(documento.getListaDocumentiEntrataPadre());
		}
		
		if(listaDocumentiCollegati.isEmpty()) {
			model.setDocumentiCollegati("");
			return;
		}
		
		List<String> chunks = new ArrayList<String>();
		
		for(Documento<?, ?> d : listaDocumentiCollegati) {
			chunks.add(calcolaStringaDocumento(d));
		}
		
		model.setDocumentiCollegati(StringUtils.join(chunks, "<br/>"));
	}

	/**
	 * Calcola la stringa per il singolo documento.
	 * 
	 * @param documento il documento
	 * 
	 * @return la stringa corrispondente al documento
	 */
	private String calcolaStringaDocumento(Documento<?, ?> documento) {
		StringBuilder sb = new StringBuilder();
		if(documento instanceof DocumentoSpesa) {
			sb.append("S");
		} else {
			sb.append("E");
		}
		sb.append("/")
			.append(documento.getAnno());
		if(documento.getTipoDocumento() != null) {
			sb.append("/")
				.append(documento.getTipoDocumento().getCodice());
		}
		return sb.append("/")
			.append(documento.getNumero())
			.toString();
	}

	/**
	 * Calcola la stringa per le note di credito
	 * 
	 * @param documento il documento
	 */
	private void calcolaStringaNoteCredito(D documento) {
		List<Documento<?, ?>> listaDocumentiCollegati = new ArrayList<Documento<?,?>>();
		listaDocumentiCollegati.addAll(documento.getListaNoteCreditoEntrataFiglio());
		listaDocumentiCollegati.addAll(documento.getListaNoteCreditoSpesaFiglio());
		
		if(listaDocumentiCollegati.isEmpty()) {
			model.setNoteCredito("");
			return;
		}
		
		List<String> chunks = new ArrayList<String>();
		for(Documento<?, ?> d : listaDocumentiCollegati) {
			chunks.add(calcolaStringaDocumento(d));
		}
		
		model.setNoteCredito(StringUtils.join(chunks, "<br/>"));
	}

	/**
	 * Wrap per le quote
	 */
	private void wrapQuote() {
		D documento = model.getDocumento();
		List<S> listaSubdocumento = documento.getListaSubdocumenti();
		List<RegistrazioneMovFin> listaRegistrazioneMovFin = model.getListaRegistrazioneMovFin();
		List<E> listaWrapper = new ArrayList<E>();
		
		for(S subdocumento : listaSubdocumento) {
			E wrapper = wrapSubdocumento(subdocumento, listaRegistrazioneMovFin);
			listaWrapper.add(wrapper);
		}
		
		if(!documento.getListaSubdocumentoIva().isEmpty()) {
			// Ho i dati iva sul documento
			impostaDatiIvaSuiWrapperDaDocumento(documento, listaWrapper);
		}
		// L'impostazione dei dati via IVA sulle quote nel caso in cui sia il subdocumento ad avere un'IVA associata e' demandata alla classe gestente il subdocumento
		
		model.setListaElementoQuota(listaWrapper);
	}
	
	/**
	 * Wrap del subdocumento.
	 * 
	 * @param subdocumento             il subdocumento da wrappare
	 * @param listaRegistrazioneMovFin la lista delle registrazioni
	 * @return il wrapper
	 */
	private E wrapSubdocumento(S subdocumento, List<RegistrazioneMovFin> listaRegistrazioneMovFin) {
		final String methodName = "wrapSubdocumento";
		
		RegistrazioneMovFin registrazioneMovimentoFin = null;
		Class<?> clazz = subdocumento.getClass();
		
		log.debug(methodName, "Wrap per il subdocumento con uid " + subdocumento.getUid() + " e classe " + clazz.getSimpleName());
		
		for(RegistrazioneMovFin rmf : listaRegistrazioneMovFin) {
			if(clazz.isInstance(rmf.getMovimento()) && rmf.getMovimento().getUid() == subdocumento.getUid() && 
					!StatoOperativoRegistrazioneMovFin.ANNULLATO.equals(rmf.getStatoOperativoRegistrazioneMovFin())) {
				log.debug(methodName, "Registrazione trovata con uid " + rmf.getUid());
				registrazioneMovimentoFin = rmf;
				break;
			}
		}
		return wrapSubdocumento(subdocumento, registrazioneMovimentoFin);
	}
	
	/**
	 * Wrap del subdocumento.
	 * 
	 * @param subdocumento        il subdocumento da wrappare
	 * @param registrazioneMovFin la registrazione da wrappare
	 * @return il wrapper
	 */
	protected E wrapSubdocumento(S subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		return null;
	}

	/**
	 * Impostazione dei dati iva del documento sui wrapper.
	 * 
	 * @param documento    il documento avente i dati iva
	 * @param listaWrapper i wrapper su cui impostare i dati dell'iva
	 */
	private void impostaDatiIvaSuiWrapperDaDocumento(D documento, List<E> listaWrapper) {
		final String methodName = "impostaDatiIvaSuiWrapperDaDocumento";
		List<SI> listaSubdocumentoIva = documento.getListaSubdocumentoIva();
		
		BigDecimal totaleImponibileIva = BigDecimal.ZERO;
		BigDecimal totaleImpostaIva = BigDecimal.ZERO;
		BigDecimal totaleQuoteRilevantiIva = BigDecimal.ZERO;
		BigDecimal totaleImponibileIvaImpostato = BigDecimal.ZERO;
		BigDecimal totaleImpostaIvaImpostato = BigDecimal.ZERO;
		
		int indiceUltimaQuotaRilevanteIva = -1;
		
		// Calcolo totali iva del documento
		for(SI si : listaSubdocumentoIva) {
			totaleImponibileIva = totaleImponibileIva.add(si.getTotaleImponibileMovimentiIva()).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
			totaleImpostaIva = totaleImpostaIva.add(si.getTotaleImpostaMovimentiIva()).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
		}
		log.debug(methodName, "Totale dati iva sul documento: totale imponibile = " + totaleImponibileIva.toString() + " --- totale imposta = " + totaleImpostaIva.toString());
		
		// Calcolo totale note rilevanti iva
		for(int i = 0; i < listaWrapper.size(); i++) {
			E wrapper = listaWrapper.get(i);
			if(Boolean.TRUE.equals(wrapper.getSubdocumento().getFlagRilevanteIVA())) {
				indiceUltimaQuotaRilevanteIva = i;
				totaleQuoteRilevantiIva = totaleQuoteRilevantiIva.add(wrapper.getSubdocumento().getImportoNotNull()).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
			}
		}
		log.debug(methodName, "Totale degli importi per le note rilevanti iva = " + totaleQuoteRilevantiIva.toString());
		
		if(indiceUltimaQuotaRilevanteIva == -1) {
			log.debug(methodName, "Nessuna quota rilevante iva per cui impostare i valori");
			return;
		}
		
		// Impostazione dei dati
		for(E wrapper : listaWrapper) {
			wrapper.setDatiIvaSuDocumento(true);
			if(wrapper.isRilevanteIva()) {
				BigDecimal imponibile = wrapper.getSubdocumento().getImportoNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImponibileIva)
						.setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
				BigDecimal imposta = wrapper.getSubdocumento().getImportoNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImpostaIva)
						.setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
				
				totaleImponibileIvaImpostato = totaleImponibileIvaImpostato.add(imponibile).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
				totaleImpostaIvaImpostato = totaleImpostaIvaImpostato.add(imposta).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
				
				log.debug(methodName, "Subdocumento " + wrapper.getSubdocumento().getNumero() + " --- imponibile da impostare = " + imponibile.toString() + " --- imposta da impostare = " + imposta.toString()
						+ " --- incrementale imponibile = " + totaleImponibileIvaImpostato.toString() + " --- incrementale imposta = " + totaleImpostaIvaImpostato.toString());
				
				wrapper.setImponibileDaImpostare(imponibile);
				wrapper.setImpostaDaImpostare(imposta);
			}
		}
		
		BigDecimal differenzaImponibile = totaleImponibileIva.subtract(totaleImponibileIvaImpostato);
		BigDecimal differenzaImposta = totaleImpostaIva.subtract(totaleImpostaIvaImpostato);
		log.debug(methodName, "Totale imponibile = " + totaleImponibileIva.toString() + " --- totale imposta = " + totaleImpostaIva.toString()
				+ " --- imponibile impostato = " + totaleImponibileIvaImpostato.toString() + " --- imposta impostato = " + totaleImpostaIvaImpostato.toString()
				+ " --- differenza imponibile = "  + differenzaImponibile.toString() + " --- differenza impota = " + differenzaImposta.toString());
		
		if(differenzaImponibile.signum() > 0 || differenzaImposta.signum() > 0) {
			// Devo impostare le differenze
			E lastWrapper = listaWrapper.get(indiceUltimaQuotaRilevanteIva);
			BigDecimal oldImponibile = lastWrapper.getImponibileDaImpostare();
			BigDecimal oldImposta = lastWrapper.getImpostaDaImpostare();
			
			BigDecimal newImponibile = oldImponibile.add(differenzaImponibile).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
			BigDecimal newImposta = oldImposta.add(differenzaImposta).setScale(FormatUtils.MATH_CONTEXT.getPrecision(), FormatUtils.MATH_CONTEXT.getRoundingMode());
			
			lastWrapper.setImponibileDaImpostare(newImponibile);
			lastWrapper.setImpostaDaImpostare(newImposta);
			
			log.debug(methodName, "Subdocumento " + lastWrapper.getSubdocumento().getNumero() + " --- vecchio imponibile = " + oldImponibile.toString() + " --- vecchia imposta = " + oldImposta.toString()
					+ " --- nuovo imponibile = " + newImponibile.toString() + " --- nuova imposta = " + newImposta.toString());
		}
	}

	/**
	 * Preparazione per lo step 1: pulisco il model dello step2
	 */
	public void prepareStep1() {
		model.setMovimentoEP(null);
		model.setSubdocumento(null);
	}
	
	/**
	 * Ingresso nello step 1.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String step1() {
		final String methodName = "step1";
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		Boolean shouldReWrap = sessionHandler.getParametro(BilSessionParameter.PRIMA_NOTA_INTEGRATA_DOCUMENTO_RICALCOLA_QUOTE);
		if(Boolean.TRUE.equals(shouldReWrap)) {
			sessionHandler.setParametro(BilSessionParameter.PRIMA_NOTA_INTEGRATA_DOCUMENTO_RICALCOLA_QUOTE, null);
			// Ricalcolo le registrazioni
			try {
				ricercaMovimentiEP();
			} catch(WebServiceInvocationFailureException wsife) {
				log.warn(methodName, "Fallimento nel recupero dei movimenti EP: " + wsife.getMessage());
			}
			// Ri-wrappo le quote
			wrapQuote();
		}
		
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciPrimaNota()}.
	 */
	public void prepareInserisciPrimaNota() {
		model.setPrimaNota(null);
		model.setClassificatoreGSA(null);
	}

	/**
	 * Salvataggio della prima nota integrata
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciPrimaNota() {
		return registraPrimaNota(true);
	}
	
	/**
	 * Registrazione della prima nota.
	 * 
	 * @param mayValidate se sia possibile la validazione
	 * @return una stringa corrispondente al risultato dell'invocazione 
	 */
	protected String registraPrimaNota(boolean mayValidate) {

		final String methodName = "inserisciPrimaNota";
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrata(mayValidate);
		logServiceRequest(req);
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RegistraPrimaNotaIntegrata.class, res));
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Registrata prima nota integrata con uid " + res.getPrimaNota().getUid());
		
		model.setPrimaNota(res.getPrimaNota());
//		model.getPrimaNota().setListaMovimentiEP(response.getPrimaNota().getListaMovimentiEP());
		
		// Sia qui che in sessione
		impostaInformazioneSuccesso();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		sessionHandler.setParametro(BilSessionParameter.PRIMA_NOTA_INTEGRATA_DOCUMENTO_RICALCOLA_QUOTE, Boolean.TRUE);
		return SUCCESS;
	
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciPrimaNota()}.
	 */
	public void validateInserisciPrimaNota() {
		
		checkNotNull(model.getPrimaNota(), "Prima nota integrata", true);
		// Validazione dei dati di base della prima nota
		validazioneDatiPrimaNota();
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaPrimaNota()}.
	 */
	public void prepareAggiornaPrimaNota() {
		model.setPrimaNota(null);
		model.setClassificatoreGSA(null);
	}

	/**
	 * Aggiornamento della prima nota integrata
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaPrimaNota() {
		return registraPrimaNota(true);
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaPrimaNota()}.
	 */
	public void validateAggiornaPrimaNota() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "Prima nota integrata", true);
		// Validazione dei dati di base della prima nota
		validazioneDatiPrimaNota();
	}
	
	/**
	 * Validazione dei dati di base per la prima nota.
	 */
	private void validazioneDatiPrimaNota() {
		Date now = new Date();
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione");
		checkCondition(model.getPrimaNota().getDataRegistrazione() == null || !model.getPrimaNota().getDataRegistrazione().after(now),
			ErroreCore.FORMATO_NON_VALIDO.getErrore("Data registrazione", "non puo' essere una data futura"));
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione");
		for(E elementoQuota : model.getListaElementoQuota()) {
			checkNotNullNorInvalidUid(elementoQuota.getRegistrazioneMovFin(), "Registrazione per il subdocumento " + elementoQuota.getSubdocumento().getNumero());
		}
	}
	
}

