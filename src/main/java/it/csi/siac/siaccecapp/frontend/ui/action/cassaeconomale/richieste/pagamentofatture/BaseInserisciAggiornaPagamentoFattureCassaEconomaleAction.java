/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamentofatture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture.BaseInserisciAggiornaPagamentoFattureCassaEconomaleModel;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di action base per l'inserimento o aggiornamento del pagamento fatturei.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public abstract class BaseInserisciAggiornaPagamentoFattureCassaEconomaleAction<M extends BaseInserisciAggiornaPagamentoFattureCassaEconomaleModel> extends BaseInserisciAggiornaRichiestaEconomaleAction<M> {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = 2623804031790792071L;

	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO = "InserisciPagamentoFattureCassaEconomaleModel";
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO= "AggiornaPagamentoFattureCassaEconomaleModel";
	
	/** Il servizio per i documenti di spesa */
	@Autowired protected transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient DocumentoIvaService documentoIvaService;
	@Autowired private transient DocumentoService documentoService;
	
	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {

		caricaListeClassificatoriGenerici();
		caricaListaValuta();
		caricaListaTipoDocumento();
		caricaListaClasseSoggetto();
		caricaListaStati();

	}

	/**
	 * Caricamento della lista dei tipi giustificativo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaValuta() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaValuta";
		List<Valuta> listaValuta = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		if(listaValuta == null) {
			log.debug(methodName, "Lista di Valuta non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaValuta request = model.creaRequestRicercaValuta();
			logServiceRequest(request);
			RicercaValutaResponse response = documentoIvaService.ricercaValuta(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaValuta.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaValuta = response.getListaValuta();
			sessionHandler.setParametro(BilSessionParameter.LISTA_VALUTA, listaValuta);
		}
		model.setListaValuta(listaValuta);
	}
	
	/**
	 * Caricamento della lista dei tipi giustificativo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaTipoDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoDocumento";
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		if(listaTipoDocumento == null) {
			log.debug(methodName, "Lista di Tipo Documento non presente in sessione. Caricamento da servizio");
			// Carico la lista da servizio
			RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(TipoFamigliaDocumento.SPESA);
			logServiceRequest(request);
			RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(RicercaTipoDocumento.class, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			listaTipoDocumento = response.getElencoTipiDocumento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaTipoDocumento);
		}
		//eventualmente aggiungere ilfiltraggiosul tipogruppo isFattura
		model.setListaTipoDocumento(listaTipoDocumento);
	}

					
	/**
	 * Carica la liste delle classi Soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ListeGestioneSoggetto.class, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	
	/**
	 * Carica la lista stati operativi del documento (per ora necessaria solo alla ricerca)
	 */
	protected void caricaListaStati() {
		final String methodName = "caricaListaStati";
		log.debug(methodName, "Caricamento della lista degli stati operativi del Documento");
		
		List<StatoOperativoDocumento> listaStato = Arrays.asList(StatoOperativoDocumento.values());
		model.setListaStatoOperativoDocumento(listaStato);
	}
	
	
	@Override
	protected boolean isValidTipoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		return richiestaEconomale != null
			&& richiestaEconomale.getTipoRichiestaEconomale() != null
			&& BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_PAGAMENTO_FATTURE.getConstant().equals(richiestaEconomale.getTipoRichiestaEconomale().getCodice());
	}
	
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		checkNotNullNorInvalidUid(model.getDocumentoSpesa(), "Fattura ");
		
			// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		
		//necessario per caricare i dati del soggetto x la mod pagamento. Non serve dare errore
		try {
			checkSoggettoFattura(model.getDocumentoSpesa().getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		
		checkNotNullNorEmpty(re.getDescrizioneDellaRichiesta(), "Descrizione della spesa");
		
		
	}
	
	@Override
	protected void checkSoggettoFattura(Soggetto soggettoFattura) throws WebServiceInvocationFailureException {
		if (soggettoFattura == null || StringUtils.isBlank(soggettoFattura.getCodiceSoggetto())) {
			model.getRichiestaEconomale().setSoggetto(null);
			return;
		}
		super.checkSoggettoFattura(soggettoFattura);
	}
	
	/**
	 * Ottiene l'eventuale fattura precedentemente associata alla richiesta
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniFatturaAssociata() {
		String methodName = "ottieniFatturaAssociata";
		if(model.getRichiestaEconomale().getSubdocumenti() != null) {
			model.setListaSubdocumentoSpesa(model.getRichiestaEconomale().getSubdocumenti());
			if(!model.getListaSubdocumentoSpesa().isEmpty()) {
				// Prendo il primo subdocumento per recuperare i dati del documento
				SubdocumentoSpesa ss = model.getListaSubdocumentoSpesa().get(0);
				model.setDocumentoSpesa(ss.getDocumento());
			}
		}
		
		// Pulizia di alcuni dati del documento. Questo in quanto alcuni getter del soggetto possono lanciare una NPE
		if(model.getDocumentoSpesa() != null) {
			model.getDocumentoSpesa().setRegistroUnico(null);
			log.debug(methodName, "RegistroUnico set to null!");
			if(model.getDocumentoSpesa().getSoggetto() != null) {
				model.getDocumentoSpesa().getSoggetto().setElencoClass(null);
				log.debug(methodName, "ElencoClass set to null!");
			}
		}
		
		return SUCCESS;
	}
	
	@Override
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiAggiornamento(richiestaEconomale);
		
		model.setListaSubdocumentoSpesa(richiestaEconomale.getSubdocumenti());
		DocumentoSpesa documentoSpesa = null;
		for(SubdocumentoSpesa ss : richiestaEconomale.getSubdocumenti()) {
			if(ss != null && ss.getDocumento() != null && ss.getDocumento().getUid() != 0) {
				documentoSpesa = ss.getDocumento();
				break;
			}
		}
		model.setDocumentoSpesa(documentoSpesa);
		
	}


	@Override
	protected BigDecimal ottieniImporto() {
		if(model.getListaSubdocumentoSpesa() == null || model.getListaSubdocumentoSpesa().isEmpty()) {
			return BigDecimal.ZERO;
		}
		BigDecimal importo = BigDecimal.ZERO;
		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			importo = importo.add(ss.getImporto());
		}
		model.getDocumentoSpesa().setImporto(importo);
		return importo;
	}
	
	/**
	 * Impostazione dei dati del soggetto nella richiesta economale.
	 * 
	 * @param richiestaEconomale la richiesta da popolare
	 * @param soggetto           il soggetto tramite cui popolare la richiesta
	 */
	@Override
	protected void impostazioneDatiSoggetto(RichiestaEconomale richiestaEconomale, Soggetto soggetto) {
		richiestaEconomale.setSoggetto(soggetto);
		
		// Spiattello i dati sulla richiesta economale
		//richiestaEconomale.setMatricola(soggetto.getMatricola());
		richiestaEconomale.setCodiceBeneficiario(soggetto.getCodiceSoggetto());
		richiestaEconomale.setNome(soggetto.getNome());
		// SIAC-4792: se il soggetto non ha cognome (persona giuridica) prendo la denominazione
		richiestaEconomale.setCognome(StringUtils.isNotBlank(soggetto.getCognome()) ? soggetto.getCognome() : soggetto.getDenominazione());
		richiestaEconomale.setCodiceFiscale(soggetto.getCodiceFiscale());
	}
	
	/**
	 * Ricerca delle quote per il documento di spesa.
	 * 
	 * @param docSpesa il documento le cui quote sono da ricercare
	 */
	public void ricercaQuoteDocumentoSpesa(DocumentoSpesa docSpesa) {
		final String methodName = "ricercaQuoteDocumentoSpesa";
		RicercaQuoteByDocumentoSpesa request = model.creaRequestRicercaQuoteByDocumentoSpesa(docSpesa);
		logServiceRequest(request);
		RicercaQuoteByDocumentoSpesaResponse response = documentoSpesaService.ricercaQuoteByDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaQuoteByDocumentoSpesa.class, response));
			addErrori(response);
		}
		
		log.debug(methodName, "Trovati " + response.getSubdocumentiSpesa().size() + " subdocumenti di spesa per il documento " + model.getDocumentoSpesa().getUid());
		docSpesa.setListaSubdocumenti(response.getSubdocumentiSpesa());
	
	}
	
	/**
	 * Molto simile alla versione della classe base
	 * Non viene piu richiamato il metodo checkMovimentoGestione 
	 * JIRA-3015
	 */
	@Override
	protected void checkMovimentoGestione(Impegno movimentoGestione, SubImpegno subMovimentoGestione) {
		final String methodName = "checkMovimentoGestione";
		checkNotNull(movimentoGestione, "Impegno", true);
		checkCondition(movimentoGestione.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno impegno"));
		checkCondition(movimentoGestione.getNumeroBigDecimal() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero impegno"));
		
		if(hasErrori()) {
			// Se ho errori, esco subito
			return;
		}
		
		// La chiave logica del movimento
		final String chiaveMovimentoGestione = movimentoGestione.getAnnoMovimento() + "/" + movimentoGestione.getNumeroBigDecimal();
		
		Impegno impegno;
		List<SubImpegno> subImp;
		try {
			RicercaImpegnoPerChiaveOttimizzatoResponse res = ottieniImpegnoDaServizio(movimentoGestione);
			impegno = res.getImpegno();
			subImp = res.getElencoSubImpegniTuttiConSoloGliIds() != null ? res.getElencoSubImpegniTuttiConSoloGliIds() : new ArrayList<SubImpegno>();
		} catch(WebServiceInvocationFailureException wsife) {
			// Ho gia' fatto il log
			log.info(methodName, wsife.getMessage());
			return;
		}
		
		log.debug(methodName, "Trovato movimento di gestione " + impegno.getUid() + " da servizio corrispondente alla chiave " + chiaveMovimentoGestione);
		model.setMovimentoGestione(impegno);
		
		checkCondition(subImp.isEmpty() || (subMovimentoGestione != null && subMovimentoGestione.getNumeroBigDecimal() != null), ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore(), true);
		
		if(subMovimentoGestione != null && subMovimentoGestione.getNumeroBigDecimal() != null && impegno.getElencoSubImpegni() != null) {
			// La chiave logica del submovimento
			final String chiaveSubMovimentoGestione = chiaveMovimentoGestione + "/" + subMovimentoGestione.getNumeroBigDecimal();
			
			log.debug(methodName, "Ricerca del subimpegno");
			SubImpegno subImpegno = ComparatorUtils.findByNumeroMovimentoGestione(impegno.getElencoSubImpegni(), subMovimentoGestione);
			checkCondition(subImpegno != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", chiaveSubMovimentoGestione), true);
			log.debug(methodName, "Trovato submovimento di gestione " + impegno.getUid() + " da servizio corrispondente alla chiave " + chiaveSubMovimentoGestione);
			model.setSubMovimentoGestione(subImpegno);
		}
		
	}
	
	
	/**
	 * Impostazione della modalit&agrave; di pagamento del dipendente prelevandola dalla mod di pagamento della quota
	 * 
	 * <br/>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa non &eacute; contanti, occorre preimpostare il campo con il valore della modalit&agrave: di pagamento del dipendente
	 *     presente sull'anagrafica soggetto (se compilata).
	 * </p>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa &eacute; 'Contanti' impostare il campo fisso a Contanti senza lasciare la possibilit&agrave; di modificarlo
	 * </p>
	 * 
	 * @param movimento   il movimento da popolare
	 * @param tipoDiCassa il tipo di cassa
	 */
	@Override
	protected void impostaModalitaPagamentoDipendente(Movimento movimento, TipoDiCassa tipoDiCassa) {
		final String methodName = "impostaModalitaPagamentoDipendente";
		if(TipoDiCassa.CONTANTI.equals(tipoDiCassa)) {
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), "CON");
			log.debug(methodName, "Cassa 'CONTANTI'. Ricercata la modalita' di pagamento di tipo 'CON'. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			return;
		}
		
		log.debug(methodName, "Cassa non contanti. Derivo la modalita' dalla quota ");
		recuperaModPagamentoDaQuota();
		
		ModalitaPagamentoSoggetto mps = model.getMpsDaQuota();
		if(mps == null || mps.getModalitaAccreditoSoggetto() == null) {
			log.debug(methodName, "Modalita' di pagamento non valorizzata correttamente");
			return;
		}
		
		ModalitaAccreditoSoggetto mas = mps.getModalitaAccreditoSoggetto();
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), mas.getCodice());
		log.debug(methodName, "Cassa non 'CONTANTI'. Ricercata la modalita' di pagamento per il tipo " + mas.getCodice() + " associata al soggetto. Trovata? "
				+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
		movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		
		// Imposto anche il dettaglio del pagamento
		impostaDettaglioDelPagamento(movimento, modalitaPagamentoDipendente);
	}
	/**
	 * Impostazione della modalit&agrave; di pagamento del dipendente.
	 * <br/>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa non &eacute; contanti, occorre preimpostare il campo con il valore della modalit&agrave: di pagamento del dipendente
	 *     presente sull'anagrafica soggetto (se compilata).
	 * </p>
	 * <p>
	 *     Se la modalit&agrave; di pagamento della cassa &eacute; 'Contanti' impostare il campo fisso a Contanti senza lasciare la possibilit&agrave; di modificarlo
	 * </p>
	 * 
	 * @param movimento         il movimento da popolare
	 * @param modPagamentoCassa la modalita di pagamento cassa
	 */
	@Override
	protected void impostaModalitaPagamentoDipendenteDaCassa(Movimento movimento, ModalitaPagamentoCassa modPagamentoCassa) {
		final String methodName = "impostaModalitaPagamentoDipendenteDaCassa";
		
		if(TipoDiCassa.CONTANTI.equals(modPagamentoCassa.getTipoDiCassa())) {
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), "CON");
			log.debug(methodName, "Cassa 'CONTANTI'. Ricercata la modalita' di pagamento di tipo 'CON'. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			return;
		}
		if(TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(modPagamentoCassa.getTipoDiCassa())) {
			log.debug(methodName, "Cassa non contanti. Derivo la modalita' dalla quota ");
			recuperaModPagamentoDaQuota();
			
			ModalitaPagamentoSoggetto mps = model.getMpsDaQuota();
			if(mps == null || mps.getModalitaAccreditoSoggetto() == null) {
				log.debug(methodName, "Modalita' di pagamento non valorizzata correttamente");
				return;
			}
			
			ModalitaAccreditoSoggetto mas = mps.getModalitaAccreditoSoggetto();
			ModalitaPagamentoDipendente modalitaPagamentoDipendente = ComparatorUtils.findByCodice(model.getListaModalitaPagamentoDipendente(), mas.getCodice());
			log.debug(methodName, "Cassa non 'CONTANTI'. Ricercata la modalita' di pagamento per il tipo " + mas.getCodice() + " associata al soggetto. Trovata? "
					+ (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
			movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
			
//			 Imposto anche il dettaglio del pagamento
//			impostaDettaglioDelPagamento(movimento, modalitaPagamentoDipendente);
			return;
		}
	}
	
	/**
	 * Caricamento dei dati dal dettaglio del pagamento nella cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String caricaDettaglioPagamento() {
		final String methodName = "caricaDettaglioPagamento";
		Movimento movimento = ottieniMovimentoDalModel();
		movimento.setDettaglioPagamento("");
		movimento.setBic("");
		movimento.setContoCorrente("");
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = movimento.getModalitaPagamentoDipendente();
		log.debug(methodName, "Modalita selezionata? " + (modalitaPagamentoDipendente == null ? "false" : "true, con uid " + modalitaPagamentoDipendente.getUid()));
		if(modalitaPagamentoDipendente == null) {
			// Non ho la modalita' di pagamento. Esco
			// TODO: svuotare i dati?
			return SUCCESS;
		}
		// Ricerco la modalita' di pagamento
		modalitaPagamentoDipendente = ComparatorUtils.searchByUidEventuallyNull(model.getListaModalitaPagamentoDipendente(), modalitaPagamentoDipendente);
		log.debug(methodName, "Modalita trovata? " + (modalitaPagamentoDipendente != null));
		if(modalitaPagamentoDipendente == null) {
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("modalita' di pagamento del dipendente", "non e' presente tra quelle elencate"));
			return SUCCESS;
		}
		
//		//nel caso del pagamento fatture i dettagli del pagamento sono quelli della mps della quota -- NON VA QUI
		// SIAC-3891: dovrebbe esserci un errore da qualche parte. Non replicato, ma aggiungiamo tutti i controlli del caso
		ModalitaPagamentoSoggetto mps = model.getMpsDaQuota();
		if(mps != null && mps.getModalitaAccreditoSoggetto() != null && modalitaPagamentoDipendente.getModalitaAccreditoSoggetto() != null
				&& modalitaPagamentoDipendente.getModalitaAccreditoSoggetto().getCodice() != null
				&& modalitaPagamentoDipendente.getModalitaAccreditoSoggetto().getCodice().equals(mps.getModalitaAccreditoSoggetto().getCodice())){
			movimento.setDettaglioPagamento(mps.getIban());
			movimento.setBic(mps.getBic());
			movimento.setContoCorrente(mps.getContoCorrente());
		}
		
		
		return SUCCESS;
	}
		
	/**
	 * Recupera la mod di pagamento dalla quota
	 */
	private void recuperaModPagamentoDaQuota() {

		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			//Recupero la mod pag della 1 quota(se sono qui è perchè sono tutte uguali)
			if (ss.getModalitaPagamentoSoggetto()!= null && ss.getModalitaPagamentoSoggetto().getUid()!=0) {
					model.setMpsDaQuota(ss.getModalitaPagamentoSoggetto());
					return;
			}
		}
		
	}
	
	/**
	 * imposta gli importi:
	 * <ol>
	 *     <li>da pagare</li>
	 *     <li>split reverse</li>
	 *     <li>importo totale</li>
	 * </ol>
	 * @param richiestaEconomale la richiesta economale
	 */
	protected void impostaImportiRichiesta(RichiestaEconomale richiestaEconomale) {
		BigDecimal importo = BigDecimal.ZERO;
		BigDecimal importoSplitReverse = BigDecimal.ZERO;

		model.setListaSubdocumentoSpesa(richiestaEconomale.getSubdocumenti());
		
		for(SubdocumentoSpesa ss : richiestaEconomale.getSubdocumenti()) {
			
			if(ss != null && ss.getDocumento() != null && ss.getDocumento().getUid() != 0) {
				importo = importo.add(ss.getImporto());
				importoSplitReverse=importoSplitReverse.add(ss.getImportoSplitReverse()!=null ? ss.getImportoSplitReverse() : BigDecimal.ZERO);
				if (model.getDocumentoSpesa()==null) {
					model.setDocumentoSpesa(ss.getDocumento());
				}
				break;
			}
		}
		model.getDocumentoSpesa().setImporto(importo);
		model.setImportoPagato(model.getRichiestaEconomale().getImporto());
		model.setImportoSplitReverse(importoSplitReverse);		
	}
	
}
