/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.AggiornaCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class GenericTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCategoriaCespitiAction extends GenericCategoriaCespitiAction<AggiornaCategoriaCespitiModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -398779689056953324L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoCalcolo();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName ="execute";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCategoriaCespiti(null);
		RicercaDettaglioCategoriaCespitiResponse response = ottieniRicercaDettaglioCategoriaCespitiResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento della categoria.");
			return INPUT;
		}
		CategoriaCespiti categoriaCespiti = response.getCategoriaCespiti();
		if(categoriaCespiti == null || categoriaCespiti.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("categoria cespiti con uid + " + model.getUidCategoriaCespiti()));
			return INPUT;
		}
		model.setCategoriaCespiti(categoriaCespiti);
		return SUCCESS;		
	}
	
	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		validaCampiCategoriaCespiti();
	}

	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		final String methodName = "salva";
		//creo la request da passare al servizio
		AggiornaCategoriaCespiti req = model.creaRequestAggiornaCategoriaCespiti();
		//chiamo il servizio di inserimento
		AggiornaCategoriaCespitiResponse response = classificazioneCespiteService.aggiornaCategoriaCespiti(req);
		//controllo se si siano verificati error
		if(response.hasErrori()) {
			//Si sono verificati errori 
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'inserimento della categoria.");
			return INPUT;
		}
		//popolo il model con i dati restituiti dalla response
		model.setCategoriaCespiti(response.getCategoriaCespiti());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Salva.
	 *
	 * @return the string
	 * @throws FrontEndBusinessException in caso di eccezione
	 */
	public String ricaricaDaAnnulla() throws FrontEndBusinessException {
		final String methodName = "ricaricaDaAnnulla";
		log.info(methodName, "start");
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		
		RicercaDettaglioCategoriaCespitiResponse response = ottieniRicercaDettaglioCategoriaCespitiResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento della categoria.");
			return INPUT;
		}
		CategoriaCespiti categoriaCespiti = response.getCategoriaCespiti();
		if(categoriaCespiti == null || categoriaCespiti.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("categoria cespiti con uid + " + model.getUidCategoriaCespiti()));
			return INPUT;
		}
		model.setCategoriaCespiti(categoriaCespiti);
		return SUCCESS;		

	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
		super.checkCasoDUsoApplicabile();
		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
		
		boolean consentito = AzioniConsentiteFactory.isConsentito(AzioniConsentite.CATEGORIA_CESPITI_AGGIORNA, azioniConsentite);
		if(!consentito) {
			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}

}
