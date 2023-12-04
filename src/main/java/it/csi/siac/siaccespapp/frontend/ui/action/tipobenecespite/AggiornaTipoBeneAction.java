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
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.AggiornaTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaTipoBeneAction extends GenericTipoBeneAction<AggiornaTipoBeneModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 1788397965664152025L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName ="execute";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setTipoBeneCespite(null);
		RicercaDettaglioTipoBeneCespiteResponse response = ottieniRicercaDettaglioTipoBeneCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento della categoria.");
			return INPUT;
		}
		TipoBeneCespite tipoBeneCespite = response.getTipoBeneCespite();
		if(tipoBeneCespite == null || tipoBeneCespite.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("categoria cespiti con uid + " + model.getTipoBeneCespite().getUid()));
			return INPUT;
		}
		model.setTipoBeneCespite(tipoBeneCespite);
		return SUCCESS;		
	}
	
	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		TipoBeneCespite tipoBeneCespite = model.getTipoBeneCespite();
		
		checkNotNull(tipoBeneCespite, "tipo bene");
		checkNotNullNorEmpty(tipoBeneCespite.getCodice(), "codice tipo bene");
		checkNotNullNorEmpty(tipoBeneCespite.getDescrizione(), "descrizione tipo bene");
		checkPresenzaIdEntita(tipoBeneCespite.getCategoriaCespiti());
		
	}

	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		final String methodName = "salva";
		//creo la request da passare al servizio
		AggiornaTipoBeneCespite req = model.creaRequestAggiornaTipoBeneCespite();
		//chiamo il servizio di inserimento
		AggiornaTipoBeneCespiteResponse response = classificazioneCespiteService.aggiornaTipoBeneCespite(req);
		//controllo se si siano verificati error
		if(response.hasErrori()) {
			//Si sono verificati errori 
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'inserimento della categoria.");
			return INPUT;
		}
		//popolo il model con i dati restituiti dalla response
		model.setTipoBeneCespite(response.getTipoBeneCespite());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile() {
//		super.checkCasoDUsoApplicabile();
//		List<AzioneConsentita> azioniConsentite = sessionHandler.getAzioniConsentite();
//		
//		boolean consentito = AzioniConsentiteFactory.isConsentito(AzioniConsentite.CATEGORIA_CESPITI_AGGIORNA, azioniConsentite);
//		if(!consentito) {
//			throw new GenericFrontEndMessagesException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non si dispone dei permessi necessari per l'esecuzione").getTesto(),
//					GenericFrontEndMessagesException.Level.ERROR);
//		}
	}

}
