/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccommonapp.util.exception.ApplicationException;
import it.csi.siac.siaccommonapp.util.exception.FrontEndCheckedException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.AggiornaDocumentoIvaSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.StampaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIvaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'inserimento del Documento Iva Spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 - 12/06/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaDocumentoIvaSpesaAction extends GenericDocumentoIvaSpesaAction<AggiornaDocumentoIvaSpesaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1163571826338675652L;

	@Autowired
	private transient StampaIvaService stampaIvaService;

	@Override
	public void prepare() throws Exception {
		// Pulisco le liste
		cleanErroriMessaggiInformazioni();
		// Di default, apro la prima pagina. Deve arrivarmi un ordine dalla
		// pagina per aprire la seconda
		model.setFlagAperturaSecondaPagina(Boolean.FALSE);
		
	}

	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		
		model.setModificheARegistroAbilitate(false);
		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA, null);
		sessionHandler.setParametro(BilSessionParameter.LISTA_ATTIVITA_IVA, null);
		sessionHandler.setParametro(BilSessionParameter.LISTA_REGISTRO_IVA, null);
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Check caso d'uso applicabile
		checkCasoDUsoApplicabile(model.getTitolo());

		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();

		try {
			// Ricerca di dettaglio del subdocumento iva
			ricercaDettaglioSubdocumentoIva(model.getUidSubdocumentoIva());
			
			// Controllo se il documento iva sia aggiornabile
			checkDocumentoIvaAggiornabile();
						
			//nel caso di utente backoffice, carico anche la lista di registri iva modificabili
			gestioneModificheARegistro();
			
			// Ricerca dettaglio documento spesa
			ricercaDettaglioDocumento();

			// Ricerca di dettaglio del soggetto
			caricaSoggetto();

			// Ricerca della quota se associata
			if (Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())) {
				ricercaDettaglioQuota();
			}

			// Effettua i controlli sulla nota di credito
			checkNotaCredito();

			// Caricamento classificatori
			caricamentoListe(TipoFamigliaDocumento.SPESA);

			// Caricamento flag per quota iva differita
			populateFlagQuotaIvaDifferita();

			// Caricamento flag per il protocollo provvisorio
			populateFlagProtocolloProvvisorio();			

		} catch (FrontEndCheckedException e) {
			log.error(methodName, "Errore a livello di front-end: " + e.getMessage(), e);
			throw new ApplicationException(e);
		}

		if (hasErrori()) {
			log.info(methodName, "Errori presenti: non e' possibile effettuare l'aggiornamento del subdocumento iva "
					+ model.getUidSubdocumentoIva());
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		// Ho caricato tutto: sono pronto a mostrare la pagina
		return SUCCESS;
	}
	
	/**
	 * Controlla se l'utente &egrave abilitato a modificare il registro associato al documento.
	 * In caso sia abilitato, popola i campi necessari a permettere tale modifica.
	 * */
	private void gestioneModificheARegistro() {
		final String methodName = "gestioneModificheARegistro";
		if(!isUtenteBackOffice()){
			return;
		}
		model.setModificheARegistroAbilitate(true);
		RegistroIva registroAttuale = model.getListaRegistroIvaAggiornamento().isEmpty()? new RegistroIva() : model.getListaRegistroIvaAggiornamento().get(0);
		model.setRegistroIvaAggiornamento(registroAttuale);
		try {
			caricaListaRegistroIva();
		} catch (WebServiceInvocationFailureException e) {
			model.setListaRegistroIvaAggiornamento(new ArrayList<RegistroIva>());
			log.debug(methodName, "non sono stati trovati registri.");
		}
		model.setListaRegistroIvaAggiornamento(model.getListaRegistroIva());
	}


	/**
	 * Controlla se l'utente sia abilitato alle modifiche del registro di un documento di spesa ("OP-IVA-aggDocIvaSpeBackOffice").
	 * returns <code>true</code> se l'utente 6egrave abilitato, <code>false</code> altrimenti.
	 * */
	private boolean isUtenteBackOffice() {
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		return AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.DOCUMENTO_IVA_AGGIORNA_SPESA_BACKOFFICE, listaAzioniConsentite);
	}

	/**
	 * Popola il flag per la quota iva differita.
	 */
	private void populateFlagQuotaIvaDifferita() {
		TipoRelazione tr = model.getSubdocumentoIva().getTipoRelazione();
		model.setFlagQuotaIvaDifferita(Boolean.valueOf(TipoRelazione.QUOTE_PER_IVA_DIFFERITA == tr));
	}

	/**
	 * Popola il flag per il protocollo provvisorio.
	 */
	private void populateFlagProtocolloProvvisorio() {
		TipoRegistroIva tri = model.getTipoRegistroIva();
		model.setFlagIvaImmediata(Boolean.valueOf(TipoEsigibilitaIva.IMMEDIATA == tri.getTipoEsigibilitaIva()));
				
	}

	@Override
	protected void impostaDatiNelModelNota(SubdocumentoIvaSpesa nota) throws WebServiceInvocationFailureException {
		model.setNota(nota);
		// Imposto le aliquote
		model.setListaAliquotaSubdocumentoIvaNota(nota.getListaAliquotaSubdocumentoIva());
				
		model.setAttivitaIvaNota(nota.getAttivitaIva());
		// Imposto il tipoRegistroIva
		model.setTipoRegistroIvaNota(nota.getRegistroIva().getTipoRegistroIva());
		
		model.setFlagRilevanteIRAPNota(nota.getFlagRilevanteIRAP());
		
		// carico le liste per popolare le select disabilitate
		model.setListaTipoRegistrazioneIva(Arrays.asList(nota.getTipoRegistrazioneIva()));
		model.setListaTipoRegistroIva(Arrays.asList(nota.getRegistroIva().getTipoRegistroIva()));
		model.setListaAttivitaIva(Arrays.asList(nota.getAttivitaIva()));
		model.setListaRegistroIva(Arrays.asList(nota.getRegistroIva()));
		
	}

	@Override
	protected void impostaFlagNota() {
		// Imposto l'ingresso nella pagina delle note
		model.setFlagAperturaSecondaPagina(Boolean.TRUE);
		model.setAperturaTabNotaCredito(Boolean.TRUE);
		model.setFlagNotaCreditoIvaDisponibile(Boolean.TRUE);
	}

	/**
	 * Controlla i dati della nota di credito.
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             nel caso in cui l'invocazione del servizio restituisca
	 *             un'errore
	 */
	private void checkNotaCredito() throws WebServiceInvocationFailureException {
		final String methodName = "checkNotaCredito";
		// qui controllo se ho una nota credito!!
		// Se ho già la nota di credito nel model, allora non faccio nulla
		if (model.getNota() != null) {
			log.debug(methodName, "Nota gia' presente nel model");
			model.setFlagNotaCreditoIvaPresente(Boolean.TRUE);
			model.setFlagNotaCreditoIvaDisponibile(Boolean.TRUE);
			
			// L'importo totale delle note e' pari a totale spese + totale entrate
			model.setImportoTotaleNote(model.getDocumento().calcolaImportoTotaleNoteCollegateSpesa().add(model.getDocumento().calcolaImportoTotaleNoteCollegateEntrata()));
			// importo da dedurre della quota se la registrazione e' sulla singola
			// quota, totale importo da dedurre se la registrazione
			// e' sull'intero documento
			if (Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())) {
				model.setImportoDaDedurre(model.getSubdocumento().getImportoDaDedurre());
			} else {
				model.setImportoDaDedurre(model.calcolaTotaleImportoDaDedurre());
			}
			
			
			return;
		}

		SubdocumentoIvaSpesa sis = model.getSubdocumentoIva();
		DocumentoSpesa documento = model.getDocumento();
		List<DocumentoSpesa> listaNotaCredito = documento.getListaNoteCreditoSpesaFiglio();
		if (listaNotaCredito == null || listaNotaCredito.isEmpty()) {
			log.debug(methodName, "Non ho note di credito sul documento");
			model.setFlagNotaCreditoIvaDisponibile(Boolean.FALSE);
			return;
		}

		model.setFlagNotaCreditoIvaDisponibile(Boolean.TRUE);

		List<SubdocumentoIvaSpesa> listaNotaCreditoIva = sis.getListaNoteDiCredito();
		boolean notaCreditoPresente = listaNotaCreditoIva != null && !listaNotaCreditoIva.isEmpty();
		model.setFlagNotaCreditoIvaPresente(Boolean.valueOf(notaCreditoPresente));																					

		if (notaCreditoPresente) {
			// Popolo la nota presente
			SubdocumentoIvaSpesa nota = listaNotaCreditoIva.get(0);

			impostaDatiNelModelNota(nota);
			model.setListaAttivitaIva(Arrays.asList(nota.getAttivitaIva()));

			model.setTipoRegistroIva(nota.getRegistroIva().getTipoRegistroIva());
			model.setListaTipoRegistroIva(Arrays.asList(nota.getRegistroIva().getTipoRegistroIva()));

			model.setListaRegistroIva(Arrays.asList(nota.getRegistroIva()));
			model.setListaTipoRegistrazioneIva(Arrays.asList(nota.getTipoRegistrazioneIva()));		
			
		} else {
			caricaListaTipoRegistrazioneIva();
			caricaListaTipoRegistroIva();
			caricaListaAttivitaIva();
		}	

		// L'importo totale delle note è pari a totale spese + totale entrate
		model.setImportoTotaleNote(documento.calcolaImportoTotaleNoteCollegateSpesa().add(documento.calcolaImportoTotaleNoteCollegateEntrata()));
		// importo da dedurre della quota se la registrazione è sulla singola
		// quota, totale importo da dedurre se la registrazione
		// è sull'itero documento
		if (Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())) {
			model.setImportoDaDedurre(model.getSubdocumento().getImportoDaDedurre());
		} else {
			model.setImportoDaDedurre(model.calcolaTotaleImportoDaDedurre());
		}
	}

	/**
	 * Il sistema verifica se il Subdocumento Iva selezionato &egrave; modificabile effettuando i seguenti controlli:
	 * <ul>
	 *     <li>Il registro non deve essere <strong>BLOCCATO</strong>, in questo caso segnalare un messaggio di Attenzione indicando che il registro selezionato
	 *         &egrave; Bloccato ('Dati Iva non gestibili perch&eacute; il registro selezionato &egrave; stato bloccato.').</li>
	 *     <li>Lo stato del Documento Iva Spesa deve essere diverso da <trong>'PD'</strong>, ovvero quando ancora nessuna delle sue quote
	 *         &egrave; stata pagata; se la registrazione Iva &egrave; fatta sulla singola quota allora lo stato del Documento Iva Spesa
	 *         &egrave; diverso da 'PD' quando quella quota non &egrave; ancora stata pagata.</li>
	 *     <li>Il documento iva non deve  appartenere ad un registro stampato in definitivo, ovvero quando i campi (derivati) del Documento Iva
	 *         <em>'flagStampaDef_Provv'</em> e <em>'flagStampaDef_Def'</em> sono entrambi = 0
	 *         <br/>
	 *         NOTA: questo controllo &egrave; l'unico da mantenere se l'operatore ha l'azione <em>OP-IVA-aggDocIvaSpeBackOffice</em> abilitata,
	 *         se l'azione &egrave; stata passata tra i parametri (ad es. se il servizio &egrave; stato richiamato da web app).</li>
	 *     <li>Il documento di riferimento (se presente, ad es. per i documenti relativi alla Testata IVA questo controllo non &egrave; da fare)
	 *         <strong>NON DEVE</strong> avere nessuna quota liquidata o con ordinativo associato.</li>
	 * </ul>
	 * Mutuato dal back-end
	 * @throws GenericFrontEndMessagesException nel caso in cui il documento non sia aggiornabile
	 */
	private void checkDocumentoIvaAggiornabile() {
		final String methodName = "checkDocumentoIvaAggiornabile";
		SubdocumentoIvaSpesa sis = model.getSubdocumentoIva();
		// Condizione 3
		try {
			boolean registroIvaStampato = isRegistroIvaStampato(sis.getDataProtocolloDefinitivo());
			checkBusinessCondition(!registroIvaStampato, ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", "dati Iva non gestibili perche' il registro selezionato e' stato gia' stampato in definitivo per il periodo corrispondente alla data di protocollo definitivo."));
			log.debug(methodName, "Il registro non e' gia' stampato in definitivo per la data di protocollo definitiva");
			
			registroIvaStampato = isRegistroIvaStampato(sis.getDataProtocolloProvvisorio());
			checkBusinessCondition(!registroIvaStampato, ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", "dati Iva non gestibili perche' il registro selezionato e' stato gia' stampato in definitivo per il periodo corrispondente alla data di protocollo provvisorio."));
			log.debug(methodName, "Il registro non e' gia' stampato in definitivo per la data di protocollo provvisoria");
		} catch (WebServiceInvocationFailureException e) {
			log.info(methodName, e.getMessage());
			throw new GenericFrontEndMessagesException("Eccezione nel controllo di stampa del registro iva", e);
		}
		
		if(isUtenteBackOffice()) {
			log.debug(methodName, "L'utente ha i permessi di backoffice: non effettuo i controlli ulteriori");
			return;
		}
		
		// Condizione 1
		checkBusinessCondition(!Boolean.TRUE.equals(sis.getRegistroIva().getFlagBloccato()),
				ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", "dati Iva non gestibili perche' il registro selezionato e' stato bloccato."));
		log.debug(methodName, "Il registro non e' bloccato");
		
		// Condizione 2
		checkBusinessCondition(!StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.equals(sis.getStatoSubdocumentoIva()), ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Subdocumento iva", StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO.getCodice()));
		log.debug(methodName, "Il registro non e' in PROVVISORIO_DEFINITIVO");
		
		// Condizione 4
		try {
			checkDatiDocumento();
		} catch(WebServiceInvocationFailureException e) {
			log.info(methodName, e.getMessage());
			throw new GenericFrontEndMessagesException("Eccezione nel controllo dei legami del documento", e);
		}
	}
	
	/**
	 * Controlla se il registro iva sia stato stampato in definitivo.
	 * @param dataProtocollo la data di protocollo da controllare
	 * @return <code>true</code> se il registro Iva &eacute; stato stampato in definitivo; <code>false</code> in caso contrario
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	private boolean isRegistroIvaStampato(Date dataProtocollo) throws WebServiceInvocationFailureException {
		final String methodName = "isRegistroIvaStampato";
		if(dataProtocollo == null){
			log.debug(methodName, "Il subdocumento iva non ha la data protocollo, non puo' essere stato stampato: returning false.");
			return false;
		}
		
		RicercaStampaIva request = model.creaRequestRicercaStampaIva(dataProtocollo);
		RicercaStampaIvaResponse response = stampaIvaService.ricercaStampaIva(request);

		if (response.hasErrori()) {
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaStampaIva.class, response));
		}

		for (StampaIva si : response.getListaStampaIva()) {
			if (TipoStampa.DEFINITIVA.equals(si.getTipoStampa())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Controllo dei dati collegati al documento.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void checkDatiDocumento() throws WebServiceInvocationFailureException {
		ContaDatiCollegatiSubdocumentoIvaSpesa req = model.creaRequestContaDatiCollegatiSubdocumentoIvaSpesa();
		ContaDatiCollegatiSubdocumentoIvaSpesaResponse res = documentoIvaSpesaService.contaDatiCollegatiSubdocumentoIvaSpesa(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			String msg = createErrorInServiceInvocationString(ContaDatiCollegatiSubdocumentoIvaSpesa.class, res);
			throw new WebServiceInvocationFailureException(msg);
		}
		
		Long ordinativiCollegati = res.getOrdinativiCollegati() != null ? res.getOrdinativiCollegati() : Long.valueOf(0L);
		Long liquidazioniCollegate = res.getLiquidazioniCollegate() != null ? res.getLiquidazioniCollegate() : Long.valueOf(0L);
		
		checkBusinessCondition(ordinativiCollegati.longValue() == 0L, 
				ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
					"Dati Iva non gestibili perche' il documento di riferimento ha " + (ordinativiCollegati.longValue() > 1 ? ordinativiCollegati.longValue() + " ordinativi collegati" : "un ordinativo collegato") + "."));
		checkBusinessCondition(liquidazioniCollegate.longValue() == 0L, 
				ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento subdocumento iva", 
					"Dati Iva non gestibili perche' il documento di riferimento ha " + (liquidazioniCollegate.longValue() > 1 ? liquidazioniCollegate.longValue() + " liquidazioni collegate" : "una liquidazione collegata") + "."));
	}

	@Override
	protected void impostaDatiNelModel(SubdocumentoIvaSpesa sis) throws WebServiceInvocationFailureException {
		super.impostaDatiNelModel(sis);
		// Imposto il tipo registrazioneIva
		model.setListaTipoRegistrazioneIvaAggiornamento(Arrays.asList(sis.getTipoRegistrazioneIva()));

		// Imposto l'attivitaIva
		model.setAttivitaIva(sis.getAttivitaIva());
		// Imposto la listaAttivitaIva
		model.setListaAttivitaIvaAggiornamento(sis.getAttivitaIva() != null ? Arrays.asList(sis.getAttivitaIva()): new ArrayList<AttivitaIva>());

		// Imposto il tipoRegistroIva
		model.setTipoRegistroIva(sis.getRegistroIva().getTipoRegistroIva());
		model.setListaTipoRegistroIvaAggiornamento(Arrays.asList(sis.getRegistroIva().getTipoRegistroIva()));

		// Imposto la listaRegistroIVA
		model.setListaRegistroIvaAggiornamento(Arrays.asList(sis.getRegistroIva()));

		// Imposto il flag per il protocollo
		model.setFlagIvaImmediata(Boolean.valueOf(TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA == model.getTipoRegistroIva()) || Boolean.valueOf(TipoRegistroIva.CORRISPETTIVI == model.getTipoRegistroIva()));
	}

	@Override
	protected void caricamentoListe(TipoFamigliaDocumento tipoFamigliaDocumento)
			throws WebServiceInvocationFailureException {

		caricaListaAliquotaIva();
		caricaListaTipoDocumento(tipoFamigliaDocumento);
		
	}

	/**
	 * Ottiene la lista dei registri iva dal servizio. <br>
	 * L'operazione pu&oacute; essere lanciata se e solo se &eacute; stato selezionato
	 * un tipo di registro iva.
	 * 
	 * @param tipoRegistroIva
	 *            il tipo di registro
	 * @param attivitaIva
	 *            l'attivita
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             nel caso in cui l'invocazione del servizio restituisca
	 *             un'errore
	 */
	private void caricaListaRegistroIvaNota(TipoRegistroIva tipoRegistroIva, AttivitaIva attivitaIva)
			throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaRegistroIva";
		// Ho il registro Iva. Allora ricerco anche il registro
		if (tipoRegistroIva != null) {
			RicercaRegistroIva request = model.creaRequestRicercaRegistroIva(tipoRegistroIva, attivitaIva);
			logServiceRequest(request);
			RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
			logServiceResponse(response);

			if (response.hasErrori()) {
				log.info(methodName, "Errore nell'invocazione del servizio RicercaRegistroIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException(
						"Errore nell'invocazione del servizio RicercaRegistroIva");
			}

			model.setListaRegistroIvaNota(response.getListaRegistroIva());
		}
	}

	/**
	 * Prepare per l'aggiornamento del documento iva.
	 */
	public void prepareAggiornaDocumentoIva() {
		// Riapro sul tab corretto
		model.setAperturaTabNotaCredito(Boolean.FALSE);
	}

	/**
	 * Aggiorna il documento Iva di spesa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaDocumentoIva() {
		final String methodName = "aggiornaDocumentoIva";
		SubdocumentoIvaSpesa sis = null;

		// Differenziazione dei due casi: documento iva o quota per iva
		// differita
		try {
			if (Boolean.TRUE.equals(model.getFlagQuotaIvaDifferita())) {
				sis = invocazioneServizioAggiornaQuotaIvaDifferita();
			} else {
				sis = invocazioneServizioAggiornaSubdocumentoIva();
			}
		} catch (WebServiceInvocationFailureException e) {
			log.info(methodName, e.getMessage());
			return INPUT;
		}

		log.debug(methodName, "Aggiornamento del documentoIvaSpesa con uid " + sis.getUid() + " andata a buon fine");
		model.setSubdocumentoIva(sis);
		// Imposto l'uid nel model
		model.setUidSubdocumentoIva(sis.getUid());
		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Invoca il servizio di aggiornamento del documento iva.
	 * 
	 * @return il documento aggiornato
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             nel caso di errori nell'invocazione del servizio
	 */
	private SubdocumentoIvaSpesa invocazioneServizioAggiornaSubdocumentoIva()
			throws WebServiceInvocationFailureException {
		AggiornaSubdocumentoIvaSpesa request = model.creaRequestAggiornaSubdocumentoIvaSpesa();
		logServiceRequest(request);
		AggiornaSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.aggiornaSubdocumentoIvaSpesa(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Ho degli errori
			addErrori(response);
			throw new WebServiceInvocationFailureException(
					"Errore nell'invocazione del servizio AggiornaSubdocumentoIvaSpesa");
		}
		// Documento IVA aggiornato
		return response.getSubdocumentoIvaSpesa();
	}

	/**
	 * Invoca il servizio di aggiornamento dal quota iva differita.
	 * 
	 * @return il documento aggiornato
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             nel caso di errori nell'invocazione del servizio
	 */
	private SubdocumentoIvaSpesa invocazioneServizioAggiornaQuotaIvaDifferita()
			throws WebServiceInvocationFailureException {
		AggiornaQuotaIvaDifferitaSpesa request = model.creaRequestAggiornaQuotaIvaDifferitaSpesa();
		logServiceRequest(request);
		AggiornaQuotaIvaDifferitaSpesaResponse response = documentoIvaSpesaService
				.aggiornaQuotaIvaDifferitaSpesa(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Ho degli errori
			addErrori(response);
			throw new WebServiceInvocationFailureException(
					"Errore nell'invocazione del servizio AggiornaQuotaIvaDifferitaSpesa");
		}
		// Documento IVA aggiornato
		return response.getSubdocumentoIvaSpesa();
	}

	/**
	 * Valida l'aggiornamento del Documento Iva.
	 */
	public void validateAggiornaDocumentoIva() {
		SubdocumentoIvaSpesa sis = model.getSubdocumentoIva();
		// Controllo i campi obbligatorii
		// Se il tipoRegistro è ACQUISTI IVA ESIGIBILITA DIFFERITA o ACQUISTI
		// IVA ESIGIBILITA IMMEDIATA
		checkCondition(!model.getListaAliquotaSubdocumentoIva().isEmpty(),
				ErroreFin.NON_CI_SONO_MOVIMENTI_IVA_ASSOCIATI.getErrore());

		
		//nel caso di registrazione senza protocollo la data non deve essere obbligatoria
		if(sis.getNumeroProtocolloDefinitivo() != null){
			checkCondition(sis.getDataProtocolloDefinitivo() != null , ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo definitivo"));
		}
		if(sis.getNumeroProtocolloProvvisorio() != null){
			checkCondition(sis.getDataProtocolloProvvisorio() != null , ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo provvisorio"));
		}
		
		//CR-3791
		if(!isUtenteBackOffice()){
			Date dataProtocollo = model.getFlagIvaImmediata().booleanValue() ? sis.getDataProtocolloDefinitivo() : sis.getDataProtocolloProvvisorio();
			checkCondition(dataProtocollo == null || (model.getDocumento().getDataEmissione() != null
					&& !dataProtocollo.before(model.getDocumento().getDataEmissione())),
			ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
			checkModificheARegistroAbilitate(sis.getRegistroIva());
		}
		// Controllo totali
		BigDecimal totaleParziale = model.getTotaleTotaleMovimentiIva();
		BigDecimal importo = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())
				? model.getSubdocumento().getImporto() : model.getImportoRilevanteIva();
		controlloImporti(totaleParziale, importo);
	}

//	/**
//	 * Controlla gli importi e fornisce eventuali messaggi di errore.
//	 * 
//	 * @param totaleParziale
//	 *            il totale parziale
//	 * @param importo
//	 *            l'importo del documento di riferimento
//	 */
//	private void controlloImporti(BigDecimal totaleParziale, BigDecimal importo) {
//		// Se il documento è INTRASTAT, effettuo il controllo più blando
//		if (Boolean.TRUE.equals(model.getSubdocumentoIva().getFlagIntracomunitario())) {
//			warnCondition(importo.subtract(totaleParziale).signum() != 0,
//					ErroreFin.DOCUMENTO_IVA_SPESA_INTRASTAT.getErrore());
//		} else {
//			ErroreFin erroreImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())
//					? ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_IMPORTO_QUOTA
//					: ErroreFin.TOTALE_MOVIMENTI_IVA_NON_MAGGIORE_TOTALE_IMPORTO_RILEVANTE_IVA;
//			ErroreFin warningImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())
//					? ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_DELLA_QUOTA
//					: ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_RILEVANTE_IVA;
//
//			String totaleParzialeFormatted = FormatUtils.formatCurrency(totaleParziale);
//			String importoFormatted = FormatUtils.formatCurrency(importo);
//			boolean isNotaCredito = model.getDocumento().getTipoDocumento().isNotaCredito();
//
//			checkCondition((isNotaCredito && importo.add(totaleParziale).signum() >= 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() >= 0), erroreImporti.getErrore(totaleParzialeFormatted, importoFormatted));
//			warnCondition((isNotaCredito && importo.add(totaleParziale).signum() == 0) || (!isNotaCredito && importo.subtract(totaleParziale).signum() == 0),
//					warningImporti.getErrore(totaleParzialeFormatted, importoFormatted));
//		}
//	}

	/**
	 * Carica i movimenti Iva relativi al subdocumento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimentiIva() {
		return SUCCESS;
	}

	/**
	 * Prepare specifico per l'apertura del collapse dei MovimentiIva
	 */
	public void prepareApriCollapseMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(new AliquotaSubdocumentoIva());
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}

	/**
	 * Apre il collapse dei Movimenti Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriCollapseMovimentiIva() {
		// Non faccio nulla: restituisco solo la pagina vuota
		return SUCCESS;
	}

	/**
	 * Popola il movimento e restituisce la pagina corretta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriModaleMovimentiIva() {
		// Prendo il movimento iva relativo alla riga selezionata
		int riga = model.getRiga().intValue();
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = ReflectionUtil
				.deepClone(model.getListaAliquotaSubdocumentoIva().get(riga));
		model.setAliquotaSubdocumentoIva(aliquotaSubdocumentoIva);

		model.setPercentualeAliquotaIva(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeAliquota());
		model.setPercentualeIndetraibilitaAliquotaIva(
				aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeIndetraibilita());
		return SUCCESS;
	}

	/**
	 * Prepare specifico per l'inserimento del MovimentiIva
	 */
	public void prepareInserisciMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(null);
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}

	/**
	 * Inserisce il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciMovimentiIva() {
		final String methodName = "inserisciMovimentiIva";
		validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), false, model.getDocumento().getTipoDocumento().isNotaCredito());
		if (hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'inserimento del movimento iva");
			return SUCCESS;
		}

		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: aggiungo il movimento alla lista in sessione
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());
		// Pulisco l'aliquota
		model.setAliquotaSubdocumentoIva(null);

		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Popola l'aliquota a partire dall'uid della stessa e dalla lista delle
	 * possibili.
	 * 
	 * @param aliquota
	 *            l'aliquota da cui ottenere l'uid
	 * @param lista
	 *            la lista in cui cercare l'aliquota
	 */
	private void popolaAliquotaIva(AliquotaSubdocumentoIva aliquota, List<AliquotaIva> lista) {
		// Ottengo i dati dell'aliquota
		AliquotaIva aliquotaIva = ComparatorUtils.searchByUid(lista, aliquota.getAliquotaIva());
		aliquota.setAliquotaIva(aliquotaIva);
	}

	/**
	 * Prepare specifico per l'aggiornamento del MovimentiIva
	 */
	public void prepareAggiornaMovimentiIva() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIva(null);
		model.setPercentualeAliquotaIva(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIva(BigDecimal.ZERO);
	}

	/**
	 * Aggiorna il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaMovimentiIva() {
		final String methodName = "aggiornaMovimentiIva";
		validateMovimentiIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaSubdocumentoIva(), true, model.getDocumento().getTipoDocumento().isNotaCredito());
		if (hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'aggiornamento del movimento iva");
			return SUCCESS;
		}
		// Popolo l'aliquota
		popolaAliquotaIva(model.getAliquotaSubdocumentoIva(), model.getListaAliquotaIva());
		// Non ho errori: sostituisco il movimento nella lista in sessione
		int indiceDaEliminare = model.getRiga().intValue();
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);
		model.addAliquotaSubdocumentoIva(model.getAliquotaSubdocumentoIva());

		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Elimina il Movimento Iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaMovimentiIva() {
		int indiceDaEliminare = model.getRiga().intValue();
		// Elimino il movimento dalla lista in sessione
		model.getListaAliquotaSubdocumentoIva().remove(indiceDaEliminare);

		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/*
	 * ************************************************* NOTA
	 * *************************************************************************
	 * ****
	 */

	/**
	 * Prepare per l'inserimento della nota di credito.
	 */
	public void prepareInserisciNotaCredito() {
		// Riapro sul tab corretto
		model.setAperturaTabNotaCredito(Boolean.TRUE);
	}

	/**
	 * Inserisce una nota di credito associata al documento iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciNotaCredito() {
		final String methodName = "inserisciNotaCredito";

		InserisceNotaCreditoIvaSpesa request = model.creaRequestInserisceNotaCreditoIvaSpesa();
		logServiceRequest(request);
		InserisceNotaCreditoIvaSpesaResponse response = documentoIvaSpesaService.inserisceNotaCreditoIvaSpesa(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Ho degli errori
			log.info(methodName, "Errore nell'invocazione del servizio InserisceNotaCreditoIvaSpesa per la nota");
			addErrori(response);
			return INPUT;
		}
		// Documento IVA inserito
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		log.debug(methodName,
				"Inserimento della nota documentoIvaSpesa con uid " + sis.getUid() + " avvenuto con successo");
		model.setNota(sis);
		// Carico i messaggi
		impostaInformazioneSuccesso();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();

		return SUCCESS;
	}

	/**
	 * Validazione per l'inserimento della nota di credito.
	 */
	public void validateInserisciNotaCredito() {
		validateNota();
	}

	/**
	 * Prepare per l'aggiornamento della nota di credito.
	 */
	public void prepareAggiornaNotaCredito() {
		// Riapro sul tab corretto
		model.setAperturaTabNotaCredito(Boolean.TRUE);
	}

	/**
	 * Aggiorna la nota di credito associata al documento iva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaNotaCredito() {
		final String methodName = "aggiornaNotaCredito";

		AggiornaNotaCreditoIvaSpesa request = model.creaRequestAggiornaNotaCreditoIvaSpesa();
		logServiceRequest(request);
		AggiornaNotaCreditoIvaSpesaResponse response = documentoIvaSpesaService.aggiornaNotaCreditoIvaSpesa(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			// Ho degli errori
			log.info(methodName, "Errore nell'invocazione del servizio AggiornaNotaCreditoIvaSpesa");
			addErrori(response);
			return INPUT;
		}
		// Documento IVA aggiornato
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		log.debug(methodName,
				"Aggiornamento della nota documentoIvaSpesa con uid " + sis.getUid() + " avvenuta con successo");
		model.setNota(sis);
		// Imposto l'uid nel model
		impostaInformazioneSuccesso();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();

		return SUCCESS;
	}

	/**
	 * Validazione per l'aggiornamento della nota di credito.
	 */
	public void validateAggiornaNotaCredito() {
		validateNota();
	}

	/**
	 * Valida la nota.
	 */
	private void validateNota() {
		SubdocumentoIvaSpesa sis = model.getNota();
		// Controllo i campi obbligatorii
		checkNotNullNorInvalidUid(sis.getTipoRegistrazioneIva(), "Tipo Registrazione");
		checkNotNull(model.getTipoRegistroIvaNota(), "Tipo Registro Iva");
		checkNotNullNorInvalidUid(sis.getRegistroIva(), "Registro");
		// Se il tipoRegistro è ACQUISTI IVA ESIGIBILITA DIFFERITA o ACQUISTI
		// IVA ESIGIBILITA IMMEDIATA
		checkCondition(
				model.getTipoRegistroIvaNota() != TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA
						|| sis.getDataProtocolloDefinitivo() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo definitivo"));
		checkCondition(
				model.getTipoRegistroIvaNota() != TipoRegistroIva.ACQUISTI_IVA_DIFFERITA
						|| sis.getDataProtocolloProvvisorio() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data protocollo provvisorio"));
		checkCondition(!model.getListaAliquotaSubdocumentoIvaNota().isEmpty(),
				ErroreFin.NON_CI_SONO_MOVIMENTI_IVA_ASSOCIATI.getErrore());

		TipoRegistroIva tri = model.getTipoRegistroIvaNota();
		// Carica il RegistroIva
		if (tri != null) {
			try {
				caricaListaRegistroIvaNota(tri, model.getAttivitaIvaNota());
			} catch (WebServiceInvocationFailureException e) {
				log.debug("prepareInserisciDocumentoIva", e.getMessage());
			}
		}
		
		if(!isUtenteBackOffice()){
			
			checkModificheARegistroAbilitate(sis.getRegistroIva());
		// Validazione data protocollo
			Date dataProtocollo = tri == TipoRegistroIva.ACQUISTI_IVA_IMMEDIATA ? sis.getDataProtocolloDefinitivo()
				: sis.getDataProtocolloProvvisorio();
			if (dataProtocollo != null) {
				checkCondition(!dataProtocollo.before(model.getDocumento().getDataEmissione()),
						ErroreFin.DATA_REGIST_ANTECEDENTE_DATA_EMISSIONE_DOC.getErrore());
			}
			
		
		}
		// Controllo totali
		BigDecimal importoTotaleNote = model.getImportoTotaleNote();
		BigDecimal totaleParziale = model.getTotaleTotaleMovimentiIvaNota();
		ErroreFin erroreImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())
				? ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_PUO_ESSERE_MAGGIORE_DELL_IMPORTO_DA_DEDURRE_SULLA_QUOTA
				: ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_PUO_ESSERE_MAGGIORE_DELL_IMPORTO_DA_DEDURRE_SU_QUOTE_RILEVANTI_IVA;
		ErroreFin warningImporti = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota())
				? ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_DA_DEDURRE_SULLA_QUOTA
				: ErroreFin.IL_TOTALE_DEI_MOVIMENTI_IVA_NON_E_UGUALE_ALL_IMPORTO_DA_DEDURRE_SU_QUOTE_RILEVANTI_IVA;

		checkCondition(importoTotaleNote.add(totaleParziale).signum() >= 0, erroreImporti.getErrore());
		warnCondition(importoTotaleNote.add(totaleParziale).signum() == 0, warningImporti.getErrore());
	}

	/**
	 * Carica i movimenti Iva relativi alle note del subdocumento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimentiIvaNota() {
		return SUCCESS;
	}

	/**
	 * Prepare specifico per l'apertura del collapse dei MovimentiIva delle
	 * note.
	 */
	public void prepareApriCollapseMovimentiIvaNota() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIvaNota(new AliquotaSubdocumentoIva());
		model.setPercentualeAliquotaIvaNota(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIvaNota(BigDecimal.ZERO);
	}

	/**
	 * Apre il collapse dei Movimenti Iva delle note.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriCollapseMovimentiIvaNota() {
		// Non faccio nulla: restituisco solo la pagina vuota
		return SUCCESS;
	}

	/**
	 * Popola il movimento delle note e restituisce la pagina corretta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String apriModaleMovimentiIvaNota() {
		// Prendo il movimento iva relativo alla riga selezionata
		int riga = model.getRiga().intValue();
		AliquotaSubdocumentoIva aliquotaSubdocumentoIva = ReflectionUtil
				.deepClone(model.getListaAliquotaSubdocumentoIvaNota().get(riga));
		model.setAliquotaSubdocumentoIvaNota(aliquotaSubdocumentoIva);

		model.setPercentualeAliquotaIvaNota(aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeAliquota());
		model.setPercentualeIndetraibilitaAliquotaIvaNota(
				aliquotaSubdocumentoIva.getAliquotaIva().getPercentualeIndetraibilita());
		return SUCCESS;
	}

	/**
	 * Prepare specifico per l'inserimento del MovimentiIva delle note.
	 */
	public void prepareInserisciMovimentiIvaNota() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIvaNota(null);
		model.setPercentualeAliquotaIvaNota(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIvaNota(BigDecimal.ZERO);
	}

	/**
	 * Inserisce il Movimento Iva per le note.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciMovimentiIvaNota() {
		final String methodName = "inserisciMovimentiIvaNota";
		AliquotaSubdocumentoIva asin = model.getAliquotaSubdocumentoIvaNota();
		validateMovimentiIva(asin, model.getListaAliquotaSubdocumentoIvaNota(), false, true);
		if (hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'inserimento del movimento iva per la nota");
			return SUCCESS;
		}

		popolaAliquotaIva(asin, model.getListaAliquotaIva());
		// Non ho errori: aggiungo il movimento alla lista in sessione
		model.addAliquotaSubdocumentoIvaNota(asin);
		// Pulisco l'aliquota
		model.setAliquotaSubdocumentoIva(null);

		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Prepare specifico per l'aggiornamento del MovimentiIva delle note.
	 */
	public void prepareAggiornaMovimentiIvaNota() {
		// Pulisco il Movimento Iva
		model.setAliquotaSubdocumentoIvaNota(null);
		model.setPercentualeAliquotaIvaNota(BigDecimal.ZERO);
		model.setPercentualeIndetraibilitaAliquotaIvaNota(BigDecimal.ZERO);
	}

	/**
	 * Aggiorna il Movimento Iva delle note.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaMovimentiIvaNota() {
		final String methodName = "aggiornaMovimentiIvaNota";
		AliquotaSubdocumentoIva asin = model.getAliquotaSubdocumentoIvaNota();
		validateMovimentiIva(asin, model.getListaAliquotaSubdocumentoIvaNota(), true, true);
		if (hasErrori()) {
			log.info(methodName, "Errore nella validazione dell'aggiornamento del movimento iva per la nota");
			return SUCCESS;
		}
		// Popolo l'aliquota
		popolaAliquotaIva(asin, model.getListaAliquotaIva());
		// Non ho errori: sostituisco il movimento nella lista in sessione
		int indiceDaEliminare = model.getRiga().intValue();
		model.getListaAliquotaSubdocumentoIvaNota().remove(indiceDaEliminare);
		model.addAliquotaSubdocumentoIvaNota(asin);

		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Elimina il Movimento Iva delle note.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaMovimentiIvaNota() {
		int indiceDaEliminare = model.getRiga().intValue();
		// Elimino il movimento dalla lista in sessione
		model.getListaAliquotaSubdocumentoIvaNota().remove(indiceDaEliminare);

		impostaInformazioneSuccesso();
		return SUCCESS;
	}

}
