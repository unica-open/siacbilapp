/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.action.fatturaelettronica;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
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
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
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
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
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
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String importa() {
		final String methodName = "importa";
		// Ricerco il dettaglio della fattura
		RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(request);
		RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
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
		
		// Redirezione verso inserimento documento spesa
		return SUCCESS;
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
			log.info(methodName, createErrorInServiceInvocationString(request, response));
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
			log.info(methodName, createErrorInServiceInvocationString(request, response));
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
