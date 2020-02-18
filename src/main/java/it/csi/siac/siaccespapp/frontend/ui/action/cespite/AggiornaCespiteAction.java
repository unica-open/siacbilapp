/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import java.math.BigDecimal;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.AggiornaCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCespiteAction extends GenericCespiteAction<AggiornaCespiteModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 2747102495853628947L;
	
	private static final String SUCCESS_PRIMA_NOTA = "success_prima_nota";

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe(true);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName ="execute";
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCespite(null);
		RicercaDettaglioCespiteResponse response = ottieniRicercaDettaglioCespiteResponse();
		if(response.hasErrori()) {
			addErrori(response);
			log.debug(methodName, "Si sono verificati errrori nel reperimento del cespite.");
			return INPUT;
		}
		Cespite cespite = response.getCespite();
		if(cespite == null || cespite.getUid() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("cespite con uid + " + model.getCespite().getUid()));
			return INPUT;
		}
		model.setCespite(cespite);
		
		Integer uidPrimaNota = sessionHandler.getParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE);
		BigDecimal importoMassimoCespite = sessionHandler.getParametro(BilSessionParameter.IMPORTO_CESPITE_SU_PRIMA_NOTA);
		sessionHandler.setParametro(BilSessionParameter.UID_PRIMA_NOTA_DA_COLLEGARE_A_CESPITE, null);
		sessionHandler.setParametro(BilSessionParameter.UID_MOVIMENTO_DETTAGLIO_DA_COLLEGARE_A_CESPITE, null);
		model.setUidPrimaNota(uidPrimaNota);
		model.setImportoMassimoCespite(importoMassimoCespite);
		return SUCCESS;		
	}
	
	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		Cespite cespite = model.getCespite();
		checkAllCampiCespite(cespite);
		//SIAC-6567
		checkCondition(model.getUidPrimaNota() == null || model.getUidPrimaNota().intValue() == 0 
				||  model.getImportoMassimoCespite() == null || model.getImportoMassimoCespite().compareTo(model.getCespite().getValoreIniziale()) >=0
						, ErroreBil.IMPORTI_NON_COERENTI.getErrore("L'importo del cespite risulta essere maggiore dell'importo del dettaglio della prima nota a cui lo si vuole collegare."));
	}

	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		final String methodName = "salva";
		//creo la request da passare al servizio
		AggiornaCespite req = model.creaRequestAggiornaCespite();
		//chiamo il servizio di inserimento
		AggiornaCespiteResponse response = cespiteService.aggiornaCespite(req);
		//controllo se si siano verificati error
		if(response.hasErrori()) {
			//Si sono verificati errori 
			addErrori(response);
			log.debug(methodName, "Si sono verificati errori nell'inserimento della categoria.");
			return INPUT;
		}
		//popolo il model con i dati restituiti dalla response
		model.setCespite(response.getCespite());
		impostaInformazioneSuccesso();
		return model.getUidPrimaNota() != null && model.getUidPrimaNota().intValue()!=0? SUCCESS_PRIMA_NOTA: SUCCESS;
	}
	
}
