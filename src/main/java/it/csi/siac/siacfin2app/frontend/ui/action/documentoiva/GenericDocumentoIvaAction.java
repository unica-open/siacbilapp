/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.functor.ArithmeticComparator;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.GenericDocumentoIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistroIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;

/**
 * Classe generica di action per il Documento Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/06/2014
 * 
 * @param <M> la tipizzazione del model
 *
 */
public class GenericDocumentoIvaAction<M extends GenericDocumentoIvaModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7580894215440010307L;
	
	/** Serviz&icirc; dell'attivi&agrave; iva */
	@Autowired protected transient AttivitaIvaCapitoloService attivitaIvaCapitoloService;
	/** Serviz&icirc; del documento iva */
	@Autowired private transient DocumentoIvaService documentoIvaService;
	/** Serviz&icirc; del documento */
	@Autowired private transient DocumentoService documentoService;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	/** Serviz&icirc; del registro iva */
	@Autowired protected transient RegistroIvaService registroIvaService;
	/** Serviz&icirc; del soggetto */
	@Autowired private transient SoggettoService soggettoService;
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio) ||
				FaseBilancio.CHIUSO.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Carica i dati del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "caricaSoggetto";
		// Ricerca di dettaglio del documento
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio RicercaSoggettoPerChiave");
			addErrori(response);
			throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaSoggettoPerChiave per soggetto " + 
					model.getSoggetto().getCodiceSoggetto());
		}
		// Imposto i dati del soggetto
		model.setSoggetto(response.getSoggetto());
	}
	
	/**
	 * Carica i classificatori per il Subdocumento Iva.
	 * @param tipoFamigliaDocumento il tipo di famiglia del documento
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricamentoListe(TipoFamigliaDocumento tipoFamigliaDocumento) throws WebServiceInvocationFailureException {
		caricaListaValuta();
		caricaListaTipoRegistrazioneIva();
		caricaListaTipoRegistroIva();
		caricaListaAttivitaIva();
		caricaListaRegistroIva(model.getTipoRegistroIva(), model.getAttivitaIva());
		caricaListaAliquotaIva();
		
		// Intracomunitario (l'attività è caricata nel caricaListaAttivitaIva)
		caricaListaRegistroIvaIntracomunitario(TipoRegistroIva.VENDITE_IVA_IMMEDIATA, model.getAttivitaIvaIntracomunitarioDocumento());
		caricaListaTipoDocumento(tipoFamigliaDocumento);
	}
	
	/**
	 * Ottiene la lista delle valute dal servizio.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaValuta() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaValuta";
		// Cerco la lista in sessione
		List<Valuta> listaValuta = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		if(listaValuta == null) {
			RicercaValuta request = model.creaRequestRicercaValuta();
			logServiceRequest(request);
			RicercaValutaResponse response = documentoIvaService.ricercaValuta(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaValuta");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaValuta");
			}
			listaValuta = response.getListaValuta();
			// Metto in sessione la lista
			sessionHandler.setParametro(BilSessionParameter.LISTA_VALUTA, listaValuta);
		}
		model.setListaValuta(listaValuta);
	}
	
	/**
	 * Ottiene la lista dei tipi di Registrazione Iva dal servizio.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaTipoRegistrazioneIva() throws WebServiceInvocationFailureException {
		caricaListaTipoRegistrazioneIva(Boolean.FALSE, Boolean.FALSE);
	}
	
	/**
	 * Ottiene la lista dei tipi di Registrazione Iva dal servizio.
	 * 
	 * @param entrata se il tipo sia di entrata
	 * @param spesa   se il tipo sia di spesa
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaTipoRegistrazioneIva(Boolean entrata, Boolean spesa) throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoRegistrazioneIva";
		
		List<TipoRegistrazioneIva> listaTipoRegistrazioneIva = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA);
		Boolean filtroUscita = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_USCITA, Boolean.class);
		Boolean filtroEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_ENTRATA, Boolean.class);
		
		if(listaTipoRegistrazioneIva == null || !(entrata.equals(filtroEntrata) && spesa.equals(filtroUscita))) {
			// Non ho la lista, oppure il filtro non è corretto: ricerco nuovamente
			RicercaTipoRegistrazioneIva request = model.creaRequestRicercaTipoRegistrazioneIva(entrata, spesa);
			logServiceRequest(request);
			RicercaTipoRegistrazioneIvaResponse response = documentoIvaService.ricercaTipoRegistrazioneIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaTipoRegistrazioneIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaTipoRegistrazioneIva");
			}
			listaTipoRegistrazioneIva = response.getListaTipoRegistrazioneIva();
			// Metto in sessione oltre alla lista anche il filtro
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA, listaTipoRegistrazioneIva);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_USCITA, spesa);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_REGISTRAZIONE_IVA_FILTRO_ENTRATA, entrata);
		}
		
		model.setListaTipoRegistrazioneIva(listaTipoRegistrazioneIva);
	}
	
	/**
	 * Ottiene la lista dei tipi di registro iva dal servizio.
	 */
	protected void caricaListaTipoRegistroIva() {
		List<TipoRegistroIva> listaTipoRegistroIva = Arrays.asList(TipoRegistroIva.values());
		model.setListaTipoRegistroIva(listaTipoRegistroIva);
	}
	
	/**
	 * Ottiene la lista delle attivitaIva dal servizio.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaAttivitaIva() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaAttivitaIva";
		boolean quota = Boolean.TRUE.equals(model.getTipoSubdocumentoIvaQuota());
		
		log.debug(methodName, "Caricamento per la quota? " + quota);
		// Casi differenti
		if(quota) {
			caricaListaAttivitaIvaPerQuote();
		} else {
			caricaListaAttivitaIvaTotale();
		}
	}
	
	/**
	 * Carica tutte le attivita iva relative al capitolo collegato alla quota, se presenti.
	 * <br>
	 * Si riduce al caso base se non vi &eacute; un capitolo o se il capitolo non ha attivita collegate.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaAttivitaIvaPerQuote() throws WebServiceInvocationFailureException {
		caricaListaAttivitaIvaTotale();
	}
	
	/**
	 * Carica tutte le attivita iva dal servizio.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaAttivitaIvaTotale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaAttivitaIvaTotale";
		// Cerco la lista in sessione
		List<AttivitaIva> listaAttivitaIva = sessionHandler.getParametro(BilSessionParameter.LISTA_ATTIVITA_IVA);
		
		if(listaAttivitaIva == null) {
			RicercaAttivitaIva request = model.creaRequestRicercaAttivitaIva();
			logServiceRequest(request);
			RicercaAttivitaIvaResponse response = documentoIvaService.ricercaAttivitaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaAttivitaIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaAttivitaIva");
			}
			
			listaAttivitaIva = response.getListaAttivitaIva();
			// Metto in sessione la lista
			sessionHandler.setParametro(BilSessionParameter.LISTA_ATTIVITA_IVA, listaAttivitaIva);
		}
		
		model.setListaAttivitaIva(listaAttivitaIva);
		model.setListaAttivitaIvaIntracomunitario(listaAttivitaIva);
		// Inizio a popolare eventualmente l'attivita
		model.popolaAttivitaIvaSePresente();
	}

	
	/**
	 * Ottiene la lista dei registri iva dal servizio.
	 * <br>
	 * L'operazione pu&oacute; essere lanciata se e solo se &eacute; stato selezionato un tipo di registro iva.
	 * <br>
	 * Il tipo registro Iva e l'attivita Iva vengono impostati con i valori di dafult.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaRegistroIva() throws WebServiceInvocationFailureException {
		caricaListaRegistroIva(model.getTipoRegistroIva(), model.getAttivitaIva());
	}
	
	/**
	 * Ottiene la lista dei registri iva dal servizio.
	 * <br>
	 * L'operazione pu&oacute; essere lanciata se e solo se &eacute; stato selezionato un tipo di registro iva.
	 * 
	 * @param tipoRegistroIva il tipo di registro
	 * @param attivitaIva     l'attivita
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaRegistroIva(TipoRegistroIva tipoRegistroIva, AttivitaIva attivitaIva) throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaRegistroIva";
		// Ho il registro Iva. Allora ricerco anche il registro
		if(tipoRegistroIva != null) {
			RicercaRegistroIva request = model.creaRequestRicercaRegistroIva(tipoRegistroIva, attivitaIva);
			logServiceRequest(request);
			RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaRegistroIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaRegistroIva");
			}
			
			model.setListaRegistroIva(response.getListaRegistroIva());
		}
	}
	
	/**
	 * Ottiene la lista dei registri iva dal servizio per l'intracomunitario.
	 * <br>
	 * L'operazione pu&oacute; essere lanciata se e solo se &eacute; stato selezionato un tipo di registro iva.
	 * 
	 * @param tipoRegistroIva il tipo di registro
	 * @param attivitaIva     l'attivita
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaRegistroIvaIntracomunitario(TipoRegistroIva tipoRegistroIva, AttivitaIva attivitaIva) throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaRegistroIvaIntracomunitario";
		// Ho il registro Iva. Allora ricerco anche il registro
		if(tipoRegistroIva != null) {
			RicercaRegistroIva request = model.creaRequestRicercaRegistroIva(tipoRegistroIva, attivitaIva);
			logServiceRequest(request);
			RicercaRegistroIvaResponse response = registroIvaService.ricercaRegistroIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaRegistroIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaRegistroIva");
			}
			
			model.setListaRegistroIvaIntracomunitario(response.getListaRegistroIva());
		}
	}
	
	/**
	 * Controlla se la lista dei Tipo Documento sia presente in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 * @param tipoFamigliaDocumento il tipo di famiglia del documento
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento) throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoDocumento";
		// Cerco la lista in sessione
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		
		if(listaTipoDocumento == null) {
			RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(tipoFamigliaDocumento);
			logServiceRequest(request);
			RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaTipoDocumento");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaTipoDocumento");
			}
			
			listaTipoDocumento = response.getElencoTipiDocumento();
			// Metto in sessione la lista
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaTipoDocumento);
		}
	}
	
	/**
	 * Ottiene la lista delle aliquote iva dal servizio.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio restituisca un'errore
	 */
	protected void caricaListaAliquotaIva() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaAliquotaIva";
		// Cerco la lista in sessione
		List<AliquotaIva> listaAliquotaIva = sessionHandler.getParametro(BilSessionParameter.LISTA_ALIQUOTA_IVA);
		
		if(listaAliquotaIva == null) {
			RicercaAliquotaIva request = model.creaRequestRicercaAliquotaIva();
			logServiceRequest(request);
			RicercaAliquotaIvaResponse response = documentoIvaService.ricercaAliquotaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del servizio RicercaAliquotaIva");
				addErrori(response);
				throw new WebServiceInvocationFailureException("Errore nell'invocazione del servizio RicercaAliquotaIva");
			}
			listaAliquotaIva = response.getListaAliquotaIva();
			// Metto la lista in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_ALIQUOTA_IVA, listaAliquotaIva);
		}
		
		model.setListaAliquotaIva(listaAliquotaIva);
	}
	
	/***
	 * Controlla se siano abilitate le modifiche per un dato registro
	 * @param registroDaControllare il registro da controllare
	 * */
	protected void checkModificheARegistroAbilitate(RegistroIva registroDaControllare) {
		if(registroDaControllare == null){
			return;
		}
		List<RegistroIva> listatipoRegistroIva = sessionHandler.getParametro(BilSessionParameter.LISTA_REGISTRO_IVA);
		RegistroIva registro = ComparatorUtils.searchByUid(listatipoRegistroIva, registroDaControllare);
		checkCondition(!Boolean.TRUE.equals(registro.getFlagBloccato()), ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Inserimento subdocumento iva", "dati Iva non gestibili perche' il registro selezionato e' stato bloccato."));
	}
	
	/**
	 * Valida l'inserimento del movimento iva.
	 * 
	 * @param aliquotaSubdocumentoIva      l'aliquota da validare
	 * @param listaAliquotaSubdocumentoIva la lista delle aliquote
	 * @param validazioneAggiornamento     se effettuare la validazione sull'unicit&agrave; da aggiornamento
	 * @param isNotaCredito                se &eacute; una nota di credito
	 */
	protected void validateMovimentiIva(AliquotaSubdocumentoIva aliquotaSubdocumentoIva, List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIva,
			boolean validazioneAggiornamento, boolean isNotaCredito) {
		if(aliquotaSubdocumentoIva == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Movimento iva"));
			return;
		}
		AliquotaIva aliquotaIva = aliquotaSubdocumentoIva.getAliquotaIva();
		
//		// Non considero il caso dell'uguaglianza. Ma al momento non mi serve
//		String errorString = (ArithmeticComparator.GREATER.equals(operator) || ArithmeticComparator.GREATER_OR_EQUAL.equals(operator)) ?
//				"negativo" : "positivo";
		
		checkNotNullNorInvalidUid(aliquotaIva, "Aliquota iva");
		checkNotNull(aliquotaSubdocumentoIva.getImponibile(), "Imponibile");
		checkNotNull(aliquotaSubdocumentoIva.getImposta(), "Imposta");
		checkNotNull(aliquotaSubdocumentoIva.getTotale(), "Totale");
		
		if(isNotaCredito){
			checkCondition(aliquotaSubdocumentoIva.getImponibile().compareTo(BigDecimal.ZERO)<=0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("Imponibile", ": deve essere negativo"));
			checkCondition(aliquotaSubdocumentoIva.getImposta().compareTo(BigDecimal.ZERO)<=0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("Imposta", ": deve essere negativo"));
			checkCondition(aliquotaSubdocumentoIva.getTotale().compareTo(BigDecimal.ZERO)<=0 , ErroreCore.FORMATO_NON_VALIDO.getErrore("Totale", ": deve essere negativo"));
		}
		log.debug("validateMovimentiIva", "possato il check note credito");
		checkCondition(!hasErrori() &&
			ArithmeticComparator.EQUAL.performOperation(
				aliquotaSubdocumentoIva.getTotale().subtract(aliquotaSubdocumentoIva.getImponibile().add(aliquotaSubdocumentoIva.getImposta())),
				BigDecimal.ZERO),
			ErroreFin.DATI_INCONSISTENTI_IMPOSSIBILE_AGGIORNARE_LA_TABELLA_DEI_MOVIMENTI_IVA.getErrore());
		log.debug("validateMovimentiIva", "possato il check del totale");
		
		if(!hasErrori()) {
			Integer riga = model.getRiga();
			// Controllo di non inserire più di un movimento con la stessa aliquota
			for(int i = 0; i < listaAliquotaSubdocumentoIva.size(); i++) {
				AliquotaSubdocumentoIva asi = listaAliquotaSubdocumentoIva.get(i);
				if(asi.getAliquotaIva().getUid() == aliquotaIva.getUid() && (!validazioneAggiornamento || i != riga.intValue())) {
					// Ho trovato un'incongruenza: segnalo ed esco
					log.debug("validateMovimentiIva", "trovato errore per riga: " + i + " . validazioneAggiornamento: " + validazioneAggiornamento 
							+ "asi.getAliquotaIva().getUid(): " + asi.getAliquotaIva().getUid() + " aliquotaIva.getUid(): " + aliquotaIva.getUid());
					addErrore(ErroreFin.DATI_INCONSISTENTI_IMPOSSIBILE_AGGIORNARE_LA_TABELLA_DEI_MOVIMENTI_IVA.getErrore());
					break;
				}
			}
		}
	}
	
}
