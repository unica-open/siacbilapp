/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.associa.movimento;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.AggiornaAllegatoAttoModel.TabVisualizzazione;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di Action per l'associazione tra movimento e allegato atto.
 * <br>
 * Gestisce la navigazione standard tra le pagine.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 30/set/2014 - aggiunta la classe base
 * @version 1.0.2 - 07/ott/2014 - spezzata la classe in tre parti + base
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AssociaMovimentoAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AssociaMovimentoAllegatoAttoAction extends AssociaMovimentoAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5526154664100115914L;
	
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ContoTesoreriaService contoTesoreriaService;


	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Caricamento dell'allegato atto
		RicercaDettaglioAllegatoAtto req = model.creaRequestRicercaDettaglioAllegatoAtto();
		logServiceRequest(req);
		RicercaDettaglioAllegatoAttoResponse res = allegatoAttoService.ricercaDettaglioAllegatoAtto(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioAllegatoAtto.class, res));
			throwExceptionFromErrori(res.getErrori());
		}
		model.setAllegatoAtto(res.getAllegatoAtto());
		model.setAttoAmministrativo(res.getAllegatoAtto().getAttoAmministrativo());
		
		// Caricamento liste
		log.debug(methodName, "Caricamento liste classificatori");
		caricaListaClasseSoggetto();
		log.debug(methodName, "Caricamento sedi secondarie e modalita di pagamento");
		caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto();
		caricaListaContoTesoreria();
		
		return SUCCESS;
	}
	
	/**
	 * Carica le liste di sede secondaria e di modalita pagamento
	 */
	private void caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto() {
		// Ottengo le liste dalla sessione
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		// Imposto le liste nel model
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CONTO_TESORERIA);
		if(listaInSessione == null) {
			LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
			logServiceRequest(request);
			LeggiContiTesoreriaResponse response = contoTesoreriaService.leggiContiTesoreria(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoTesoreria");
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}. Pulisce il model.
	 */
	public void prepareCompleteStep1() {
		model.setSoggetto(null);
		model.setSedeSecondariaSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setContoTesoreria(null);
	}
	
	/**
	 * Metodo per il completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		// Controllo se il soggetto e' stato modificato
		checkSoggettoModificato();
		// SIAC-5311 SIOPE+: caricamento della lista assenza CIG
		caricaListaAssenzaCig();
		return SUCCESS;
	}
	
	/**
	 * Controlla se il &eacute; stato modificato da un precedente inserimento. In tal caso, pulisce i dati della seconda pagina.
	 */
	private void checkSoggettoModificato() {
		final String methodName = "checkSoggettoModificato";
		final Soggetto soggetto = model.getSoggetto();
		final List<SubdocumentoSpesa> listaSpesa = model.getListaSubdocumentoSpesa();
		final List<SubdocumentoEntrata> listaEntrata = model.getListaSubdocumentoEntrata();
		boolean toClean = false;
		
		for(int i = 0; i < listaSpesa.size() && !toClean; i++) {
			SubdocumentoSpesa ss = listaSpesa.get(i);
			Impegno impegno = ss.getImpegno();
			toClean = isCodiceSoggettoDiverso(soggetto, impegno);
		}
		for(int i = 0; i < listaEntrata.size() && !toClean; i++) {
			SubdocumentoEntrata se = listaEntrata.get(i);
			Accertamento accertamento = se.getAccertamento();
			toClean = isCodiceSoggettoDiverso(soggetto, accertamento);
		}
		if(toClean) {
			log.debug(methodName, "Codice soggetto cambiato. Necessario ripulire le liste");
			listaSpesa.clear();
			listaEntrata.clear();
		}
	}
	
	/**
	 * Comunica se il codice del soggetto fornito in input e del soggetto del movimento di gestione siano differenti.
	 * 
	 * @param soggetto          il soggetto da cui partire
	 * @param movimengoGestione il movimento da controllare
	 * 
	 * @return <code>true</code> se il codice soggetto &eacute; distinto; <code>false</code> in caso contrario
	 */
	private boolean isCodiceSoggettoDiverso(Soggetto soggetto, MovimentoGestione movimengoGestione) {
		return movimengoGestione != null && movimengoGestione.getSoggetto() != null
				&& !soggetto.getCodiceSoggetto().equals(movimengoGestione.getSoggetto().getCodiceSoggetto());
	}
	
	/**
	 * Caricamento della lista dei motifvi di assenza CIG
	 */
	private void caricaListaAssenzaCig() {
		if(!model.getListaSiopeAssenzaMotivazione().isEmpty()) {
			return;
		}
		
		// Ricerca la causale come codifica generica
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeAssenzaMotivazione.class);
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		// Se ho errori ignoro la response
		if(!res.hasErrori()) {
			model.setListaSiopeAssenzaMotivazione(res.getCodifiche(SiopeAssenzaMotivazione.class));
		}
	}

	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 * @throws WebServiceInvocationFailureException 
	 */
	public void validateCompleteStep1() throws WebServiceInvocationFailureException {
		// Controllo la validita del soggetto
		Soggetto soggetto = model.getSoggetto();
		checkCondition(soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"), true);
		checkNotNullNorInvalidUid(model.getModalitaPagamentoSoggetto(), "Modalita pagamento");
		checkSoggettoValido(soggetto, Arrays.asList(StatoOperativoAnagrafica.VALIDO, StatoOperativoAnagrafica.SOSPESO));
		//SIAC-6840
		//la modalita' AVVISO PAGOPA non e' ammessa per la funzione Associa Movimento
		if(model.getModalitaPagamentoSoggetto() != null) {
			//SIAC-8853
			checkCondition(!checkModalitaPagamentoIsPagoPA(model.getModalitaPagamentoSoggetto().getUid(), model.getListaModalitaPagamentoSoggetto()), 
					ErroreFin.MOD_PAGO_PA_NON_AMMESSA.getErrore());	
		}
	}
	
	
	/**
	 * Metodo per l'ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Completamento per lo step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		InserisceElenco req = model.creaRequestInserisceElenco();
		logServiceRequest(req);
		InserisceElencoResponse res = allegatoAttoService.inserisceElencoConDocumentiConQuote(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio InserisceElencoConDocumentiConQuote");
			addErrori(res);
			return INPUT;
		}
		
		log.debug(methodName, "Elenco inserito correttamente con uid " + res.getElencoDocumentiAllegato().getUid()
				+ " per allegato atto " + model.getUidAllegatoAtto());
		// Pulisco la sessione delle ancore
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_ASSOCIAZIONE_MOVIMENTO);
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		// Importo il tab su cui redirigere in sessione
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, TabVisualizzazione.ELENCO);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(!model.getListaSubdocumentoSpesa().isEmpty() || !model.getListaSubdocumentoEntrata().isEmpty(),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("movimenti"));
		checkCondition(model.getListaSubdocumentoSpesa().isEmpty()
				|| (model.getModalitaPagamentoSoggetto() != null && model.getModalitaPagamentoSoggetto().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("modalita' pagamento soggetto"));
	}
	
	/**
	 * Ottiene le liste dei dati nel model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String obtainLists() {
		return SUCCESS;
	}
	
}

