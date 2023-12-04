/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.stampe;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.stampe.InviaAllegatoAttoChecklistModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiAllegatoAttoChecklist;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiAllegatoAttoChecklistResponse;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.ChecklistOption;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.ChecklistOptionValue;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.ChecklistText;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(InviaAllegatoAttoChecklistAction.MODEL_SESSION_NAME)
//SIAC-8804
public class InviaAllegatoAttoChecklistAction extends GenericAllegatoAttoAction<InviaAllegatoAttoChecklistModel> {


	private static final long serialVersionUID = 2607624185170570088L;

	/** Nome del model per la sessione */
	protected static final String MODEL_SESSION_NAME = "InviaAllegatoAttoChecklist";

	
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

		// Controllo sull'applicabilita' del caso d'uso riferentesi allo stato del bilancio
		checkCasoDUsoApplicabile(model.getTitolo());
		// Distrugge le vecchie ancore
		destroyAnchors();

		LeggiAllegatoAttoChecklist req = model.buildLeggiAllegatoAttoChecklist();
		logServiceRequest(req);
		LeggiAllegatoAttoChecklistResponse res = allegatoAttoService.leggiAllegatoAttoChecklist(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
//			log.error(methodName, createErrorInServiceInvocationString(req, res));
//			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, res));
		}
		
		model.setChecklist(res.getAllegatoAttoChecklist());
		
		return SUCCESS;
	}
	
	
	public String invia() {
		return SUCCESS;
	}

	public void validateInvia() {
		
		Checklist checklist = model.getChecklist();
		
		validateChecklistOption(checklist.getSoggettoCreditore());
		validateChecklistOption(checklist.getSommaDovuta());
		//validateChecklistOption(checklist.getCausale());
		validateChecklistOption(checklist.getModalitaPagamento());
		validateChecklistOption(checklist.getScadenza());
		validateChecklistOption(checklist.getEstremiProvvedimentoDirigenziale());
		validateChecklistOption(checklist.getAllegati());
		validateChecklistOption(checklist.getEsigibileSpesa());
		validateChecklistOption(checklist.getCongruitaSpesaSommaImpegnata());
		validateChecklistOption(checklist.getApplicazioneNormativaFiscale());
		validateChecklistOption(checklist.getIva());
		validateChecklistOption(checklist.getEntrataVincolata());
		if (ChecklistOptionValue.Si.equals(checklist.getEntrataVincolata().getValue())) {
			validateChecklistText(checklist.getAccertamento());
			validateChecklistText(checklist.getIncasso());
		}
		validateChecklistOption(checklist.getCig());
		validateChecklistOption(checklist.getCup());
		validateChecklistOption(checklist.getContributiva());
		validateChecklistOption(checklist.getPubblicazione());
		//validateChecklistOption(checklist.getRegolaritaContabile());
	}

	private void validateChecklistOption(ChecklistOption checklistOption) {
		if (checklistOption != null && checklistOption.getValue() == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(checklistOption.getLabel()));
		}
	}

	private void validateChecklistText(ChecklistText checklistText) {
		if (checklistText != null && StringUtils.isBlank(checklistText.getValue())) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore(checklistText.getLabel()));
		}
	}
	
	/**
	 * Distrugge le famiglie delle ancore non pi&uacute; utilizzate.
	 */
	private void destroyAnchors() {
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_INSERIMENTO);
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_ASSOCIAZIONE_DOCUMENTO);
		destroyAnchorFamily(GenericAllegatoAttoAction.CDU_ASSOCIAZIONE_MOVIMENTO);
	}

	
}
