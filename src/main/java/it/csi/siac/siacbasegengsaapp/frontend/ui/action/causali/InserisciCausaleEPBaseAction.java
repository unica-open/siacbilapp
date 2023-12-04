/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.InserisciCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceCausaleResponse;

/**
 * Classe di action per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 * @param <M> la tipizzazione del model
 *
 */

public abstract class InserisciCausaleEPBaseAction <M extends InserisciCausaleEPBaseModel>extends BaseInserisciAggiornaCausaleEPBaseAction<M>  {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4469636595935362556L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		return SUCCESS;
	}
	
	@Override
	public String annullaStep1() {
		model.setCausaleEP(null);
		model.setElementoPianoDeiConti(null);
		model.setSoggetto(null);
		model.setClassificatoreEP1(null);
		model.setClassificatoreEP2(null);
		model.setClassificatoreEP3(null);
		model.setClassificatoreEP4(null);
		model.setClassificatoreEP5(null);
		model.setClassificatoreEP6(null);
		model.setTipoEvento(null);
		
		return SUCCESS;
	}
	
	@Override
	public String annullaStep2() {
		// Pulisco la lista
		model.getListaContoTipoOperazione().clear();
		
		return SUCCESS;
	}
	
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		// Inserimento della causale
		InserisceCausale request = model.creaRequestInserisceCausale();
		logServiceRequest(request);
		InserisceCausaleResponse response = causaleService.inserisceCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della causale EP");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Inserita correttamente causale EP con uid " + response.getCausaleEP().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setCausaleEP(response.getCausaleEP());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		// Pulisaco la sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_EP, null);
		sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, null);
		return SUCCESS;
	}

	
}
