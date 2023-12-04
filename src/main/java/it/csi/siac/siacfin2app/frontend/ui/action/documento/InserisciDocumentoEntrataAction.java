/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.InserisciDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Classe di action per l'inserimento del Documento di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 25/03/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciDocumentoEntrataAction extends GenericDocumentoAction<InserisciDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1919984150384201989L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	@AnchorAnnotation(value = "OP-ENT-insDocEnt", name = "Documento STEP 1")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("Inserimento documento di entrata");
		
		// Caricamento delle liste
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.ENTRATA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		checkAndObtainListaClassiSoggetto();
		
		precaricaDocumentoCollegato();
		// Per la pulizia
		aggiungiInSessionePerPulizia(BilSessionParameter.MODEL_INSERISCI_DOCUMENTO_ENTRATA);
		
		caricaProvvisoriDiCassa();
		
		// SIAC-7571
		// Dati FEL
		caricaDatiFatturaFEL();
		// TODO CONTROLLARE SE SELEZIONA QUELLO CORRETTO
		impostaDefaultFatturaFELStep1();			
		
		return SUCCESS;
	}

	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		// Caricamento liste
		checkAndObtainListaCodiceBollo();
		model.popolaParametriDiDefaultStep2();
		
		//SIAC-7567
		//controllo che il soggetto sia o meno una PA
		model.setCheckCanale(model.getSoggetto().isSoggettoPA());
		
		//SIAC-7571
		impostaDefaultFatturaFELStep2();

		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocEnt", name = "Documento STEP 2")
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		return salvaDocumento();
	}
	
	/**
	 * SIAC-7567
	 * Inserimento del Documento di Entrata tramite chiamata asincrona
	 * questo metodo cappello richiama la validazione dei campi e ne esegue i controlli
	 * se superati si eseguono i controlli per il warning in caso di soggetto PA
	 * se presenti messaggi, a seguito del warning, verranno resituti all'utente
	 * in caso di validazione da parte dell'utente si prosegue con il giro solito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3Asincrono() {
		String methodName = "enterStep3Asincrono";

		log.debug(methodName, "INIZIO validazione asincrona");
		
		//richiamo la validazione
		validateEnterStep3();
		//non vado avanti se ho errori
		if(model.getErrori().size() > 0 && !model.getErrori().isEmpty()) {
			return "validationError";
		}
		
		//controllo se il debitore e' una PA
		//se ci sono messaggi e non ho la conferma li devo resituire all'utente
		if(checkWarningCigCupPA(model.getDocumento(), model.getSoggetto()) && Boolean.FALSE.equals(model.isProseguireConElaborazioneCheckPA())) {
			return "validationError";
		}
		
		log.debug(methodName, "FINE validazione asincrona");
		
		return "validationSuccess";
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocEnt", name = "Documento STEP 3")
	public String step3() {
		impostaInformazioneSuccesso();
		leggiEventualiMessaggiAzionePrecedente();
		return SUCCESS;
	}

	/**
	 * Metodo per la redirezione verso l'aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso la ripetizione dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocEnt", name = "Documento STEP 1")
	public String ripeti() {
		DocumentoEntrata documentoSalvato = model.getDocumento();
		Soggetto soggettoAssociato = model.getSoggetto();
		cleanModel();
		model.impostaDatiRipeti(documentoSalvato, soggettoAssociato);
		
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaCodiceBollo();
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.ENTRATA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso l'aggiornamento, con il tab delle quote.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String quote() {
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripetiSalva() {
		return salvaDocumento();
	}

	/**
	 * SIAC-7567
	 * Inserimento del Documento di Entrata tramite chiamata asincrona
	 * questo metodo cappello richiama la validazione dei campi e ne esegue i controlli
	 * se superati si eseguono i controlli per il warning in caso di soggetto PA
	 * se presenti messaggi, a seguito del warning, verranno resituti all'utente
	 * in caso di validazione da parte dell'utente si prosegue con il giro solito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripetiSalvaAsincrono() {
		String methodName = "ripetiSalvaAsincrono";
		
		log.debug(methodName, "FINE validazione asincrona");
		
		//richiamo la validazione
		validateRipetiSalva();
		//non vado avanti se ho errori
		if(model.getErrori().size() > 0 && !model.getErrori().isEmpty()) {
			return "validationError";
		}
		
		//controllo se il debitore e' una PA
		//se ci sono messaggi e non ho la conferma li devo resituire all'utente
		if(checkWarningCigCupPA(model.getDocumento(), model.getSoggetto()) && Boolean.FALSE.equals(model.isProseguireConElaborazioneCheckPA())) {
			return "validationError";
		}
		
		log.debug(methodName, "FINE validazione asincrona");
		
		return "validationSuccess";
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocEnt", name = "Documento STEP 3")
	public String ripetiStep3() {
		//SIAC-7567
		impostaInformazioneSuccesso();
		leggiEventualiMessaggiAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'ingresso nello step2.
	 */
	public void validateEnterStep2() {
		final String methodName = "validateEnterStep2";
		log.debug(methodName, "Validazione campi obbligatori");
		
		DocumentoEntrata documento = model.getDocumento();
		Soggetto soggetto = model.getSoggetto();
		
		checkNotNullNorInvalidUid(documento.getTipoDocumento(), "Tipo");
		checkNotNull(documento.getAnno(), "Anno");
		checkNotNullNorEmpty(documento.getNumero(), "Numero");
		checkNotNull(documento.getDataEmissione(), "Data");
		
		// SIAC 6677
		Date today = new Date();
		Calendar todayCal = Calendar.getInstance();
		todayCal.setTime(today);
		todayCal.set(Calendar.HOUR_OF_DAY, 0);
		todayCal.set(Calendar.MINUTE, 0);
		todayCal.set(Calendar.SECOND, 0);
		checkCondition(
				documento.getDataOperazione() == null ||  documento.getDataOperazione().compareTo(todayCal.getTime()) <= 0,
						ErroreFin.DATA_OPERAZIONE_SUCCESSIVA_ALLA_DATA_ODIERNA.getErrore());
		
		// Controlla se il soggetto sia valido
		boolean controlloSoggetto = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		checkCondition(controlloSoggetto, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		
		log.debug(methodName, "Validazione logica campi");
		if(documento.getAnno() != null) {
			checkCondition(model.getAnnoEsercizioInt().compareTo(documento.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			if(documento.getDataEmissione() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(documento.getDataEmissione());
				checkCondition(documento.getAnno().compareTo(cal.get(Calendar.YEAR)) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
			}
		}
		
		if(controlloSoggetto) {
			validaSoggetto(true);
		}
	}
	
	/**
	 * Validazione per l'ingresso nello step3.
	 */
	public void validateEnterStep3() {
		final String methodName = "validateEnterStep3";
		log.debug(methodName, "Validazione campi obbligatori");
		
		DocumentoEntrata documento = model.getDocumento();
		checkNotNull(documento.getImporto(), "Importo");
		checkNotNullNorEmpty(documento.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(documento.getCodiceBollo(), "Imposta di bollo");
		
		log.debug(methodName, "Validazione logica campi");
		// L'importo deve essere non negativo -- NO CR-3469 -- SI SIAC-7193
		checkCondition(documento.getImporto() == null || documento.getImporto().signum() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
//		// L'arrotondamento deve essere non-positivo -- NO, cr 2889
//		checkCondition(documento.getArrotondamento() == null || documento.getArrotondamento().signum() <= 0,
//				ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": deve essere minore o uguale a zero"));
		// Importo + arrotondamento > 0
		checkCondition(documento.getImporto() == null || documento.getArrotondamento() == null || documento.getImporto().add(documento.getArrotondamento()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore o uguale a zero"));
		// Se la data di scadenza è presente, deve essere maggiore o uguale la data del documento
		checkCondition(documento.getDataEmissione() == null || documento.getDataScadenza() == null || 
				documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
				ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		checkCondition(!(documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Repertorio","data e numero devono essere entrambi presenti o assenti"));
		
		checkCondition(model.getTotaleProvvisori() == null || documento.getImporto().compareTo(model.getTotaleProvvisori()) >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo","deve essere maggiore o uguale al totale dei provvisori selezionati"));
		
		if(model.getUidDocumentoCollegato() != null){
			checkPerDocumentoCollegato();
		}
		
		//SIAC-5257
		checkProtocollo(documento);
		
		//SIAC 6677 
		checkAndFillCodAvviso(documento);
		
		//SIAC-7567
		checkErrorCigCupPA(documento, model.getSoggetto());
		
	}
	
	/**
	 * Validazione per la ripetizione del salvataggio.
	 */
	public void validateRipetiSalva() {
		validateEnterStep2();
		validateEnterStep3();
	}
	
	/**
	 * Richiamo al metodo {@link #salvataggioDocumento()} se non ci sono provvisori di cassa selezionati o {@link #salvataggioDocumentoPerProvvisori()} altrimenti
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvaDocumento(){
		if(model.getListaUidProvvisori() == null || model.getListaUidProvvisori().isEmpty()){
			return salvataggioDocumento();
		}
		return salvataggioDocumentoPerProvvisori();
	}
	
	/**
	 * Effettua il salvataggio del documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvataggioDocumento() {
		final String methodName = "salvataggioDocumento";
			
		log.debug(methodName, "Creazione della request InserisceDocumentoEntrata");
		InserisceDocumentoEntrata request = model.creaRequestInserisceDocumentoEntrata();
		logServiceRequest(request);
		
		log.debug(methodName, "Invocazione del servizio InserisceDocumentoEntrataService");
		InserisceDocumentoEntrataResponse response = documentoEntrataService.inserisceDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		DocumentoEntrata documentoEntrata = response.getDocumentoEntrata();
		
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoEntrata.getUid());
		model.popolaModel(documentoEntrata);
		caricaClassificatori();
		
		// Fornisci messaggio successo
		// SIAC-5257
		setMessaggiInSessionePerActionSuccessiva();
		
		return SUCCESS;
	}
	
	/**
	 * Effettua il salvataggio del documento e di una quota per ogni provvisorio selezionato.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvataggioDocumentoPerProvvisori() {
		final String methodName = "salvataggioDocumentoPerProvvisori";
		
		log.debug(methodName, "Creazione della request InserisceDocumentoPerProvvisoriEntrata");
		InserisceDocumentoPerProvvisoriEntrata request = model.creaRequestInserisceDocumentoPerProvvisoriEntrata();
		logServiceRequest(request);
		
		log.debug(methodName, "Invocazione del servizio InserisceDocumentoEntrataService");
		InserisceDocumentoPerProvvisoriEntrataResponse response = documentoEntrataService.inserisceDocumentoPerProvvisoriEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		DocumentoEntrata documentoEntrata = response.getDocumentoEntrata();
		
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoEntrata.getUid());
		model.popolaModel(documentoEntrata);
		caricaClassificatori();
		
		// SIAC-5257
		setMessaggiInSessionePerActionSuccessiva();
		
		return SUCCESS;
	}

	/**
	 * Carica i classificatori dalla sessione.
	 */
	private void caricaClassificatori() {
		DocumentoEntrata documento = model.getDocumento();
		
		TipoDocumento tipoDocumento = documento.getTipoDocumento();
		List<TipoDocumento> listaTipoDocumento = model.getListaTipoDocumento();
		tipoDocumento = ComparatorUtils.searchByUid(listaTipoDocumento, tipoDocumento);
		
		CodiceBollo codiceBollo = documento.getCodiceBollo();
		List<CodiceBollo> listaCodiceBollo = model.getListaCodiceBollo();
		codiceBollo = ComparatorUtils.searchByUid(listaCodiceBollo, codiceBollo);
		
		documento.setTipoDocumento(tipoDocumento);
		documento.setCodiceBollo(codiceBollo);
	}
	
	/**
	 * Effettua i controlli per i documenti collegati
	 */
	private void checkPerDocumentoCollegato() {
		Documento<?, ?> documentoCollegato = "Entrata".equals(model.getClasseDocumentoCollegato()) ?
				model.getDocumentoEntrataCollegato() :
				model.getDocumentoSpesaCollegato();
		BigDecimal importoDocumentoCollegato = documentoCollegato.getImporto();
		BigDecimal importo = model.getDocumento().getImporto();
		
		if(Boolean.TRUE.equals(model.getFlagSubordinato())) {
			checkCondition(importo.subtract(importoDocumentoCollegato).signum() <= 0,
				ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Il documento",
					"di tipo subordinato, l'importo non puo' superare l'importo del documento origine"));
		} else if (Boolean.TRUE.equals(model.getFlagRegolarizzazione())) {
			checkCondition(importo.subtract(importoDocumentoCollegato).signum() == 0,
				ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Il documento",
					"di tipo regolarizzazione, l'importo deve essere uguale all'importo del documento origine"));
		}
	}
	
	/**
	 * Precarica il documento dal documento collegato.
	 */
	private void precaricaDocumentoCollegato() {
		if(model.getUidDocumentoCollegato() == null || model.getClasseDocumentoCollegato() == null) {
			return;
		}
		
		Documento<?, ?> documentoCollegato = null;
		if("Entrata".equals(model.getClasseDocumentoCollegato())) {
			RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoCollegato());
			RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
			documentoCollegato = response.getDocumento();
			model.setDocumentoEntrataCollegato(response.getDocumento());
		} else {
			RicercaDettaglioDocumentoSpesa request = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoCollegato());
			RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(request);
			documentoCollegato = response.getDocumento();
			model.setDocumentoSpesaCollegato(response.getDocumento());
		}
		
		// Precaricamento dati
		DocumentoEntrata documentoEntrata = model.getDocumento();
		if(documentoEntrata == null) {
			documentoEntrata = new DocumentoEntrata();
			model.setDocumento(documentoEntrata);
		}
		
		documentoEntrata.setAnno(documentoCollegato.getAnno());
		documentoEntrata.setAnnoRepertorio(documentoCollegato.getAnnoRepertorio());
		documentoEntrata.setRegistroRepertorio(documentoCollegato.getRegistroRepertorio());
		documentoEntrata.setNumeroRepertorio(documentoCollegato.getNumeroRepertorio());
		documentoEntrata.setDataRepertorio(documentoCollegato.getDataRepertorio());
		documentoEntrata.setDescrizione(documentoCollegato.getDescrizione());
		documentoEntrata.setCodiceBollo(documentoCollegato.getCodiceBollo());
		
		model.setSoggetto(documentoCollegato.getSoggetto());
	}
	
	private TipoDocumento cercaTipoDocINS() {
		for(TipoDocumento td : model.getListaTipoDocumento()){
			if(td.getCodice().equals(BilConstants.TIPO_DOCUMENTO_INC.getConstant())){
				return td;
			}
		}
		return null;
	}
	
	/**
	 * carica gli eventuali provvisori di cassa presenti in sessione
	 */
	private void caricaProvvisoriDiCassa() {
		List<Integer> listaUidDaSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_UID_PROVVISORI);
		if(listaUidDaSessione == null || listaUidDaSessione.isEmpty()){
			sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, null);
			return;
		}
		model.setListaUidProvvisori(listaUidDaSessione);
		BigDecimal importo = sessionHandler.getParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI);
		if(model.getDocumento() == null) {
			model.setDocumento(new DocumentoEntrata());
		}
		model.getDocumento().setImporto(importo);
		model.setTotaleProvvisori(importo);
		sessionHandler.setParametro(BilSessionParameter.LISTA_UID_PROVVISORI, null);
		sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, null);
		model.getDocumento().setTipoDocumento(cercaTipoDocINS());
	}
	
	/**
	 * Controlla che se presente il codice Avviso pago PA non superi le 18 cifre
	 * Nel caso le cifre fossero minori di 18 applica un fill left
	 * @param documento
	 */
	private void checkAndFillCodAvviso(DocumentoEntrata documento){

		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length() == 0 ||
				isNumeric(documento.getCodAvvisoPagoPA()),
				ErroreFin.COD_AVVISO_PAGO_PA_NUMERICO.getErrore());
		
		int maxLength = CostantiFin.CODICE_AVVISO_PAGO_PA_LENGTH;
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length() <= maxLength,
				ErroreFin.COD_AVVISO_PAGO_PA_MAXLENGTH.getErrore());
		if(documento.getCodAvvisoPagoPA() != null && documento.getCodAvvisoPagoPA().length() > 0 && documento.getCodAvvisoPagoPA().length() < maxLength){
			int diff = maxLength - documento.getCodAvvisoPagoPA().length();
			StringBuilder codAvviso = new StringBuilder();
			for(int i = 0; i < diff; i++){
				codAvviso.append("0");
			}
			codAvviso.append(documento.getCodAvvisoPagoPA());
			documento.setCodAvvisoPagoPA(codAvviso.toString());
		}	
	
	}

	/**
	 * SIAC-7571
	 * Impostazione dei default della fattura FEL per lo step 1, in merito a nuova gestione di FEL a importo negativo
	 * <ul>
	 *     <li>Anno documento = Anno data documento FEL (entit&agrave; Fattura Elettronica)</li>
	 *     <li>Tipo documento = vedi nuovi tipi NCF e FSN</li>
	 *     <li>Numero documento = numero documento FEL (entit&agrave; Fattura Elettronica) <strong>(LOTTO M)</strong></li>
	 *     <li>Soggetto Intestatario = Soggetto identificato dall'operatore nell'importazione del documento FEL e passato come parametro</li>
	 *     <li>Data documento = data documento FEL (entit&agrave; Fattura Elettronica)</li>
	 *     <li>Data ricezione = data ricezione FEL (entit&agrave; Portale Fatture)</li>
	 * </ul>
	 */
	private void impostaDefaultFatturaFELStep1() {
		final String methodName = "impostaDefaultFatturaFELStep1";
		FatturaFEL fatturaFEL = model.getFatturaFEL();
		if(fatturaFEL == null) {
			// Non ho la fattura: esco
			return;
		}
		
		DocumentoEntrata documentoEntrata = model.getDocumento() != null ? model.getDocumento() : new DocumentoEntrata();
		
		// Numero Documento =  Numero Documento FEL
		documentoEntrata.setNumero(fatturaFEL.getNumero());
		
		// Anno documento = Anno data documento FEL
		if(fatturaFEL.getData() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fatturaFEL.getData());
			
			Integer anno = cal.get(Calendar.YEAR);
			documentoEntrata.setAnno(anno);
			log.debug(methodName, "Fattura.data = " + fatturaFEL.getData() + " => documento.anno = " + documentoEntrata.getAnno());
			
			// Data documento = data documento FEL
			documentoEntrata.setDataEmissione(fatturaFEL.getData());
			log.debug(methodName, "Fattura.data = " + fatturaFEL.getData() + " => documento.dataCreazioneDocumento = " + documentoEntrata.getDataEmissione());
		}
		
		// Tipo documento
		impostaTipoDocumentoDaFatturaFEL(documentoEntrata);
		
		// Soggetto Intestatario = Soggetto identificato dall'operatore nell'importazione del documento FEL e passato come parametro
		log.debug(methodName, "Soggetto = " + (model.getSoggettoFEL() != null ? model.getSoggettoFEL().getCodiceSoggetto() : "null"));
		model.setSoggetto(ReflectionUtil.deepClone(model.getSoggettoFEL()));
		
		// Data ricezione = data ricezione FEL
		//TODO controllare la data se è quella giusta
		if(fatturaFEL.getPortaleFattureFEL() != null) {
			documentoEntrata.setDataOperazione(fatturaFEL.getPortaleFattureFEL().getDataRicezione());
			log.debug(methodName, "Fattura.portaleFattureFEL.dataRicezione = " + fatturaFEL.getPortaleFattureFEL().getDataRicezione() + " => documento.dataRicezionePortale = " + documentoEntrata.getDataRicezionePortale());
		}
		
		//passo i dati al model
		model.setDocumento(documentoEntrata);
	}

	/**
	 * SIAC-7571
	 * Impostazione dei default della fattura FEL per lo step 2.
	 * <ul>
	 *     <li>Dati repertorio = Dati protocollo (Numero, Data, Anno e registroProtocollo) documento FEL (entit&agrave; protocollo) <strong>(LOTTO M aggiunti registro e anno)</strong></li>
	 *     <li>Descrizione = Causale con progressivo pi&uacute; basso del documento FEL se presente (entit&agrave; Causale)</li>
	 *     <li>Importo = importo totale documento FEL se presente (entit&agrave; Fattura Elettronica), vedi 1.3.9</li>
	 *     <li>Arrotondamento = importo arrotondamento documento FEL se presente (entit&agrave; Fattura Elettronica)</li>
	 *     <li>Codice Ufficio Destinatario = Codice Destinatario (entit&agrave; Fattura Elettronica) se presente in archivio altrimenti non si valorizza</li>
	 *     <li>Elenco Ordini = tutti i 'numero documento' (entit&agrave; OrdineAcquisto) associati alla fattura che si sta importando <strong>(LOTTO M)</strong></li>
	 * </ul>
	 */
	private void impostaDefaultFatturaFELStep2() {
		final String methodName = "impostaDefaultFatturaFELStep2";
		FatturaFEL fatturaFEL = model.getFatturaFEL();
		if(fatturaFEL == null) {
			// Non ho la fattura: esco
			return;
		}
		
		// Se non ho il documento lo inizializzo. Dovrei averlo, ma null-safe
		DocumentoEntrata documentoEntrata = model.getDocumento() != null ? model.getDocumento() : new DocumentoEntrata();

		// Dati repertorio = Dati protocollo (Numero e Data) documento FEL
		if(fatturaFEL.getProtocolloFEL() != null) {
			// Registro protocollo copiato dalla fattura
			documentoEntrata.setRegistroRepertorio(fatturaFEL.getProtocolloFEL().getRegistroProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.registroProtocollo = " + fatturaFEL.getProtocolloFEL().getRegistroProtocollo() + " => documento.registroProtocollo= " + documentoEntrata.getRegistroRepertorio());
			
			// Numero repertorio copiato dalla fattura
			documentoEntrata.setNumeroRepertorio(fatturaFEL.getProtocolloFEL().getNumeroProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.numeroProtocollo = " + fatturaFEL.getProtocolloFEL().getNumeroProtocollo() + " => documento.numeroRepertorio = " + documentoEntrata.getNumeroRepertorio());
			// Data repertorio copiata dalla fattura
			documentoEntrata.setDataRepertorio(fatturaFEL.getProtocolloFEL().getDataRegProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.dataRegProtocollo = " + fatturaFEL.getProtocolloFEL().getDataRegProtocollo() + " => documento.dataRepertorio = " + documentoEntrata.getDataRepertorio());
			
			// SIAC-3101 : l'anno protocollo potrebbe non essere impostato
			Integer annoRepertorio = null;
			try {
				annoRepertorio = Integer.valueOf(fatturaFEL.getProtocolloFEL().getAnnoProtocollo());
			} catch(NumberFormatException nfe) {
				log.warn(methodName, "Attenzione! Anno di protocollo non valido per la fattura " + fatturaFEL.getUid() + " - impostazione del default a null");
			}
			documentoEntrata.setAnnoRepertorio(annoRepertorio);
			log.debug(methodName, "Fattura.protocolloFEL.annoProtocollo = " + fatturaFEL.getProtocolloFEL().getAnnoProtocollo() + " => documento.annoProtocollo= " + documentoEntrata.getAnnoRepertorio());
		}
		
		// Non injetto qua gli ordini perche' potrebbero essere cancellati. Passo il dato alla creazione della request
		
		// Descrizione = Causale con progressivo piu' basso del documento FEL se presente
		if(fatturaFEL.getCausaliFEL() != null) {
			CausaleFEL causaleFEL = null;
			// Calcolo il progressivo piu' basso
			for(CausaleFEL c : fatturaFEL.getCausaliFEL()) {
				if(causaleFEL == null || causaleFEL.getProgressivo().compareTo(c.getProgressivo()) > 0) {
					causaleFEL = c;
				}
			}
			
			if(causaleFEL != null) {
				// Se ho la causale, imposto la descrizione
				documentoEntrata.setDescrizione(causaleFEL.getCausale());
				log.debug(methodName, "Causale.progressivo = " + causaleFEL.getProgressivo() + " => Causale.causale = " + causaleFEL.getCausale() + " => documento.descrizione = " + documentoEntrata.getDescrizione());
			}
		}
		
		// Importo = importo totale documento FEL se presente
		impostaImportoFEL(documentoEntrata, fatturaFEL);
		
		// Arrotondamento = importo arrotondamento documento FEL se presente
		documentoEntrata.setArrotondamento(fatturaFEL.getArrotondamento());
		log.debug(methodName, "Fattura.arrotondamento = " + fatturaFEL.getArrotondamento() + " => documento.arrotondamento = " + documentoEntrata.getArrotondamento());
		
		// Codice Ufficio Destinatario = Codice Destinatario
//		TODO vale anche per entrata il PCC?
//		impostaUfficioDestinatario(documentoEntrata, fatturaFEL);
	}
	
}
