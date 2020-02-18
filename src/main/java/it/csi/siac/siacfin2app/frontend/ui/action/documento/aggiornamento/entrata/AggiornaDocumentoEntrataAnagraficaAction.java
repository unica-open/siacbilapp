/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe di action per l'aggiornamento del Documento di entrata, sezione Anagrafica.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataAnagraficaAction extends AggiornaDocumentoEntrataBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	private static final String PROCEED = "proceed";
	
	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;
	@Autowired private transient CodificheService codificheService;
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		// Inizializzo la action
		initAction();
		// Pulisco le ancore dell'inserimento
		destroyAnchorFamily("OP-SPE-insDocSpe");
		destroyAnchorFamily("OP-ENT-insDocEnt");

		// Pulisco la sessione dei parametri da cancellare
		pulisciSessione();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		//SIAC-5333
		leggiEventualiErroriAzionePrecedente();
		checkCasoDUsoApplicabile("Aggiornamento documento di entrata");
		// Ottengo i dati del documento
		ottieniTipoDocumentoNotaCredito();
		
		return ottieniDocumento();
	}

	/**
	 * Aggiornamento dell'anagrafica del Documento di Entrata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoAnagrafica() {
		final String methodName = "aggiornamentoAnagrafica";
		AggiornaDocumentoDiEntrata request = model.creaRequestAggiornaDocumentoDiEntrata();
		logServiceRequest(request);

		AggiornaDocumentoDiEntrataResponse response = documentoEntrataService.aggiornaDocumentoDiEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}

		impostaInformazioneSuccesso();
		DocumentoEntrata documento = response.getDocumentoEntrata();
		log.debug(methodName, "Aggiornamento effettuato per Documento Entrata con uid: " + documento.getUid());

		// Imposto il dato nel model
		model.setDocumento(documento);

		// Tolgo il flag per l'ingresso sulle quote, se presente
		model.setIngressoTabQuote(Boolean.FALSE);
		// Ricalcolo il documento e tutti i dati associati
		return ottieniDocumento();
	}

	/**
	 * Valida l'aggiornamento dell'anagrafica del Documento di Entrata
	 */
	public void validateAggiornamentoAnagrafica() {
		final String methodName = "validateAggiornamentoAnagrafica";

		try {
			validaCampi();
		} catch (ParamValidationException e) {
			log.debug(methodName, "Errori nella validazione dei campi");
		}
	}

	/**
	 * Validazione dei campi.
	 */
	private void validaCampi() {
		final String methodName = "validaCampi";

		DocumentoEntrata documento = model.getDocumento();

		// Validazione campi obbligatori
		checkCondition(documento.getDataEmissione() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data"), true);
		checkCondition(documento.getImporto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"), true);
		checkCondition(StringUtils.trimToNull(documento.getDescrizione()) != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		checkCondition(documento.getCodiceBollo() != null && documento.getCodiceBollo().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Imposta di bollo"));

		if (model.getDocumentoIncompleto()) {
			// Se lo stato operativo e' INCOMPLETO, controllo il soggetto. Altrimenti posso evitarlo
			checkNotNullNorEmpty(model.getSoggetto().getCodiceSoggetto(), "Codice");
			validaSoggetto(true);
		}

		log.debug(methodName, "Campi obbligatorii: errori rilevati? " + hasErrori());

		// La data di emissione del documento deve essere coerente con l'anno
		// dello stesso
		if (documento.getDataEmissione() != null) {
			Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(documento.getDataEmissione()));
			checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
		}

		//SIAC-7193 e SIAC-7208
		checkCondition(documento.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
		// Validazione degli importi
		validazioneImporti(documento, BigDecimal.ZERO);
		// Se la data di scadenza è presente, deve essere maggiore o uguale la
		// data del documento
		checkCondition(
				documento.getDataEmissione() == null || documento.getDataScadenza() == null
						|| documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
				ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());

		checkCondition(!(documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Repertorio", "data e numero devono essere entrambi presenti o assenti"));
		
		// SIAC-5257
		checkProtocollo(documento);
		
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
				
				
				//SIAC 6677 
				 checkAndFillCodAvviso( documento);
				
				

		log.debug(methodName, "Validazione logica: errori rilevati? " + hasErrori());
	}

	/**
	 * Ottiene il documento dal servizio, e ne ricalcola i dati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione.
	 */
	private String ottieniDocumento() {
		final String methodName = "ottieniDocumento";
		try {
			caricamentoDatiDocumento();
			caricamentoDatiSoggetto();
		} catch(WebServiceInvocationFailureException e) {
			// Se è fallito il caricamento dei dati, loggo ed esco
			log.error(methodName, "Errore nell'invocazione dei servizi per il documento di uid " +
					model.getUidDocumentoDaAggiornare() + ": " + e.getMessage());
			return INPUT;
		}

		model.impostaFlags(AzioniConsentiteFactory.isGestioneIvaConsentito(sessionHandler.getAzioniConsentite()));

		// Caricamento liste
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaCodiceBollo();
		checkAndObtainListaTipoAvviso();
		checkAndObtainListaTipiAtto();
		checkAndObtainListaNoteTesoriere();
		checkAndObtainListaContoTesoreria();

		checkAndObtainListaTipiFinanziamento();

		// Servono per la lista delle note
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.ENTRATA, Boolean.TRUE, Boolean.FALSE);

		// Imposta i dati nel model
		model.impostaListaTipoDocumentoNoteCredito();
		model.calcoloImporti();
		checkAttivazioneRegContabili();
		return SUCCESS;
	}

	/**
	 * Carica i dati relativi al documento.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private void caricamentoDatiDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoDatiDocumento";
		// Ricerca di dettaglio del documento
		RicercaDettaglioDocumentoEntrata request = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoDaAggiornare());
		logServiceRequest(request);

		RicercaDettaglioDocumentoEntrataResponse response = documentoEntrataService.ricercaDettaglioDocumentoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.error(methodName, errorMessage);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMessage);
		}

		model.impostaDatiDocumento(response.getDocumento());
		model.calcoloImporti();
		log.debug(methodName, model.getTotaleDaPagareQuote());
	}

	/**
	 * Carica i dati relativi al soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private void caricamentoDatiSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoDatiSoggetto";
		// Ricerca di dettaglio del documento
		if (model.getSoggetto() != null) {
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);

			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);

			if (response.hasErrori()) {
				String errorMessage = createErrorInServiceInvocationString(request, response);
				log.error(methodName, errorMessage);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMessage);
			}
			List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			model.impostaDatiSoggetto(response.getSoggetto(), listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto);
		}
	}
	
	/**
	 * carica nel model il Tipo Documento realtivo a note credito
	 * 
	 */
	public void ottieniTipoDocumentoNotaCredito() {
		// prelevo la lista dei tipi documento
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.ENTRATA, false, false);
		if (model.getListaTipoDocumento() != null && !model.getListaTipoDocumento().isEmpty()) {
			TipoDocumento tipoDocumentoNC = ComparatorUtils.findByCodice(model.getListaTipoDocumento(), BilConstants.CODICE_NOTE_ACCREDITO.getConstant());
			model.setTipoDocumentoNotaCredito(tipoDocumentoNC);
		}
		
		
	}
	
	/**
	 * Attivazione delle registrazioni contabili.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String attivaRegistrazioniContabili() {
		final String methodName = "attivaRegistrazioniContabili";
		log.debug(methodName, "Uid del documento per cui attivare le registrazioni contabili da aggiornare: " + model.getDocumento().getUid());
		
		AttivaRegistrazioniContabiliEntrata request = model.creaRequestAttivaRegistrazioniContabiliEntrata();
		logServiceRequest(request);
		
		AttivaRegistrazioniContabiliEntrataResponse response = documentoEntrataService.attivaRegistrazioniContabiliEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Registrazioni contabili attivate per il documento " + model.getDocumento().getUid());
		model.setDocumento(response.getDocumentoEntrata());
		impostaInformazioneSuccesso();
		if(verificaPossibilitaEImpostaDatiPerRedirezione(response)){
			return PROCEED;
		}
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return ottieniDocumento();
	}

	/**
	 * @param response
	 */
	private boolean verificaPossibilitaEImpostaDatiPerRedirezione(AttivaRegistrazioniContabiliEntrataResponse response) {
		boolean richiestaRedirezione = Boolean.TRUE.equals(model.getValidazionePrimaNotaDaDocumento());
		if(!model.getAbilitatoPrimaNotaDaFinanziaria() || !richiestaRedirezione) {
			return false;
		}
		boolean inseritaRegistrazione = response.getRegistrazioniMovFinInserite() != null && !response.getRegistrazioniMovFinInserite().isEmpty() && response.getRegistrazioniMovFinInserite().get(0) != null &&response.getRegistrazioniMovFinInserite().get(0).getUid() != 0;
		if(inseritaRegistrazione) {
			model.setUidDaCompletare(response.getRegistrazioniMovFinInserite().get(0).getUid());
			return true;
		}
		
		addErrore(ErroreGEN.OPERAZIONE_NON_CONSENTITA.getErrore("Condizioni per la creazione di una registrazione sulla contabilit&agrave; generale non soddisfatte, impossibile validare ora la prima nota."));
		return false;		
	}
	
	/**
	 * Validazione per il metodo {@link #attivaRegistrazioniContabili()}.
	 */
	public void validateAttivaRegistrazioniContabili() {
		checkCondition(model.getDocumento().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("documento"));
	}
	
	/**
	 * Prepare controlla necessita richiesta ulteriore conferma.
	 */
	public void prepareControllaNecessitaRichiestaUlterioreConferma() {
		model.setEsisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota(false);
	}
	
	/**
	 * Controlla se sia necessaria una richiesta ulteriore conferma
	 *
	 * @return the string
	 */
	public String controllaNecessitaRichiestaUlterioreConferma() {
		final String methodName = "controllaNecessitaRichiestaUlterioreConferma";
		
		if(model.isEsisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota()) {
			//ho gia' calcolato che almeno una delle quote dei documenti ha un accertamento con registrazione. Tanto basra, esco.
			return SUCCESS;
		}
		
		for (SubdocumentoEntrata subdocumentoEntrata : model.getListaSubdocumentoEntrata()) {
			
			List<RegistrazioneMovFin> registrazioni = ottieniRegistrazioniCollegateAlMovimentoGestione(subdocumentoEntrata);
			
			for (RegistrazioneMovFin registrazioneMovFin : registrazioni) {
				if(!StatoOperativoRegistrazioneMovFin.ANNULLATO.equals(registrazioneMovFin.getStatoOperativoRegistrazioneMovFin())) {
					model.setEsisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota(true);
					log.debug(methodName, "trovata registrazione per l'accertamento della quota " + subdocumentoEntrata.getUid());
					return SUCCESS;
				}
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene quali siano le registrazioni collegate al movimento di gestione del subdocumento di entrata
	 *
	 * @param subdocumentoEntrata il subdocumento di entrata per il quale effettuare la ricerca
	 * @return la lista di registrazioni trovate (vuota nel caso in cui )
	 */
	private List<RegistrazioneMovFin> ottieniRegistrazioniCollegateAlMovimentoGestione(SubdocumentoEntrata subdocumentoEntrata) {
		final String methodName = "ottieniRegistrazioniCollegateAlMovimentoGestione";
		List<RegistrazioneMovFin> registrazioni = new ArrayList<RegistrazioneMovFin>();
		Accertamento accertamento = subdocumentoEntrata.getAccertamento();
		if(accertamento == null || accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null) {
			//la quota, per motivi ignoti (potrebbe essere una NCD) non ha l'accertamento: non puo' esistere una registrazione collegata all'accertamento della quota. Passo alla prossima quota.
			return registrazioni;
		}
		
		//chiamo il servizio di ricerca registrazioni contabili
		RicercaSinteticaRegistrazioneMovFin req = model.creaRequestRicercaSinteticaRegistrazioneMovFin(accertamento.getAnnoMovimento(), accertamento.getNumero(), subdocumentoEntrata.getSubAccertamento() != null? subdocumentoEntrata.getSubAccertamento().getNumero() : null, ottieniTipoEventoAccertamento());
		RicercaSinteticaRegistrazioneMovFinResponse res = registrazioneMovFinService.ricercaSinteticaRegistrazioneMovFin(req);
		registrazioni = res.getRegistrazioniMovFin();
		
		if(res.hasErrori() || registrazioni == null || registrazioni.isEmpty()) {
			addErrori(res);
			StringBuilder sb = new StringBuilder()
					.append("Impossibile reperire la registrazione associata all'accertamento ")
					.append( accertamento.getAnnoMovimento())
					.append("/")
					.append(accertamento.getNumero())
					.append(". La ricerca si e' conclusa")
					.append(res.hasErrori() ? " con " : " senza ")
					.append("errori.");
			log.debug(methodName,  sb.toString() );
			return registrazioni;
		}
		return registrazioni;
	}
	
	/**
	 * Ricerca e imposta le liste dei tipi evento di entrata e di spesa
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	private TipoEvento ottieniTipoEventoAccertamento() {
		final String methodName = "ottieniTipoEventoAccertamento";
		if(model.getTipoEventoAccertamento() != null) {
			return model.getTipoEventoAccertamento();
		}
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				log.info(methodName, createErrorInServiceInvocationString(request, response));
				return null;
			}
			listaTipoEvento = response.getCodifiche(TipoEvento.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		
		for (TipoEvento tipoEvento : listaTipoEvento) {
			if(tipoEvento != null && BilConstants.DESCRIZIONE_TIPO_EVENTO_ACCERTAMENTO.getConstant().equals(tipoEvento.getDescrizione())) {
				return tipoEvento;
			}
		}
		
		return null;
	}
	
	
	/**
	 * Controlla che se presente il codice Avviso pago PA non superi le 18 cifre
	 * Nel caso le cifre fossero minori di 18 applica un fill left
	 * @param documento
	 */
	private void checkAndFillCodAvviso(DocumentoEntrata documento){
		
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length()==0 ||
		isNumeric(documento.getCodAvvisoPagoPA()),
		ErroreFin.COD_AVVISO_PAGO_PA_NUMERICO.getErrore());
		
		
		int maxLength = Constanti.CODICE_AVVISO_PAGO_PA_LENGTH;
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length() <= maxLength,
				ErroreFin.COD_AVVISO_PAGO_PA_MAXLENGTH.getErrore());
		if(documento.getCodAvvisoPagoPA()!= null && documento.getCodAvvisoPagoPA().length()>0 && documento.getCodAvvisoPagoPA().length()< maxLength){
			int diff = maxLength -documento.getCodAvvisoPagoPA().length();
			StringBuilder codAvviso = new StringBuilder();
			for(int i=0; i<diff; i++){
				codAvviso.append("0");
			}
			codAvviso.append(documento.getCodAvvisoPagoPA());
			documento.setCodAvvisoPagoPA(codAvviso.toString());
		}	
	}
	
}
