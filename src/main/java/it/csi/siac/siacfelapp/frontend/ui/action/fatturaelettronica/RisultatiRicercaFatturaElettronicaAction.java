/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.action.fatturaelettronica;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica.RisultatiRicercaFatturaElettronicaModel;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;
import it.csi.siac.sirfelser.model.TipoDocumentoFEL;
/**
 * Classe di action per i risultati di ricerca della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaFatturaElettronicaAction extends GenericBilancioAction<RisultatiRicercaFatturaElettronicaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4506958236547063575L;
	
	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;
	@Autowired private transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		// Imposto la posizione die start
		Integer posizioneStart = ottieniPosizioneStartDaSessione();
		log.debug(methodName, "Posizione start: " + posizioneStart);
		model.setSavedDisplayStart(posizioneStart);
		
		caricaListaClasseSoggetto();
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
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ListeGestioneSoggetto.class, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Leggo i messaggi delle azioni precedenti
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		caricaFaseBilancio();
		return SUCCESS;
	}
	
	/**
	 * Caricamento della fase di bilancio.
	 */
	private void caricaFaseBilancio() {
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		
		if(faseBilancio != null) {
			// Ho gia' la fase di bilancio in sessione
			return;
		}
		
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		
		faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}

	/**
	 * Importazione della prima nota.
	 * Con la SIAC-7571 sono stati aggiunti dei controlli sulla presenza o meno dei dati in sessione
	 * ed il controllo per il reindirizzamento ai documenti di entrata.
	 * Con la SIAC-8273 e' stato aggiunto un controllo per adeguare l'importo totale lordo (se nullo)
	 * utilizzando l'importo totale netto associato alla fattura
	 *
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String importa() {
		final String methodName = "importa";
		
		//SIAC-7571
		//ricerco i dati solo se non li ho in sessione
		FatturaFEL fatturaFEL = null;
		if(!sessionHandler.containsKey(BilSessionParameter.FATTURA_FEL)) {
			
			// Ricerco il dettaglio della fattura
			RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
			logServiceRequest(request);
			RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioFatturaElettronica.class, response));
				addErrori(response);
				return INPUT;
			}
			
			fatturaFEL = response.getFatturaFEL();
			
			try {
				checkFatturaImportabile(fatturaFEL);
			} catch(FrontEndBusinessException febe) {
				log.info(methodName, "Fattura non importabile: " + febe.getMessage());
				return INPUT;
			}
			
			// Impostazione dei dati necessarii in sessione
			sessionHandler.setParametro(BilSessionParameter.FATTURA_FEL, fatturaFEL);
			
		} else {
			//se ce l'ho riprendo dalla sessione per il controllo sull'importo
			fatturaFEL = sessionHandler.getParametro(BilSessionParameter.FATTURA_FEL);
		}
		
		//SIAC-7571
		//ricerco i dati solo se non li ho in sessione
		if(!sessionHandler.containsKey(BilSessionParameter.SOGGETTO)) {
			// Impostazione dei dati necessarii in sessione
			Soggetto soggetto = model.getSoggetto();			
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
		}
		
		//SIAC-8273 il dato e' facolatatvo ma se l'utente lo desidera passiamo il netto come importo totale
		if(fatturaFEL.getImportoTotaleDocumento() == null && StringUtils.isNotBlank(model.getAdeguaImportoNonValorizzato())) {
			fatturaFEL.setImportoTotaleDocumento(fatturaFEL.getImportoTotaleNetto());
		}
		
		//SIAC-7571 se ha importo minore di zero e ho la risposta da parte dell'utente redirigo la destinazione
		if(TipoDocumentoFEL.FATTURA.getCodice().equals(fatturaFEL.getTipoDocumentoFEL().getCodice())
				&& controlloImportoTotaleLordoFattura(fatturaFEL) 
				&& !StringUtils.isBlank(model.getSceltaUtente())
				&& "FSN".equals(model.getSceltaUtente())) {
			// metto la scelta in sessione per mostrara all'utente in fase di inserimento
			sessionHandler.setParametro(BilSessionParameter.TIPO_DOCUMENTO_IMPORTA_FATTURA, model.getSceltaUtente());
			return "fatturaAttiva";
		}

		//SIAC-7571 se ha importo minore di zero e ho la risposta da parte dell'utente redirigo la destinazione
		if(TipoDocumentoFEL.FATTURA.getCodice().equals(fatturaFEL.getTipoDocumentoFEL().getCodice())
				&& controlloImportoTotaleLordoFattura(fatturaFEL) 
				&& !StringUtils.isBlank(model.getSceltaUtente())
				&& "NCD".equals(model.getSceltaUtente())) {
			// metto la scelta in sessione per mostrara all'utente in fase di inserimento
			sessionHandler.setParametro(BilSessionParameter.TIPO_DOCUMENTO_IMPORTA_FATTURA, model.getSceltaUtente());
			return "notaCredito";
		}
		
		// Redirezione verso inserimento documento spesa
		return SUCCESS;
	}
	
	/**
	 * SIAC-8273
	 * Importazione fatture.
	 * Si addatta il metodo asincrono per gestire piu' chiamate ajax, questo metodo
	 * si puo considerare una fase preliminare in cui viene ricercata la fattura
	 * con dei controlli preliminari, se la fattura e' valida la salviamo in sessione
	 * e controlliamo l'importo totale del documento, in caso di valore null, provvediamo ad
	 * informare l'utente per consentirne la corretta gestione.
	 * 
	 * @return un JSON
	 */
	public String cercaFatturaAsincrono() {
		final String methodName = "importa";
		
		//SIAC-7571
		validateImporta();
		if(model.getErrori().size() > 0 && !model.getErrori().isEmpty()) {
			return INPUT;
		}
		
		// Ricerco il dettaglio della fattura
		RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(request);
		RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioFatturaElettronica.class, response));
			addErrori(response);
			return INPUT;
		}
		
		FatturaFEL fatturaFEL = response.getFatturaFEL();
		
		try {
			checkFatturaImportabile(fatturaFEL);
		} catch(FrontEndBusinessException febe) {
			log.info(methodName, "Fattura non importabile: " + febe.getMessage());
			return INPUT;
		}
		
		Soggetto soggetto = model.getSoggetto();
		// Impostazione dei dati necessarii in sessione
		sessionHandler.setParametro(BilSessionParameter.FATTURA_FEL, fatturaFEL);
		sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
		
		//SIAC-8273 il dato e' facolatatvo ma se l'utente lo desidera passiamo il netto come importo totale
		if(fatturaFEL.getImportoTotaleDocumento() == null && StringUtils.isBlank(model.getAdeguaImportoNonValorizzato())) {
			addMessaggio(ErroreBil.FATTURA_CON_IMPORTO_NON_VALORIZZATO.getErrore());
			return "askImporto";
		}
		
		// Redirezione verso inserimento documento spesa
		return SUCCESS;
	}
	
	/**
	 * SIAC-7571
	 * Importazione della prima nota.
	 * Il metodo riportato riporta il metodo del vecchio giro con
	 * la validazione in testa ed il controllo sulle fatture della segnalazione
	 * Si utilizza questa funzionalita' per permettere la scelta da parte dell'utente
	 * e consentirne la redirezione alle pagine
	 * 
	 * @return un JSON
	 */
	public String importaAsincrono() {
		final String methodName = "importa";
		
		//SIAC-7571
		//ricerco i dati solo se non li ho in sessione
		FatturaFEL fatturaFEL = null;
		if(!sessionHandler.containsKey(BilSessionParameter.FATTURA_FEL)) {
		
			//SIAC-7571
			validateImporta();
			if(model.getErrori().size() > 0 && !model.getErrori().isEmpty()) {
				return INPUT;
			}
			
			// Ricerco il dettaglio della fattura
			RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
			logServiceRequest(request);
			RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioFatturaElettronica.class, response));
				addErrori(response);
				return INPUT;
			}
			
			fatturaFEL = response.getFatturaFEL();
			
			try {
				checkFatturaImportabile(fatturaFEL);
			} catch(FrontEndBusinessException febe) {
				log.info(methodName, "Fattura non importabile: " + febe.getMessage());
				return INPUT;
			}
			
			Soggetto soggetto = model.getSoggetto();
			// Impostazione dei dati necessarii in sessione
			sessionHandler.setParametro(BilSessionParameter.FATTURA_FEL, fatturaFEL);
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
		
		} else {
			//se ce l'ho riprendo dalla sessione per il controllo sull'importo
			fatturaFEL = sessionHandler.getParametro(BilSessionParameter.FATTURA_FEL);
		}
		
		// SIAC-7571 devo ottenre una validazione da parte dell'utente
		if(TipoDocumentoFEL.FATTURA.getCodice().equals(fatturaFEL.getTipoDocumentoFEL().getCodice())
				&& (controlloImportoTotaleLordoFattura(fatturaFEL) || controlloSuNettoImportabile(fatturaFEL))
				&& StringUtils.isBlank(model.getSceltaUtente())) {
			addMessaggio(ErroreBil.FATTURA_CON_IMPORTO_NEGATIVO.getErrore());
			return "askDestination";
		}
		
		// Redirezione verso inserimento documento spesa
		return SUCCESS;
	}
	
	/**
	 * SIAC-8273
	 * Controllo sull'importo totale del documento.
	 * @param fatturaFEL
	 * @return
	 */
	private boolean controlloImportoTotaleLordoFattura(FatturaFEL fatturaFEL) {
		//SIAC-8004
		return fatturaFEL.getImportoTotaleDocumento() != null
				//SIAC-7759 controllo l'importo lordo anziche' il netto
				&& BigDecimal.ZERO.compareTo(fatturaFEL.getImportoTotaleDocumento()) > 0; 
	}
	
	/**
	 * SIAC-8273
	 * Controllo l'importo netto in caso non avessi il totale 
	 * valorizzato e fosse richiesto l'adeguamento dell'importo.
	 * @param fatturaFEL
	 * @return
	 */
	private boolean controlloSuNettoImportabile(FatturaFEL fatturaFEL) {
		return fatturaFEL.getImportoTotaleDocumento() == null && StringUtils.isNotBlank(model.getAdeguaImportoNonValorizzato())
				&& controlloImportoTotaleNettoFattura(fatturaFEL);
	}
	
	/**
	 * SIAC-8273
	 * Controllo il netto per gestire una redirezione 
	 * da parte dell'utente in caso di importo negativo.
	 * @param fatturaFEL
	 * @return
	 */
	private boolean controlloImportoTotaleNettoFattura(FatturaFEL fatturaFEL) {
		return fatturaFEL.getImportoTotaleNetto() != null
				&& BigDecimal.ZERO.compareTo(fatturaFEL.getImportoTotaleNetto()) > 0;
	}
	
	/**
	 * Controlla se la fattura elettronica sia importabile.
	 * <br/>
	 * Una fattura &eacute; importabile se:
	 * <ul>
	 *     <li>&eacute; in stato <code>DA IMPORTARE</code></li>
	 *     <li>&eacute; in stato <code>SOSPESO</code></li>
	 * </ul>
	 * Inoltre non deve avere un importo negativo, sia questo un dettaglio (entit&agrave; <code>Dettaglio Pagamento di FEL</code>)
	 * o il totale documento (entit&agrave; <code>Fattura Elettronica di FEL</code>), se cos&iacute; fosse viene segnalato il messaggio
	 * <code>&lt;FIN_ERR_0197, Operazione non compatibile, 'Operazione', 'la fattura presenta valori negativi'&lt;</code>.
	 * 
	 * @param fatturaFEL la fattura da controllare.
	 * @throws FrontEndBusinessException in caso di errore di business nei controlli
	 */
	private void checkFatturaImportabile(FatturaFEL fatturaFEL) throws FrontEndBusinessException {
		if(!StatoAcquisizioneFEL.DA_ACQUISIRE.equals(fatturaFEL.getStatoAcquisizioneFEL()) && ! StatoAcquisizioneFEL.SOSPESA.equals(fatturaFEL.getStatoAcquisizioneFEL())) {
			Errore errore = ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Importazione", "stato incongruente");
			addErrore(errore);
			throw new FrontEndBusinessException(errore.getTesto());
		}
		
		//SIAC-8273 non e' piu' una condizione di errore
		//SIAC-8004 controllo che l'importo non sia nullo altrimenti blocco l'importazione
//		if(fatturaFEL.getImportoTotaleDocumento() == null) {
//			Errore errore = ErroreBil.FATTURA_CON_IMPORTO_NON_VALORIZZATO.getErrore();
//			addErrore(errore);
//			throw new FrontEndBusinessException(errore.getTesto());
//		}
		
		//CR 3469 - ammetto anche valori negativi, quando le importo poi considero il valore assoluto
//		checkImportoFattura(fatturaFEL.getImportoTotaleDocumento());
//		for(PagamentoFEL pagamentoFEL : fatturaFEL.getPagamenti()) {
//			for(DettaglioPagamentoFEL dettaglioPagamentoFEL : pagamentoFEL.getElencoDettagliPagamento()) {
//				checkImportoFattura(dettaglioPagamentoFEL.getImportoPagamento());
//			}
//		}
	}

//	/**
//	 * Importo deve essere non negativo.
//	 * 
//	 * @param importo l'importo da controllare
//	 * @throws FrontEndBusinessException nel caso in cui l'importo sia negativo
//	 */
//	private void checkImportoFattura(BigDecimal importo) throws FrontEndBusinessException {
//		if(importo != null && importo.signum() < 0) {
//			Errore errore = ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Importazione", "la fattura presenta valori negativi");
//			addErrore(errore);
//			throw new FrontEndBusinessException(errore.getTesto());
//		}
//	}
	
	/**
	 * Validazione per il metodo {@link #importa()}.
	 */
	public void validateImporta() {
		checkFaseDiBilancio();
		checkCondition(model.getFatturaFEL() != null && model.getFatturaFEL().getIdFattura() != null && model.getFatturaFEL().getIdFattura().intValue() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Fattura elettronica"));
		// Controlli sul soggetto
		checkSoggetto();
	}

	/**
	 * Controlla che la fase di bilancio sia compatibile con l'operazione. Le fasi di bilancio accettate sono:
	 * <ul>
	 *     <li>ESERCIZIO_PROVVISORIO</li>
	 *     <li>GESTIONE</li>
	 *     <li>ASSESTAMENTO</li>
	 *     <li>PREDISPOSIZIONE_CONSUNTIVO</li>
	 * </ul>
	 */
	private void checkFaseDiBilancio() {
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		if(faseBilancio == null) {
			// ricarico
			caricaFaseBilancio();
		}
		checkCondition(FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio),
			ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Gestione Fatture FEL", "Bilancio in " + faseBilancio), true);
	}

	/**
	 * Controlla che il soggetto utilizzato sia valido.
	 */
	private void checkSoggetto() {
		final String methodName = "checkSoggetto";
		checkCondition(model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"), true);
		
		log.debug(methodName, "Caricamento dei dati del soggetto per codice " + model.getSoggetto().getCodiceSoggetto());
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Ho errori: esco subito
			log.info(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			addErrori(response);
			return;
		}
		
		checkCondition(response.getSoggetto() != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("soggetto", "codice " + model.getSoggetto().getCodiceSoggetto()), true);
		Soggetto soggetto = response.getSoggetto();
		checkCondition(StatoOperativoAnagrafica.VALIDO.equals(soggetto.getStatoOperativo()) || StatoOperativoAnagrafica.SOSPESO.equals(soggetto.getStatoOperativo()),
				ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
		// Imposto il soggetto nel model
		model.setSoggetto(soggetto);
	}

	/**
	 * Consultazione della prima nota.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Uid della fattura elettronica da consultare: " + model.getFatturaFEL().getUid());
		return SUCCESS;
	}
	
	/**
	 * Annullamento della causale EP.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String sospendi() {
		final String methodName = "sospendi";
		
		SospendiFatturaElettronica request = model.creaRequestSospendiFatturaElettronica();
		logServiceRequest(request);
		SospendiFatturaElettronicaResponse response = fatturaElettronicaService.sospendiFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(SospendiFatturaElettronica.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Sospesa fattura elettronica con id " + model.getFatturaFEL().getIdFattura());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #sospendi()}.
	 */
	public void validateSospendi() {
		checkFaseDiBilancio();
		checkCondition(model.getFatturaFEL() != null && model.getFatturaFEL().getIdFattura() != null && model.getFatturaFEL().getIdFattura().intValue() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Fattura elettronica"));
	}
	
}
