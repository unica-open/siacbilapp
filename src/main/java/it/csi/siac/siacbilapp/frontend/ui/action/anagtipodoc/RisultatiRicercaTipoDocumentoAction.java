/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.anagtipodoc;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc.RisultatiRicercaTipoDocumentoModel;
import it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Action per i risultati di ricerca del TipoDocumento
 * 
* @author Lobue Filippo
* @version 1.0.0 - 20/set/2019
* 
*/
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoDocumentoAction  extends GenericBilancioAction<RisultatiRicercaTipoDocumentoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3466397660907796909L;
	
	@Autowired
	private transient TipoDocumentoFELService tipoDocumentoServiceFEL;
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		int startPosition = 0;
		// Controllo che non vi sia una pagina salvata in sessione
		if (sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START) != null) {
			// Imposto il numero di pagina dalla sessione
			startPosition = ((Integer) sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START)).intValue();
		}
		model.setSavedDisplayStart(startPosition);
		
		log.debug(methodName, "startPosition = " + startPosition);
		//cerco info azione precedente
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
 
	}

	
	
	/**
	 * Redirezione alla action di aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		checkCasoDUsoApplicabile(model.getTitolo());
		log.debug("aggiorna", "TipoDocumento da aggiornare: " + model.getUidDaAggiornare() + ". Pagina in cui rientrare: " + model.getiDisplayStart());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, model.getiDisplayStart());
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	
	/**
	 * Annullamento del Tipo Documento
	 * 
	 * @return la String corrispondente al risultato dell'invocazione
	 */
	public String annulla() {
		final String methodName = "annulla";
		
		
		// Creo la request per l'eliminazione del Tipo Documento
		AnnullaTipoDocumentoFEL req = model.creaRequestAnnullaTipoComponenteImportiCapitolo();
		logServiceRequest(req);
		// Invocazione del servizio
		AnnullaTipoDocumentoFELResponse response = tipoDocumentoServiceFEL.annullaTipoDocumentoFEL(req);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(AnnullaTipoDocumentoFEL.class, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Annullato tipo documento con codice " + model.getUidTipoDocumento());
		// Imposto il parametro RIENTRO per avvertire la action di ricaricare la lista
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		// Imposto il successo in sessione
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
}