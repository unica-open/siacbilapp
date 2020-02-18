/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.RisultatiRicercaCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AnnullaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespitiResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class GenericTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaCategoriaCespitiAction extends GenericCategoriaCespitiAction<RisultatiRicercaCategoriaCespitiModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 804926032570858212L;
	
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
	public void validateEliminaCategoria() {
		checkCondition(model.getUidCategoriaCespiti() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti da eliminare"));
	}
	
	/**
	 * Elimina categoria.
	 *
	 * @return the string
	 */
	public String eliminaCategoria() {
		final String methodName ="eliminaCategoria";
		EliminaCategoriaCespiti req = model.creaRequestEliminaCategoriaCespiti();
		EliminaCategoriaCespitiResponse res = classificazioneCespiteService.eliminaCategoriaCespiti(req);
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
	 * Annulla categoria.
	 *
	 * @return the string
	 */
	public String annullaCategoria() {
		final String methodName ="eliminaCategoria";
		if(model.getMessaggi() != null && !model.getMessaggi().isEmpty()) {
			return INPUT;
		}
		AnnullaCategoriaCespiti req = model.creaRequestAnnullaCategoriaCespiti();
		AnnullaCategoriaCespitiResponse res = classificazioneCespiteService.annullaCategoriaCespiti(req);
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
	 * Validazione per il metodo {@link #annullaCategoria()}
	 */
	public void validateAnnullaCategoria() {
		checkCondition(model.getUidCategoriaCespiti() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti da annullare"));
		if(model.getUidCategoriaCespiti() != 0 && !model.isForce()) {
			checkAnnullabilita();
		}
	}

	/**
	 * Controlla l'annullabilit&agrave; della categoria
	 */
	private void checkAnnullabilita() {
		final String methodName = "checkAnnullabilita";
		VerificaAnnullabilitaCategoriaCespiti req = model.creaRequestVerificaAnnullabilitaCategoriaCespiti();
		VerificaAnnullabilitaCategoriaCespitiResponse res = classificazioneCespiteService.verificaAnnullabilitaCategoriaCespiti(req);
		if(res.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio di verifica annullabilita' categoria");
			addErrori(res);
			return;
		}
		addMessaggi(res.getListaMessaggio());
	}

}
