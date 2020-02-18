/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per i la consultazione della prima nota libera (comune tra ambito FIN e GSA)
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaPrimaNotaLiberaBaseAction<M extends ConsultaPrimaNotaLiberaBaseModel> extends GenericBilancioAction<M>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 7098826208857238649L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// SIAC-5242: rendere la consultazione utilizzabile quando il bilancio e' in stato CHIUSO
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		// TODO: Davvero utile?
		checkCasoDUsoApplicabile("");
		
		PrimaNota primaNotaLibera = null;
		try {
			primaNotaLibera = caricaPrimaNotaLiberaDaServizio();
		} catch(WebServiceInvocationFailureException wsife) {
			setErroriInSessionePerActionSuccessiva();
			setMessaggiInSessionePerActionSuccessiva();
			setInformazioniInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		log.debug(methodName, "Trovata primaNotaLibera corrispondente all'uid " + primaNotaLibera.getUid());
		
		model.setPrimaNotaLibera(primaNotaLibera);
		model.setDataRegistrazioneDefinitivaVisibile(primaNotaLibera.getDataRegistrazioneLibroGiornale() != null);
		calcolaTotali();
		return SUCCESS;
	}
	/**
	 * Caricamento del dettaglio della causale EP dal servizio.
	 * 
	 * @return la causale del servizio
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private PrimaNota caricaPrimaNotaLiberaDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "caricaPrimaNotaLiberaDaServizio";
		
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
			String errorMsg = "Nessuna prima nota corrispondente all'uid " + model.getPrimaNotaLibera().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getPrimaNotaLibera().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
	
		return response.getPrimaNota();
	}
	
	/**
	 * Calcolo dei totali dare e avere per la prima nota
	 */
	private void calcolaTotali(){
		PrimaNota p = model.getPrimaNotaLibera();
		for (MovimentoEP mEP :  p.getListaMovimentiEP()){
			for (MovimentoDettaglio movDett : mEP.getListaMovimentoDettaglio()){
				if (OperazioneSegnoConto.DARE.equals(movDett.getSegno())) {
					model.setTotaleDare(model.getTotaleDare().add(movDett.getImporto()));
				} else {
					model.setTotaleAvere(model.getTotaleAvere().add(movDett.getImporto()));
				}
			}
			
		}
		
	}
}
