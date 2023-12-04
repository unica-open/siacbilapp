/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.InserisciDocumentoSpesaModel;
//import it.csi.siac.siacfin2app.frontend.ui.util.helper.TipoDocumentoFELHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoImpresa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Classe di action per l'inserimento del Documento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/03/2014
 * @version 1.1.0 - 11/11/2015 - CR-2550 - modifica gestione flag codice PCC
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciDocumentoSpesaAction extends GenericDocumentoSpesaAction<InserisciDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1919984150384201989L;
	private static final String[] TIPI_DOCUMENTO_IMPORTABILI_DA_FEL_CODES = new String[] {"FAT","FPR", "NCD", "NTE"};
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public void prepare() throws Exception {
		// Pulizia di errori/messaggi/informazioni, per ogni operazione effettuata
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// In ingresso della pagina, pulisco il model e richiamo il prepare originale
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("Inserimento documento di spesa");
		
		// Caricamento delle liste
		caricaListe();
		
		// SIAC-4680
		caricaAzionePerSAC();
		
		// Per la pulizia
		aggiungiInSessionePerPulizia(BilSessionParameter.MODEL_INSERISCI_DOCUMENTO_SPESA);
		
		// Dati FEL
		caricaDatiFatturaFEL();
		caricaProvvisoriDiCassa();
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento dell'azione per la gestione della SAC
	 */
	private void caricaAzionePerSAC() {
		model.setNomeAzioneSAC(AzioneConsentitaEnum.DOCUMENTO_SPESA_INSERISCI.getNomeAzione());
	}

	/**
	 * Caricamento delle liste
	 */
	private void caricaListe(){
		// Caricamento delle varie liste per la gestione della UI
		
		// I tipi di documento utilizzato sono solo quelli di spesa, con eventuale filtro subordinato/regolarizzazione da chiamante
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.SPESA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaCodiceUfficioDestinatarioPCC();
		checkAndObtainListaCodicePCC();
		
		// SIAC-5311 SIOPE+
		// SIAC-5115: tolta la causale sospensione
		checkAndObtainListeSIOPE();
	}
	
	/**
	 * Controlla se la lista delle Causali Sospensione sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	private void checkAndObtainListeSIOPE() {
		if(!model.getListaSiopeDocumentoTipo().isEmpty() && !model.getListaSiopeDocumentoTipoAnalogico().isEmpty()) {
			// Ho gia' le liste nel model
			return;
		}
		
		// Ricerca la causale come codifica generica
		@SuppressWarnings("unchecked")
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeDocumentoTipo.class, SiopeDocumentoTipoAnalogico.class);
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		// Se ho errori ignoro la response
		// TODO: gestire gli errori?
		if(!res.hasErrori()) {
			model.setListaSiopeDocumentoTipo(res.getCodifiche(SiopeDocumentoTipo.class));
			model.setListaSiopeDocumentoTipoAnalogico(res.getCodifiche(SiopeDocumentoTipoAnalogico.class));
		}
	}
	
	/**
	 * Ingresso nello step 1
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step1() {
		//per sicurezza prendo la lista dal clone che e' correttamente impostato e non sporcato da dati di sessione
		model.setListaTipoDocumento(model.getListaTipoDocumentoClone());
		// Precaricamento dei dati collegati
		precaricaDocumentoCollegato();
		// Impostazione default
		impostaDefaultDocumento();
		// Caricamento dei dati FEL
		impostaDefaultFatturaFELStep1();
		
		// Caricamento delle liste
		caricaListe();
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei default per il documento
	 */
	private void impostaDefaultDocumento() {
		// SIAC-5437: il tipo documento deve essere di default ANALOGICO
		if(model.getDocumento() == null) {
			// Inizializzo il documento
			model.setDocumento(new DocumentoSpesa());
		}
		
		SiopeDocumentoTipo siopeDocumentoTipo = ComparatorUtils.findByCodice(model.getListaSiopeDocumentoTipo(), BilConstants.CODICE_SIOPE_DOCUMENTO_TIPO_ANALOGICO.getConstant());
		model.getDocumento().setSiopeDocumentoTipo(siopeDocumentoTipo);
	}

	/**
	 * Preparazione per il metodo {@link #enterStep2()}
	 */
	public void prepareEnterStep2() {
		// Pulisco i dati forniti dall'interfaccia
		DocumentoSpesa d = model.getDocumento();
		if(d == null) {
			// Null-safe. Non dovrebbe succedere, ma per sicurezza
			return;
		}
		// Tutti i dati sono su documento
		d.setTipoDocumento(null);
		d.setAnno(null);
		d.setNumero(null);
		d.setDataEmissione(null);
		d.setDataRicezionePortale(null);
		d.setStrutturaAmministrativoContabile(null);
		d.setFlagBeneficiarioMultiplo(null);
		d.setCollegatoCEC(null);
		// SIAC-5311 SIOPE+
		d.setSiopeDocumentoTipo(null);
		d.setSiopeDocumentoTipoAnalogico(null);
		
		// Tranne i dati del soggetto
		model.setSoggetto(null);
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		// Caricamento liste
		checkAndObtainListaCodiceBollo();
		checkAndObtainListaTipoImpresa();
		impostaDefaultFatturaFELStep2();
		
		// CR-2550
		caricaTipoDocumento();
		// Carica i dati del documento
		//CR-2381
		popolaParametriDiDefaultStep2();

		return SUCCESS;
	}
	
	/**
	 * Caricamento dei dati del tipo documento 
	 */
	private void caricaTipoDocumento() {
		final String methodName = "caricaTipoDocumento";
		// Se non ho i dati del tipo documento, esco
		if(model.getDocumento() == null || model.getDocumento().getTipoDocumento() == null) {
			log.debug(methodName, "Dati tipo documento non presenti");
			return;
		}
		// Ricerca tipo documento via uid
		log.debug(methodName, "Ricerca tipo documento per uid " + model.getDocumento().getTipoDocumento().getUid());
		TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), model.getDocumento().getTipoDocumento());
		log.debug(methodName, "FlagComunicaPCC? " + (tipoDocumento != null ? tipoDocumento.getFlagComunicaPCC() : "null"));
		// Impostazione dei dati del tipo documento nel model
		model.getDocumento().setTipoDocumento(tipoDocumento);
	}

	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		// Segnaposto: permette il refresh della pagina
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #enterStep3()}
	 */
	public void prepareEnterStep3() {
		// Pulizia dei campi relativi alla pagina
		if(model.getDocumento() == null) {
			// Null-safe. Non dovrebbe servire, ma meglio controllare
			return;
		}
		
		// Tutti i campi sono injettati direttamente nel documento
		puliziaDatiDocumentoStep2(model.getDocumento());
	}

	/**
	 * Pulizia dei dati del documento relativi allo step 2
	 * @param documentoSpesa il documento da pulire
	 */
	private void puliziaDatiDocumentoStep2(DocumentoSpesa documentoSpesa) {
		// Tutti i dati sull'interfaccia utente
		documentoSpesa.setImporto(null);
		documentoSpesa.setArrotondamento(null);
		documentoSpesa.setDescrizione(null);
		documentoSpesa.setTerminePagamento(null);
		documentoSpesa.setDataScadenza(null);
		documentoSpesa.setRegistroRepertorio(null);
		documentoSpesa.setAnnoRepertorio(null);
		documentoSpesa.setNumeroRepertorio(null);
		documentoSpesa.setDataRepertorio(null);
		documentoSpesa.setCodiceFiscalePignorato(null);
		documentoSpesa.setCodiceBollo(null);
		documentoSpesa.setTipoImpresa(null);
		documentoSpesa.setNote(null);
		documentoSpesa.setCodicePCC(null);
		documentoSpesa.setCodiceUfficioDestinatario(null);
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		// Salvataggio del documento
		return salvaDocumento();
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step3() {
		// Caricamento dell'informazione di successo. Placeholder per il refresh
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
		// Redirezione verso l'aggiornamento: prendo l'uid del documento per comporre la request corretta
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso la ripetizione dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripeti() {
		// Ripetizione: mantengo i dati vecchi
		DocumentoSpesa documentoSalvato = model.getDocumento();
		Soggetto soggettoAssociato = model.getSoggetto();
		// Pulisco il model
		cleanModel();
		// Impostazione dei default
		model.impostaDatiRipeti(documentoSalvato, soggettoAssociato);
		
		// Ricarico le liste
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaCodiceBollo();
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.SPESA, model.getFlagSubordinato(), model.getFlagRegolarizzazione());
		checkAndObtainListaTipoImpresa();
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso l'aggiornamento, con il tab delle quote.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String quote() {
		// Redirezione verso l'aggiornamento: prendo l'uid del documento per comporre la request corretta
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripetiSalva() {
		// Salvataggio con ripetizione: redirigo semplicemente al salvataggio
		return salvaDocumento();
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripetiStep3() {
		// Ingresso nello step 3 a seguito del salvataggio. Placeholder
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'ingresso nello step2.
	 */
	public void validateEnterStep2() {
		final String methodName = "validateEnterStep2";
		log.debug(methodName, "Validazione campi obbligatori");
		
		
		//SIAC-5346
		checkAbilitatoInserimento();
		// Controlli di coerenza per l'ingresso nel secondo step
		
		DocumentoSpesa documento = model.getDocumento();
		Soggetto soggetto = model.getSoggetto();
		
		// Controlli sui dati obbligatori
		checkNotNullNorInvalidUid(documento.getTipoDocumento(), "Tipo");
		checkNotNull(documento.getAnno(), "Anno");
		checkNotNullNorEmpty(documento.getNumero(), "Numero");
		checkNotNull(documento.getDataEmissione(), "Data");
		
		// Controllo sulla presenza del soggetto
		boolean controlloSoggetto = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		checkCondition(controlloSoggetto, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		
		log.debug(methodName, "Validazione logica campi");
		if(documento.getAnno() != null) {
			// L'anno del documento non puo' essere successivo all'anno di bilancio
			checkCondition(model.getAnnoEsercizioInt().compareTo(documento.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			if(documento.getDataEmissione() != null) {
				// La data di emissione deve essere dello stesso anno del documento
				Integer annoEmissione = Integer.valueOf(FormatUtils.formatDateYear(documento.getDataEmissione()));
				// Possibile refuso di analisi?
				checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
			}
		}
		
		if(controlloSoggetto) {
			// Se il soggetto e' presente lo devo validare
			validaSoggetto(true);
		}
		// SIAC-4680
		validazioneSAC();
		// SIAC-5311 SIOPE+
		validazioneSiope();
		
	}

	/**
	 * Check se si sia o meno abilitati  all'inserimento: 
	 * per gli operatori che possiedono l’azione OP-SPE-LimitaDatiFELDec non deve essere possibile inserire i tipi documento importabili da FEL
	 * FAT, NCD, NTE, FPR
	 */
	private void checkAbilitatoInserimento() {
		TipoDocumento tipoDocumento = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), model.getDocumento().getTipoDocumento());
		boolean inserimentoTipoDocumentoImportabileDaFEL = Arrays.asList(TIPI_DOCUMENTO_IMPORTABILI_DA_FEL_CODES).contains(tipoDocumento.getCodice());
		//               non  devo imporre limitazioni sui dpcumenti FEL
		checkCondition(!AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite())
				//sto importando un documento FEL
				|| model.getFatturaFEL() != null
				//sto inserendo da cruscotto un documento con tipo documento non importabile da fel
				|| !inserimentoTipoDocumentoImportabileDaFEL
				, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("inserimento documento spesa di tipo " + tipoDocumento.getCodice()), true);
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
		documento.setSiopeDocumentoTipoAnalogico(siopeDocumentoTipoAnalogico);
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
		// Recupero la SAC dalla lista in sessione
		List<StrutturaAmministrativoContabile> lista = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		sac = ComparatorUtils.searchByUidWithChildren(lista, sac);
		// Imposto la SAC nel documento
		model.getDocumento().setStrutturaAmministrativoContabile(sac);
	}
	
	/**
	 * Validazione per l'ingresso nello step3.
	 */
	public void validateEnterStep3() {
		final String methodName = "validateEnterStep3";
		log.debug(methodName, "Validazione campi obbligatori");
		
		// Validazione ingresso step 3
		
		DocumentoSpesa documento = model.getDocumento();
		// Validazione campi oggbligatori
		checkNotNull(documento.getImporto(), "Importo");
		checkNotNullNorEmpty(documento.getDescrizione(), "Descrizione");
		checkNotNullNorInvalidUid(documento.getCodiceBollo(), "Imposta di bollo");
		
		log.debug(methodName, "Validazione logica campi");
		// L'importo deve essere maggiore di zero -- NO CR 3469: maggiore o uguale a zero
		checkCondition(documento.getImporto() == null || documento.getImporto().signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"));
		// L'arrotondamento deve essere minore di zero -- NO, cr 2889
		
		// Importo + arrotondamento > 0
		checkCondition(documento.getImporto() == null
				|| documento.getArrotondamento() == null
				|| documento.getImporto().add(documento.getArrotondamento()).signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore o uguale a zero"));
		
		// Se la data di scadenza e' presente, deve essere maggiore o uguale la data del documento
		checkCondition(documento.getDataEmissione() == null || documento.getDataScadenza() == null
				|| documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
				ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
		
		// Data e numero repertorio devono essere contemporaneamente valorizzati o non valorizzati
		// Controllo via XOR per scrivere di meno
		checkCondition(!(documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Repertorio","data e numero devono essere entrambi presenti o assenti"));
		
		// Se il codice fiscale pignorato e' presente, deve essere sintatticamente valido 
		checkCondition(StringUtils.isEmpty(documento.getCodiceFiscalePignorato()) || ValidationUtil.isValidCodiceFiscale(documento.getCodiceFiscalePignorato()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Soggetto pignorato", ": il codice fiscale del soggetto non e' sintatticamente valido"));
		
		// CR-2550
		checkCondition(!model.isCodicePccObbligatorio() || (documento.getCodicePCC() != null && documento.getCodicePCC().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice PCC"));
		
		if(model.getUidDocumentoCollegato() != null) {
			// Se ho un documento collegato, devono esservi i controlli corrispondenti
			checkPerDocumentoCollegato();
		}
		
		// SIAC-5257
		checkProtocollo(documento);
		
		//SIAC 6677 
		//SIAC-6840 si riabilita il metodo
		checkAndFillCodAvviso(documento);
	}
	
	/**
	 * Validazione per la ripetizione del salvataggio.
	 */
	public void validateRipetiSalva() {
		// Validazione per la ripetizione del salvataggio: controllo insieme ingresso step2 e step3
		validateEnterStep2();
		// CR-2550
		caricaTipoDocumento();
		validateEnterStep3();
	}
	
	/**
	 * Annullamento dei dati per lo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1() {
		// Pulisco i campi
		model.setSoggetto(null);
		
		// Pulizia dei dati del documento
		model.getDocumento().setAnno(null);
		model.getDocumento().setNumero(null);
		model.getDocumento().setTipoDocumento(null);
		model.getDocumento().setDataEmissione(null);
		model.getDocumento().setDataRicezionePortale(null);
		model.getDocumento().setFlagBeneficiarioMultiplo(null);
		model.getDocumento().setCollegatoCEC(null);
		
		// Ricalcolo i dati della fattura
		impostaDefaultFatturaFELStep1();
		
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati per lo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2() {
		// Pulisco i campi
		puliziaDatiDocumentoStep2(model.getDocumento());
		
		// Ricalcolo i dati della fattura
		impostaDefaultFatturaFELStep2();
		
		return SUCCESS;
	}
	
	/**
	 * Richiamo al metodo {@link #salvataggioDocumento()} se non ci sono provvisori di cassa selezionati o {@link #salvataggioDocumentoPerProvvisori()} altrimenti
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvaDocumento(){
		// Salvataggio del documento
		if(model.getListaUidProvvisori() == null || model.getListaUidProvvisori().isEmpty()){
			// Se non e' un salvataggio per provvisori, provvedo al salvataggio 'normale'
			return salvataggioDocumento();
		}
		// Salvataggio per provvisori
		return salvataggioDocumentoPerProvvisori();
	}
	
	/**
	 * Effettua il salvataggio del documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvataggioDocumento() {
		final String methodName = "salvataggioDocumento";
		
		// Creazione della request e invocazione del servizio
		log.debug(methodName, "Creazione della request");
		InserisceDocumentoSpesa req = model.creaRequestInserisceDocumentoSpesa();
		logServiceRequest(req);
		
		
		log.debug(methodName, "Invocazione del servizio");
		InserisceDocumentoSpesaResponse res = documentoSpesaService.inserisceDocumentoSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errori lato servizio: li imposto nel model ed esto
			addErrori(res);
			return INPUT;
		}
		
		// Ottengo i dati da servizio
		DocumentoSpesa documentoSpesa = res.getDocumentoSpesa();
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoSpesa.getUid());
		// Ripopolo i dati del model con quelli ottenuti dal servizio
		model.popolaModel(documentoSpesa);
		// Caricamento dei classificatori
		caricaClassificatori();
		
		if(res.getMessaggi() != null){
			// Se ho messaggi li imposto nel model e in sessione
			addMessaggi(res.getMessaggi());
		}
		// SIAC-6584
		int numOrd = model.getNumeroOrdinativoSenzaNumero();
		if (numOrd > 0){
			addMessaggio(ErroreFin.FEL_ORDINE_NON_VALORIZZATO.getErrore(Integer.valueOf(numOrd)));
		}
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
		
		// Creazione della request e invocazione del servizio
		log.debug(methodName, "Creazione della request InserisceDocumentoPerProvvisoriSpesa");
		InserisceDocumentoPerProvvisoriSpesa req = model.creaRequestInserisceDocumentoPerProvvisoriSpesa();
		logServiceRequest(req);
		
		log.debug(methodName, "Invocazione del servizio InserisceDocumentoSpesaService");
		InserisceDocumentoPerProvvisoriSpesaResponse res = documentoSpesaService.inserisceDocumentoPerProvvisoriSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errori lato servizio: li imposto nel model ed esco
			addErrori(res);
			return INPUT;
		}
		// Ottengo i dati da servizio
		DocumentoSpesa documentoSpesa = res.getDocumentoSpesa();
		
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoSpesa.getUid());
		// Ripopolo i dati del model con quelli ottenuti dal servizio
		model.popolaModel(documentoSpesa);
		// Caricamento dei classificatori
		caricaClassificatori();
		
		// SIAC-5257
		setMessaggiInSessionePerActionSuccessiva();
		
		// SIAC-6584
		int numOrd = model.getNumeroOrdinativoSenzaNumero();
		if (numOrd > 0){
			addMessaggio(ErroreFin.FEL_ORDINE_NON_VALORIZZATO.getErrore(Integer.valueOf(numOrd)));
		}
		
		return SUCCESS;
	}
	
	/**
	 * Carica i classificatori dalla sessione.
	 */
	private void caricaClassificatori() {
		DocumentoSpesa documento = model.getDocumento();
		
		// Caricamento del tipo documento
		TipoDocumento tipoDocumento = ComparatorUtils.searchByUidEventuallyNull(model.getListaTipoDocumento(), documento.getTipoDocumento());
		// Caricamento del codice bollo
		CodiceBollo codiceBollo = ComparatorUtils.searchByUidEventuallyNull(model.getListaCodiceBollo(), documento.getCodiceBollo());
		// Caricamento del tipo impresa
		TipoImpresa tipoImpresa = ComparatorUtils.searchByUidEventuallyNull(model.getListaTipoImpresa(), documento.getTipoImpresa());
		// Caricamento del codice PCC
		CodicePCC codicePCC = ComparatorUtils.searchByUidEventuallyNull(model.getListaCodicePCC(), documento.getCodicePCC());
		// Caricamento del codice ufficio destinatario PCC
		CodiceUfficioDestinatarioPCC codiceUfficioDestinatario = ComparatorUtils.searchByUidEventuallyNull(model.getListaCodiceUfficioDestinatarioPCC(), documento.getCodiceUfficioDestinatario());

		// Imposto i dati nel documento
		documento.setTipoDocumento(tipoDocumento);
		documento.setCodiceBollo(codiceBollo);
		documento.setTipoImpresa(tipoImpresa);
		documento.setCodicePCC(codicePCC);
		documento.setCodiceUfficioDestinatario(codiceUfficioDestinatario);
	}
	
	/**
	 * Effettua i controlli per i documenti collegati
	 */
	private void checkPerDocumentoCollegato() {
		// Prendo il documento collegato corretto
		Documento<?, ?> documentoCollegato = "Entrata".equals(model.getClasseDocumentoCollegato()) ?
				model.getDocumentoEntrataCollegato() :
				model.getDocumentoSpesaCollegato();
		// Recupero i dati del documento
		BigDecimal importoDocumentoCollegato = documentoCollegato.getImporto();
		BigDecimal importo = model.getDocumento().getImporto();
		
		if(Boolean.TRUE.equals(model.getFlagSubordinato())) {
			// Se sono un subordinato, l'importo non puo' essere superiore del documento originale
			checkCondition(importo.subtract(importoDocumentoCollegato).signum() <= 0,
				ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Il documento",
					"di tipo subordinato, l'importo non puo' superare l'importo del documento origine"));
		} else if (Boolean.TRUE.equals(model.getFlagRegolarizzazione())) {
			// Se sono una regolarizzazione, l'importo deve essere pari al documento originale
			checkCondition(importo.subtract(importoDocumentoCollegato).signum() == 0,
				ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Il documento",
					"di tipo regolarizzazione, l'importo deve essere uguale all'importo del documento origine"));
		}
	}
	
	/**
	 * Precarica il documento dal documento collegato.
	 */
	private void precaricaDocumentoCollegato() {
		String methodName = "precaricaDocumentoCollegato";
		if(model.getUidDocumentoCollegato() == null || model.getClasseDocumentoCollegato() == null) {
			// Se non ho un documento collegato, esco
			return;
		}
		
		Documento<?, ?> documentoCollegato = null;
		log.debug(methodName, model.getClasseDocumentoCollegato());
		
		if("Entrata".equals(model.getClasseDocumentoCollegato())) {
			// Documento collegato di entrata: carico i dati via il servizio corrispondente
			RicercaDettaglioDocumentoEntrata req = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoCollegato());
			RicercaDettaglioDocumentoEntrataResponse res = documentoEntrataService.ricercaDettaglioDocumentoEntrata(req);
			documentoCollegato = res.getDocumento();
			model.setDocumentoEntrataCollegato(res.getDocumento());
		} else {
			// Documento collegato di spesa: carico i dati via il servizio corrispondente
			RicercaDettaglioDocumentoSpesa req = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoCollegato());
			RicercaDettaglioDocumentoSpesaResponse res = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
			documentoCollegato = res.getDocumento();
			model.setDocumentoSpesaCollegato(res.getDocumento());
		}
		
		// Precaricamento dati
		DocumentoSpesa documentoSpesa = model.getDocumento();
		if(documentoSpesa == null) {
			// Inizializzo il documento di spesa
			documentoSpesa = new DocumentoSpesa();
			model.setDocumento(documentoSpesa);
		}
		
		// Copio anno, dati di repertorio, codice bollo e descrizione
		documentoSpesa.setAnno(documentoCollegato.getAnno());
		documentoSpesa.setNumeroRepertorio(documentoCollegato.getNumeroRepertorio());
		documentoSpesa.setDataRepertorio(documentoCollegato.getDataRepertorio());
		documentoSpesa.setDescrizione(documentoCollegato.getDescrizione());
		documentoSpesa.setCodiceBollo(documentoCollegato.getCodiceBollo());
		
		// Copio il soggetto
		model.setSoggetto(documentoCollegato.getSoggetto());
	}
	
	/**
	 * Impostazione dei default della fattura FEL per lo step 1.
	 * <ul>
	 *     <li>Anno documento = Anno data documento FEL (entit&agrave; Fattura Elettronica)</li>
	 *     <li>Tipo documento = vedi 1.3.8</li>
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
		
		DocumentoSpesa documentoSpesa = model.getDocumento();
		//	Numero Documento =  Numero Documento FEL
		documentoSpesa.setNumero(fatturaFEL.getNumero());
		
		// Anno documento = Anno data documento FEL
		if(fatturaFEL.getData() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fatturaFEL.getData());
			
			Integer anno = cal.get(Calendar.YEAR);
			documentoSpesa.setAnno(anno);
			log.debug(methodName, "Fattura.data = " + fatturaFEL.getData() + " => documento.anno = " + documentoSpesa.getAnno());
			
			// Data documento = data documento FEL
			documentoSpesa.setDataEmissione(fatturaFEL.getData());
			log.debug(methodName, "Fattura.data = " + fatturaFEL.getData() + " => documento.dataCreazioneDocumento = " + documentoSpesa.getDataEmissione());
		}
		
		// Tipo documento
		//SIAC-7571
		if(sessionHandler.containsKey(BilSessionParameter.TIPO_DOCUMENTO_IMPORTA_FATTURA)) {
			impostaTipoDocumentoDaFatturaFEL(documentoSpesa);
		} else {
			impostaTipoDocumento(documentoSpesa, fatturaFEL);
		}
		
		// Soggetto Intestatario = Soggetto identificato dall'operatore nell'importazione del documento FEL e passato come parametro
		log.debug(methodName, "Soggetto = " + (model.getSoggettoFEL() != null ? model.getSoggettoFEL().getCodiceSoggetto() : "null"));
		model.setSoggetto(ReflectionUtil.deepClone(model.getSoggettoFEL()));
		
		// Data ricezione = data ricezione FEL
		if(fatturaFEL.getPortaleFattureFEL() != null) {
			documentoSpesa.setDataRicezionePortale(fatturaFEL.getPortaleFattureFEL().getDataRicezione());
			log.debug(methodName, "Fattura.portaleFattureFEL.dataRicezione = " + fatturaFEL.getPortaleFattureFEL().getDataRicezione() + " => documento.dataRicezionePortale = " + documentoSpesa.getDataRicezionePortale());
		}
		
		// SIAC-5311 SIOPE+
		// TipoDocumentoSiope = 'ELETTRONICO'
		SiopeDocumentoTipo siopeDocumentoTipo = ComparatorUtils.findByCodice(model.getListaSiopeDocumentoTipo(), BilConstants.CODICE_SIOPE_DOCUMENTO_TIPO_ELETTRONICO.getConstant());
		model.getDocumento().setSiopeDocumentoTipo(siopeDocumentoTipo);
		
		// idLottoSdiSiope = identificativo lotto sdi (entita' Portale fatture, id_sdi)
		String sdi = fatturaFEL.getPortaleFattureFEL() != null
				&& fatturaFEL.getPortaleFattureFEL().getIdentificativoSdi() != null
				? fatturaFEL.getPortaleFattureFEL().getIdentificativoSdi().toString()
				: null;
		model.getDocumento().setSiopeIdentificativoLottoSdi(sdi);
		
		impostaFlagDatiFelDisabilitati();
	}
	
	/**
	 * Imposta flag dati fel disabilitati.
	 */
	private void impostaFlagDatiFelDisabilitati() {
		//controllo se l'utente ha delle limitazioni sui dati importati da fattura
		boolean isLimitaDatiFel = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_SPESA_LIMITA_DATI_FEL, sessionHandler.getAzioniConsentite());
		//imposto il dato nel model
		model.setInibisciModificaDatiImportatiFEL(isLimitaDatiFel);
	}

	/**
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
		
		DocumentoSpesa documentoSpesa = model.getDocumento();
		if(documentoSpesa == null) {
			// Se non ho il documento lo inizializzo. Dovrei averlo, ma null-safe
			documentoSpesa = new DocumentoSpesa();
			model.setDocumento(documentoSpesa);
		}

		// Dati repertorio = Dati protocollo (Numero e Data) documento FEL
		if(fatturaFEL.getProtocolloFEL() != null) {
			// Registro protocollo copiato dalla fattura
			documentoSpesa.setRegistroRepertorio(fatturaFEL.getProtocolloFEL().getRegistroProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.registroProtocollo = " + fatturaFEL.getProtocolloFEL().getRegistroProtocollo() + " => documento.registroProtocollo= " + documentoSpesa.getRegistroRepertorio());
			
			// Numero repertorio copiato dalla fattura
			documentoSpesa.setNumeroRepertorio(fatturaFEL.getProtocolloFEL().getNumeroProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.numeroProtocollo = " + fatturaFEL.getProtocolloFEL().getNumeroProtocollo() + " => documento.numeroRepertorio = " + documentoSpesa.getNumeroRepertorio());
			// Data repertorio copiata dalla fattura
			documentoSpesa.setDataRepertorio(fatturaFEL.getProtocolloFEL().getDataRegProtocollo());
			log.debug(methodName, "Fattura.protocolloFEL.dataRegProtocollo = " + fatturaFEL.getProtocolloFEL().getDataRegProtocollo() + " => documento.dataRepertorio = " + documentoSpesa.getDataRepertorio());
			
			// SIAC-3101 : l'anno protocollo potrebbe non essere impostato
			Integer annoRepertorio = null;
			try {
				annoRepertorio = Integer.valueOf(fatturaFEL.getProtocolloFEL().getAnnoProtocollo());
			} catch(NumberFormatException nfe) {
				log.warn(methodName, "Attenzione! Anno di protocollo non valido per la fattura " + fatturaFEL.getUid() + " - impostazione del default a null");
			}
			documentoSpesa.setAnnoRepertorio(annoRepertorio);
			log.debug(methodName, "Fattura.protocolloFEL.annoProtocollo = " + fatturaFEL.getProtocolloFEL().getAnnoProtocollo() + " => documento.annoProtocollo= " + documentoSpesa.getAnnoRepertorio());
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
				documentoSpesa.setDescrizione(causaleFEL.getCausale());
				log.debug(methodName, "Causale.progressivo = " + causaleFEL.getProgressivo() + " => Causale.causale = " + causaleFEL.getCausale() + " => documento.descrizione = " + documentoSpesa.getDescrizione());
			}
		}
		
		// Importo = importo totale documento FEL se presente
		impostaImportoFEL(documentoSpesa, fatturaFEL);
		
		// Arrotondamento = importo arrotondamento documento FEL se presente
		documentoSpesa.setArrotondamento(fatturaFEL.getArrotondamento());
		log.debug(methodName, "Fattura.arrotondamento = " + fatturaFEL.getArrotondamento() + " => documento.arrotondamento = " + documentoSpesa.getArrotondamento());
		
		// Codice Ufficio Destinatario = Codice Destinatario
		impostaUfficioDestinatario(documentoSpesa, fatturaFEL);
	}

	/**
	 * Imposta il tipo di documento dalla fattura.
	 * <br/>
	 * Il sistema determina il tipo documento seguendo queste regole:
	 * <ul>
	 *     <li>
	 *         se tipo documento FEL = <code>TD01</code> o <code>TD02</code> &rarr; tipo documento finanziario = <code>FAT</code>
	 *     </li>
	 *     <li>
	 *         se tipo documento FEL = <code>TD03</code> o <code>TD06</code> &rarr; tipo documento finanziario = <code>FPR</code>
	 *     </li>
	 *     <li>
	 *         <strong>DA NON SVILUPPARE IN V01:</strong>
	 *         <ul>
	 *             <li>
	 *                 Se tipo documento FEL = <code>TD04</code> &rarr; tipo documento finanziario = <code>NCD</code>
	 *                 <br/>
	 *                 Inoltre in questo caso se la Fattura FEL di tipo <code>TD04</code> presenta UNA SOLA Fattura Collegata (entit&agrave; Fatture Collegate) &eacute; necessario ricercare in contabilit&agrave;
	 *                 finanziaria la fattura collegata (tramite il codice del fornitore del documento FEL e i campi <code>FattureCollegate.numero</code> e <code>FattureCollegate.data</code>) e verificare
	 *                 che ce ne sia solamente una e che non sia in stato <code>ANNULLATO</code> o <code>EMESSO</code>, in questo caso si potrebbe richiamare il CDU di aggiorna documento di questa
	 *                 Fattura finanziaria e proporre la maschera per l'inserimento delle Note di credito da inserire, altrimenti come per le <code>NTE</code> si potr&agrave; inserire l'<code>NCD</code>
	 *                 solamente come documento singolo di spesa – <strong>DA DEFINIRE</strong>
	 *             </li>
	 *             <li>
	 *                 Se tipo documento FEL = <code>TD05</code> &rarr; tipo documento finanziario = <code>NTE</code>
	 *                 <br/>
	 *                 (su Contabilia non viene associata ad una fattura, come per la nota NCD ma nasce come documento singolo di spesa, quindi anche se avesse fatture collegate in FEL non verrebbero collegate
	 *                 anche in Contabilia in automatico, e quindi si potrebbe importare solamente come documento singolo di spesa di tipo <code>NTE</code>) – <strong>DA DEFINIRE</strong>
	 *             </li>
	 *         </ul>
	 *     </li>
	 * </ul>
	 * 
	 * @param documentoSpesa il documento di spesa
	 * @param fatturaFEL la fattura
	 */
	private void impostaTipoDocumento(DocumentoSpesa documentoSpesa, FatturaFEL fatturaFEL) {
		final String methodName = "impostaTipoDocumento";
		if(fatturaFEL.getTipoDocumentoFEL() == null) {
			// Se la fattura non ha il tipo documento, non imposto il dato
			log.info(methodName, "Tipo documento FEL non impostato per la fattura " + fatturaFEL.getUid() + " - preimpostazione del campo saltata");
			return;
		}
		
		
		
//		TipoDocumentoFELHelper helper = TipoDocumentoFELHelper.byTipoDocumentoFEL(fatturaFEL.getTipoDocumentoFEL());
//		if(helper == null) {
//			// Non dovrebbe mai succedere, ma null-safe
//			log.error(methodName, "Il tipo documento FEL " + fatturaFEL.getTipoDocumentoFEL() + " non ha un corrispondente helper");
//			return;
//		}
//		// Recupero il codice e il tipo documento corrispondente
//		String codiceTipoDocumento = helper.getCodiceTipoDocumento();
//		TipoDocumento tipoDocumento = ComparatorUtil.findByCodice(model.getListaTipoDocumento(), codiceTipoDocumento);
//		
//		if(tipoDocumento == null) {
//			// Nessun tipo documento corrispondente al codice
//			log.info(methodName, "Tipo documento non trovato per codice  " + codiceTipoDocumento);
//			return;
//		}
//		// Imposto il tipo documento
//		documentoSpesa.setTipoDocumento(tipoDocumento);
		
		
		/*
		 * SIAC-7557-VG
		 * model.getListaTipoDocumento(), fatturaFEL.getDocTipoSpesa()
		 */
		if(fatturaFEL.getDocTipoSpesa()!= null && model.getListaTipoDocumento()!=null && !model.getListaTipoDocumento().isEmpty()){
			for(TipoDocumento td :model.getListaTipoDocumento()){
				if(fatturaFEL.getDocTipoSpesa().intValue() == td.getUid()){
					documentoSpesa.setTipoDocumento(td);
				}
			}
		}
		
		
		
		
		log.debug(methodName, "Fattura.tipoDocumentoFEL.codice = " + fatturaFEL.getTipoDocumentoFEL().getCodice()
				+ " => documento.tipoDocumento = " + (documentoSpesa.getTipoDocumento() != null ? documentoSpesa.getTipoDocumento().getUid() : "null"));
	}
	
	/**
	 * Imposta l'ufficio destinatario.
	 * 
	 * @param documentoSpesa il documento di spesa
	 * @param fatturaFEL     la fattura
	 */
	private void impostaUfficioDestinatario(DocumentoSpesa documentoSpesa, FatturaFEL fatturaFEL) {
		final String methodName = "impostaUfficioDestinatariob";
		if(StringUtils.isBlank(fatturaFEL.getCodiceDestinatario())) {
			// Se non ho il codice destinatario esco
			return;
		}
		// Ricerco il codice ufficio destinatario pcc via codice
		String codiceUfficio = fatturaFEL.getCodiceDestinatario();
		CodiceUfficioDestinatarioPCC codiceUfficioDestinatarioPCC = ComparatorUtils.findByCodice(model.getListaCodiceUfficioDestinatarioPCC(), codiceUfficio);
		// Ignoro l'eventuale dato non presente
		documentoSpesa.setCodiceUfficioDestinatario(codiceUfficioDestinatarioPCC);
		log.debug(methodName, "Fattura.codiceDestinatario = " + fatturaFEL.getCodiceDestinatario()
				+ " => documento.getCodiceUfficioDestinatario = " + (documentoSpesa.getCodiceUfficioDestinatario() != null ? documentoSpesa.getCodiceUfficioDestinatario().getUid() : "null"));
	}

	@Override
	protected void cleanModel() {
		// Pulizia del model
		super.cleanModel();
		model.setUidDocumento(null);
	}
	
	/**
	 * carica gli eventuali provvisori di cassa presenti in sessione
	 */
	private void caricaProvvisoriDiCassa() {
		List<Integer> listaUidDaSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_UID_PROVVISORI);
		if(listaUidDaSessione == null || listaUidDaSessione.isEmpty()){
			// Se non ho i provvisori caricati, pulisco il totale ed esco
			sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, null);
			return;
		}
		// Prendo gli uid
		model.setListaUidProvvisori(listaUidDaSessione);
		// Prendo il totale dei provvisori
		BigDecimal importo = sessionHandler.getParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI);
		if(model.getDocumento() == null) {
			// Se il documento non e' impostato, lo inizializzo
			model.setDocumento(new DocumentoSpesa());
		}
		// Imposto l'importo pari al totale dei provvisori
		model.getDocumento().setImporto(importo);
		model.setTotaleProvvisori(importo);
		// Pulisco la sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_UID_PROVVISORI, null);
		sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, null);
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
