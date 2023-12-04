/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.operazionicassa;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.CassaEconomaleBaseAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.operazionicassa.CassaEconomaleOperazioniCassaGestioneModel;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.operazionicassa.OperazioneOperazioneCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per la gestione delle operazioni di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CassaEconomaleOperazioniCassaGestioneAction extends CassaEconomaleBaseAction<CassaEconomaleOperazioniCassaGestioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2592294530604200282L;
	
	@Autowired private transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Imposto la cassa economale
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		model.setCassaEconomale(cassaEconomale);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// TODO: aggiornare successivamente
		model.setInserimentoAbilitato(Boolean.TRUE);
		model.setAzioniAbilitate(Boolean.TRUE);
		
		try {
			caricaListaTipoOperazioneCassa();
			caricaListaStatoOperativoOperazioneCassa();
			
			caricaListaModalitaDiPagamento();
			caricaListaTipoAtto();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		return SUCCESS;
	}

	/**
	 * Caricamento della lista dei tipi operazione di cassa.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaTipoOperazioneCassa() throws WebServiceInvocationFailureException {
		RicercaTipoOperazioneDiCassa request = model.creaRequestRicercaTipoOperazioneDiCassa();
		logServiceRequest(request);
		RicercaTipoOperazioneDiCassaResponse response = cassaEconomaleService.ricercaTipoOperazioneDiCassa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String message = createErrorInServiceInvocationString(RicercaTipoOperazioneDiCassa.class, response);
			addErrori(response);
			throw new WebServiceInvocationFailureException(message);
		}
		List<TipoOperazioneCassa> list = response.getTipiOperazione();
		model.setListaTipoOperazioneCassa(list);
	}

	/**
	 * Caricamento della lista degli stati operativi.
	 */
	private void caricaListaStatoOperativoOperazioneCassa() {
		List<StatoOperativoOperazioneCassa> list = Arrays.asList(StatoOperativoOperazioneCassa.values());
		model.setListaStatoOperativoOperazioneCassa(list);
	}

	/**
	 * Caricamento della lista delle modalita di pagamento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaModalitaDiPagamento() throws WebServiceInvocationFailureException {
		List<ModalitaPagamentoCassa> list = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_CASSA_ECONOMALE);
		if(list == null) {
			RicercaModalitaPagamentoCassa request = model.creaRequestRicercaModalitaPagamentoCassa();
			logServiceRequest(request);
			RicercaModalitaPagamentoCassaResponse response = cassaEconomaleService.ricercaModalitaPagamentoCassa(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String message = createErrorInServiceInvocationString(RicercaModalitaPagamentoCassa.class, response);
				addErrori(response);
				throw new WebServiceInvocationFailureException(message);
			}
			list = response.getModalitaPagamentoCassa();
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_CASSA_ECONOMALE, list);
		}
		model.setListaModalitaPagamentoCassa(list);
	}

	/**
	 * Caricamento della lista dei tipi di atto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> list = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(list == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String message = createErrorInServiceInvocationString(TipiProvvedimento.class, response);
				addErrori(response);
				throw new WebServiceInvocationFailureException(message);
			}
			list = response.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, list);
		}
		model.setListaTipoAtto(list);
	}
	
	/**
	 * Ricerca i tipi di operazione di cassa.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		RicercaSinteticaOperazioneDiCassa request = model.creaRequestRicercaSinteticaOperazioneDiCassa();
		logServiceRequest(request);
		RicercaSinteticaOperazioneDiCassaResponse response = cassaEconomaleService.ricercaSinteticaOperazioneDiCassa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nella ricerca sintetica dell'operazione di cassa");
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun elemento trovato corrispondente ai parametri di ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Trovati " + response.getTotaleElementi() + " risultati");
		// Dati trovati. Imposto in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_OPERAZIONE_CASSA, response.getOperazioniCassa());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_OPERAZIONE_CASSA, request);
		return SUCCESS;
	}
	
	/**
	 * Inizio dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioInserimento() {
		final String methodName = "inizioInserimento";
		model.setOperazioneOperazioneCassa(OperazioneOperazioneCassa.INSERIMENTO);
		
		// Caricamento liste
		try {
			caricaListaTipoOperazioneCassa();
			caricaListaModalitaDiPagamento();
			caricaListaTipoAtto();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			return SUCCESS;
		}
		
		// Impostazione dei default
		OperazioneCassa operazioneCassa = new OperazioneCassa();
		// Data di sistema nell’inserimento
		operazioneCassa.setDataOperazione(new Date());
		if(!TipoDiCassa.MISTA.equals(model.getCassaEconomale().getTipoDiCassa())) {
			impostaModalitaPagamentoOperazione(operazioneCassa, model.getCassaEconomale().getTipoDiCassa());
		}
		model.setOperazioneCassa(operazioneCassa);
		
		return SUCCESS;
	}

	/**
	 * Impostazione della modalit&agrave; di pagamento.
	 * 
	 * @param operazioneCassa l'operazione
	 * @param tipoDiCassa     il tipo di cassa
	 */
	private void impostaModalitaPagamentoOperazione(OperazioneCassa operazioneCassa, TipoDiCassa tipoDiCassa) {
		final String methodName = "impostaModalitaPagamentoOperazione";
		log.debug(methodName, "Ricerca tra le modalitaPagamentoCassa");
		List<ModalitaPagamentoCassa> list = model.getListaModalitaPagamentoCassa();
		ModalitaPagamentoCassa modalitaPagamentoCassa = null;
		
		for(ModalitaPagamentoCassa mpc : list) {
			if(tipoDiCassa.getCodice().equals(mpc.getCodice())) {
				modalitaPagamentoCassa = mpc;
				break;
			}
		}
		
		operazioneCassa.setModalitaPagamentoCassa(modalitaPagamentoCassa);
	}
	
	/**
	 * Inserimento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		InserisceOperazioneDiCassa request = model.creaRequestInserisceOperazioneDiCassa();
		logServiceRequest(request);
		InserisceOperazioneDiCassaResponse response = cassaEconomaleService.inserisceOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceOperazioneDiCassa.class, response));
			addErrori(response);
			return INPUT;
		}
		OperazioneCassa operazioneCassa = response.getOperazioneCassa();
		log.debug(methodName, "Inserito OperazioneCassa con uid " + operazioneCassa.getUid());
		
		// Inserimento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaOperazioneCassaRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #inserimento()}.
	 */
	public void validateInserimento() {
		validateOperazioneCassa();
	}
	
	/**
	 * Validazione dell'OperazioneCassa.
	 */
	private void validateOperazioneCassa() {
		OperazioneCassa operazioneCassa = model.getOperazioneCassa();
		checkNotNull(operazioneCassa, "Operazione di cassa da inserire", true);
		
		checkNotNull(operazioneCassa.getDataOperazione(), "Data operazione");
		checkNotNullNorInvalidUid(operazioneCassa.getTipoOperazioneCassa(), "Tipo operazione");
		checkNotNull(operazioneCassa.getImporto(), "Importo");
		checkNotNullNorInvalidUid(operazioneCassa.getModalitaPagamentoCassa(), "Modalita' di pagamento");
		
		// L'importo deve essere in valore assoluto
		if(operazioneCassa.getImporto() != null) {
			operazioneCassa.setImporto(operazioneCassa.getImporto().abs());
		}
		
		AttoAmministrativo attoAmministrativo = model.getAttoAmministrativo();
		if(attoAmministrativo != null) {
			checkProvvedimento(attoAmministrativo);
		}
	}
	
	/**
	 * Controlla la correttezza dei dati del provvedimento.
	 * @param attoAmministrativo l'atto amministrativo
	 */
	private void checkProvvedimento(AttoAmministrativo attoAmministrativo) {
		checkCondition(!(attoAmministrativo.getAnno() == 0 ^ attoAmministrativo.getNumero() == 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e numero atto sono obbligatorii"));
		if(attoAmministrativo.getAnno() != 0 && attoAmministrativo.getNumero() != 0) {
			// Ho effettivamente ricercato l'anno. Proseguo
			checkProvvedimentoEsistente();
		}
	}
	
	/**
	 * Controlla che il provvedimento sia esistente ed univoco.
	 */
	private void checkProvvedimentoEsistente() {
		final String methodName = "checkProvvedimentoEsistente";
		// Invocazione del servizio
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo di non aver errori
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvedimento.class, response));
			addErrori(response);
			return;
		}
		// Controllo di aver almeno un provvedimento
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		// Controllo di avere al piu' un provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getAttoAmministrativo().getStrutturaAmmContabile(), true);
		
		AttoAmministrativo aa = response.getListaAttiAmministrativi().get(0);
		checkCondition(StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti()),
				ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione Allegato atto", "Definitivo"));
		// Imposto l'atto nel model
		model.setAttoAmministrativo(aa);
	}
	
	/**
	 * Inizio dell'aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inizioAggiornamento() {
		final String methodName = "inizioAggiornamento";
		// Cerco il tipo per uid
		List<OperazioneCassa> listaOperazioni = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_OPERAZIONE_CASSA);
		OperazioneCassa operazioneCassa = model.getOperazioneCassa();
		OperazioneCassa operazioneCassaTrovata = ComparatorUtils.searchByUid(listaOperazioni, operazioneCassa);
		// Imposto i dati nel model
		model.setOperazioneCassa(operazioneCassaTrovata);
		model.setAttoAmministrativo(operazioneCassaTrovata.getAttoAmministrativo());
		
		// Imposto il tipo di operazione
		model.setOperazioneOperazioneCassa(OperazioneOperazioneCassa.AGGIORNAMENTO);
		
		// Caricamento liste
		try {
			caricaListaTipoOperazioneCassa();
			caricaListaModalitaDiPagamento();
			caricaListaTipoAtto();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	/**
	 * Aggiornamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		AggiornaOperazioneDiCassa request = model.creaRequestAggiornaOperazioneDiCassa();
		logServiceRequest(request);
		AggiornaOperazioneDiCassaResponse response = cassaEconomaleService.aggiornaOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AggiornaOperazioneDiCassa.class, response));
			addErrori(response);
			return INPUT;
		}
		
		OperazioneCassa operazioneCassa = response.getOperazioneCassa();
		log.debug(methodName, "Aggiornato OperazioneCassa con uid " + operazioneCassa.getUid());
		
		// Aggiornamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		impostaOperazioneCassaRicercaSeNonPresente();
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}.
	 */
	public void validateAggiornamento() {
		validateOperazioneCassa();
	}
	
	/**
	 * Annullamento del tipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullamento() {
		final String methodName = "annullamento";
		AnnullaOperazioneDiCassa request = model.creaRequestAnnullaOperazioneDiCassa();
		logServiceRequest(request);
		AnnullaOperazioneDiCassaResponse response = cassaEconomaleService.annullaOperazioneDiCassa(request);
		logServiceResponse(response);
		// Se ho errori, esco subito
		if(response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(AnnullaOperazioneDiCassa.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		OperazioneCassa operazioneCassa = response.getOperazioneCassa();
		log.debug(methodName, "Annullato OperazioneCassa con uid " + operazioneCassa.getUid());
		
		// Annullamento effettuato con successo
		impostaInformazioneSuccesso();
		// Rieffettuo la ricerca
		model.setOperazioneCassaRicerca(new OperazioneCassa());
		String resultRicerca = ricerca();
		log.debug(methodName, "Risultato della ricerca: " + resultRicerca + ". Errori riscontrati? " + hasErrori());
		return resultRicerca;
	}
	
	/**
	 * Visualizzazione degli importi della cassa economale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String importiCassa() {
		final String methodName = "importiCassa";
		
		log.debug(methodName, "Calcolo importi cassa economale per cassa con uid " + model.getCassaEconomale().getUid());
		try {
			CassaEconomale cassaEconomaleConImporti = calcolaImportiCassaEconomale();
			model.getCassaEconomale().setDisponibilitaCassaContanti(cassaEconomaleConImporti.getDisponibilitaCassaContantiNotNull());
			model.getCassaEconomale().setDisponibilitaCassaContoCorrente(cassaEconomaleConImporti.getDisponibilitaCassaContoCorrenteNotNull());
		} catch(WebServiceInvocationFailureException wsife) {
			// Errore nel calcolo... Non importa: ritorno quanto ho in pancia
			log.warn(methodName, "Errore nel calcolo degli importi. Restituisco i valori precedenti. Errore riscontrato: " + wsife.getMessage());
		}
		
		return SUCCESS;
	}
	
	/**
	 * Imposta l'operazione di cassa per la ricerca qualora non sia gi&agrave; presente nel model.
	 */
	private void impostaOperazioneCassaRicercaSeNonPresente() {
		if(model.getOperazioneCassaRicerca() == null) {
			model.setOperazioneCassaRicerca(new OperazioneCassa());
		}
	}
	
}
