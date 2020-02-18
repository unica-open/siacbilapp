/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggetta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSommaNonSoggettaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Ritenute.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaRitenuteAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8827018333452450449L;

	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;

	/**
	 * Restituisce la lista delle ritenute relative al documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaRitenute() {
		//imposto i flag necessari
		model.forzaCalcoloImportoEsenteProposto();
		model.impostaFlagEditabilitaRitenute();
		return SUCCESS;
	}
	
	/**
	 * Inizio per l'inserimento della ritenuta.
	 * <br>
	 * Pulisce i campi.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserisciOnere() {
		// Pulisco i dati relativi all'onere
		model.setDettaglioOnere(null);
		model.setAttivitaOnere(null);
		return SUCCESS;
	}
	
	
	/**
	 * Inserisce l'Onere nella lista delle ritenute.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciOnere() {
		final String methodName = "inserisciOnere";
		try {
			// Validazione dei dati
			validaInserisciOnere();
		} catch(ParamValidationException e) {
			//vi sono dei dati non  corretti. Loggo ma vado comunque avanti
			log.info(methodName, "Errore nella validazione dei dati, dato non presente: " + e.getMessage());
		}
		
		if(hasErrori()) {
			//nel model ho degli errori da mostrare
			log.info(methodName, "Errore nella validazione del dato");
			return SUCCESS;
		}
		
		// Inserisco l'onere
		InserisciOnereSpesa request = model.creaRequestInserisciOnereSpesa();
		logServiceRequest(request);
		InserisciOnereSpesaResponse response = documentoSpesaService.inserisciOnereSpesa(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Errori nell'invocazione
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		// Ricalcolo i classificatori del dettaglio onere
		DettaglioOnere dettaglioOnere = response.getDettaglioOnere();
		//ricaldolo i classificatori
		ricalcolaClassificatoriDettaglioOnere(dettaglioOnere);
		
		// Ricalcolo gli oneri
		model.getListaDettaglioOnere().add(dettaglioOnere);
		
		// Ricalcolo il proposto se d'uopo
		if(isNaturaOnereEsenzione(dettaglioOnere, BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant())) {
			// si tratta di esenzione, imposto il dato per il calcolo dell'importo
			model.forzaCalcoloImportoEsenteProposto();
		}
		//ricalcolo gli importi
		model.calcoloImporti();
		// Appongo il messaggio di successo
		impostaInformazioneSuccesso();
		// Pulisco
		cleanOnere();
		
		return SUCCESS;
	}
	
	/**
	 * Controlla se il dettaglio onere fornito &eacute; della natura fornita.
	 * 
	 * @param dettaglioOnere il dettaglio
	 * @param codiceNatura   il codice della natura
	 *
	 * @return <code>true</code> se il dettaglio ha una natura con dato codice; <code>false</code> altrimenti
	 */
	private boolean isNaturaOnereEsenzione(DettaglioOnere dettaglioOnere, String codiceNatura) {
		return dettaglioOnere != null && dettaglioOnere.getTipoOnere() != null && dettaglioOnere.getTipoOnere().getNaturaOnere() != null
				&& codiceNatura != null && codiceNatura.equals(dettaglioOnere.getTipoOnere().getNaturaOnere().getCodice());
	}
	
	/**
	 * Ricalcola le fasi iniziali per l'aggiornamento dell'onere.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornaOnere() {
		// Ottengo il dettaglio onere dalla lista
		DettaglioOnere don = ComparatorUtils.searchByUid(model.getListaDettaglioOnere(), model.getDettaglioOnere());
		// Ottengo un clone
		don = ReflectionUtil.deepClone(don);
		
		// Carico le liste per l'aggiornamento
		TipoOnere tipoOnere = don.getTipoOnere();
		
		//carico le liste necessarie all'aggiornamento
		obtainListaAttivitaOnere(tipoOnere);
		obtainListaCausale770(tipoOnere);
		obtainListaTipoSommaNonSoggetta(tipoOnere);
		
		// Imposto i dati presenti nel dettaglio, sovrascrivendo quelli eventualmente presenti
		model.setAttivitaOnere(don.getAttivitaOnere());
		model.setCausale770(don.getCausale770());
		model.setTipoSommaNonSoggetta(don.getCodiceSommaNonSoggetta());
		
		// Imposto il clone nel model
		model.setDettaglioOnere(don);
		
		// Ritorno la pagina creata
		return SUCCESS;
	}

	/**
	 * Aggiorna l'Onere della lista delle ritenute.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaOnere() {
		final String methodName = "aggiornaOnere";
		try {
			validaInserisciOnere();
		} catch(ParamValidationException e) {
			//la validazione non e' andata a buon fine. Loggo questo fatto.
			log.info(methodName, "Errore nella validazione dei dati, dato non presente: " + e.getMessage());
		}
		
		if(hasErrori()) {
			//gestisco gli errori eventualmente presenti
			log.info(methodName, "Errore nella validazione del dato");
			return SUCCESS;
		}
		
		// Aggiorno l'onere
		AggiornaOnereSpesa request = model.creaRequestAggiornaOnereSpesa();
		logServiceRequest(request);
		AggiornaOnereSpesaResponse response = documentoSpesaService.aggiornaOnereSpesa(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		// Ricalcolo i classificatori dell'onere
		DettaglioOnere dettaglioOnere = response.getDettaglioOnere();
		ricalcolaClassificatoriDettaglioOnere(dettaglioOnere);
		
		// Ricalcolo gli oneri
		List<DettaglioOnere> lista = model.getListaDettaglioOnere();
		int index = ComparatorUtils.getIndexByUid(lista, dettaglioOnere);
		lista.set(index, dettaglioOnere);
		
		// Ricalcolo il proposto se d'uopo
		if(isNaturaOnereEsenzione(dettaglioOnere, BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant())) {
			model.forzaCalcoloImportoEsenteProposto();
		}
		//ricalcolo gli importi
		model.calcoloImporti();
		// Appongo il messaggio di successo
		impostaInformazioneSuccesso();
		// Pulisco i dati
		cleanOnere();
		
		return SUCCESS;
	}
	
	/**
	 * Elimina l'Onere dalla lista delle ritenute.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaOnere() {
		final String methodName = "eliminaOnere";
		
		try {
			validaEliminaOnere();
		} catch(ParamValidationException e) {
			// Posso far scomparire l'errore: non ci sono problemi. Loggo soltanto che qualcosa è andato male
			log.debug(methodName, "Errore nella validazione: " + e.getMessage());
		}
		
		if(hasErrori()) {
			//mostro gli errori presenti a video
			log.info(methodName, "Errore nella validazione del dato");
			return SUCCESS;
		}
		
		// Elimino l'onere
		EliminaOnereSpesa request = model.creaRequestEliminaOnereSpesa();
		logServiceRequest(request);
		EliminaOnereSpesaResponse response = documentoSpesaService.eliminaOnereSpesa(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Errore nella validazione del dato
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		// Ricalcolo gli oneri
		List<DettaglioOnere> lista = model.getListaDettaglioOnere();
		int index = ComparatorUtils.getIndexByUid(lista, model.getDettaglioOnere());
		DettaglioOnere dettaglioOnere = lista.remove(index);
		
		// Ricalcolo il proposto se d'uopo
		if(isNaturaOnereEsenzione(dettaglioOnere, BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant())) {
			model.forzaCalcoloImportoEsenteProposto();
		}
		//ricalcolo gli importi
		model.calcoloImporti();
		// Appongo il messaggio di successo
		impostaInformazioneSuccesso();
		// Pulisco i dati
		cleanOnere();
		return SUCCESS;
	}
	
	/**
	 * Salva la testa delle ritenute.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String salvaRitenute() {
		final String methodName = "salvaRitenute";
		// Aggiorno il documento
		AggiornaDocumentoDiSpesa request = model.creaRequestAggiornaDocumentoDiSpesa();
		logServiceRequest(request);
		AggiornaDocumentoDiSpesaResponse response = documentoSpesaService.aggiornaDocumentoDiSpesa(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Errore nell'invocazione del servizio
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		model.setDocumento(response.getDocumentoSpesa());
		
		//imposto un messaggio all'utente che indichi che l'operazione e' avvenuta con successo
		impostaInformazioneSuccesso();
		model.setImportoEsenteProposto(model.getDocumento().getRitenuteDocumento().getImportoEsente());
		
		return SUCCESS;
	}
	
	/**
	 * Pulisce i dati del model relativi all'onere
	 */
	private void cleanOnere() {
		//pulisco i dati nel model
		model.setDettaglioOnere(null);
		model.setAttivitaOnere(null);
		model.setCausale770(null);
	}
	
	/**
	 * Ricalcola i classificatori all'interno del dettaglio dell'onere.
	 * 
	 * @param dettaglioOnere il dettaglio da ripopolare
	 */
	private void ricalcolaClassificatoriDettaglioOnere(DettaglioOnere dettaglioOnere) {
		// Ottengo le liste dalla sessione
		List<TipoOnere> listaTipoOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ONERE);
		List<AttivitaOnere> listaAttivitaOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_ATTIVITA_ONERE);
		List<Causale770> listaCausale770 = sessionHandler.getParametro(BilSessionParameter.LISTA_CAUSALE_770);
		
		// Ottengo i classificatori con uid dall'onere
		TipoOnere tipoOnere = dettaglioOnere.getTipoOnere();
		Causale770 causale770 = dettaglioOnere.getCausale770();
		AttivitaOnere attivitaOnere = dettaglioOnere.getAttivitaOnere();
		
		// Ottengo i corrispondenti dalle liste
		tipoOnere = ComparatorUtils.searchByUid(listaTipoOnere, tipoOnere);
		causale770 = ComparatorUtils.searchByUid(listaCausale770, causale770);
		attivitaOnere = ComparatorUtils.searchByUid(listaAttivitaOnere, attivitaOnere);
		
		// Imposto i classificatori popolati
		dettaglioOnere.setTipoOnere(tipoOnere);
		dettaglioOnere.setCausale770(causale770);
		dettaglioOnere.setAttivitaOnere(attivitaOnere);
	}

	/**
	 * Validazione dell'onere per l'inserimento.
	 * 
	 * @throws ParamValidationException nel caso in cui l'onere o il tipo non siano inizializzati
	 */
	private void validaInserisciOnere() {
		DettaglioOnere dettaglioOnere = model.getDettaglioOnere();
		//devo aver indicato un onere
		checkCondition(dettaglioOnere != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Onere"), true);
		
		//l'onere deve avere un tipo onere con una natura onere
		TipoOnere tipoOnere = dettaglioOnere.getTipoOnere();
		checkCondition(tipoOnere != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice Tributo"), true);
		checkNotNullNorInvalidUid(tipoOnere.getNaturaOnere(), "Natura onere");		
		
		//prendo i dati del tipo onere dalla lista presente in sessione
		tipoOnere = caricaTipoOnereDaSessione(tipoOnere);
		
		//l'importo imponibile e' obbligatorio
		checkNotNull(dettaglioOnere.getImportoImponibile(), "Imponibile");
		// SIAC-6099: l'imponibile non deve essere superiore all'importo del documento
		checkCondition(dettaglioOnere.getImportoImponibile() == null || dettaglioOnere.getImportoImponibile().compareTo(model.getDocumento().getImportoNetto()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("imponibile", "non puo' essere maggiore dell'importo del documento ("
					+ FormatUtils.formatCurrency(model.getDocumento().getImportoNetto()) + ")"));
		
		checkNotNullNorInvalidUid(tipoOnere, "Codice Tributo",true);
		
			
		
		BigDecimal aliquotaSoggetto = tipoOnere.getAliquotaCaricoSoggettoNotNull();
		BigDecimal aliquotaEnte = tipoOnere.getAliquotaCaricoEnteNotNull();
		// Controllo che le aliquote siano imppostate
		checkCondition(aliquotaSoggetto.signum() == 0 || dettaglioOnere.getImportoCaricoSoggetto() != null, 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo a carico del soggetto"));
		checkCondition(aliquotaEnte.signum() == 0 || dettaglioOnere.getImportoCaricoEnte() != null, 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo a carico ente"));
		
		// Controllo sull'attivita'
		boolean a = model.getAttivitaOnere() != null && model.getAttivitaOnere().getUid() != 0;
		boolean b = dettaglioOnere.getAttivitaInizio() != null;
		boolean c = dettaglioOnere.getAttivitaFine() != null;
		checkCondition((a && b && c) || (!a && !b && !c), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Attivita'", ": Attivita', dal e al devono essere tutti valorizzati o non valorizzati"));
		//controllo i totali delle quote
		checkCoerenzaTotaleQuote(dettaglioOnere);
		
		//controlli aggiunti in caso di splirt reverse
		if (tipoOnere.getNaturaOnere()!=null && tipoOnere.getNaturaOnere().getCodice().equals(model.getCodiceSplitReverse())) {
			model.getDettaglioOnere().setTipoOnere(tipoOnere);
			checkCoerenzaImportiSplitReverse(true, dettaglioOnere.getUid() != 0);
		}
	}
	
	//importoDoc+arrotondamento+totaleOneriRC >= totaleQuote
	private void checkCoerenzaTotaleQuote(DettaglioOnere dettaglioOnere) {
		
		if (dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse() == null 
				|| !TipoIvaSplitReverse.REVERSE_CHANGE.equals(dettaglioOnere.getTipoOnere().getTipoIvaSplitReverse())
				|| dettaglioOnere.getImportoCaricoSoggetto() == null){
			//effettuo i controlli solo se aggiorno/inserisco un onere con tipoIvaSplitReverse = RC
			return;
		}
		BigDecimal totaleAttualeOneriRC = model.ricalcolaTotaleImponibileOneriRC();
		for(DettaglioOnere detOnere : model.getListaDettaglioOnere()){
			if(detOnere.getUid() == dettaglioOnere.getUid()){
				//se sono in aggiornamento (ho già l'onere nella lista), sottraggo dal totale il vecchio importo dell'onere  prima di aggiungere il nuovo
				totaleAttualeOneriRC = totaleAttualeOneriRC.subtract(detOnere.getImportoCaricoSoggetto());
			}
		}
		totaleAttualeOneriRC = totaleAttualeOneriRC.add(dettaglioOnere.getImportoCaricoSoggetto());
		BigDecimal totaleDaVerificare = model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()).add(totaleAttualeOneriRC).subtract(model.getTotaleQuote());
		//importoDoc+arrotondamento+totaleOneriRC >= totaleQuote
		checkCondition(totaleDaVerificare.compareTo(BigDecimal.ZERO) >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("imposta", ": il totale di importo documento, arrotondamento e oneri di tipo reverse/change deve essere maggiore o uguale al totale delle quote"));
	}
	
	//importoDoc+arrotondamento+totaleOneriRC >= totaleQuote
	private void checkCoerenzaTotaleQuotePerEliminazione(DettaglioOnere dettaglioOnere) {
		DettaglioOnere don = dettaglioOnere;
		for(DettaglioOnere detOnere : model.getListaDettaglioOnere()){
			if(detOnere.getUid() == dettaglioOnere.getUid()){
				don = detOnere;
				break;
			}
		}
		if (don.getTipoOnere().getTipoIvaSplitReverse() == null 
				|| !TipoIvaSplitReverse.REVERSE_CHANGE.equals(don.getTipoOnere().getTipoIvaSplitReverse())
				|| don.getImportoCaricoSoggetto() == null){
			//effettuo i controlli solo se elimino un onere con tipoIvaSplitReverse = RC
			return;
		}
		BigDecimal totaleAttualeOneriRC = model.ricalcolaTotaleImponibileOneriRC();
		totaleAttualeOneriRC = totaleAttualeOneriRC.subtract(don.getImportoCaricoSoggetto());
		//importoDoc+arrotondamento+totaleOneriRC >= totaleQuote
		BigDecimal totaleDaVerificare = model.getDocumento().getImporto().add(model.getDocumento().getArrotondamento()).add(totaleAttualeOneriRC).subtract(model.getTotaleQuote());
		checkCondition(totaleDaVerificare.compareTo(BigDecimal.ZERO) >= 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale di importo documento, arrotondamento e oneri di tipo reverse/change deve essere maggiore o uguale al totale delle quote"));
	}

	/**
	 * Ottiene la lista dei tipi di onere dalla sessione e cerca il corrispondente.
	 * 
	 * @param tipoOnere il tipo onere da ricercare
	 * 
	 * @return il tipo onere presente nella lista caricata dal servizio
	 */
	private TipoOnere caricaTipoOnereDaSessione(TipoOnere tipoOnere) {
		List<TipoOnere> listaTipoOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ONERE);
		TipoOnere tipo = ComparatorUtils.searchByUid(listaTipoOnere, tipoOnere);
		//non voglio ritornare null
		return tipo != null ? tipo : tipoOnere;
	}
	
	/**
	 * Validazione per l'eliminazione dell'onere.
	 */
	private void validaEliminaOnere() {
		Integer rigaDaEliminare = model.getRigaDaEliminare();
		checkCondition(rigaDaEliminare != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Riga dettaglio onere"), true);
		
		// Ottiene il dettaglio dalla lista
		DettaglioOnere dettaglioOnere = model.getListaDettaglioOnere().get(rigaDaEliminare.intValue());
		//il dettaglio onere e' obbligatorio
		checkCondition(dettaglioOnere != null && dettaglioOnere.getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Dettaglio onere"), true);
		//controllo gli importi
		checkCoerenzaTotaleQuotePerEliminazione(dettaglioOnere);
		model.setDettaglioOnere(dettaglioOnere);
	}
	
	/**
	 * Ottiene la lista delle Attivita Onere relative al tipo di onere fornito.
	 * 
	 * @param tipoOnere il tipo di onere tramite cui filtrare le Attivita
	 */
	private void obtainListaAttivitaOnere(TipoOnere tipoOnere) {
		//chiamo il servizio di ricerca dell'attivita' onere
		RicercaAttivitaOnere request = model.creaRequestRicercaAttivitaOnere(tipoOnere);
		RicercaAttivitaOnereResponse response = documentoService.ricercaAttivitaOnere(request);
		if(!response.hasErrori()) {
			//nel caso in cui il servizio sia andato a buon fine, imposto nel model i dati
			logServiceResponse(response);
			model.setListaAttivitaOnere(response.getElencoAttivitaOnere());
		}
	}
	
	/**
	 * Ottiene la lista delle Causali 770 relative al tipo di onere fornito.
	 * 
	 * @param tipoOnere il tipo di onere tramite cui filtrare le Causali
	 */
	private void obtainListaCausale770(TipoOnere tipoOnere) {
		// chiamo il servizio di ricerca causale
		RicercaCausale770 request = model.creaRequestRicercaCausale770(tipoOnere);
		RicercaCausale770Response response = documentoService.ricercaCausale770(request);
		if(!response.hasErrori()) {
			//nel caso in cui il servizio sia andato a buon fine, imposto nel model i dati
			logServiceResponse(response);
			model.setListaCausale770(response.getElencoCausali());
		}
	}
	
	
	private void obtainListaTipoSommaNonSoggetta(TipoOnere tipoOnere) {
		// chiamo il servizio di ricerca somma non soggetta
		RicercaSommaNonSoggetta request = model.creaRequestRicercaSommaNonSoggetta(tipoOnere);
		RicercaSommaNonSoggettaResponse response = documentoService.ricercaSommaNonSoggetta(request);
		if(!response.hasErrori()) {
			//nel caso in cui il servizio sia andato a buon fine, imposto nel model i dati
			logServiceResponse(response);
			model.setListaSommeNonSoggette(response.getElencoCodiciSommaNonSoggetta());
		}
	}
	
	/**
	 * Caricamento dei dati della fattura FEL se presente
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaFatturaFEL() {
		final String methodName = "caricaFatturaFEL";
		if(model.getFatturaFEL() != null && model.getFatturaFEL().getIdFattura() != null) {
			// Ho gia' la fattura valorizzata
			log.debug(methodName, "Fattura FEL gia' valorizzata");
			return SUCCESS;
		}
		
		//ricerco i dati della fattura elettronica
		RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(request);
		RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			//si sono verificati errori. esco.
			addErrori(response);
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			return INPUT;
		}
		
		model.setFatturaFEL(response.getFatturaFEL());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #caricaFatturaFEL()}.
	 */
	public void validateCaricaFatturaFEL() {
		checkCondition(model.getDocumento().getFatturaFEL() != null && model.getDocumento().getFatturaFEL().getIdFattura() != null,
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("fattura FEL"));
	}
}
