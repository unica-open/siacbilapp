/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.handler.session.SessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrataFactory;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;
/**
 * Classe di action per la gestione di Ratei e Risconti della prima nota integrata.
 * 
 * @author Valentina
 * @version 1.0.0 - 11/07/2016
 * @param <M> tipizzazione del model
 */
public class GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseAction<M extends GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseModel> extends  GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4944169468232436801L;
	
	private static final Map<TipoCollegamento, String> MAPPING_DATI_FINANZIARI;
	
	static {
		Map<TipoCollegamento, String> tmp = new HashMap<TipoCollegamento, String>();
		tmp.put(TipoCollegamento.IMPEGNO, "impegno");
		tmp.put(TipoCollegamento.SUBIMPEGNO, "impegno");
		tmp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, "impegno");
		tmp.put(TipoCollegamento.ACCERTAMENTO, "accertamento");
		tmp.put(TipoCollegamento.SUBACCERTAMENTO, "accertamento");
		tmp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, "accertamento");
		tmp.put(TipoCollegamento.LIQUIDAZIONE, "liquidazione");
		tmp.put(TipoCollegamento.DOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_SPESA, "documentoSpesa");
		tmp.put(TipoCollegamento.DOCUMENTO_ENTRATA, "documentoEntrata");
		tmp.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, "documentoEntrata");
		
		MAPPING_DATI_FINANZIARI = Collections.unmodifiableMap(tmp);
	}
	
	
	@Autowired private transient PrimaNotaService primaNotaService;
	
	
	@Override
	public String execute() {
		final String methodName = "execute";
		checkCasoDUsoApplicabile();
		
		try {
			PrimaNota primaNota = caricaPrimaNotaDaServizio();
			leggiEventualiInformazioniAzionePrecedente();
			log.debug(methodName, "Trovata primaNota corrispondente all'uid " + primaNota.getUid());
			//CR-3647
			//filtraListaMovimentiEp(primaNota);
			popolaModel(primaNota);
		} catch(WebServiceInvocationFailureException wsife) {
			// Salvo tutto ed esco
			setErroriInSessionePerActionSuccessiva();
			setMessaggiInSessionePerActionSuccessiva();
			setInformazioniInSessionePerActionSuccessiva();			
			return SUCCESS;
		}
		
		//calcolaTotali();
		return SUCCESS;
	}

	private void popolaModel(PrimaNota primaNota) {
		model.setPrimaNota(primaNota);
		model.setListaElementoQuota(ElementoQuotaPrimaNotaIntegrataFactory.getInstancesFromPrimaNota(primaNota));
		Map<Integer,List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture = new HashMap<Integer, List<ElementoScritturaPrimaNotaIntegrata>>();
		for (MovimentoEP movEP : primaNota.getListaMovimentiEP()) {
			mappaMovimentoEPScritture.put(movEP.getUid(), ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaSingoloMovimentoEP(movEP, false));
		}
		model.setMappaMovimentoEPScritture(mappaMovimentoEPScritture);
		model.setRateoPrimaNota(model.getPrimaNota().getRateo());
	}

	
	/**
	 * Enter page.
	 *
	 * @return the string
	 */
	public String enterPageRisconti() {
		model.popolaEtichettaAzioneRisconti();
		return SUCCESS;
	}
	
	/**
	 * Enter page.
	 *
	 * @return the string
	 */
	public String enterPageRatei() {
		model.popolaEtichettaAzioneRateo();
		return SUCCESS;
	}
	

//	/**
//	 * (JIRA 3647)
//	 * Per i documenti, nel caso in cui ci siano pi&uacute; quote e quindi potenzialmente pi&uacute; conti economici patrimoniali assegnati uguali (se hanno lo stesso impegno sulle quote),
//	 * attualmente il sistema riporta tutti i conti economici/patrimoniali per tutte le quote presenti (anche se uguali).
//	 * <br/>
//	 * Occorre visualizzare una volta sola il codice del conto con accanto la somma di tutti gli importi presenti.
//	 * <br/>
//	 * Esempio documento con due quote a cui &eacute; stato assegnato lo stesso impegno (visualizzazione attuale)
//	 * @param primaNota la prima nota
//	 */
//	private void filtraListaMovimentiEp(PrimaNota primaNota) {
//
//		// controllo la lista che sia diversa da null
//		if (primaNota.getListaMovimentiEP() == null || primaNota.getListaMovimentiEP().isEmpty()) {
//			return;
//		}
//
//		Map<String, MovimentoDettaglio> result = new HashMap<String, MovimentoDettaglio>();
//		// scrollo la lista con i dettagli
//		for (MovimentoEP m : primaNota.getListaMovimentiEP()) {
//			for (MovimentoDettaglio md : m.getListaMovimentoDettaglio()) {
//				String key = computeGroupKeyMovimentoDettaglio(md);
//				if (!result.containsKey(key)) {
//					result.put(key, md);
//				} else {
//					// bisogna aggiornare gli importi ...
//					MovimentoDettaglio objectFromMap = result.get(key);
//					BigDecimal importoDaAggiornare = objectFromMap.getImporto().add(md.getImporto());
//					objectFromMap.setImporto(importoDaAggiornare);
//					result.put(key, objectFromMap);
//				}
//			}
//		}
//		List<MovimentoDettaglio> list = new ArrayList<MovimentoDettaglio>(result.values());
//		model.setListaMovimentoDettaglio(list);
//		
//	}

	
	/**
	 * Ottiene la lista dei conti
	 * @return il risultato dell'invocazione
	 */
	public String ottieniListaConti(){
		if(model.getUidMovimentoEPPerScritture() == 0){
			addErrore(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid movimento EP"));
			return SUCCESS;
		}
		log.info("ottieniListaConti", "model.getUidMovimentoEPPerScritture() " +model.getUidMovimentoEPPerScritture());
		List<ElementoScritturaPrimaNotaIntegrata> lista =  model.getMappaMovimentoEPScritture().get(model.getUidMovimentoEPPerScritture());


		log.info("ottieniListaConti", lista.size());

		model.setListaElementoScritturaPerElaborazione(lista);
		ricalcolaTotali();
		return SUCCESS;
	}


	/**
	 * Ricalcolo dei totali
	 */
	protected void ricalcolaTotali (){
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		for (ElementoScritturaPrimaNotaIntegrata datoScrittura : model.getListaElementoScritturaPerElaborazione()) {
			if(datoScrittura.getMovimentoDettaglio().getImporto() != null) {
				if (OperazioneSegnoConto.AVERE.equals(datoScrittura.getMovimentoDettaglio().getSegno())){
					totaleAvere = totaleAvere.add(datoScrittura.getMovimentoDettaglio().getImporto());
				} else {
					totaleDare = totaleDare.add(datoScrittura.getMovimentoDettaglio().getImporto());
				}
			}
		}
		
		model.setTotaleAvere(totaleAvere);
		model.setTotaleDare(totaleDare);
	}
	
//	/**
//	 *  key per segno e conto
//	 * @param m  il movimento di dettaglio
//	 * @return la chiave di raggruppamento
//	 */
//	private String computeGroupKeyMovimentoDettaglio(MovimentoDettaglio m) {
//		StringBuilder sb = new StringBuilder();
//		sb.append(m.getConto().getUid());
//		sb.append("_");
//		sb.append(m.getSegno());
//		return sb.toString();
//	}

	/**
	 * Caricamento del dettaglio della prima nota da servizio.
	 * 
	 * @return la prima nota del servizio
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private PrimaNota caricaPrimaNotaDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "caricaPrimaNotaDaServizio";
		
		RicercaDettaglioPrimaNota request = model.creaRequestRicercaDettaglioPrimaNotaLibera();
		logServiceRequest(request);
		RicercaDettaglioPrimaNotaResponse response = primaNotaService.ricercaDettaglioPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		if(response.getPrimaNota() == null) {
			String errorMsg = "Nessuna prima nota corrispondente all'uid " + model.getPrimaNota().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Prima nota integrata", model.getPrimaNota().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
//	
		//CR -3647
		return response.getPrimaNota();
	}
	
//	/**
//	 * Calcolo dei totali dare e avere.
//	 */
//	private void calcolaTotali(){
//		BigDecimal totaleDare = BigDecimal.ZERO;
//		BigDecimal totaleAvere = BigDecimal.ZERO;
//
//		for (MovimentoDettaglio movDett : model.getListaMovimentoDettaglio()) {
//			if (OperazioneSegnoConto.DARE.equals(movDett.getSegno())) {
//				totaleDare = totaleDare.add(movDett.getImporto());
//			} else {
//				totaleAvere = totaleAvere.add(movDett.getImporto());
//			}
//		}
//
//		model.setTotaleDare(totaleDare);
//		model.setTotaleAvere(totaleAvere);
//	}

	/**
	 * Ottiene i dati finanziari
	 * @return una stringa corrispondente ai risultati dell'invocazione
	 */
	public String ottieniDatiFinanziari() {
		final String methodName = "ottieniDatiFinanziari";
		String tipoMovimento = MAPPING_DATI_FINANZIARI.get(model.getTipoCollegamento());
		model.setTipoMovimento(tipoMovimento);
		
		OttieniEntitaCollegatePrimaNota req = model.creaRequestOttieniEntitaCollegatePrimaNota();
		OttieniEntitaCollegatePrimaNotaResponse res = primaNotaService.ottieniEntitaCollegatePrimaNota(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		impostaDatiFinanziariFromRequestEResponse(req, res);	
		
		return SUCCESS;
	}
	
	
	/**
	 *  A partire dalla request ({@link OttieniEntitaCollegatePrimaNota}) e dalla response ({@link OttieniEntitaCollegatePrimaNotaResponse}), imposta nel model e in sessione i dati necessari alla consultazione ed, eventualmente, alla gestione paginata della tabella
	 *  
	 *   @params req la request da impostare in sessione per la tabella paginata
	 *   @params req la response da cui ottenere gli elementi da consultare
	 * */
	private void impostaDatiFinanziariFromRequestEResponse(OttieniEntitaCollegatePrimaNota req,	OttieniEntitaCollegatePrimaNotaResponse res) {
		final String methodName = "impostaDatiFinanziariFromRequestEResponse";
		List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers = ElementoMovimentoConsultazionePrimaNotaIntegrataFactory.getInstances(res.getEntitaCollegate());
		popolaDatiaccessorii(wrappers);
		model.setListaDatiFinanziari(wrappers);
		if(!(TipoCollegamento.SUBDOCUMENTO_SPESA.equals(model.getTipoCollegamento()) || TipoCollegamento.SUBDOCUMENTO_ENTRATA.equals(model.getTipoCollegamento()))){
			return;
		}
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + res.getTotaleElementi());

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(getParametroSessioneRequest(), req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(getParametroSessioneLista(), res.getEntitaCollegate());
	}
	

	private SessionParameter getParametroSessioneLista() {
		return BilSessionParameter.RISULTATI_RICERCA_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN;
	}

	private SessionParameter getParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_OTTIENI_ENTITA_COLLEGATE_PRIMA_NOTA_GEN;
	}

	/**
	 *  Popola il model  con eventuali dati accessori che devono essere estrapolati dall'elemento
	 *  @param wrappers la lista di ElementoMovimentoConsultazionePrimaNotaIntegrata da cui estrapolare i dati accessori
	 * */
	private void popolaDatiaccessorii(List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> wrappers) {
		for(ElementoMovimentoConsultazionePrimaNotaIntegrata<?> el : wrappers){
			if(el.getDatiAccessorii() != null){
				//model.setDatiAccessoriiMovimentoFinanziario(el.getDatiAccessorii());
				break;
			}
		}
		
	}

	/**
	 * Validazione per il metodo {@link #ottieniDatiFinanziari()}
	 */
	public void validateOttieniDatiFinanziari() {
		checkNotNullNorInvalidUid(model.getPrimaNota(), "Prima nota");
		checkNotNull(model.getTipoCollegamento(), "Tipo collegamento");
	}
	
	/**
	 * Imposta rateo dopo aggiornamento.
	 *
	 * @return the string
	 */
	public String impostaRateoDopoAggiornamento() {
		return SUCCESS;	
	}
	

	
}
