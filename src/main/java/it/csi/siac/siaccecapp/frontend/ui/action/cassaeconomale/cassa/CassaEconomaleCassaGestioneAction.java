/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.cassa;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.CassaEconomaleBaseAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.cassa.CassaEconomaleCassaGestioneModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di action per la gestione della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CassaEconomaleCassaGestioneAction extends CassaEconomaleBaseAction<CassaEconomaleCassaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9041936855786576974L;
	
	
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipiDiCassa();
		caricaListaClasseSoggetto();
		
	}

	/**
	 * Carica la lista dei tipi di cassa e la imposta nel model.
	 */
	private void caricaListaTipiDiCassa() {
		model.setListaTipoDiCassa(Arrays.asList(TipoDiCassa.values()));
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
	public String execute() {
		// Carico la cassa economale con quella presente in sessione
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		model.setCassaEconomale(cassaEconomale);
		
		model.setTipoDiCassaOriginale(cassaEconomale.getTipoDiCassa());
		
		// Impostazione degli importi
		try {
			impostazioneImportiCassaEconomale();
		} catch(WebServiceInvocationFailureException wsife) {
			// Imposto gli errori in sessione
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Impostazione degli importi della cassa economale
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void impostazioneImportiCassaEconomale() throws WebServiceInvocationFailureException {
		CassaEconomale cassaEconomaleConImporti = calcolaImportiCassaEconomale();
		
		// Imposto nel model
		model.getCassaEconomale().setDisponibilitaCassaContanti(cassaEconomaleConImporti.getDisponibilitaCassaContantiNotNull());
		model.getCassaEconomale().setDisponibilitaCassaContoCorrente(cassaEconomaleConImporti.getDisponibilitaCassaContoCorrenteNotNull());
	}
	
//	/**
//	 * Recupera il soggetto della cassa economale
//	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
//	 */
//	private void recuperaSoggettoCassaEconomale() throws WebServiceInvocationFailureException{
//		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
//		logServiceRequest(request);
//		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
//		logServiceResponse(response);
//		
//		if(response.hasErrori()) {
//			addErrori(response);
//			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
//		}
//		if(response.getSoggetto() == null) {
//			String errorMsg = "Nessun soggetto corrispondente alla matricola " + model.getCassaEconomale().getSoggetto().getUid() + " trovato";
//			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getCassaEconomale().getSoggetto()));
//			throw new WebServiceInvocationFailureException(errorMsg);
//		}
//		
//		Soggetto soggetto = response.getSoggetto();
//	}

	/**
	 * Aggiorna la cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		// Controllo che non ci siano messaggi
		if(hasMessaggi()) {
			// Devo attendere la conferma dell'utente
			return INPUT;
		}
		
		AggiornaCassaEconomale request = model.creaRequestAggiornaCassaEconomale();
		logServiceRequest(request);
		AggiornaCassaEconomaleResponse response = cassaEconomaleService.aggiornaCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nell'invocazione dell'aggiornamento della cassa economale con uid " + model.getCassaEconomale().getUid());
			addErrori(response);
			return INPUT;
		}
		// Cassa aggiornata. Effettuo la ricerca di dettaglio per ripopolare i dati
		try {
			ricercaDettaglioCassaEconomale();
		} catch (WebServiceInvocationFailureException wsife) {
			return INPUT;
		}
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		// Se non c'è la cassa, rilancio direttamente l'errore ed esco
		checkNotNull(model.getCassaEconomale(), "Cassa", true);
		checkCondition(model.getCassaEconomale().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cassa"), true);
		// Controlli sui campi
		checkNotNullNorEmpty(model.getCassaEconomale().getDescrizione(), "Descrizione");
		checkNotNullNorEmpty(model.getCassaEconomale().getResponsabile(), "Responsabile");
		checkNotNull(model.getCassaEconomale().getTipoDiCassa(), "Tipo cassa");
		
		// Numero conto corrente: Obbligatorio solo se cassa c/c o mista
		boolean isContoCorrenteObbligatorio = TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(model.getCassaEconomale().getTipoDiCassa())
				|| TipoDiCassa.MISTA.equals(model.getCassaEconomale().getTipoDiCassa());
		checkCondition(!isContoCorrenteObbligatorio || StringUtils.isNotBlank(model.getCassaEconomale().getNumeroContoCorrente()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero conto corrente"));
		
		checkCondition(model.getCassaEconomale().getLimiteImportoNotNull().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Limite importo impegno", ": non puo' essere negativo"));
		
		// Controllo modifica tipo di cassa
		controlloVariazioneTipoDiCassa();
	}
	
	/**
	 * Effettua i controlli per la variazione del tipo di cassa.
	 * <br/>
	 * Se viene modificata la cassa da 'mista' in 'c/c' occorre controllare l'importo 'contanti', se da 'mista' viene modificata in 'contanti' occorre controllare l'importo 'c/c':
	 * se l'importo e' significativo, prima di salvare la modifica occorre avvisare l'utente chiedendo conferma dell'operazione con il seguente messaggio
	 * <code>&lt;CEC_INF_0005, Conferma aggiornamento tipologia di cassa&gt;</code>.
	 */
	private void controlloVariazioneTipoDiCassa() {
		final String methodName = "controlloVariazioneTipoDiCassa";
		TipoDiCassa originale = model.getTipoDiCassaOriginale();
		TipoDiCassa nuovo = model.getCassaEconomale().getTipoDiCassa();
		
		log.debug(methodName, "Tipo di cassa originale: " + originale + ", nuovo tipo: " + nuovo);
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione()) && !originale.equals(nuovo) && TipoDiCassa.MISTA.equals(originale)) {
			// I tipi sono diversi e non devo proseguire con l'elaborazione
			BigDecimal importo = BigDecimal.ZERO;
			if(TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(nuovo)) {
				importo = model.getCassaEconomale().getDisponibilitaCassaContantiNotNull();
			} else if (TipoDiCassa.CONTANTI.equals(nuovo)) {
				importo = model.getCassaEconomale().getDisponibilitaCassaContoCorrenteNotNull();
			}
			warnCondition(importo.signum() == 0, ErroreCEC.CEC_INF_0005.getErrore());
		}
	}

	/**
	 * Annulla la cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		AnnullaCassaEconomale request = model.creaRequestAnnullaCassaEconomale();
		logServiceRequest(request);
		AnnullaCassaEconomaleResponse response = cassaEconomaleService.annullaCassaEconomale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nell'invocazione dell'annullamento della cassa economale con uid " + model.getCassaEconomale().getUid());
			addErrori(response);
			return INPUT;
		}
		// Cassa annullata. Redirigo verso la prima pagina
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #annulla()}.
	 */
	public void validateAnnulla() {
		// Se non c'è la cassa, rilancio direttamente l'errore ed esco
		checkNotNull(model.getCassaEconomale(), "Cassa", true);
		checkCondition(model.getCassaEconomale().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cassa"));
	}
	
}
