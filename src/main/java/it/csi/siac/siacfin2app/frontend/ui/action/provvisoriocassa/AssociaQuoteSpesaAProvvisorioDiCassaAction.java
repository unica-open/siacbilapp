/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa.AssociaQuoteSpesaAProvvisorioDiCassaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumentoResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * Classe di action per le ricerche del provvisorio di cassa.
 * 
 * @author Valentina
 * @version 1.0.0 - 13/05/2016
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AssociaQuoteSpesaAProvvisorioDiCassaAction extends AssociaQuoteAProvvisorioDiCassaAction<AssociaQuoteSpesaAProvvisorioDiCassaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8374147665820143326L;
	
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
	@Override
	public String completeStep1() {
		final String methodName = "completeStep1";
		log.debug(methodName, "Caricamento delle quote");
		int totaleQuote = 0;
		try {
			totaleQuote = caricaQuote();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, "Errore nell'invocazione dei servizi: " + wsife.getMessage(), wsife);
			return INPUT;
		}
		log.debug(methodName, "Totale quote trovate: " + totaleQuote);
		if(totaleQuote == 0) {
			log.info(methodName, "Nessuna quota corrispondente ai risultati di ricerca ottenuta");
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Documento", "relativo ai parametri di ricerca"));
			return INPUT;
		}
		
		model.setListaUidSubdocumenti(new ArrayList<Integer>());
		log.debug(methodName, "Invocazione dei servizi effettuata con successo. Inizializzazione dei campi effettuata. Redirezione allo step 2");
		return SUCCESS;
	}
	
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		log.debug(methodName, "Numero di subdocumenti da associare all'elenco: " + model.getListaUidSubdocumenti().size());
		
		AssociaProvvisorioQuoteSpesa req = model.creaRequestAssociaProvvisorioQuoteSpesa();
		logServiceRequest(req);
		AssociaProvvisorioQuoteSpesaResponse res = documentoSpesaService.associaProvvisorioQuoteSpesa(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errori nell'invocazione
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}

		aggiornaQuoteAssociate(res.getListaQuote());
		
		// SIAC-6077
		BigDecimal importoDaRegolarizzare = res.getImportoDaRegolarizzare();
		model.getProvvisorio().setImportoDaRegolarizzare(importoDaRegolarizzare);
		
		model.setPulsanteAggiornaNascosto(true);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	private void aggiornaQuoteAssociate(List<SubdocumentoSpesa> listaQuoteAssociate) {
		List<SubdocumentoSpesa> listaQuote = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA_PER_PROVVISORIO);
		Map<Integer, SubdocumentoSpesa> map = new HashMap<Integer, SubdocumentoSpesa>();
		for (SubdocumentoSpesa q : listaQuote) {
			map.put(q.getUid(), q);
		}
		for (SubdocumentoSpesa q : listaQuoteAssociate) {
			SubdocumentoSpesa ss = map.get(q.getUid());
			if(ss != null) {
				ss.setProvvisorioCassa(q.getProvvisorioCassa());
			}
		}
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA_PER_PROVVISORIO, listaQuote);
	}

	/**
	 *  Controlla che l'importo da regolarizzare del provvisorio sia sufficiente a coprire l'importo da incassare delle quote
	 */
	@Override
	protected void checkImportoDaRegolarizzare() {
		BigDecimal importoDaRegolarizzare = model.getProvvisorio().getImportoDaRegolarizzare();
		BigDecimal totaleQuote = model.getTotaleSubdocumentiSpesa();
		checkCondition(totaleQuote.compareTo(importoDaRegolarizzare) <= 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il totale delle quote deve essere minore o uguale all'importo da regolarizzare del provvisorio"));
	}


	/**
	 * Controlla se la lista dei TipoDocumento sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	@Override
	protected void caricaListaTipoDocumento() throws WebServiceInvocationFailureException {
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		if(listaTipoDocumento == null) {
			RicercaTipoDocumento req = model.creaRequestRicercaTipoDocumento();
			logServiceRequest(req);
			RicercaTipoDocumentoResponse res = documentoService.ricercaTipoDocumento(req);
			logServiceResponse(res);
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException("caricaListaTipoAtto");
			}
			listaTipoDocumento = res.getElencoTipiDocumento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaTipoDocumento);
		}
		model.setListaTipoDocumento(listaTipoDocumento);
	}
	
	/**
	 * Carica le quote relative alla Spesa.
	 * 
	 * @return il numero di quote ottenute
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private int caricaQuote() throws WebServiceInvocationFailureException {
		final String methodName = "caricaQuote";
		
		RicercaQuotePerProvvisorioSpesa req = model.creaRequestRicercaQuotePerProvvisorioSpesa();
		logServiceRequest(req);
		RicercaQuotePerProvvisorioSpesaResponse res = documentoSpesaService.ricercaQuotePerProvvisorioSpesa(req);   
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorString = createErrorInServiceInvocationString(req, res);
			log.info(methodName, errorString);
			addErrori(res);
			throw new WebServiceInvocationFailureException(errorString);
		}
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_QUOTE_SPESA_PER_PROVVISORIO, res.getListaSubdocumenti());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_QUOTE_SPESA_PER_PROVVISORIO, req);
		
		log.debug(methodName, "Totale quote di spesa: " + res.getTotaleElementi());
		return res.getTotaleElementi();
	}
}
