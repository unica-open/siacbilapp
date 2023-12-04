/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.provvisoriocassa;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.GenericFrontEndMessagesException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa.AssociaQuoteAProvvisorioDiCassaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di action per le ricerche del provvisorio di cassa.
 * 
 * @author Valentina
 * @version 1.0.0 - 13/05/2016
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AssociaQuoteAProvvisorioDiCassaAction<M extends AssociaQuoteAProvvisorioDiCassaModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2517360726236191937L;
	
	/** Service per il documento */
	@Autowired protected transient DocumentoService documentoService;
	@Autowired private transient SoggettoService soggettoService;
	@Autowired private transient ProvvisorioService provvisorioService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		String methodName = "execute";
		try{
			caricaListaTipoDocumento();
			caricaProvvisorioDiCassa();
			caricaListaClasseSoggetto();
		}catch(WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * Preparazione per l'esecuzione del metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulizia del model
		model.setTipoDocumento(null);
		// Documento
		model.setAnnoDocumento(null);
		model.setNumeroDocumento(null);
		model.setNumeroSubdocumento(null);
		model.setDataEmissioneDocumento(null);
		// Movimento Gestione
		model.setAnnoMovimento(null);
		model.setNumeroMovimento(null);
		// Soggetto
		model.setSoggetto(null);
	}
	
	/**
	 * Metodo per il completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String completeStep1();
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		checkCondition(!(model.getAnnoDocumento() == null ^ (model.getNumeroDocumento() == null || StringUtils.isBlank(model.getNumeroDocumento()))),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero documento", ": devono essere entrambi valorizzati o entrambi non valorizzati"));
		checkCondition(model.getNumeroSubdocumento() == null || (model.getAnnoDocumento() != null && model.getNumeroDocumento() != null && StringUtils.isNotBlank(model.getNumeroDocumento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero subdocumento", ": deve essere valorizzato solo se indicata la chiave del documento"));
		checkCondition(!(model.getAnnoMovimento() == null ^ model.getNumeroMovimento() == null),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": devono essere entrambi valorizzati o entrambi non valorizzati"));
		
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
	 * Preparazione per il metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		// Pulisco il model
		model.setListaUidSubdocumenti(new ArrayList<Integer>());
	}
	
	/**
	 * Metodo per il completamento dello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String completeStep2();
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkCondition(!model.getListaUidSubdocumenti().isEmpty(),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Quota da associare"));
		checkImportoDaRegolarizzare();
	}
	
	/**
	 * Metodo per tornare alla precedente azione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String backToStep1() {
		cleanErroriMessaggiInformazioni();
		model.setPulsanteAggiornaNascosto(false);
		
		// SIAC-6060: ricalcolo il provvisorio di cassa
		try {
			caricaProvvisorioDiCassaDaServizio();
		} catch(WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException("Errore nel caricamento del provvisorio di cassa", wsife);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento del provvisorio da servizio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocaizone del servizio
	 */
	private void caricaProvvisorioDiCassaDaServizio() throws WebServiceInvocationFailureException{
		final String methodName = "caricaProvvisorioDiCassaDaServizio";
		RicercaProvvisorioDiCassaPerChiave req = model.creaRequestRicercaProvvisorioDiCassaPerChiave();
		RicercaProvvisorioDiCassaPerChiaveResponse res = provvisorioService.ricercaProvvisorioDiCassaPerChiave(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nell'invocazione del servizio di ricerca provvisorio per chiave");
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaProvvisorioDiCassaPerChiave.class, res));
		}
		
		if(res.getProvvisorioDiCassa() == null) {
			String errMsg = "Nessun provvisorio reperito da servizio per chiave " + model.getProvvisorio().getAnno() + "/" + model.getProvvisorio().getNumero();
			log.info(methodName, errMsg);
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvisorio di cassa", model.getProvvisorio().getAnno() + "/" + model.getProvvisorio().getNumero()));
			throw new WebServiceInvocationFailureException(errMsg);
		}
		model.setProvvisorio(res.getProvvisorioDiCassa());
	}

	/**
	 *  Controlla che l'importo da regolarizzare del provvisorio sia sufficiente a coprire l'importo da incassare delle quote
	 */
	protected void checkImportoDaRegolarizzare() {
		// To be overridden if necessary
	}

	/**
	 * Controlla se la lista dei TipoDocumento sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaTipoDocumento() throws WebServiceInvocationFailureException {
		// To be overridden if necessary
	}
	
	/**
	 * Ricerca il provvisorio tra quelli della lista presente in sessione
	 * 
	 */
	private void caricaProvvisorioDiCassa() {
		List<ProvvisorioDiCassa> listaProvvisori = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_PROVVISORIO_DI_CASSA);
		ProvvisorioDiCassa p = new ProvvisorioDiCassa();
		p.setUid(model.getUidProvvisorioDaAssociare());
		model.setProvvisorio(ComparatorUtils.searchByUid(listaProvvisori, p));
		
	}
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	private void caricaListaClasseSoggetto() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaClassiSoggetto = response.getListaClasseSoggetto();
				ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
				sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
			}
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
}
