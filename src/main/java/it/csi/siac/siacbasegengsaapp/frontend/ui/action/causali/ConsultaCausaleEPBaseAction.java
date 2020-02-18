/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.ConsultaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di action per la consultazione della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 * @param <M> la tipizzazione del model
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public abstract class ConsultaCausaleEPBaseAction <M extends ConsultaCausaleEPBaseModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2144951325800069394L;
	
	/** Serviz&icirc; della causale */
	@Autowired protected transient CausaleService causaleService;
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
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
		
		CausaleEP causaleEP = null;
		try {
			causaleEP = caricaCausaleDaServizio();
		} catch(WebServiceInvocationFailureException wsife) {
			setErroriInSessionePerActionSuccessiva();
			setMessaggiInSessionePerActionSuccessiva();
			setInformazioniInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		log.debug(methodName, "Trovata cassa economale corrispondente all'uid " + causaleEP.getUid());
		
		model.setCausaleEP(causaleEP);
		impostaTipoEvento(causaleEP);
		impostaClassificatoriEP(causaleEP.getClassificatoriEP());
		
		return SUCCESS;
	}

	/**
	 * Caricamento del dettaglio della causale EP dal servizio.
	 * 
	 * @return la causale del servizio
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private CausaleEP caricaCausaleDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "caricaCausaleDaServizio";
		
		RicercaDettaglioCausale request = model.creaRequestRicercaDettaglioCausale();
		logServiceRequest(request);
		RicercaDettaglioCausaleResponse response = causaleService.ricercaDettaglioCausale(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		if(response.getCausaleEP() == null) {
			String errorMsg = "Nessuna causale corrispondente all'uid " + model.getCausaleEP().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getCausaleEP().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		return response.getCausaleEP();
	}
	
	/**
	 * Ricerca e impostazione del tipo evento della causaleEP nel model.
	 * 
	 * @param causaleEP la causale da impostare
	 */
	private void impostaTipoEvento(CausaleEP causaleEP) {
		for (Evento evento : causaleEP.getEventi()) {
			if(evento.getTipoEvento() != null) {
				// Ho trovato i tipo evento: lo imposto ed esco
				model.setTipoEvento(evento.getTipoEvento());
				return;
			}
		}
	}
	
	/**
	 * Impostazione dei classificatori EP.
	 * 
	 * @param classificatoriEP i classificatori da impostare
	 */
	private void impostaClassificatoriEP(List<ClassificatoreEP> classificatoriEP) {
		for(ClassificatoreEP cep : classificatoriEP) {
			String label = null;
			if(cep.getTipoClassificatore() != null) {
				label = cep.getTipoClassificatore().getDescrizione();
			}
			
			if(BilConstants.CODICE_CLASSIFICATORE_EP1.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				model.setClassificatoreEP1(cep);
				model.setLabelClassificatoreEP1(label);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP2.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				model.setClassificatoreEP2(cep);
				model.setLabelClassificatoreEP2(label);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP3.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				model.setClassificatoreEP3(cep);
				model.setLabelClassificatoreEP3(label);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP4.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				model.setClassificatoreEP4(cep);
				model.setLabelClassificatoreEP4(label);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP5.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				model.setClassificatoreEP5(cep);
				model.setLabelClassificatoreEP5(label);
			}
		}
	}
}
