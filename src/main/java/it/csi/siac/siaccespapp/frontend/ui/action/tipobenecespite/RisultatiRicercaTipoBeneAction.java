/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.tipobenecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.RisultatiRicercaTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespiteResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaTipoBeneAction extends GenericTipoBeneAction<RisultatiRicercaTipoBeneModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = -965357044421448013L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;		
	}

	/**
	 * Consulta.
	 *
	 * @return the string
	 */
	public String consulta() {
		//devo solo fare la redirezione alla pagina di consultazione
		return SUCCESS;
	}
	
	
	/**
	 * Aggiorna categoria.
	 *
	 * @return the string
	 */
	public String aggiorna() {
		//devo solo fare la redirezione alla pagina di aggiornamento
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validate elimina categoria.
	 */
	public void validateEliminaTipoBene() {
		checkCondition(model.getUidTipoBeneCespite() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo bene cespite da eliminare"));
	}
	
	/**
	 * Elimina categoria.
	 *
	 * @return the string
	 */
	public String eliminaTipoBene() {
		final String methodName ="eliminaTipoBene";
		EliminaTipoBeneCespite req = model.creaRequestEliminaTipoBeneCespite();
		EliminaTipoBeneCespiteResponse res = classificazioneCespiteService.eliminaTipoBeneCespite(req);
		if(res.hasErrori()) {
			//verificati errori
			log.debug(methodName, "si sono verificati errori nella chiamata al servizio.");
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Validate annulla tipo bene.
	 */
	public void validateAnnullaTipoBene() {
		checkCondition(model.getUidTipoBeneCespite() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti da eliminare"));
		checkNotNull(model.getAnnoAnnullamento(), "anno annullamento");
		if(model.getUidTipoBeneCespite() != 0 && !model.isForce()) {
			checkAnnullabilita();
		}
	}
	
	/**
	 * Controlla l'annullabilit&agrave; della categoria
	 */
	private void checkAnnullabilita() {
		final String methodName = "checkAnnullabilita";
		VerificaAnnullabilitaTipoBeneCespite req = model.creaRequestVerificaAnnullabilitaTipoBeneCespite();
		VerificaAnnullabilitaTipoBeneCespiteResponse res = classificazioneCespiteService.verificaAnnullabilitaTipoBeneCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio di verifica annullabilita' tipo bene");
			addErrori(res);
			return;
		}
		addMessaggi(res.getListaMessaggio());
	}
	
	/**
	 * Annulla tipo bene.
	 *
	 * @return the string
	 */
	public String annullaTipoBene() {
		final String methodName ="annullaTipoBene";
		if(model.getMessaggi() != null && !model.getMessaggi().isEmpty()) {
			return INPUT;
		}
		AnnullaTipoBeneCespite req = model.creaRequestAnnullaTipoBeneCespite();
		AnnullaTipoBeneCespiteResponse res = classificazioneCespiteService.annullaTipoBeneCespite(req);
		if(res.hasErrori()) {
			//verificati errori
			log.debug(methodName, "si sono verificati errori nella chiamata al servizio.");
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}

}
