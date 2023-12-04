/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoDocumentoFactory;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.DatiFatturaPagataIncassata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RitenuteDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Anagrafica.
 * 
 * @author Marchino Alessandro
 * @version 1.1.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaAnagraficaAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6990320760805321355L;
	private static final String PROCEED = "proceed";

	@Override
	public void prepareExecute() throws Exception {
		// Svuoto il model, nel caso sia stato inizializzato precedentemente
		setModel(null);
		// Inizializzo la action
		initAction();
		// Distruggo le eventuali ancore dell'inserimento
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
		checkCasoDUsoApplicabile("Aggiornamento documento di spesa");
		//carico il tipodocuimento specifico per notecredito
		
		// Ottengo i dati del del tipo documento
		ottieniTipoDocumentoNotaCredito();
		//carico la lista di stati operativi del provvedimento
		ottieniListaStatoOperativoAtti();

		return ottieniDocumento();
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornamentoAnagrafica()}.
	 */
	public void prepareAggiornamentoAnagrafica() {
		// Pulizia dei campi che vengono re-injettati
		DocumentoSpesa d = model.getDocumento();
		if(d == null) {
			//non ho un documento. esco.
			return;
		}
		//pulisco i dati del model
		d.setCollegatoCEC(null);
		d.setDataEmissione(null);
		d.setDataRicezionePortale(null);
		d.setImporto(null);
		d.setArrotondamento(null);
		d.setDescrizione(null);
		d.setTerminePagamento(null);
		d.setDataScadenza(null);
		d.setRegistroRepertorio(null);
		d.setAnnoRepertorio(null);
		d.setNumeroRepertorio(null);
		d.setDataRepertorio(null);
		d.setCodiceFiscalePignorato(null);
		d.setCodiceBollo(null);
		d.setNote(null);
		d.setCodicePCC(null);
		d.setCodiceUfficioDestinatario(null);
		d.setStrutturaAmministrativoContabile(null);
		// SIAC-5311 SIOPE+
		d.setSiopeDocumentoTipo(null);
		d.setSiopeDocumentoTipoAnalogico(null);
		
		if(d.getFatturaFEL() != null) {
			//il documento ha una fattura FEL collegata
			d.getFatturaFEL().setIdFattura(null);
		}
		d.setDatiFatturaPagataIncassata(new DatiFatturaPagataIncassata());
		
		
		model.setOldArrotondamento(null);
		model.setOldNetto(null);
		
		// SIAC-5311 SIOPE+
		
	}

	/**
	 * Aggiornamento dell'anagrafica del Documento di Spesa.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamentoAnagrafica() {
		final String methodName = "aggiornamentoAnagrafica";
		//chiamo il servizio di aggiornamento dell'anagrafica del documento
		AggiornaDocumentoDiSpesa request = model.creaRequestAggiornaDocumentoDiSpesa();
		logServiceRequest(request);
		
		AggiornaDocumentoDiSpesaResponse response = documentoSpesaService.aggiornaDocumentoDiSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AggiornaDocumentoDiSpesa.class, response));
			addErrori(response);
			return INPUT;
		}
		//metto nel model una informazione di operazione avvenuta correttamente
		impostaInformazioneSuccesso();
		//prendo il documento con i dati aggiornati restituito dal servizio
		DocumentoSpesa documento = response.getDocumentoSpesa();
		log.debug(methodName, "Aggiornamento effettuato per Documento Spesa con uid: " + documento.getUid());
		
		// Imposto il dato nel model
		model.setDocumento(documento);
		
		// Tolgo il flag per l'ingresso sulle quote, se presente
		model.setIngressoTabQuote(Boolean.FALSE);
		// Ricalcolo il documento e tutti i dati associati
		return ottieniDocumento();
	}
	
	/**
	 * Ottiene il documento dal servizio, e ne ricalcola i dati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione.
	 */
	private String ottieniDocumento() {
		final String methodName = "ottieniDocumento";
		try {
			//carico i dati relativi al documento
			caricamentoDatiDocumento();
			//carico i dati relativi al soggetto
			caricamentoDatiSoggetto();
			//carico eventuali oneri collegati al soggetto
			ricercaOneriCollegatiAlDocumento();
		} catch(WebServiceInvocationFailureException e) {
			// Se è fallito il caricamento dei dati, loggo ed esco
			log.error(methodName, "Errore nell'invocazione dei servizi per il documento di uid " +
					model.getUidDocumentoDaAggiornare() + ": " + e.getMessage());
			return INPUT;
		}
		
		// Carico i flag
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		Boolean azioneIvaGestibile = AzioniConsentiteFactory.isGestioneIvaConsentito(azioniConsentite);
		// SIAC-5072:GESTIONE PERMESSI PER I MOTIVI DI SOSPENSIONE
		Boolean datiSospensioneEditabili = Boolean.valueOf(isDatiSospensioneEditabili());
		boolean datiFatturaPagataEditabili = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_DATI_FATTURA_PAGATA, azioniConsentite);
		model.impostaFlags(azioneIvaGestibile, datiSospensioneEditabili, datiFatturaPagataEditabili);
		
		// Caricamento liste
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaCodiceBollo();
		checkAndObtainListaTipoImpresa();
		checkAndObtainListaCommissioniDocumento();
		checkAndObtainListaNaturaOnere();
		checkAndObtainListaTipoAvviso();
		checkAndObtainListaTipiAtto();
		checkAndObtainListaNoteTesoriere();
		checkAndObtainListaContoTesoreria();
		
		// SIAC-4769
		checkAndObtainListeCausaleSospensioneAndSIOPE();
		
		//dati necessari per la gestione del PCC
		checkAndObtainListaCodiceUfficioDestinatarioPCC();
		checkAndObtainListaCodicePCC();
		
		// Non dovrebbero servire a nulla. Ma male non fanno!
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.SPESA, Boolean.TRUE, Boolean.FALSE);
		
		model.impostaListaTipoDocumentoNoteCredito();
		model.calcoloImporti();
		model.setCollegatoCECEditabile(calcolaCollegatoCECEditabile());
		model.impostaFlagEditabilitaRitenute();
		
		//setto i dati relativi alle abilitazioni utente
		// SIAC-5072
		model.setFlagNoDatiSospensioneDecentrato(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_NO_DATI_SOSPENSIONE_DECENTRATO, azioniConsentite));
		
		model.setAbilitatoGestioneAcquisti(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_GESTIONE_ACQUISTI, azioniConsentite));
		model.setNoDatiSospensioneDec(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_NO_DATI_SOSPENSIONE_DECENTRATO, azioniConsentite));
		
		// Leggo i dati della precedente azione
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		//controllo se sia possibile attivare le registrazioni contabili
		checkAttivazioneRegContabili();
		
		//SIAC-5346
		model.setInibisciModificaDatiImportatiFEL(AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_LIMITA_DATI_FEL, azioniConsentite));
		return SUCCESS;
	}
	
	/**Verifica le condizioni per cui e' editabile il checkbox collegatoCEC
	 * 
	 * @return true se non e' presente alcun provvedimento sulle quote E se il flagContabilizzaGEN/PCC = FALSE
	 */
	private Boolean calcolaCollegatoCECEditabile() {
		String methodName = "calcolaCollegatoCECEditabile";
		if(Boolean.TRUE.equals(model.getDocumento().getContabilizzaGenPcc())){
			//ho gia' attivato le registrazioni contabili, non posso piu' modificare il flag collegato CEC
			log.debug(methodName, "flag contabilizza GEN/PCC TRUE, collegatoCEC NON editabile");
			return Boolean.FALSE;
		}
		if(model.getListaSubdocumentoSpesa() == null || model.getListaSubdocumentoSpesa().isEmpty()){
			//non ho nessuna lista di subdocumenti, non posso piu' modificare il flag collegato CEC
			log.debug(methodName, "non ci sono quote, collegatoCEC editabile");
			return Boolean.TRUE;
		}
		for(SubdocumentoSpesa s : model.getListaSubdocumentoSpesa()){
			if(s.getAttoAmministrativo() != null && s.getAttoAmministrativo().getUid() != 0){
				//houn provvedimento, non posso piu' modificare il flag collegato CEC
				log.debug(methodName, "quota con provvedimento, collegatoCEC NON editabile");
				return Boolean.FALSE;
			}
		}
		//se sono arrivato fin qua, non rientro in nessuno dei precedenti casi
		log.debug(methodName, "CollegatoCEC editabile");
		return Boolean.TRUE;
	}

	/**
	 * Carica i dati relativi al documento.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio non vada a buon fine
	 */
	private void caricamentoDatiDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoDatiDocumento";
		// Ricerca di dettaglio del documento
		RicercaDettaglioDocumentoSpesa request = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoDaAggiornare());
		logServiceRequest(request);
		
		RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(request);
		logServiceResponse(response);
		
		
		
		if(response.hasErrori()) {
			//Errore nel caricamento dei dati del documento: esco
			log.error(methodName, createErrorInServiceInvocationString(RicercaDettaglioDocumentoSpesa.class, response));
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaDettaglioDocumentoSpesa");
		}
		
		//imposto nel model i dati ottenuti dal servizio
		impostaDatiDocumento(response.getDocumento());
		model.calcoloImporti();
	}
	
	/**
	 * Carica i dati relativi al soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio non vada a buon fine
	 */
	private void caricamentoDatiSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "caricamentoDatiSoggetto";
		// Ricerca di dettaglio del documento
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaSoggettoPerChiave");
		}
		
		//gestione delle sedi secondarie del soggetto
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
		//gestione delle modalita' pagamento del soggetto
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		//imposto nel model i dati del soggetto
		impostaDatiSoggetto(response.getSoggetto(), listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Ricerca gli oneri associati al documento e li presenta all'interno del model.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio non vada a buon fine
	 */
	private void ricercaOneriCollegatiAlDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaOneriCollegatiAlDocumento";
		//chiamo il servizio per ottenere gli oneri del documento
		RicercaOnereByDocumentoSpesa request = model.creaRequestRicercaOnereByDocumentoSpesa();
		logServiceRequest(request);
		RicercaOnereByDocumentoSpesaResponse response = documentoSpesaService.ricercaOnereByDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaOnereByDocumentoSpesa.class, response));
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaOnereByDocumentoSpesa");
		}
		// Nessun errore
		model.setListaDettaglioOnere(response.getListaDettagliOnere());
	}
	
	/**
	 * Valida l'aggiornamento dell'anagrafica del Documento di Spesa
	 */
	public void validateAggiornamentoAnagrafica() {
		final String methodName = "validateAggiornamentoAnagrafica";
		
		try {
			// Validazione dei campi
			validaCampi();
		} catch (ParamValidationException e) {
			//vi sono degli errori formali nei campi inseriti dall'utente
			log.debug(methodName, "Errori nella validazione dei campi");
		}
	}

	/**
	 * Validazione dei campi.
	 */
	private void validaCampi() {
		final String methodName = "validaCampi";
		
		//ottengo il documento
		DocumentoSpesa documento = model.getDocumento();
		
		// Validazione campi obbligatori
		checkCondition(documento.getDataEmissione() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data"), true);
		checkCondition(documento.getImporto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"), true);
		checkCondition(StringUtils.trimToNull(documento.getDescrizione()) != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		checkCondition(documento.getCodiceBollo() != null && documento.getCodiceBollo().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Imposta di bollo"));
		
		if(model.getDocumentoIncompleto()) {
			checkNotNullNorEmpty(model.getSoggetto().getCodiceSoggetto(), "Codice");
			//controllo che il soggetto esista
			validaSoggetto(true);
		}
		
		log.debug(methodName, "Campi obbligatorii: errori rilevati? " + hasErrori());
		
		// La data di emissione del documento deve essere coerente con l'anno dello stesso
		if(documento.getDataEmissione() != null) {
			Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(documento.getDataEmissione()));
			checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
		}
		
		// L'importo deve essere non negativo
		//SIAC-7193 e SIAC-7208: questo controllo non può piu' essere centralizzato 
		checkCondition(documento.getImporto().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"), true);
		
		// Validazione degli importi
		validazioneImporti(documento, model.ricalcolaTotaleImponibileOneriRC());
		
		// Se la data di scadenza è presente, deve essere maggiore o uguale la data del documento
		checkCondition(documento.getDataEmissione() == null || documento.getDataScadenza() == null ||
				documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
				ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		//controllo che data e numero siano entrambi presenti o entrambi sassenti
		checkCondition(!(documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Repertorio","data e numero devono essere entrambi presenti o assenti"));
		
		// Se il codice fiscale pignorato è presente, deve essere sintatticamente valido
		checkCondition(StringUtils.isEmpty(documento.getCodiceFiscalePignorato()) || ValidationUtil.isValidCodiceFiscale(documento.getCodiceFiscalePignorato()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Soggetto pignorato", ": il codice fiscale del soggetto non e' sintatticamente valido"));
		// CR-2550
		checkCondition(!model.isCodicePccObbligatorio() || (documento.getCodicePCC() != null && documento.getCodicePCC().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice PCC"));
		
		// SIAC-4680
		validazioneSAC();
		// SIAC-4749
		validazioneDatiFatturaPagataIncassata();
		
		// SIAC-5257
		checkProtocollo(documento);
		
		// SIAC-5311 SIOPE+
		validazioneSiope();
		
		//SIAC 6677 
		//SIAC-6840 si riabilita il metodo
		checkAndFillCodAvviso(documento);
		
		log.debug(methodName, "Validazione logica: errori rilevati? " + hasErrori());
	}
	
	/**
	 * Validazione del SIOPE plus
	 */
	private void validazioneSiope() {
		DocumentoSpesa documento = model.getDocumento();
		checkNotNullNorInvalidUid(documento.getSiopeDocumentoTipo(), "Tipo documento siope");
		// Popolo il dato dalla sessione
		SiopeDocumentoTipo siopeDocumentoTipo = ComparatorUtils.searchByUid(model.getListaSiopeDocumentoTipo(), documento.getSiopeDocumentoTipo());
		documento.setSiopeDocumentoTipo(siopeDocumentoTipo);
		
		// Se il documento e' analogico, devo avere il tipo di analogico
		checkCondition(!model.isTipoDocumentoSiopeAnalogico() || (documento.getSiopeDocumentoTipoAnalogico() != null && documento.getSiopeDocumentoTipoAnalogico().getUid() != 0), 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo documento analogico siope"));
		
		// Popolo il dato dalla sessione
		SiopeDocumentoTipoAnalogico siopeDocumentoTipoAnalogico = ComparatorUtils.searchByUid(model.getListaSiopeDocumentoTipoAnalogico(), documento.getSiopeDocumentoTipoAnalogico());
		//imposto nel documento il tipo siope trovato
		documento.setSiopeDocumentoTipoAnalogico(siopeDocumentoTipoAnalogico);
	}
	
	/**
	 * Validazione dei dati sulla fattura pagata/incassata
	 */
	private void validazioneDatiFatturaPagataIncassata() {
		DocumentoSpesa doc = model.getDocumento();
		//controllo che la fattura incassata abbia tutti i dati necessari
		checkCondition(doc.getDatiFatturaPagataIncassata() == null
				|| hasAllDataFatturaPagata(doc.getDatiFatturaPagataIncassata())
				|| hasNoDataFatturaPagata(doc.getDatiFatturaPagataIncassata()),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("documento pagato, note, data pagamento", ": nel caso almeno uno sia valorizzato devono essere tutti valorizzati"));
		// TODO: aggiungere i controlli sulle quote via chiamata al servizio
	}

	/**
	 * Controlla se ci sono tutti i dati della fattura pagata/incassata
	 * @param datiFatturaPagataIncassata i dati del pagamento/incasso della fattura
	 * @return se i dati sono tutti popolati
	 */
	private boolean hasAllDataFatturaPagata(DatiFatturaPagataIncassata datiFatturaPagataIncassata) {
		return Boolean.TRUE.equals(datiFatturaPagataIncassata.getFlagPagataIncassata()) && StringUtils.isNotBlank(datiFatturaPagataIncassata.getNotePagamentoIncasso()) && datiFatturaPagataIncassata.getDataOperazione() != null;
	}
	
	/**
	 * Controlla se non vi sia alcun dato della fattura pagata/incassata
	 * @param datiFatturaPagataIncassata i dati del pagamento/incasso della fattura
	 * @return se i dati sono tutti non popolati
	 */
	private boolean hasNoDataFatturaPagata(DatiFatturaPagataIncassata datiFatturaPagataIncassata) {
		return !Boolean.TRUE.equals(datiFatturaPagataIncassata.getFlagPagataIncassata()) && StringUtils.isBlank(datiFatturaPagataIncassata.getNotePagamentoIncasso()) && datiFatturaPagataIncassata.getDataOperazione() == null;
	}
	
	/**
	 * Validazione della struttura amministrativo contabile
	 */
	private void validazioneSAC() {
		StrutturaAmministrativoContabile sac = model.getDocumento().getStrutturaAmministrativoContabile();
		if(sac == null || sac.getUid() == 0) {
			//SIAC-5346: la SAC e' obbligatoria per utenti con azione limita dati fel
			checkCondition(!AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("struttura amministrativa"));
			return;
		}
		//ho impostato la sac: controllo che esista e la metto nel model
		List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		sac = ComparatorUtils.searchByUidWithChildren(lista, sac);
		model.getDocumento().setStrutturaAmministrativoContabile(sac);
	}
	
	/**
	 * Imposta i dati ottenuti dalla ricerca di dettaglio del documento.
	 * 
	 * @param documento il documento tramite cui caricare il model
	 */
	private void impostaDatiDocumento(DocumentoSpesa documento) {
		//imposto il documento nel model
		model.setDocumento(documento);
		
		// Imposta le liste
		model.setListaSubdocumentoSpesa(documento.getListaSubdocumenti());
		model.setListaDocumentoEntrata(ElementoDocumentoFactory.getInstances(documento.getListaDocumentiEntrataFiglio(), documento.getSoggetto()));
		
		// i documenti di spesa possono essere solo note credito (da analisi)
		model.setListaDocumentoSpesa(ElementoDocumentoFactory.getInstances(documento.getListaDocumentiSpesaFiglio(), documento.getSoggetto(), documento.getStatoOperativoDocumento()));
		
		//gestisco la presenza di eventuali ritenute
		RitenuteDocumento ritenuteDocumento = documento.getRitenuteDocumento();
		if(ritenuteDocumento == null) {
			ritenuteDocumento = new RitenuteDocumento();
			documento.setRitenuteDocumento(ritenuteDocumento);
		}
		model.setListaDettaglioOnere(documento.getRitenuteDocumento().getListaOnere());
		
		//calcolo gli importi da mostrare a video a partire dai dati del documento
		model.calcoloImporti();
		
		model.setSoggetto(documento.getSoggetto());
		
		model.setProgressivoNumeroSubdocumento(documento.getListaSubdocumenti().size());
		
		// Imposta dati subdocumentoIVA
		if(!documento.getListaSubdocumentoIva().isEmpty()) {
			//ho uno o piu' subdocumenti iva: ne imposto i dati nel model
			impostaSubdocumentoIva(documento.getListaSubdocumentoIva());
		}
	}
	
	/**
	 * Imposta il subdocumento iva con i parametri corretti.
	 * 
	 * @param listaSubdocumentoIva la lista da cui estrarre il subdocumento corretto
	 */
	private void impostaSubdocumentoIva(List<SubdocumentoIvaSpesa> listaSubdocumentoIva) {
		// TODO: Prendo il primo, controllare che non ce ne sia mai più di uno
		SubdocumentoIvaSpesa sis = listaSubdocumentoIva.get(0);
		model.setSubdocumentoIva(sis);
		model.setDocumentoIvaLegatoDocumentoPresente(Boolean.TRUE);
	}
	
	/**
	 * Imposta i dati ottenuti dalla ricerca di dettaglio del soggetto.
	 * 
	 * @param soggetto               il soggetto tramite cui caricare il model
	 * @param listaSediSecondarie    la lista delle sedi secondarie
	 * @param listaModalitaPagamento la lista delle modalit&agrave; di pagamento
	 */
	private void impostaDatiSoggetto(Soggetto soggetto, List<SedeSecondariaSoggetto> listaSediSecondarie, List<ModalitaPagamentoSoggetto> listaModalitaPagamento) {
		//imposto i dati relativi al soggetto nel model
		model.setSoggetto(soggetto);
		model.setListaSedeSecondariaSoggetto(listaSediSecondarie);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamento);
	}
	
	/**
	 * Controlla se la lista dei Codice Ufficio Destinatario PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaCodiceUfficioDestinatarioPCC() {
		List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC = model.getListaCodiceUfficioDestinatarioPCC();
		//controllo l'eventuale presenza nel model della lista
		if(!listaCodiceUfficioDestinatarioPCC.isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaCodiceUfficioDestinatarioPCC request = model.creaRequestRicercaCodiceUfficioDestinatarioPCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodiceUfficioDestinatarioPCCResponse response = documentoService.ricercaCodiceUfficioDestinatarioPCC(request);
		if(!response.hasErrori()) {
			//il servizio e' andato a buon fine: imposto i dati nel model
			listaCodiceUfficioDestinatarioPCC = response.getCodiciUfficiDestinatariPcc();
			model.setListaCodiceUfficioDestinatarioPCC(listaCodiceUfficioDestinatarioPCC);
		}
	}
	
	/**
	 * Controlla se la lista dei Codice PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	protected void checkAndObtainListaCodicePCC() {
		List<CodicePCC> listaCodicePCC = model.getListaCodicePCC();
		//controllo se io ho gia' la lista
		if(!listaCodicePCC.isEmpty()) {
			// Ho già la lista nel model
			return;
		}
		
		RicercaCodicePCC request = model.creaRequestRicercaCodicePCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodicePCCResponse response = documentoService.ricercaCodicePCC(request);
		if(!response.hasErrori()) {
			//il servizio e' andato a buon fine: imposto i dati nel model
			listaCodicePCC = response.getCodiciPCC();
			model.setListaCodicePCC(listaCodicePCC);
		}
	}
	
	//SIAC-5346
	/**
	 * Controlla se la lista dei Codice PCC sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 *
	 * @return the string
	 */
	public String obtainListaCodicePCCByCodiceUfficioDestinatario() {
		final String methodName = "obtainListaCodicePCCByCodiceUfficioDestinatario";
		//DA ANALISI: Se sono presenti in archivio relazioni con il CodiceUfficioDestinatario filtrare i valori del CodicePCC in base a queste relazioni e quindi al codice ufficio impostato, altrimenti visualizzarli come è attualmente.
		model.setListaCodicePCCFiltered(ReflectionUtil.deepClone(model.getListaCodicePCC()));
		//controllo che l'utente sia abilitato al filtro del codice PCC tramite 
//		if(!AzioniConsentiteFactory.isConsentito(AzioniConsentite.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite())) {
//			log.warn(methodName, "Utente non abilitato al filtro del codice PCC tramite codice ufficio.");
//			return SUCCESS;
//		}
		RicercaCodicePCC req = model.creaRequestRicercaCodicePCC(ottieniListaStruttureAmministrativoContabiliDaSessione());
		RicercaCodicePCCResponse res = documentoService.ricercaCodicePCC(req);
		if(res.hasErrori()) {
			//si sono verificati degli errori: li imposto nel model ed esco
			addErrori(res);
			log.debug(methodName, "Non ho ottenuto codici PCC filtrati");
			return SUCCESS;
		}
		if(res.getCodiciPCC().isEmpty()) {
			//non e' obbligatorio che esistano
			log.debug(methodName, "non ho trovato dei codici PCC legati all'ufficio destinatario PCC con uid: " + model.getUidCodiceUfficioDestinatarioPccToFilter());
			return SUCCESS;
		}
		model.setListaCodicePCCFiltered(res.getCodiciPCC());
		return SUCCESS;
	}
	
	/**
	 * carica nel model il Tipo Documento realtivo a note credito
	 * 
	 */
	public void ottieniTipoDocumentoNotaCredito() {
		// prelevo la lista dei tipi documento
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.SPESA, false, false);
		if (model.getListaTipoDocumento() != null && !model.getListaTipoDocumento().isEmpty()) {
			TipoDocumento tipoDocumentoNC = ComparatorUtils.findByCodice(model.getListaTipoDocumento(), BilConstants.CODICE_NOTE_CREDITO.getConstant());
			model.setTipoDocumentoNotaCredito(tipoDocumentoNC);
		}
	}
	
	/**
	 * Carica la lista degli stati operativi dell'atto
	 */
	private void ottieniListaStatoOperativoAtti() {
		model.setListaStatoOperativoAtti(Arrays.asList(StatoOperativoAtti.values()));
	}
	
	/**
	 * Attivazione delle registrazioni contabili.
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String attivaRegistrazioniContabili() {
		final String methodName = "attivaRegistrazioniContabili";
		log.debug(methodName, "Uid del documento per cui attivare le registrazioni contabili da aggiornare: " + model.getDocumento().getUid());
		
		//chiamo il servizio di attivazione delle registrazioni contabili
		AttivaRegistrazioniContabiliSpesa request = model.creaRequestAttivaRegistrazioniContabiliSpesa();
		logServiceRequest(request);
		
		AttivaRegistrazioniContabiliSpesaResponse response = documentoSpesaService.attivaRegistrazioniContabiliSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AttivaRegistrazioniContabiliSpesa.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Registrazioni contabili attivate per il documento " + model.getDocumento().getUid());
		//imposto in sessione un messaggio di avvenuta operazione
		impostaInformazioneSuccesso();
//		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		if(verificaPossibilitaRedirezioneSeRichiesta(response)) {
			//l'utente ha chiesto di validare ora la registrazione
			log.debug(methodName, "Redirezione verso la pagina di completa e valida della registrazione");
			return PROCEED;
		}
		model.setDocumento(response.getDocumentoSpesa());
		return ottieniDocumento();
	}

	/**
	 * @param response
	 */
	private boolean verificaPossibilitaRedirezioneSeRichiesta(AttivaRegistrazioniContabiliSpesaResponse response) {
		boolean redirezioneRichiesta = Boolean.TRUE.equals(model.getValidazionePrimaNotaDaDocumento());
		if(!model.getAbilitatoPrimaNotaDaFinanziaria() || !redirezioneRichiesta) {
			//l'utente non e' abilitato alla redirezione,  oppure non l'ha richiesta
			return false;
		}
		boolean inseritaRegistrazione = response.getRegistrazioniMovFinInserite() != null && !response.getRegistrazioniMovFinInserite().isEmpty() && response.getRegistrazioniMovFinInserite().get(0) != null && response.getRegistrazioniMovFinInserite().get(0).getUid() != 0;
		if(inseritaRegistrazione) {
			//la registrazione e' stata inserita,posso completarla
			model.setUidDaCompletare(response.getRegistrazioniMovFinInserite().get(0).getUid());
			return true;
		}
		
		//l'utente ha chiesto di validare la prima nota, ma il servizio 
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
	 * Controlla la abilitazioni di cui e' in possesso l'utente
	 *
	 * @return  il risultato dell'invocazione
	 */
	//SIAC-5333
	public String controllaNecessitaRichiestaUlterioreConferma() {
		return SUCCESS;
	}
	
	/**
	 * Controlla che se presente il codice Avviso pago PA non superi le 18 cifre
	 * Nel caso le cifre fossero minori di 18 applica un fill left
	 * @param documento
	 */
	private void checkAndFillCodAvviso(DocumentoSpesa documento){
		
		
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length()==0 ||
				isNumeric(documento.getCodAvvisoPagoPA()),
				ErroreFin.COD_AVVISO_PAGO_PA_NUMERICO.getErrore());
		
		int maxLength = CostantiFin.CODICE_AVVISO_PAGO_PA_LENGTH;
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
